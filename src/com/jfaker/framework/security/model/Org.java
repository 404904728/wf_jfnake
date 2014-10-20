package com.jfaker.framework.security.model;

import org.apache.commons.lang.StringUtils;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * 部门模型
 * @author yuqs
 * @since 0.1
 */
public class Org extends Model<Org> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5018575558755643041L;
	public static final Org dao = new Org();
	
	public Page<Org> paginate (int pageNumber, int pageSize, String name) {
		String sql = "from sec_org o inner join sec_org op on o.parent_org=op.id ";
		if(StringUtils.isNotEmpty(name)) {
			sql += " where o.name like '%" + name + "%' ";
		}
		sql += " order by id desc";
		return paginate(pageNumber, pageSize, "select o.*,op.name as parentName", sql);
	}
	
	public Org get(Integer id) {
		return Org.dao.findFirst("select o.*,po.id as parentId, po.name as parentName from sec_org o inner join sec_org po on o.parent_org=po.id where o.id=?", id);
	}
}
