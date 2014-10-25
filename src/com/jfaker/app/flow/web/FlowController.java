package com.jfaker.app.flow.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.snaker.engine.entity.Process;
import org.snaker.engine.model.WorkModel;

import com.jfaker.framework.security.shiro.ShiroUtils;

public class FlowController extends SnakerController {
	/**
	 * 处理流程启动或任务执行，并且将表单数据保存至实例、任务变量中
	 * 变量类型根据表单字段的首字母决定，类型分别为:S字符型,I整形,L常整形,B布尔型,D日期型,N浮点型
	 * 执行规则根据method的值决定，值分别为:0执行,-1驳回或跳转,1转主办,2转协办
	 * 适用于演示流程，或者是无业务字段的正式流程
	 */
	@SuppressWarnings("unchecked")
	public void process() {
		Map<String, Object> params = new HashMap<String, Object>();
		Enumeration<String> paraNames = getRequest().getParameterNames();
		while (paraNames.hasMoreElements()) {
			String element = paraNames.nextElement();
			int index = element.indexOf("_");
			if(index == -1) {
				params.put(element, getPara(element));
			} else {
				char type = element.charAt(0);
				String name = element.substring(index + 1);
				Object value = null;
				switch(type) {
				case 'S':
					value = getPara(element);
					break;
				case 'I':
					value = getParaToInt(element);
					break;
				case 'L':
					value = getParaToLong(element);
					break;
				case 'B':
					value = getParaToBoolean(element);
					break;
				case 'D':
					value = getParaToDate(element);
					break;
				case 'N':
					value = Double.parseDouble(getPara(element));
					break;
				default:
					value = getPara(element);
					break;
				}
				params.put(name, value);
			}
		}
		String processId = getPara(PARA_PROCESSID);
		String orderId = getPara(PARA_ORDERID);
		String taskId = getPara(PARA_TASKID);
		String nextOperator = getPara(PARA_NEXTOPERATOR);
		if (StringUtils.isEmpty(orderId) && StringUtils.isEmpty(taskId)) {
			startAndExecute(processId, ShiroUtils.getUsername(), params);
		} else {
			int method = getParaToInt(PARA_METHOD, 0);
			switch(method) {
			case 0://任务执行
				execute(taskId, ShiroUtils.getUsername(), params);
				break;
			case -1://驳回、任意跳转
				executeAndJump(taskId, ShiroUtils.getUsername(), params, getPara(PARA_NODENAME));
				break;
			case 1://转办
				if(StringUtils.isNotEmpty(nextOperator)) {
					transferMajor(taskId, ShiroUtils.getUsername(), nextOperator.split(","));
				}
				break;
			case 2://协办
				if(StringUtils.isNotEmpty(nextOperator)) {
					transferAidant(taskId, ShiroUtils.getUsername(), nextOperator.split(","));
				}
				break;
			default:
				execute(taskId, ShiroUtils.getUsername(), params);
				break;
			}
		}
		String ccOperator = getPara(PARA_CCOPERATOR);
		if(StringUtils.isNotEmpty(ccOperator)) {
			engine.order().createCCOrder(orderId, ccOperator.split(","));
		}
		redirectActiveTask();
	}
	
	public void all() {
		keepPara();
		String processId = getPara(PARA_PROCESSID);
		String orderId = getPara(PARA_ORDERID);
		String taskId = getPara(PARA_TASKID);
		if(StringUtils.isNotEmpty(processId)) {
			setAttr("process", engine.process().getProcessById(processId));
		}
		if(StringUtils.isNotEmpty(orderId)) {
			setAttr("order", engine.query().getOrder(orderId));
		}
		if(StringUtils.isNotEmpty(taskId)) {
			setAttr("task", engine.query().getTask(taskId));
		}
		
		render("all.jsp");
	}
	
	public void node() {
		String processId = getPara(PARA_PROCESSID);
		Process process = engine.process().getProcessById(processId);
		List<WorkModel> models = process.getModel().getWorkModels();
		renderJson(models);
	}
	
	public void approval() {
		keepPara();
		render("approval.jsp");
	}
	
	public void doApproval() {
		
		redirectActiveTask();
	}
}
