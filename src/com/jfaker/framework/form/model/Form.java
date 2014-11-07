package com.jfaker.framework.form.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfaker.framework.utils.DateUtils;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

public class Form extends Model<Form> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8945469242429605208L;
	private static final Logger log = LoggerFactory.getLogger(Form.class);
	private static final String TABLE_PREFIX = "TBL_";
	public static final Form dao = new Form();
	
	public Page<Form> paginate (int pageNumber, int pageSize, String name) {
		String from = " from df_form ";
		if(name != null && name.length() > 0) {
			from += " where name like '%" + name + "%' ";
		}
		from += " order by id desc ";
		return paginate(pageNumber, pageSize, "select *", from);
	}
	
	@SuppressWarnings("unchecked")
	public void process(Form model, Map<String, Object> datas) {
		if(datas == null) {
			throw new NullPointerException();
		}
		String tableName = getTableName(model);
		List<Field> fields = new ArrayList<Field>();
		for(Map.Entry<String, Object> entry : datas.entrySet()) {
			Map<String, String> fieldInfo = (Map<String, String>)entry.getValue();
			Field field = new Field();
			field.set("name", entry.getKey());
			field.set("title", fieldInfo.get("title"));
			field.set("leipiplugins", fieldInfo.get("leipiplugins"));
			field.set("tableName", tableName);
			field.set("formId", model.get("id"));
			field.set("type", fieldInfo.get("orgtype"));
			fields.add(field);
		}
		model.set("fieldNum", model.getInt("fieldNum") + fields.size());
		String check = "select count(*) from " + tableName + " where id = 1";
		boolean isExists = true;
		try {
			Db.queryLong(check);
		} catch(Exception e) {
			isExists = false;
		}
		StringBuilder sql = new StringBuilder();
		try {
			if(!isExists) {
				sql.append("CREATE TABLE ").append(tableName).append(" (");
				sql.append("ID INT NOT NULL AUTO_INCREMENT,");
				for(Field field : fields) {
					sql.append(field.getStr("name"));
					sql.append(" ").append(fieldSQL(field.getStr("leipiplugins"))).append(",");
				}
				sql.append("FORMID INT NOT NULL,");
				sql.append("UPDATETIME VARCHAR(20),");
				sql.append("PRIMARY KEY (ID)");
				sql.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
				Db.update(sql.toString());
			} else {
				List<String> fieldNames = Db.query("select name from df_field where tableName=?", tableName);
				if(fields.size() > 0) {
					for(Field field : fields) {
						if(!fieldNames.contains(field.getStr("name"))) {
							Db.update("ALTER TABLE " + tableName + " ADD COLUMN " + field.getStr("name") + fieldSQL(field.getStr("leipiplugins")));
						}
					}
				}
			}
			
			for(Field field : fields) {
				field.save();
			}
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void submit(Form model, Map<String, String[]> paraMap) {
		String tableName = getTableName(model);
		List<String> fieldNames = Db.query("select name from df_field where tableName=?", tableName);
		StringBuilder beforeSql = new StringBuilder();
		StringBuilder afterSql = new StringBuilder();
		beforeSql.append("INSERT INTO ").append(tableName);
		beforeSql.append(" (FORMID, UPDATETIME ");
		afterSql.append(") values (?,?");
		List<Object> datas = new ArrayList<Object>();
		datas.add(model.getInt("id"));
		datas.add(DateUtils.getCurrentTime());
		if(fieldNames != null) {
			StringBuilder fieldSql = new StringBuilder();
			StringBuilder valueSql = new StringBuilder();
			for(String fieldName : fieldNames) {
				String[] data = paraMap.get(fieldName);
				if(data == null) {
					continue;
				}
				fieldSql.append(",").append(fieldName);
				valueSql.append(",?");
				if(data.length == 1) {
					datas.add(data[0]);
				} else {
					String dataArr = ArrayUtils.toString(data);
					if(dataArr.length() > 1) {
						datas.add(dataArr.substring(1, dataArr.length() - 1));
					}
				}
			}
			if(fieldSql.length() > 0) {
				beforeSql.append(fieldSql.toString());
				afterSql.append(valueSql.toString());
			}
		}
		afterSql.append(")");
		beforeSql.append(afterSql.toString());
		String sql = beforeSql.toString();
		log.info("dynamic sql is:" + sql);
		log.info(datas.toString());
		Db.update(sql, datas.toArray());
	}
	
	public static void main(String[] a) {
		System.out.println(ArrayUtils.toString(new String[]{"aaa","bbb","ccc"}));
	}
	
	private String getTableName(Form model) {
		return TABLE_PREFIX + model.getStr("name");
	}
	
	private String fieldSQL(String leipiplugins) {
        if(leipiplugins.equalsIgnoreCase("textarea") 
        		|| leipiplugins.equalsIgnoreCase("listctrl")) {
            return " TEXT";
        }
        else if(leipiplugins.equalsIgnoreCase("checkboxs")) {
            return " TINYINT(1) NOT NULL DEFAULT 0";
        } else {
            return " VARCHAR(255) NOT NULL DEFAULT ''";
        }
	}
}
