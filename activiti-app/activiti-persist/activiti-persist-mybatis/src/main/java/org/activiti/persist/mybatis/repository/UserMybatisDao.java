package org.activiti.persist.mybatis.repository;

import java.util.List;
import java.util.Map;

import org.activiti.entity.mybatis.User;
import org.activiti.persist.mybatis.MyBatisRepository;

/**
 * 通过@MapperScannerConfigurer扫描目录中的所有接口, 动态在Spring Context中生成实现.
 * 方法名称必须与Mapper.xml中保持一致.
 * 
 * @author chenlg
 */
@MyBatisRepository
public interface UserMybatisDao {

	User findOne(Long id);

	List<User> search(Map<String, Object> parameters);
	
	List<User> findAll();
	
	void save(User user);
	
	void update(User user);

	void delete(Long id);
	
	User findByLoginName(String loginName);
	
	Long count();
}
