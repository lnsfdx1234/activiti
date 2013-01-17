package org.activiti.biz.service.account;

import java.util.List;

import org.activiti.biz.security.shiro.ShiroService;
import org.activiti.biz.security.utils.Digests;
import org.activiti.biz.service.ServiceException;
import org.activiti.entity.mybatis.User;
import org.activiti.persist.mybatis.repository.UserMybatisDao;
import org.activiti.utils.extension.date.DateProvider;
import org.activiti.utils.extension.encrypt.Encodes;
import org.apache.commons.lang3.StringUtils;
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
@Service
@Transactional(readOnly = true)
public class AccountService {
	private static final int SALT_SIZE = 8;
	private static Logger logger = LoggerFactory
			.getLogger(AccountService.class);

	@Autowired
	private ShiroService shiroService;
	
	/**
	 * 如果是使用hibernate 
	 * 实体对象更换成需要的实体
	 * <p>改使用的 spring data jpa</p> 
	 */
//	@Autowired
//	private UserDao userDao;
	
	/**
	 * 如果是使用mybatis
	 *  实体对象需要更换
	 */ 
	@Autowired
	private UserMybatisDao userDao;
	
	private DateProvider dateProvider = DateProvider.DEFAULT;

	public List<User> getAllUser() {
		return (List<User>) userDao.findAll();
	}

	public User getUser(Long id) {
		return userDao.findOne(id);
	}

	public User findUserByLoginName(String loginName) {
		return userDao.findByLoginName(loginName);
	}

	@Transactional(readOnly = false)
	public void registerUser(User user) {
		entryptPassword(user);
		user.setRoles("user");
		user.setRegisterDate(dateProvider.getDate());

		userDao.save(user);
	}

	@Transactional(readOnly = false)
	public void updateUser(User user) {
		if (StringUtils.isNotBlank(user.getPlainPassword())) {
			entryptPassword(user);
		}
		//如果使用mybatis 必须使用update 方法
		userDao.update(user);
	}

	@Transactional(readOnly = false)
	public void deleteUser(Long id) {
		if (isSupervisor(id)) {
			logger.warn("操作员{}尝试删除超级管理员用户", getCurrentUserName());
			throw new ServiceException("不能删除超级管理员用户");
		}
		userDao.delete(id);

	}
	/**
	 * 获取当前用户数量.
	 */
	public Long getUserCount() {
		return userDao.count();
	}
	

	/**
	 * 判断是否超级管理员.
	 */
	private boolean isSupervisor(Long id) {
		return id == 1;
	}

	/**
	 * 取出Shiro中的当前用户LoginName.
	 */
	private String getCurrentUserName() {
		return shiroService.getCurrentUserName();
	}

	/**
	 * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
	 */
	private void entryptPassword(User user) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		user.setSalt(Encodes.encodeHex(salt));

		byte[] hashPassword = Digests.sha1(user.getPlainPassword().getBytes(),
				salt, ShiroService.HASH_INTERATIONS);
		user.setPassword(Encodes.encodeHex(hashPassword));
	}

	public void setDateProvider(DateProvider dateProvider) {
		this.dateProvider = dateProvider;
	}
}
