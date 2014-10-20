package com.jfaker.framework.security.web.validate;

import com.jfaker.framework.security.model.User;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * UserValidator.
 */
public class UserValidator extends Validator {
	
	protected void validate(Controller controller) {
		validateRequiredString("user.username", "usernameMsg", "请输入用户名称!");
		validateRequiredString("user.plainPassword", "passwordMsg", "请输入密码!");
	}
	
	protected void handleError(Controller controller) {
		controller.keepModel(User.class);
		
		String actionKey = getActionKey();
		System.out.println("actionKey=>" + actionKey);
		if (actionKey.equals("/security/user/save"))
			controller.render("userAdd.jsp");
		else if (actionKey.equals("/security/user/update"))
			controller.render("userEdit.jsp");
	}
}
