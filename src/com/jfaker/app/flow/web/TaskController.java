package com.jfaker.app.flow.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snaker.engine.access.Page;
import org.snaker.engine.access.QueryFilter;
import org.snaker.engine.entity.HistoryOrder;
import org.snaker.engine.entity.HistoryTask;
import org.snaker.engine.entity.Order;
import org.snaker.engine.entity.Process;
import org.snaker.engine.entity.Task;
import org.snaker.engine.entity.WorkItem;
import org.snaker.engine.model.TaskModel.TaskType;
import org.snaker.engine.model.WorkModel;

import com.jfaker.framework.security.shiro.ShiroUtils;
import com.jfinal.core.ActionKey;

/**
 * Snaker流程引擎常用Controller
 * @author yuqs
 * @since 0.1
 */
public class TaskController extends SnakerController {
	private static final Logger log = LoggerFactory.getLogger(TaskController.class);
	
	public void active() {
		List<String> list = ShiroUtils.getGroups();
		list.add(ShiroUtils.getUsername());
		log.info(list.toString());
		String[] assignees = new String[list.size()];
		list.toArray(assignees);
		
		Page<WorkItem> majorPage = new Page<WorkItem>(5);
		Page<WorkItem> aidantPage = new Page<WorkItem>(3);
		Page<HistoryOrder> ccorderPage = new Page<HistoryOrder>(3);
		List<WorkItem> majorWorks = engine
				.query()
				.getWorkItems(majorPage, new QueryFilter()
				.setOperators(assignees)
				.setTaskType(TaskType.Major.ordinal()));
		List<WorkItem> aidantWorks = engine
				.query()
				.getWorkItems(aidantPage, new QueryFilter()
				.setOperators(assignees)
				.setTaskType(TaskType.Aidant.ordinal()));
		List<HistoryOrder> ccWorks = engine
				.query()
				.getCCWorks(ccorderPage, new QueryFilter()
				.setOperators(assignees)
				.setState(1));
		
		setAttr("majorWorks", majorWorks);
		setAttr("majorTotal", majorPage.getTotalCount());
		setAttr("aidantWorks", aidantWorks);
		setAttr("aidantTotal", aidantPage.getTotalCount());
		setAttr("ccorderWorks", ccWorks);
		setAttr("ccorderTotal", ccorderPage.getTotalCount());
		render("activeTask.jsp");
	}
	
	/**
	 * 根据当前用户查询待办任务列表
	 */
	public void user() {
		Page<WorkItem> page = new Page<WorkItem>();
		page.setPageNo(getParaToInt("pageNo", 1));
		engine.query().getWorkItems(page, 
				new QueryFilter().setOperator(ShiroUtils.getUsername()));
		setAttr("page", page);
		render("userTask.jsp");
	}

    public void addActor() {
        setAttr("orderId", getPara("orderId"));
        setAttr("taskName", getPara("taskName"));
        render("actor.jsp");
    }

    public void doAddActor() {
        List<Task> tasks = engine.query().getActiveTasks(new QueryFilter().setOrderId(getPara("orderId")));
        for(Task task : tasks) {
            if(task.getTaskName().equalsIgnoreCase(getPara("taskName")) && StringUtils.isNotEmpty(getPara("operator"))) {
                engine.task().addTaskActor(task.getId(), getPara("operator"));
            }
        }
        renderJson("success");
    }

