package com.jfaker.app.flow.web;

import org.snaker.engine.access.Page;
import org.snaker.engine.access.QueryFilter;
import org.snaker.engine.entity.Surrogate;

import com.jfaker.app.flow.SnakerEngineFacets;
import com.jfinal.core.Controller;

/**
 * 委托授权
 * @author yuqs
 * @since 0.1
 */
public class SurrogateController extends Controller {
	public void index() {
		Page<Surrogate> page = new Page<Surrogate>();
		page.setPageNo(getParaToInt("pageNo", 1));
		SnakerEngineFacets.facets.searchSurrogate(page, new QueryFilter());
		setAttr("page", page);
		render("surrogateList.jsp");
	}
	
	public void add() {
		setAttr("processNames", SnakerEngineFacets.facets.getAllProcessNames());
		render("surrogateAdd.jsp");
	}

	public void edit() {
		setAttr("surrogate", SnakerEngineFacets.facets.getSurrogate(getPara()));
		setAttr("processNames", SnakerEngineFacets.facets.getAllProcessNames());
		render("surrogateEdit.jsp");
	}
	
	public void view() {
		setAttr("surrogate", SnakerEngineFacets.facets.getSurrogate(getPara()));
		render("surrogateView.jsp");
	}
	
	public void save() {
		Surrogate model = getModel(Surrogate.class);
		SnakerEngineFacets.facets.addSurrogate(model);
		redirect("/snaker/surrogate");
	}
	
	public void update() {
		Surrogate model = getModel(Surrogate.class);
		SnakerEngineFacets.facets.addSurrogate(model);
		redirect("/snaker/surrogate");
	}
	
	public void delete() {
		SnakerEngineFacets.facets.deleteSurrogate(getPara());
		redirect("/snaker/surrogate");
	}
}
