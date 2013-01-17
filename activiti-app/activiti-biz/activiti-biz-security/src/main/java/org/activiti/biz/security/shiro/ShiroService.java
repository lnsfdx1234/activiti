package org.activiti.biz.security.shiro;

import org.activiti.biz.security.shiro.ShiroDbRealm.ShiroUser;
import org.activiti.entity.mybatis.User;
import org.activiti.persist.mybatis.repository.UserMybatisDao;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户管理类.
 * 
 * @author chenlg
 */
// Spring Service Bean的标识.
@Service("shiroService")
@Transactional(readOnly = true)
public class ShiroService {

	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	
	private static Logger logger = LoggerFactory.getLogger(ShiroService.class);
	
	@Autowired
	private UserMybatisDao  userDao;
	
	/**
	 * 根据用户登录名。获取用户对象
	 * 
	 * @param loginName
	 * @return user
	 */
	public User findUserByLoginName(String loginName) {
		if(logger.isInfoEnabled())
			logger.info("【登录】有用户请求登记。还未验证密码。【用户名:{?}】",loginName);
		return userDao.findByLoginName(loginName);
	}
	
	/**
	 * 取出Shiro中的当前用户LoginName.
	 */
	public String getCurrentUserName() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.loginName;
	}
	/**
	 * 取出Shiro中的当前用户id.
	 */
	public Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}
	
	/**
	 * 取出Shiro中的当前用户ShiroUser.
	 */
	public ShiroUser getCurrentShiroUser() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user;
	}

	/**
	 * 更新Shiro中当前用户的用户名.
	 */
	public void updateCurrentUserName(String userName) {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		user.name = userName;
	}

	
}
