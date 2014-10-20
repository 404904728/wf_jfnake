package com.jfaker.framework.security.web.validate;

import com.jfaker.framework.security.model.Authority;
import com.jfaker.framework.security.model.Resource;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * AuthorityValidator.
 */
public class AuthorityValidator extends Validator {
	
	protected void validate(Controller controller) {
		validateRequiredString("authority.name", "nameMsg", "请输入权限名称!");
	}
	
	protected void handleError(Controller controller) {
		controller.keepModel(Authority.class);
		controller.setAttr("resources", Resource.dao.getAll());
		String actionKey = getActionKey();
		if (actionKey.equals("/security/authority/save"))
			controller.render("authorityAdd.jsp");
		else if (actionKey.equals("/security/authority/update"))
			controller.render("authorityEdit.jsp");
	}
}
