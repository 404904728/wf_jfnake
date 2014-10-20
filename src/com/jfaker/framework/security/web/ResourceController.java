package com.jfaker.framework.security.web;

import com.jfaker.framework.security.model.Resource;
import com.jfaker.framework.security.web.validate.ResourceValidator;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * ResourceController
 * @author yuqs
 * @since 0.1
 */
public class ResourceController extends Controller {
	public void index() {
		String name = getPara("name");
		setAttr("name", name);
		setAttr("page", Resource.dao.paginate(getParaToInt("pageNo", 1), 10, name));
		render("resourceList.jsp");
	}
	
	public void add() {
		render("resourceAdd.jsp");
	}
	
	public void edit() {
		setAttr("resource", Resource.dao.get(getParaToLong()));
		render("resourceEdit.jsp");
	}
	
	public void view() {
		setAttr("resource", Resource.dao.get(getParaToLong()));
		render("resourceView.jsp");
	}
	
	@Before(ResourceValidator.class)
	public void save() {
		getModel(Resource.class).save();
		redirect("/security/resource");
	}
	
	@Before(ResourceValidator.class)
	public void update() {
		getModel(Resource.class).update();
		redirect("/security/resource");
	}
	
	public void delete() {
		Resource.dao.deleteById(getParaToInt());
		redirect("/security/resource");
	}
}


