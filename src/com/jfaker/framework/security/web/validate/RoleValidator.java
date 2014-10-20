package com.jfaker.framework.security.web.validate;

import com.jfaker.framework.security.model.Role;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * RoleValidator.
 */
public class RoleValidator extends Validator {
	
	protected void validate(Controller controller) {
		validateRequiredString("role.name", "nameMsg", "请输入角色名称!");
	}
	
	protected void handleError(Controller controller) {
		controller.keepModel(Role.class);
		
		String actionKey = getActionKey();
		if (actionKey.equals("/security/role/save"))
			controller.render("roleAdd.jsp");
		else if (actionKey.equals("/security/role/update"))
			controller.render("roleEdit.jsp");
	}
}
