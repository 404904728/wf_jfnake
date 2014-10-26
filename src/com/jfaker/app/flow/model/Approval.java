package com.jfaker.app.flow.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;

public class Approval extends Model<Approval> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2386449387832849587L;
	public static final Approval dao = new Approval();
	
	public List<Approval> findByFlow(String orderId, String taskName) {
		return Approval.dao.find("select * from flow_approval where orderId = ? and taskName = ?", orderId, taskName);
	}
}
