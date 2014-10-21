package com.jfaker.app.flow.web;

import org.snaker.engine.access.Page;
import org.snaker.engine.access.QueryFilter;
import org.snaker.engine.entity.Surrogate;

/**
 * 委托授权
 * @author yuqs
 * @since 0.1
 */
public class SurrogateController extends SnakerController {
	public void index() {
		Page<Surrogate> page = new Page<Surrogate>();
		page.setPageNo(getParaToInt("pageNo", 1));
		searchSurrogate(page, new QueryFilter());
		setAttr("page", page);
		render("surrogateList.jsp");
	}
	
	public void add() {
		setAttr("processNames", getAllProcessNames());
		render("surrogateAdd.jsp");
	}

	public void edit() {
		setAttr("surrogate", getSurrogate(getPara()));
		setAttr("processNames", getAllProcessNames());
		render("surrogateEdit.jsp");
	}
	
	public void view() {
		setAttr("surrogate", getSurrogate(getPara()));
		render("surrogateView.jsp");
	}
	
	public void save() {
		Surrogate model = getModel(Surrogate.class);
		addSurrogate(model);
		redirect("/snaker/surrogate");
	}
	
	public void update() {
		Surrogate model = getModel(Surrogate.class);
		addSurrogate(model);
		redirect("/snaker/surrogate");
	}
	
	public void delete() {
		deleteSurrogate(getPara());
		redirect("/snaker/surrogate");
	}
}
