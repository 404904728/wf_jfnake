package com.jfaker.framework.form.web;

import com.jfaker.framework.form.model.Field;
import com.jfaker.framework.form.model.Form;
import com.jfinal.core.Controller;

public class FieldController extends Controller {
	public void index() {
		int formId = getParaToInt();
		setAttr("form", Form.dao.findById(formId));
		setAttr("fields", Field.dao.find("select * from df_field where formId=?", formId));
		render("fieldList.jsp");
	}
}
