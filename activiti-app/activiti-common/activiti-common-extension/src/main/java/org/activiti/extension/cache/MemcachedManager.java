/**
 * Copyright (c) 2005-2012 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package org.activiti.extension.cache;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import net.spy.memcached.MemcachedClient;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

/**
 * 对SpyMemcached Client的二次封装,提供常用的Get/GetBulk/Set/Delete/Incr/Decr函数的同步与异步操作封装.
 * 
 * 未提供封装的函数可直接调用getClient()取出Spy的原版MemcachedClient来使用.
 * 
 * @author chenlg
 */
@Component
@ManagedResource(objectName = MemcachedManager.MBEAN_NAME, description = "memcached Management Bean")
public class MemcachedManager implements DisposableBean {

	private static Logger logger = LoggerFactory.getLogger(MemcachedManager.class);
	/**
	 * MemcachedManager的Mbean的注册名称.
	 */
	public static final String MBEAN_NAME = "memcached:name=Memcached";
	private MemcachedClient memcachedClient;

	private long shutdownTimeout = 2500;

	private long updateTimeout = 2500;

	/**
	 * Get方法, 转换结果类型并屏蔽异常, 仅返回Null.
	 */
	@ManagedOperation(description = "Get方法, 转换结果类型并屏蔽异常, 仅返回Null.")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "key", description = "key")})
	public <T> T get(String key) {
		try {
			return (T) memcachedClient.get(key);
		} catch (RuntimeException e) {
			handleException(e, key);
			return null;
		}
	}

	/**
	 * GetBulk方法, 转换结果类型并屏蔽异常.
	 */
	@ManagedOperation(description = "GetBulk方法, 转换结果类型并屏蔽异常.")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "key", description = "key")})
	public <T> Map<String, T> getBulk(Collection<String> keys) {
		try {
			return (Map<String, T>) memcachedClient.getBulk(keys);
		} catch (RuntimeException e) {
			handleException(e, StringUtils.join(keys, ","));
			return null;
		}
	}

	/**
	 * 异步Set方法, 不考虑执行结果.
	 */
	@ManagedOperation(description = "异步Set方法, 不考虑执行结果.")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "key", description = "key"),
		@ManagedOperationParameter(name = "expiredTime", description = "expiredTime"),
		@ManagedOperationParameter(name = "value", description = "value")})
	public void set(String key, int expiredTime, Object value) {
		memcachedClient.set(key, expiredTime, value);
	}

	/**
	 * 安全的Set方法, 保证在updateTimeout秒内返回执行结果, 否则返回false并取消操作.
	 */
	@ManagedOperation(description = "安全的Set方法, 保证在updateTimeout秒内返回执行结果, 否则返回false并取消操作.")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "key", description = "key"),
		@ManagedOperationParameter(name = "expiration", description = "expiration"),
		@ManagedOperationParameter(name = "value", description = "value")})
	public boolean safeSet(String key, int expiration, Object value) {
		Future<Boolean> future = memcachedClient.set(key, expiration, value);
		try {
			return future.get(updateTimeout, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			future.cancel(false);
		}
		return false;
	}

	/**
	 * 异步 Delete方法, 不考虑执行结果.
	 */
	@ManagedOperation(description = "异步 Delete方法, 不考虑执行结果.")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "key", description = "key")})
	public void delete(String key) {
		memcachedClient.delete(key);
	}

	/**
	 * 安全的Delete方法, 保证在updateTimeout秒内返回执行结果, 否则返回false并取消操作.
	 */
	@ManagedOperation(description = "安全的Delete方法, 保证在updateTimeout秒内返回执行结果, 否则返回false并取消操作.")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "key", description = "key")})
	public boolean safeDelete(String key) {
		Future<Boolean> future = memcachedClient.delete(key);
		try {
			return future.get(updateTimeout, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			future.cancel(false);
		}
		return false;
	}

	/**
	 * Incr方法.
	 */
	public long incr(String key, int by, long defaultValue) {
		return memcachedClient.incr(key, by, defaultValue);
	}

	/**
	 * Decr方法.
	 */
	public long decr(String key, int by, long defaultValue) {
		return memcachedClient.decr(key, by, defaultValue);
	}

	/**
	 * 异步Incr方法, 不支持默认值, 若key不存在返回-1.
	 */
	public Future<Long> asyncIncr(String key, int by) {
		return memcachedClient.asyncIncr(key, by);
	}

	/**
	 * 异步Decr方法, 不支持默认值, 若key不存在返回-1.
	 */
	public Future<Long> asyncDecr(String key, int by) {
		return memcachedClient.asyncDecr(key, by);
	}

	private void handleException(Exception e, String key) {
		logger.warn("spymemcached client receive an exception with key:" + key, e);
	}

	@Override
	public void destroy() throws Exception {
		if (memcachedClient != null) {
			memcachedClient.shutdown(shutdownTimeout, TimeUnit.MILLISECONDS);
		}
	}
	@ManagedAttribute(description = "获取 MemcachedClient 实例")
	public MemcachedClient getMemcachedClient() {
		return memcachedClient;
	}
	@ManagedAttribute
	public void setMemcachedClient(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}
	@ManagedAttribute
	public void setUpdateTimeout(long updateTimeout) {
		this.updateTimeout = updateTimeout;
	}
	@ManagedAttribute
	public void setShutdownTimeout(long shutdownTimeout) {
		this.shutdownTimeout = shutdownTimeout;
	}
}