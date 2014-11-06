package com.jfaker.framework.form.web;

import java.util.Map;

import org.snaker.engine.helper.JsonHelper;

import com.jfaker.framework.form.model.Form;
import com.jfaker.framework.security.shiro.ShiroUtils;
import com.jfaker.framework.utils.DateUtils;
import com.jfinal.core.Controller;

public class FormController extends Controller {
	public void index() {
		String name = getPara("name");
		setAttr("page", Form.dao.paginate(getParaToInt("pageNo", 1), 10, name));
		keepPara();
		render("formList.jsp");
	}
	
	public void add() {
		render("formAdd.jsp");
	}
	
	public void view() {
		setAttr("form", Form.dao.findById(getParaToInt()));
		render("formView.jsp");
	}
	
	public void edit() {
		setAttr("form", Form.dao.findById(getParaToInt()));
		render("formEdit.jsp");
	}
	
	public void designer() {
		setAttr("form", Form.dao.findById(getParaToInt()));
		render("formDesigner.jsp");
	}
	
	public void save() {
		Form model = getModel(Form.class);
		model.set("creator", ShiroUtils.getUsername());
		model.set("createTime", DateUtils.getCurrentTime());
		model.set("fieldNum", 0);
		model.save();
		redirect("/config/form");
	}
	
	public void update() {
		getModel(Form.class).update();
		redirect("/config/form");
	}
	
	@SuppressWarnings("unchecked")
	public void processor() {
		Form model = null;
		try {
			model = Form.dao.findById(getParaToInt("formid"));
			Map<String, Object> map = JsonHelper.fromJson(getPara("parse_form"), Map.class);
			Map<String, Object> datas = (Map<String, Object>)map.get("add_fields");
			Form.dao.process(model, datas);
			model.set("originalHtml", map.get("template"));
			model.set("parseHtml", map.get("parse"));
			model.update();
			renderJson(Boolean.TRUE);
		} catch(Exception e) {
			e.printStackTrace();
			renderJson(Boolean.FALSE);
		}
	}
	
	public void delete() {
		Form.dao.deleteById(getParaToInt());
		redirect("/config/form");
	}
	
	public void use() {
		setAttr("form", Form.dao.findById(getParaToInt()));
		render("formUse.jsp");
	}
	
	public void submit() {
		Form model = Form.dao.findById(getParaToInt("form.id"));
		Map<String, String[]> paraMap = getParaMap();
		Form.dao.submit(model, paraMap);
		redirect(getPara("url"));
	}
}
