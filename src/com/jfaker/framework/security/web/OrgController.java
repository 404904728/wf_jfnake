package com.jfaker.framework.security.web;

import com.jfaker.framework.security.model.Org;
import com.jfaker.framework.security.web.validate.OrgValidator;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * OrgController
 * @author yuqs
 * @since 0.1
 */
public class OrgController extends Controller {
	public void index() {
		String name = getPara("name");
		setAttr("page", Org.dao.paginate(getParaToInt("pageNo", 1), 10, name));
		setAttr("lookup", getPara("lookup"));
		setAttr("name", name);
		render("orgList.jsp");
	}
	
	public void add() {
		render("orgAdd.jsp");
	}
	
	public void view() {
		setAttr("org", Org.dao.get(getParaToInt()));
		render("orgView.jsp");
	}
	
	public void edit() {
		setAttr("org", Org.dao.get(getParaToInt()));
		render("orgEdit.jsp");
	}
	
	@Before(OrgValidator.class)
	public void save() {
		getModel(Org.class).save();
		redirect("/security/org");
	}
	
	@Before(OrgValidator.class)
	public void update() {
		getModel(Org.class).update();
		redirect("/security/org");
	}
	
	public void delete() {
		Org.dao.deleteById(getParaToInt());
		redirect("/security/org");
	}
}


