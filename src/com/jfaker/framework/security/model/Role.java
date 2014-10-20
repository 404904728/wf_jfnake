package com.jfaker.framework.security.model;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * 角色模型
 * @author yuqs
 * @since 0.1
 */
public class Role extends Model<Role> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8781209142247805658L;
	public static final Role dao = new Role();
	
	public Page<Role> paginate (int pageNumber, int pageSize, String name) {
		String sql = "from sec_role ";
		if(StringUtils.isNotEmpty(name)) {
			sql += " where name like '%" + name + "%' ";
		}
		sql += " order by id desc ";
		return paginate(pageNumber, pageSize, "select *", sql);
	}
	
	public List<Authority> getAuthorities(Long id) {
		return Authority.dao.find("select a.* from sec_authority a "
				+ "LEFT JOIN sec_role_authority ra ON a.id=ra.authority_id "
				+ "LEFT JOIN sec_role r ON r.id=ra.role_id "
				+ "WHERE r.id=?", id);
	}
	
	public List<Role> getAll() {
		return Role.dao.find("select * from sec_role");
	}
	
	public void insertCascade(Long id, Integer authorityId) {
		Db.update("insert into sec_role_authority (role_id, authority_id) values (?,?)", id, authorityId);
	}
	
	public void deleteCascade(Long id) {
		Db.update("delete from sec_role_authority where role_id = ?", id);
	}
}
