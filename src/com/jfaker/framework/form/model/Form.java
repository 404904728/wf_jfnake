package com.jfaker.framework.form.model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

public class Form extends Model<Form> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8945469242429605208L;
	public static final Form dao = new Form();
	
	public Page<Form> paginate (int pageNumber, int pageSize, String name) {
		String from = " from df_form ";
		if(name != null && name.length() > 0) {
			from += " where name like '%" + name + "%' ";
		}
		from += " order by id desc ";
		return paginate(pageNumber, pageSize, "select *", from);
	}
}
