package com.jfaker.framework.security.web.validate;

import com.jfaker.framework.security.model.Menu;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * MenuValidator.
 */
public class MenuValidator extends Validator {
	
	protected void validate(Controller controller) {
		validateRequiredString("menu.name", "nameMsg", "请输入菜单名称!");
	}
	
	protected void handleError(Controller controller) {
		controller.keepModel(Menu.class);
		
		String actionKey = getActionKey();
		if (actionKey.equals("/security/menu/save"))
			controller.render("menuAdd.jsp");
		else if (actionKey.equals("/security/menu/update"))
			controller.render("menuEdit.jsp");
	}
}
