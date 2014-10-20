package com.jfaker.framework.dict.web;

import com.jfaker.framework.dict.model.Dict;
import com.jfaker.framework.dict.model.DictItem;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.tx.Tx;

/**
 * DictController
 * @author yuqs
 * @since 0.1
 */
public class DictController extends Controller {
	public void index() {
		String name = getPara("name");
		setAttr("page", Dict.dao.paginate(getParaToInt("pageNo", 1), 10, name));
		setAttr("name", name);
		render("dictList.jsp");
	}
	
	public void add() {
		render("dictAdd.jsp");
	}
	
	public void view() {
		int dictId = getParaToInt();
		setAttr("dict", Dict.dao.findById(dictId));
		setAttr("dictItems", DictItem.dao.getAll(dictId));
		render("dictView.jsp");
	}
	
	public void edit() {
		int dictId = getParaToInt();
		setAttr("dict", Dict.dao.findById(dictId));
		setAttr("dictItems", DictItem.dao.getAll(dictId));
		render("dictEdit.jsp");
	}
	
	@Before({DictValidator.class, Tx.class })
	public void save() {
		Dict model = getModel(Dict.class);
		model.save();
		String[] itemNames = getParaValues("itemNames");
		Integer[] orderbys = getParaValuesToInt("orderbys");
		String[] codes = getParaValues("codes");
		for(int i = 0; i < itemNames.length; i++) {
			DictItem ci = new DictItem();
			ci.set("name", itemNames[i]);
			ci.set("orderby", orderbys[i]);
			ci.set("code", codes[i]);
			ci.set("dictionary", model.get("id"));
			ci.save();
		}
		redirect("/security/dict");
	}
	
	@Before({DictValidator.class, Tx.class})
	public void update() {
		Dict model = getModel(Dict.class);
		model.update();
		
		DictItem.dao.deleteByDictId(model.getInt("id"));
		String[] itemNames = getParaValues("itemNames");
		Integer[] orderbys = getParaValuesToInt("orderbys");
		String[] codes = getParaValues("codes");
		for(int i = 0; i < itemNames.length; i++) {
			DictItem ci = new DictItem();
			ci.set("name", itemNames[i]);
			ci.set("orderby", orderbys[i]);
			ci.set("code", codes[i]);
			ci.set("dictionary", model.get("id"));
			ci.save();
		}
		redirect("/security/dict");
	}
	
	public void delete() {
		Dict.dao.deleteById(getParaToInt());
		redirect("/security/dict");
	}
}


