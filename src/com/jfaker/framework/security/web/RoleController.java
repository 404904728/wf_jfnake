package com.jfaker.framework.security.web;

import java.util.List;

import com.jfaker.framework.security.model.Authority;
import com.jfaker.framework.security.model.Role;
import com.jfaker.framework.security.web.validate.RoleValidator;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.tx.Tx;

/**
 * RoleController
 * @author yuqs
 * @since 0.1
 */
public class RoleController extends Controller {
	public void index() {
		String name = getPara("name");
		setAttr("name", name);
		setAttr("page", Role.dao.paginate(getParaToInt("pageNo", 1), 10, name));
		render("roleList.jsp");
	}
	
	public void add() {
		setAttr("authorities", Authority.dao.getAll());
		render("roleAdd.jsp");
	}
	
	public void edit() {
		setAttr("role", Role.dao.findById(getParaToInt()));
		List<Authority> authorities = Authority.dao.getAll();
		List<Authority> auths = Role.dao.getAuthorities(getParaToLong());
		for(Authority auth : authorities) {
			for(Authority sels : auths) {
				if(auth.getLong("id").longValue() == sels.getLong("id").longValue())
				{
					auth.put("selected", 1);
				}
				if(auth.get("selected") == null)
				{
					auth.put("selected", 0);
				}
			}
		}
		setAttr("authorities", authorities);
		render("roleEdit.jsp");
	}
	
	public void view() {
		setAttr("role", Role.dao.findById(getParaToLong()));
		setAttr("authorities", Role.dao.getAuthorities(getParaToLong()));
		render("roleView.jsp");
	}
	
	@Before(RoleValidator.class)
	public void save() {
		Integer[] orderIndexs = getParaValuesToInt("orderIndexs");
		Role model = getModel(Role.class);
		model.save();
		for(Integer orderIndex : orderIndexs) {
			Role.dao.insertCascade(model.getLong("id"), orderIndex);
		}
		redirect("/security/role");
	}
	
	@Before(RoleValidator.class)
	public void update() {
		getModel(Role.class).update();
		redirect("/security/role");
	}
	
	@Before(Tx.class)
	public void delete() {
		Role.dao.deleteCascade(getParaToLong());
		Role.dao.deleteById(getParaToLong());
		redirect("/security/role");
	}
}