    public void tip(String orderId, String taskName) {
        List<Task> tasks = engine.query().getActiveTasks(new QueryFilter().setOrderId(getPara("orderId")));
        StringBuilder builder = new StringBuilder();
        String createTime = "";
        for(Task task : tasks) {
            if(task.getTaskName().equalsIgnoreCase(getPara("taskName"))) {
                String[] actors = engine.query().getTaskActorsByTaskId(task.getId());
                for(String actor : actors) {
                    builder.append(actor).append(",");
                }
                createTime = task.getCreateTime();
            }
        }
        if(builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        Map<String, String> data = new HashMap<String, String>();
        data.put("actors", builder.toString());
        data.put("createTime", createTime);
        renderJson(data);
    }
	
	/**
	 * 活动任务查询列表
	 */
	public void activeMore() {
		Page<WorkItem> page = new Page<WorkItem>();
		page.setPageNo(getParaToInt("pageNo", 1));
		List<String> list = ShiroUtils.getGroups();
		list.add(ShiroUtils.getUsername());
		log.info(list.toString());
		String[] assignees = new String[list.size()];
		list.toArray(assignees);
		int taskType = getParaToInt("taskType");
		engine.query().getWorkItems(page, 
				new QueryFilter().setOperators(assignees).setTaskType(taskType));
		setAttr("page", page);
		setAttr("taskType", taskType);
		render("activeTaskMore.jsp");
	}
	
	/**
	 * 活动任务查询列表
	 */
	public void activeCCMore() {
		Page<HistoryOrder> page = new Page<HistoryOrder>();
		page.setPageNo(getParaToInt("pageNo", 1));
		List<String> list = ShiroUtils.getGroups();
		list.add(ShiroUtils.getUsername());
		log.info(list.toString());
		String[] assignees = new String[list.size()];
		list.toArray(assignees);
		engine
				.query()
				.getCCWorks(page, new QueryFilter()
				.setOperators(assignees)
				.setState(1));
		setAttr("page", page);
		render("activeCCMore.jsp");
	}
	
	/**
	 * 测试任务的执行
	 * @param model
	 * @return
	 */
	public void exec() {
		execute(getPara("taskId"), ShiroUtils.getUsername(), null);
		redirect("/snaker/task/active");;
	}
	
	/**
	 * 活动任务的驳回
	 * @param model
	 * @param taskId
	 * @return
	 */
	public void reject() {
		String error = "";
		try {
			executeAndJump(getPara("taskId"), ShiroUtils.getUsername(), null, null);
		} catch(Exception e) {
			error = "?error=1";
		}
		redirect("/snaker/task/active" + error);
	}
	
	/**
	 * 历史完成任务查询列表
	 * @param model
	 * @return
	 */
	public void history() {
		Page<WorkItem> page = new Page<WorkItem>();
		page.setPageNo(getParaToInt("pageNo", 1));
		engine.query().getHistoryWorkItems(page, 
				new QueryFilter().setOperator(ShiroUtils.getUsername()));
		setAttr("page", page);
		render("historyTask.jsp");
	}
	
	/**
	 * 历史任务撤回
	 */
	public void undo() {
		String returnMessage = "";
		try {
			engine.task().withdrawTask(getPara("taskId"), ShiroUtils.getUsername());
			returnMessage = "任务撤回成功.";
		} catch(Exception e) {
			returnMessage = e.getMessage();
		}
		setAttr("returnMessage", returnMessage);
		redirect("/snaker/task/history");
	}
	
	/**
	 * 流程实例查询
	 * @param model
	 * @param page
	 * @return
	 */
	@ActionKey("/snaker/order")
	public void order() {
		Page<HistoryOrder> page = new Page<HistoryOrder>();
		page.setPageNo(getParaToInt("pageNo", 1));
		engine.query().getHistoryOrders(page, new QueryFilter());
		setAttr("page", page);
		render("order.jsp");
	}
	
	/**
	 * 抄送实例已读
	 */
	@ActionKey("/snaker/ccread")
	public void ccread() {
		List<String> list = ShiroUtils.getGroups();
		list.add(ShiroUtils.getUsername());
		log.info(list.toString());
		String[] assignees = new String[list.size()];
		list.toArray(assignees);
		engine.order().updateCCStatus(getPara("id"), assignees);
		redirect(getPara("url"));
	}
	
	/**
	 * 流程实例all视图
	 */
	@ActionKey("/snaker/all")
	public String all() {
		String processId = getPara("processId");
		String orderId = getPara("orderId");
		String taskId = getPara("taskId");
		String type = getPara("type");
		Process process = engine.process().getProcessById(processId);
		if(StringUtils.isNotEmpty(process.getInstanceUrl())) {
			StringBuilder buffer = new StringBuilder();
			buffer.append("redirect:");
			buffer.append(process.getInstanceUrl());
			buffer.append("?processId=").append(processId);
			buffer.append("&processName=").append(process.getName());
			if(StringUtils.isNotEmpty(orderId)) {
				buffer.append("&orderId=").append(orderId);
			}
			if(StringUtils.isNotEmpty(taskId)) {
				buffer.append("&taskId=").append(taskId);
			}
			
			return buffer.toString();
		}
		List<WorkModel> models = process.getModel().getWorkModels();
		String currentTaskName = null;
		if(StringUtils.isNotEmpty(orderId)) {
			Order order = engine.query().getOrder(orderId);
			setAttr("order", order);
			List<HistoryTask> tasks = engine.query().getHistoryTasks(new QueryFilter().setOrderId(orderId));
			for(HistoryTask history : tasks) {
				setAttr("variable_" + history.getTaskName(), history.getVariableMap());
			}
		}
		
		if(StringUtils.isNotEmpty(taskId)) {
			Task task = engine.query().getTask(taskId);
			setAttr("task", task);
			currentTaskName = task.getTaskName();
		} else {
			currentTaskName = models.isEmpty() ? "" : models.get(0).getName();
		}
		
		if(!"cc".equalsIgnoreCase(type)) {
			setAttr("current", currentTaskName);
		}
		setAttr("works", models);
		setAttr("process", process);
		setAttr("operator", ShiroUtils.getUsername());
		return "flow/all";
	}
}
