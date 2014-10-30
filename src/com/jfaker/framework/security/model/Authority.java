package com.jfaker.framework.security.model;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * 权限模型
 * @author yuqs
 * @since 0.1
 */
public class Authority extends Model<Authority> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8781209142247805658L;
	public static final Authority dao = new Authority();
	
	public Page<Authority> paginate (int pageNumber, int pageSize, String name) {
		String sql = "from sec_authority";
		if(StringUtils.isNotEmpty(name)) {
			sql += " where name like '%" + name + "%' ";
		}
		sql += " order by id desc";
		return paginate(pageNumber, pageSize, "select *", sql);
	}
	
	public List<Authority> getAll() {
		return Authority.dao.find("select * from sec_authority");
	}
	
	public List<Resource> getResources(Integer id) {
		return Resource.dao.find("select r.* from sec_resource r "
				+ "LEFT JOIN sec_authority_resource ar ON r.id=ar.resource_id "
				+ "LEFT JOIN sec_authority a ON a.id=ar.authority_id "
				+ "WHERE a.id=?", id);
	}
	
	public void insertCascade(Integer id, Integer resourceId) {
		Db.update("insert into sec_authority_resource (authority_id, resource_id) values (?,?)", id, resourceId);
	}
	
	public void deleteCascade(Integer id) {
		Db.update("delete from sec_authority_resource where authority_id = ?", id);
	}
}
