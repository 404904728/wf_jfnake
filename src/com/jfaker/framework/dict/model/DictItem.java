package com.jfaker.framework.dict.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

public class DictItem extends Model<DictItem> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2706867741882170405L;
	public static final DictItem dao = new DictItem();
	
	public List<DictItem> getAll(int dictId) {
		return DictItem.dao.find("select * from conf_dictitem where dictionary = ? order by orderby", dictId);
	}
	
	public List<DictItem> getAll(String dictName) {
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("select ci.id,ci.name,ci.code,ci.orderby,ci.dictionary,ci.description from conf_dictitem ci ");
		sqlBuffer.append(" left outer join conf_dictionary cd on cd.id = ci.dictionary ");
		sqlBuffer.append(" where cd.name = ? order by ci.orderby");
		return DictItem.dao.find(sqlBuffer.toString(), dictName);
	}
	
	public void deleteByDictId(int dictId) {
		Db.update("delete from conf_dictitem where dictionary = ?", dictId);
	}
}
