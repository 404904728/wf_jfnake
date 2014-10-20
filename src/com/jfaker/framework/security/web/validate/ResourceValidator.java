package com.jfaker.framework.security.web.validate;

import com.jfaker.framework.security.model.Resource;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * ResourceValidator.
 */
public class ResourceValidator extends Validator {
	
	protected void validate(Controller controller) {
		validateRequiredString("resource.name", "nameMsg", "请输入资源名称!");
		validateRequiredString("resource.source", "sourceMsg", "请输入资源值!");
	}
	
	protected void handleError(Controller controller) {
		controller.keepModel(Resource.class);
		
		String actionKey = getActionKey();
		if (actionKey.equals("/security/resource/save"))
			controller.render("resourceAdd.jsp");
		else if (actionKey.equals("/security/resource/update"))
			controller.render("resourceAdd.jsp");
	}
}
