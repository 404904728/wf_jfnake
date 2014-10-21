package com.jfaker.framework.security.web;

import java.util.List;

import com.jfaker.framework.security.model.Authority;
import com.jfaker.framework.security.model.Resource;
import com.jfaker.framework.security.web.validate.AuthorityValidator;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.tx.Tx;

/**
 * AuthorityController
 * @author yuqs
 * @since 0.1
 */
public class AuthorityController extends Controller {
	public void index() {
		keepPara();
		setAttr("page", Authority.dao.paginate(getParaToInt("pageNo", 1), 10, getPara("name")));
		render("authorityList.jsp");
	}
	
	public void add() {
		setAttr("resources", Resource.dao.getAll());
		render("authorityAdd.jsp");
	}
	
	public void edit() {
		setAttr("authority", Authority.dao.findById(getParaToInt()));
		List<Resource> resources = Resource.dao.getAll();
		List<Resource> resss = Authority.dao.getResources(getParaToLong());
		for(Resource res : resources) {
			for(Resource selRes : resss) {
				if(res.getLong("id").longValue() == selRes.getLong("id").longValue())
				{
					res.put("selected", 1);
				}
				if(res.get("selected") == null)
				{
					res.put("selected", 0);
				}
			}
		}
		setAttr("resources", resources);
		render("authorityEdit.jsp");
	}
	
	public void view() {
		setAttr("authority", Authority.dao.findById(getParaToLong()));
		setAttr("resources", Authority.dao.getResources(getParaToLong()));
		render("authorityView.jsp");
	}
	
	@Before(AuthorityValidator.class)
	public void save() {
		Integer[] orderIndexs = getParaValuesToInt("orderIndexs");
		Authority model = getModel(Authority.class);
		model.save();
		for(Integer orderIndex : orderIndexs) {
			Authority.dao.insertCascade(model.getLong("id"), orderIndex);
		}
		redirect("/security/authority");
	}
	
	@Before(AuthorityValidator.class)
	public void update() {
		getModel(Authority.class).update();
		redirect("/security/authority");
	}
	
	@Before(Tx.class)
	public void delete() {
		Authority.dao.deleteCascade(getParaToLong());
		Authority.dao.deleteById(getParaToLong());
		redirect("/security/authority");
	}
}


