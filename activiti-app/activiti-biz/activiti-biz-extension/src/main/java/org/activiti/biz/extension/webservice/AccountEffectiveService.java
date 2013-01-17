package org.activiti.biz.extension.webservice;

import java.util.List;
import java.util.Map;

import org.activiti.entity.mybatis.User;
import org.activiti.persist.mybatis.repository.UserMybatisDao;
import org.activiti.utils.core.mapper.JsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;

/**
 * 更高效的AccountService实现，基于MyBatis 的方案，以JSON格式存储Memcached中的内容。
 * 可以增加缓存方案 可以使用memcached 或者 redis
 * @author chenlg
 */ 
@Service
@Transactional(readOnly = true)
public class AccountEffectiveService {

	@Autowired
	private UserMybatisDao userDao;
 

	private final JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();


	/**
	 * 先访问Memcached, 使用JSON字符串存放对象以节约空间.
	 */
	public User getUser(Long id) {
		String key = MemcachedObjectType.USER.getPrefix() + id;

		User user = null;
		//String jsonString = memcachedClient.get(key);
		String jsonString = null;
		if (jsonString == null) {
			user = userDao.findOne(id);
			if (user != null) {
				jsonString = jsonMapper.toJson(user);
			//	memcachedClient.set(key, MemcachedObjectType.USER.getExpiredTime(), jsonString);
			}
		} else {
			user = jsonMapper.fromJson(jsonString, User.class);
		}
		return user;
	}

	public List<User> searchUser(String loginName, String name) {
		Map<String, Object> parameters = Maps.newHashMap();
		parameters.put("loginName", loginName);
		parameters.put("name", name);
		return userDao.search(parameters);
	}

	@Transactional
	public void saveUser(User user) {
		userDao.save(user);
	}

	@Transactional
	public void deleteUser(Long id) {
		userDao.delete(id);
	}
}
