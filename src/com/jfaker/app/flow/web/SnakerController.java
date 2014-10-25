package com.jfaker.app.flow.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.snaker.engine.SnakerEngine;
import org.snaker.engine.access.Page;
import org.snaker.engine.access.QueryFilter;
import org.snaker.engine.entity.HistoryTask;
import org.snaker.engine.entity.Order;
import org.snaker.engine.entity.Process;
import org.snaker.engine.entity.Surrogate;
import org.snaker.engine.entity.Task;
import org.snaker.engine.helper.StreamHelper;
import org.snaker.engine.model.TaskModel.TaskType;
import org.snaker.jfinal.plugin.SnakerPlugin;

import com.jfinal.core.Controller;

public class SnakerController extends Controller {
	public static final String PARA_PROCESSID = "processId";
	public static final String PARA_ORDERID = "orderId";
	public static final String PARA_TASKID = "taskId";
	public static final String PARA_METHOD = "method";
	public static final String PARA_NEXTOPERATOR = "nextOperator";
	public static final String PARA_NODENAME = "nodeName";
	public static final String PARA_CCOPERATOR = "ccOperator";
	public static final String URL_ACTIVETASK = "/snaker/task/active";
	protected SnakerEngine engine = SnakerPlugin.getEngine();
	public void initFlows() {
		engine.process().deploy(StreamHelper.getStreamFromClasspath("flows/actorall.snaker"));
		engine.process().deploy(StreamHelper.getStreamFromClasspath("flows/group.snaker"));
		engine.process().deploy(StreamHelper.getStreamFromClasspath("flows/leave.snaker"));
		engine.process().deploy(StreamHelper.getStreamFromClasspath("flows/forkjoin.snaker"));
		engine.process().deploy(StreamHelper.getStreamFromClasspath("flows/custom.snaker"));
		engine.process().deploy(StreamHelper.getStreamFromClasspath("flows/free.snaker"));
	}
	
	public void redirectActiveTask() {
		redirect(URL_ACTIVETASK);
	}
	
	protected void flowData(String orderId, String taskName) {
		if (StringUtils.isNotEmpty(orderId) && StringUtils.isNotEmpty(taskName)) {
			List<HistoryTask> histTasks = engine.query()
					.getHistoryTasks(
							new QueryFilter().setOrderId(orderId).setName(
									taskName));
			List<Map<String, Object>> vars = new ArrayList<Map<String,Object>>();
			for(HistoryTask hist : histTasks) {
				vars.add(hist.getVariableMap());
			}
			setAttr("vars", vars);
			setAttr("histTasks", histTasks);
		}
	}
	
	public SnakerEngine getEngine() {
		return engine;
	}
	
	public List<String> getAllProcessNames() {
		List<Process> list = engine.process().getProcesss(new QueryFilter());
		List<String> names = new ArrayList<String>();
		for(Process entity : list) {
			if(names.contains(entity.getName())) {
				continue;
			} else {
				names.add(entity.getName());
			}
		}
		return names;
	}
	
	public Order startInstanceById(String processId, String operator, Map<String, Object> args) {
		return engine.startInstanceById(processId, operator, args);
	}
	
	public Order startInstanceByName(String name, Integer version, String operator, Map<String, Object> args) {
		return engine.startInstanceByName(name, version, operator, args);
	}
	
	public Order startAndExecute(String name, Integer version, String operator, Map<String, Object> args) {
		Order order = engine.startInstanceByName(name, version, operator, args);
		List<Task> tasks = engine.query().getActiveTasks(new QueryFilter().setOrderId(order.getId()));
		List<Task> newTasks = new ArrayList<Task>();
		if(tasks != null && tasks.size() > 0) {
			Task task = tasks.get(0);
			newTasks.addAll(engine.executeTask(task.getId(), operator, args));
		}
		return order;
	}
	
	public Order startAndExecute(String processId, String operator, Map<String, Object> args) {
		Order order = engine.startInstanceById(processId, operator, args);
		List<Task> tasks = engine.query().getActiveTasks(new QueryFilter().setOrderId(order.getId()));
		List<Task> newTasks = new ArrayList<Task>();
		if(tasks != null && tasks.size() > 0) {
			Task task = tasks.get(0);
			newTasks.addAll(engine.executeTask(task.getId(), operator, args));
		}
		return order;
	}
	
	public List<Task> execute(String taskId, String operator, Map<String, Object> args) {
		return engine.executeTask(taskId, operator, args);
	}
	
	public List<Task> executeAndJump(String taskId, String operator, Map<String, Object> args, String nodeName) {
		return engine.executeAndJumpTask(taskId, operator, args, nodeName);
	}
	
	public List<Task> transferMajor(String taskId, String operator, String... actors) {
		List<Task> tasks = engine.task().createNewTask(taskId, TaskType.Major.ordinal(), actors);
		engine.task().complete(taskId, operator);
		return tasks;
	}
	
	public List<Task> transferAidant(String taskId, String operator, String... actors) {
		List<Task> tasks = engine.task().createNewTask(taskId, TaskType.Aidant.ordinal(), actors);
		engine.task().complete(taskId, operator);
		return tasks;
	}
	
	public void addSurrogate(Surrogate entity) {
		if(entity.getState() == null) {
			entity.setState(1);
		}
		engine.manager().saveOrUpdate(entity);
	}
	
	public void deleteSurrogate(String id) {
		engine.manager().deleteSurrogate(id);
	}
	
	public Surrogate getSurrogate(String id) {
		return engine.manager().getSurrogate(id);
	}
	
	public List<Surrogate> searchSurrogate(Page<Surrogate> page, QueryFilter filter) {
		return engine.manager().getSurrogate(page, filter);
	}
}
