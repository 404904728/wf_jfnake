package com.jfaker.app.flow.web;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.snaker.engine.SnakerEngine;
import org.snaker.engine.access.Page;
import org.snaker.engine.access.QueryFilter;
import org.snaker.engine.entity.HistoryOrder;
import org.snaker.engine.entity.HistoryTask;
import org.snaker.engine.entity.Process;
import org.snaker.engine.entity.Task;
import org.snaker.engine.helper.AssertHelper;
import org.snaker.engine.helper.StreamHelper;
import org.snaker.engine.helper.StringHelper;
import org.snaker.engine.model.ProcessModel;

import com.jfaker.app.flow.SnakerEngineFacets;
import com.jfaker.app.flow.SnakerHelper;
import com.jfaker.framework.security.shiro.ShiroUtils;
import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;

/**
 * 流程定义
 * @author yuqs
 * @since 0.1
 */
public class ProcessController extends Controller {
	private SnakerEngine getEngine() {
		return SnakerEngineFacets.facets.getEngine();
	}
	/**
	 * 流程定义查询列表
	 */
	public void index() {
		QueryFilter filter = new QueryFilter();
		String displayName = getPara("displayName");
		if(StringHelper.isNotEmpty(displayName)) {
			filter.setDisplayName(displayName);
		}
		Page<Process> page = new Page<Process>();
		page.setPageNo(getParaToInt("pageNo", 1));
		getEngine().process().getProcesss(page, filter);
		setAttr("page", page);
		render("processList.jsp");
	}
	
	/**
	 * 初始化流程定义
	 */
	public void init() {
		SnakerEngineFacets.facets.initFlows();
		redirect("/snaker/process");
	}
	
	/**
	 * 根据流程定义部署
	 */
	public void deploy() {
		render("processDeploy.jsp");
	}
	
	/**
	 * 新建流程定义
	 */
	public void add() {
		render("processAdd.jsp");
	}
	
	/**
	 * 新建流程定义[web流程设计器]
	 */
	public void designer() {
		String processId = getPara("processId");
		if(StringUtils.isNotEmpty(processId)) {
			Process process = getEngine().process().getProcessById(processId);
			AssertHelper.notNull(process);
			ProcessModel processModel = process.getModel();
			if(processModel != null) {
				String json = SnakerHelper.getModelJson(processModel);
				setAttr("process", json);
			}
			setAttr("processId", processId);
		}
		render("processDesigner.jsp");
	}
	
	/**
	 * 编辑流程定义
	 */
	public void edit() {
		Process process = getEngine().process().getProcessById(getPara());
		setAttr("process", process);
		if(process.getDBContent() != null) {
            try {
            	setAttr("content", StringHelper.textXML(new String(process.getDBContent(), "UTF-8")));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
		render("processEdit.jsp");
	}
	
	/**
	 * 根据流程定义ID，删除流程定义
	 */
	public void delete() {
		getEngine().process().undeploy(getPara());
		redirect("/snaker/process");
	}
	
	/**
	 * 添加流程定义后的部署
	 */
	public void doFileDeploy() {
		InputStream input = null;
		try {
			String id = getPara("id");
			UploadFile file = getFile("snakerFile");
			input = new FileInputStream(file.getFile());
			if(StringUtils.isNotEmpty(id)) {
				getEngine().process().redeploy(id, input);
			} else {
				getEngine().process().deploy(input);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		redirect("/snaker/process");
	}
	
	/**
	 * 保存流程定义[web流程设计器]
	 * @param model
	 * @return
	 */
	public void doStringDeploy() {
		InputStream input = null;
		try {
			String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" + SnakerHelper.convertXml(getPara("model"));
			System.out.println("model xml=\n" + xml);
			String id = getPara("id");
			input = StreamHelper.getStreamFromString(xml);
			if(StringUtils.isNotEmpty(id)) {
				getEngine().process().redeploy(id, input);
			} else {
				getEngine().process().deploy(input);
			}
		} catch (Exception e) {
			e.printStackTrace();
			renderJson(false);
		} finally {
			if(input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		renderJson(true);
	}
	
	public void processStart() {
		SnakerEngineFacets.facets.startInstanceByName(getPara("processName"), null, ShiroUtils.getUsername(), null);
		redirect("/snaker/process");
	}
	
	public void json() {
		String orderId = getPara("orderId");
		HistoryOrder order = getEngine().query().getHistOrder(orderId);
		List<Task> tasks = getEngine().query().getActiveTasks(new QueryFilter().setOrderId(orderId));
		Process process = getEngine().process().getProcessById(order.getProcessId());
		AssertHelper.notNull(process);
		ProcessModel model = process.getModel();
		Map<String, String> jsonMap = new HashMap<String, String>();
		if(model != null) {
			jsonMap.put("process", SnakerHelper.getModelJson(model));
		}
		
		//{"activeRects":{"rects":[{"paths":[],"name":"任务3"},{"paths":[],"name":"任务4"},{"paths":[],"name":"任务2"}]},"historyRects":{"rects":[{"paths":["TO 任务1"],"name":"开始"},{"paths":["TO 分支"],"name":"任务1"},{"paths":["TO 任务3","TO 任务4","TO 任务2"],"name":"分支"}]}}
		if(tasks != null && !tasks.isEmpty()) {
			jsonMap.put("active", SnakerHelper.getActiveJson(tasks));
		}
		renderJson(jsonMap);
	}
	
	public void display() {
		String orderId = getPara("orderId");
		HistoryOrder order = getEngine().query().getHistOrder(orderId);
		setAttr("order", order);
		List<HistoryTask> tasks = getEngine().query().getHistoryTasks(new QueryFilter().setOrderId(orderId));
		setAttr("tasks", tasks);
		render("processView.jsp");
	}
}
