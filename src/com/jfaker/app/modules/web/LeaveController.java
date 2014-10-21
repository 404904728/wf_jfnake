package com.jfaker.app.modules.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.jfaker.app.flow.SnakerInterceptor;
import com.jfaker.app.flow.web.SnakerController;
import com.jfaker.framework.security.shiro.ShiroUtils;
import com.jfinal.aop.Before;

/**
 * 请假流程Controller
 * @author yuqs
 * @since 0.1
 */
public class LeaveController extends SnakerController {
	@Before(SnakerInterceptor.class)
	public void apply() {
		render("apply.jsp");
	}
	
	@Before(SnakerInterceptor.class)
	public void approveDept() {
		render("approveDept.jsp");
	}
	
	@Before(SnakerInterceptor.class)
	public void approveBoss() {
		render("approveBoss.jsp");
	}
	
	/**
	 * 申请保存
	 */
	public void applySave() {
		String processId = getPara("processId");
		String orderId = getPara("orderId");
		String taskId = getPara("taskId");
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("day", getParaToInt("day"));
		args.put("reason", getPara("reason"));
		args.put("apply.operator", ShiroUtils.getUsername());
		args.put("approveDept.operator", ShiroUtils.getUsername());
		args.put("approveBoss.operator", ShiroUtils.getUsername());
        if(StringUtils.isEmpty(orderId) && StringUtils.isEmpty(taskId)) {
        	startAndExecute(processId, ShiroUtils.getUsername(), args);
        } else {
        	execute(taskId, ShiroUtils.getUsername(), args);
        }
		redirect("/snaker/task/active");
	}
	
	/**
	 * 部门经理审批保存
	 */
	public void approveDeptSave() {
		String orderId = getPara("orderId");
		String taskId = getPara("taskId");
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("approveDept.suggest", getPara("approveDept.suggest"));
		args.put("departmentResult", getPara("departmentResult"));
		args.put("nextOperator", getPara("nextOperator"));
		args.put("nextOperatorName", getPara("nextOperatorName"));
		args.put("ccOperator", getPara("ccOperator"));
		args.put("ccOperatorName", getPara("ccOperatorName"));
		String departmentResult = getPara("departmentResult");
		if(departmentResult.equals("-1")) {
			executeAndJump(taskId, ShiroUtils.getUsername(), args, null);
		} else if(departmentResult.equals("2")) {
			String nextOperator = getPara("nextOperator");
			transfer(taskId, ShiroUtils.getUsername(), nextOperator.split(","));
		} else {
			execute(getPara("taskId"), ShiroUtils.getUsername(), args);
		}
		String ccOperator = getPara("ccOperator");
		if(StringUtils.isNotEmpty(ccOperator)) {
			getEngine().order().createCCOrder(orderId, ccOperator.split(","));
		}
		
		redirect("/snaker/task/active");
	}
	
	/**
	 * 总经理审批保存
	 */
	public void approveBossSave() {
		String taskId = getPara("taskId");
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("approveBoss.suggest", getPara("approveBoss.suggest"));
        String bossResult = getPara("bossResult");
        if(bossResult.equals("-1")) {
        	executeAndJump(taskId, ShiroUtils.getUsername(), args, null);
        } else {
        	execute(taskId, ShiroUtils.getUsername(), args);
        }
        redirect("/snaker/task/active");
	}
}
