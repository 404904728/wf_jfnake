package com.jfaker.app.modules.model;

import com.jfinal.plugin.activerecord.Model;

public class Borrow extends Model<Borrow> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3366223825642786110L;
	public static final Borrow dao = new Borrow();
	
	public Borrow findByOrderId(String orderId) {
		return Borrow.dao.findFirst("select * from flow_borrow where orderId = ? ", orderId);
	}
}
