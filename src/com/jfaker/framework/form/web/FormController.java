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
		render("formDesigner.jsp");
	}
	
	@SuppressWarnings("unchecked")
	public void save() {
		Form model = getModel(Form.class);
		model.set("creator", ShiroUtils.getUsername());
		model.set("createTime", DateUtils.getCurrentTime());
		System.out.println("type=>" + getPara("type"));
		System.out.println("formid=>" + getPara("formid"));
		System.out.println("parse_form=>" + getPara("parse_form"));
		Map<String, Object> map = JsonHelper.fromJson(getPara("parse_form"), Map.class);
		System.out.println(map);
	}
}
