package com.jfaker.app.flow;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

public class SnakerInterceptor implements Interceptor {
	@Override
	public void intercept(ActionInvocation ai) {
		Controller controller = ai.getController();
		controller.setAttr("processId", controller.getPara("processId"));
		controller.setAttr("orderId", controller.getPara("orderId"));
		controller.setAttr("taskId", controller.getPara("taskId"));
	}
}
