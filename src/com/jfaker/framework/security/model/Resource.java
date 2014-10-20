package com.jfaker.framework.security.model;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * 资源模型
 * @author yuqs
 * @since 0.1
 */
public class Resource extends Model<Resource> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8781209142247805658L;
	public static final Resource dao = new Resource();
	
	public Page<Resource> paginate (int pageNumber, int pageSize, String name) {
		String sql = "from sec_resource r left join sec_menu m on r.menu=m.id";
		if(StringUtils.isNotEmpty(name)) {
			sql += " where r.name like '%" + name + "%' ";
		}
		sql += " order by id desc";
		return paginate(pageNumber, pageSize, "select r.*,m.name as menuName", sql);
	}
	
	public Resource get(Long id) {
		return Resource.dao.findFirst("select r.*,m.name as menuName from sec_resource r left join sec_menu m on r.menu=m.id where r.id=?", id);
	}
	
	public List<Resource> getAll() {
		return Resource.dao.find("select * from sec_resource");
	}
	
	public List<Resource> getWithAuthAll() {
		return Resource.dao.find("select r.*, a.name as authorityName from sec_resource r "
				+ "LEFT JOIN sec_authority_resource ar ON r.id=ar.resource_id "
				+ "LEFT JOIN sec_authority a ON a.id=ar.authority_id ");
	}
}
