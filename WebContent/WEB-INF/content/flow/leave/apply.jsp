<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="en">
	<head>
		<title>请假流程</title>
		<%@ include file="/common/meta.jsp"%>
		<link rel="stylesheet" href="${ctx}/styles/css/style.css" type="text/css" media="all" />
		<script src="${ctx}/styles/js/jquery-1.8.3.min.js" type="text/javascript"></script>
		<script src="${ctx}/styles/js/snaker/snaker.util.js" type="text/javascript"></script>
	</head>

	<body>
		<form id="inputForm" action="${ctx }/snaker/flow/process" method="post" target="mainFrame">
			<input type="hidden" name="processId" value="${processId }" />
			<input type="hidden" name="orderId" value="${orderId }" />
			<input type="hidden" name="taskId" value="${taskId }" />
			<table class="table_all" align="center" border="0" cellpadding="0"
			cellspacing="0" style="margin-top: 0px">
				<tr>
					<td class="td_table_1"><span>请假人名称：</span></td>
					<td class="td_table_2" colspan="3">
						<input type="text" class="input_240" readonly="readonly" name="S_apply.operator" value="${variable_apply['apply.operator'] != null ? variable_apply['apply.operator'] : operator  }" />
					</td>
				</tr>
				<tr>
					<td class="td_table_1"><span>请假理由：</span></td>
					<td class="td_table_2" colspan="3">
						<textarea class="input_textarea_320" id="reason" name="S_reason">${variable_apply['reason'] }</textarea>
					</td>
				</tr>
				<tr>
					<td class="td_table_1"><span>请假天数：</span></td>
					<td class="td_table_2" colspan="3">
						<input type="text" class="input_240" id="day" name="I_day" value="${variable_apply['day'] }" />天
					</td>
				</tr>
				<tr>
					<td class="td_table_1"><span>部门经理：</span></td>
					<td class="td_table_2">
						<input type="text" class="input_240" name="S_approveDept.operator" value="${variable_apply['approveDept.operator'] != null ? variable_apply['approveDept.operator'] : operator }" />
					</td>
					<td class="td_table_1"><span>总经理：</span></td>
					<td class="td_table_2">
						<input type="text" class="input_240" name="S_approveBoss.operator" value="${variable_apply['approveBoss.operator'] != null ? variable_apply['approveBoss.operator'] : operator }" />
					</td>
				</tr>
			</table>
			<table align="center" border="0" cellpadding="0"
				cellspacing="0">
				<tr align="left">
					<td colspan="1">
						<input type="submit" class="button_70px" name="submit" value="提交">
						&nbsp;&nbsp;
						<input type="button" class="button_70px" name="reback" value="返回"
							onclick="history.back()">
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
