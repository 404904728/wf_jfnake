package com.jfaker.framework.dict.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

public class Dict extends Model<Dict> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3290069328905665226L;
	public static final Dict dao = new Dict();
	
	public Page<Dict> paginate (int pageNumber, int pageSize, String name) {
		String from = " from conf_dictionary ";
		if(name != null && name.length() > 0) {
			from += " where name like '%" + name + "%' ";
		}
		from += " order by id desc ";
		return paginate(pageNumber, pageSize, "select *", from);
	}
	
	public Map<String, String> getItemsByName(String name) { 
		List<DictItem> items = DictItem.dao.getAll(name);
		if(items == null || items.isEmpty()) return Collections.emptyMap();
        Map<String, String> dicts = new TreeMap<String, String>();
        for(DictItem item : items) {
            dicts.put(item.getStr("code"), item.getStr("name"));
        }
        return dicts;
	}
}
