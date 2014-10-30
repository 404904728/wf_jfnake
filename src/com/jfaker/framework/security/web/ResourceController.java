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
		keepPara();
		setAttr("page", Resource.dao.paginate(getParaToInt("pageNo", 1), 10, getPara("name")));
		render("resourceList.jsp");
	}
	
	public void add() {
		render("resourceAdd.jsp");
	}
	
	public void edit() {
		setAttr("resource", Resource.dao.get(getParaToInt()));
		render("resourceEdit.jsp");
	}
	
	public void view() {
		setAttr("resource", Resource.dao.get(getParaToInt()));
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


