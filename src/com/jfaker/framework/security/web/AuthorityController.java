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
		List<Resource> resss = Authority.dao.getResources(getParaToInt());
		for(Resource res : resources) {
			for(Resource selRes : resss) {
				if(res.getInt("id").intValue() == selRes.getInt("id").intValue())
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
		setAttr("authority", Authority.dao.findById(getParaToInt()));
		setAttr("resources", Authority.dao.getResources(getParaToInt()));
		render("authorityView.jsp");
	}
	
	@Before({AuthorityValidator.class, Tx.class})
	public void save() {
		Integer[] orderIndexs = getParaValuesToInt("orderIndexs");
		Authority model = getModel(Authority.class);
		model.save();
		if(orderIndexs != null) {
			for(Integer orderIndex : orderIndexs) {
				Authority.dao.insertCascade(model.getInt("id"), orderIndex);
			}
		}
		redirect("/security/authority");
	}
	
	@Before({AuthorityValidator.class, Tx.class})
	public void update() {
		Integer[] orderIndexs = getParaValuesToInt("orderIndexs");
		Authority model = getModel(Authority.class);
		model.update();
		Authority.dao.deleteCascade(getParaToInt());
		if(orderIndexs != null) {
			for(Integer orderIndex : orderIndexs) {
				Authority.dao.insertCascade(model.getInt("id"), orderIndex);
			}
		}
		redirect("/security/authority");
	}
	
	@Before(Tx.class)
	public void delete() {
		Authority.dao.deleteCascade(getParaToInt());
		Authority.dao.deleteById(getParaToInt());
		redirect("/security/authority");
	}
}


