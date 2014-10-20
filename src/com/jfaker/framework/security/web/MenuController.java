package com.jfaker.framework.security.web;

import com.jfaker.framework.security.model.Menu;
import com.jfaker.framework.security.web.validate.MenuValidator;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * MenuController
 * @author yuqs
 * @since 0.1
 */
public class MenuController extends Controller {
	public void index() {
		String name = getPara("name");
		setAttr("name", name);
		setAttr("page", Menu.dao.paginate(getParaToInt("pageNo", 1), 10, name));
		setAttr("lookup", getPara("lookup"));
		render("menuList.jsp");
	}
	
	public void add() {
		render("menuAdd.jsp");
	}
	
	public void view() {
		setAttr("menu", Menu.dao.get(getParaToInt()));
		render("menuView.jsp");
	}
	
	public void edit() {
		setAttr("menu", Menu.dao.get(getParaToInt()));
		render("menuEdit.jsp");
	}
	
	@Before(MenuValidator.class)
	public void save() {
		getModel(Menu.class).save();
		redirect("/security/menu");
	}
	
	@Before(MenuValidator.class)
	public void update() {
		getModel(Menu.class).update();
		redirect("/security/menu");
	}
	
	public void delete() {
		Menu.dao.deleteById(getParaToInt());
		redirect("/security/menu");
	}
}


