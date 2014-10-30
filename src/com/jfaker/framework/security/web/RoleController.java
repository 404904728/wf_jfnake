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
		keepPara();
		setAttr("page", Role.dao.paginate(getParaToInt("pageNo", 1), 10, getPara("name")));
		render("roleList.jsp");
	}
	
	public void add() {
		setAttr("authorities", Authority.dao.getAll());
		render("roleAdd.jsp");
	}
	
	public void edit() {
		setAttr("role", Role.dao.findById(getParaToInt()));
		List<Authority> authorities = Authority.dao.getAll();
		List<Authority> auths = Role.dao.getAuthorities(getParaToInt());
		for(Authority auth : authorities) {
			for(Authority sels : auths) {
				if(auth.getInt("id").intValue() == sels.getInt("id").intValue())
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
		setAttr("role", Role.dao.findById(getParaToInt()));
		setAttr("authorities", Role.dao.getAuthorities(getParaToInt()));
		render("roleView.jsp");
	}
	
	@Before(RoleValidator.class)
	public void save() {
		Integer[] orderIndexs = getParaValuesToInt("orderIndexs");
		Role model = getModel(Role.class);
		model.save();
		for(Integer orderIndex : orderIndexs) {
			Role.dao.insertCascade(model.getInt("id"), orderIndex);
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
		Role.dao.deleteCascade(getParaToInt());
		Role.dao.deleteById(getParaToInt());
		redirect("/security/role");
	}
}


