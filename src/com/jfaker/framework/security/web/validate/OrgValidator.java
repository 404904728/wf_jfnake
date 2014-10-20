package com.jfaker.framework.security.web.validate;

import com.jfaker.framework.security.model.Org;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * OrgValidator.
 */
public class OrgValidator extends Validator {
	
	protected void validate(Controller controller) {
		validateRequiredString("org.name", "nameMsg", "请输入部门名称!");
	}
	
	protected void handleError(Controller controller) {
		controller.keepModel(Org.class);
		
		String actionKey = getActionKey();
		if (actionKey.equals("/security/org/save"))
			controller.render("orgAdd.jsp");
		else if (actionKey.equals("/security/org/update"))
			controller.render("orgEdit.jsp");
	}
}
