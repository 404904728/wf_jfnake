package com.jfaker.app.modules.web;

import com.jfaker.app.flow.web.SnakerController;
import com.jfaker.framework.security.shiro.ShiroUtils;

/**
 * 请假流程Controller
 * @author yuqs
 * @since 0.1
 */
public class LeaveController extends SnakerController {
	public void apply() {
		keepPara();
		setAttr("operator", ShiroUtils.getUsername());
		render("apply.jsp");
	}
	
	public void approveDept() {
		keepPara();
		render("approveDept.jsp");
	}
	
	public void approveBoss() {
		keepPara();
		render("approveBoss.jsp");
	}
}
