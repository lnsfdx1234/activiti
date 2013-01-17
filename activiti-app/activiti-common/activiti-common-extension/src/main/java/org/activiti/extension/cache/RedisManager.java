/**
 * 
 */
package org.activiti.extension.cache;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

/**
 * 封装redis 缓存服务器服务接口
 * 未提供封装的函数可直接调用getJedis()取出redis的原版jedis来使用.
 * @author chenlg
 */
@Component
@ManagedResource(objectName = RedisManager.MBEAN_NAME, description = "Redis Management Bean")
public class RedisManager {
	/**
	 * RedisManager的Mbean的注册名称.
	 */
	public static final String MBEAN_NAME = "redis:name=Redis";

	private static Logger managerLogger = LoggerFactory
			.getLogger(RedisManager.class);


	// 操作redis客户端
	private static Jedis jedis;
	
	@Autowired
	@Qualifier("jedisConnectionFactory")
	private JedisConnectionFactory jedisConnectionFactory;
	
	
	/**
	 * 获取一个jedis 客户端
	 * 
	 * @return
	 */
	private Jedis getJedis() {
		if (jedis == null) {
			return jedisConnectionFactory.getShardInfo().createResource();
		}
		return jedis;
	}

	/**
	 * 通过key删除（字节）
	 * 
	 * @param key
	 */
	@ManagedOperation(description = "通过key删除（字节）")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "key", description = "key")})
	public void del(byte[] key) {
		this.getJedis().del(key);
		managerLogger.info("通过key删除（字节） key-->{}", key);
	}

	/**
	 * 通过key删除
	 * 
	 * @param key
	 */
	@ManagedOperation(description = "通过key删除")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "key", description = "key")})
	public void del(String key) {
		this.getJedis().del(key);
		managerLogger.info("通过key删除 key-->{}", key);
	}

	/**
	 * 添加key value 并且设置存活时间(byte)
	 * 
	 * @param key
	 * @param value
	 * @param liveTime
	 */
	@ManagedOperation(description = "添加key value 并且设置存活时间(byte)")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "key", description = "key"),
		@ManagedOperationParameter(name = "value", description = "value"),
		@ManagedOperationParameter(name = "liveTime", description = "liveTime")})
	public void set(byte[] key, byte[] value, int liveTime) {
		this.set(key, value);
		this.getJedis().expire(key, liveTime);
		managerLogger.info(
				"添加key value 并且设置存活时间(byte) key->{},value->{},liveTime->{}",
				key, value, liveTime);
	}

	/**
	 * 添加key value 并且设置存活时间
	 * 
	 * @param key
	 * @param value
	 * @param liveTime
	 */
	@ManagedOperation(description = "添加key value 并且设置存活时间")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "key", description = "key"),
	@ManagedOperationParameter(name = "value", description = "value"),
	@ManagedOperationParameter(name = "liveTime", description = "liveTime") })
	public void set(String key, String value, int liveTime) {
		this.set(key, value);
		this.getJedis().expire(key, liveTime);
		managerLogger.info(
				"添加key value 并且设置存活时间 key->{},value->{},liveTime->{}", key,
				value, liveTime);
	}

	/**
	 * 添加key value
	 * 
	 * @param key
	 * @param value
	 */
	@ManagedOperation(description = "添加key value")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "key", description = "key"),
		@ManagedOperationParameter(name = "value", description = "value")})
	public void set(String key, String value) {
		this.getJedis().set(key, value);
		managerLogger.info("添加key value  key->{},value->{}", key, value);
	}

	/**
	 * 添加key value (字节)(序列化)
	 * 
	 * @param key
	 * @param value
	 */
	@ManagedOperation(description = "添加key value (字节)(序列化)")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "key", description = "key"),
		@ManagedOperationParameter(name = "value", description = "value")})
	public void set(byte[] key, byte[] value) {
		this.getJedis().set(key, value);
		managerLogger.info("添加key value (字节)(序列化)  key->{},value->{}", key,
				value);
	}

	/**
	 * 获取redis value (String)
	 * 
	 * @param key
	 * @return
	 */
	@ManagedOperation(description = "获取redis value (String)")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "key", description = "key")})
	public String get(String key) {
		String value = this.getJedis().get(key);
		managerLogger.info("获取redis value (String)  key->{},value->{}", key,
				value);
		return value;
	}

	/**
	 * 获取redis value (byte [] )(反序列化)
	 * 
	 * @param key
	 * @return
	 */
	@ManagedOperation(description = "获取redis value (byte [] )(反序列化)")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "key", description = "key")})
	public byte[] get(byte[] key) {
		return this.getJedis().get(key);
	}

	/**
	 * 通过正则匹配keys
	 * 
	 * @param pattern
	 * @return
	 */
	@ManagedOperation(description = "通过正则匹配keys")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "pattern", description = "pattern")})
	public Set<String> keys(String pattern) {
		return this.getJedis().keys(pattern);
	}

	/**
	 * 检查key是否已经存在
	 * 
	 * @param key
	 * @return
	 */
	@ManagedOperation(description = "检查key是否已经存在")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "key", description = "key")})
	public boolean exists(String key) {
		return this.getJedis().exists(key);
	}

	/**
	 * 清空redis 所有数据
	 * 
	 * @return
	 */
	@ManagedOperation(description = "清空redis 所有数据")
	public String flushDB() {
		managerLogger.info("清空redis 所有数据");
		return this.getJedis().flushDB();
	}

	/**
	 * 查看redis里有多少数据
	 */
	@ManagedOperation(description = "查看redis里有多少数据")
	public long dbSize() {
		return this.getJedis().dbSize();
	}

	/**
	 * 检查是否连接成功
	 * 
	 * @return
	 */
	@ManagedOperation(description = "检查是否连接成功")
	public String ping() {
		return this.getJedis().ping();
	}


}
