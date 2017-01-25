package com.yb.funny.entity;

import java.util.Date;

public class Order {
	
	//订单编号
	private int orderid;
	//订单类型
	private int ordertype;
	//订单数额
	private int orderamount;
	//电话号码
	private int phonenumber;
	//订货人
	private int orderperson;
	//订单发起时间
	private Date starttime;
	//订单状态
	private int status;
	
	
	
	
	/**
	 * @return the phonenumber
	 */
	public int getPhonenumber() {
		return phonenumber;
	}
	/**
	 * @param phonenumber the phonenumber to set
	 */
	public void setPhonenumber(int phonenumber) {
		this.phonenumber = phonenumber;
	}
	/**
	 * @return the orderid
	 */
	public int getOrderid() {
		return orderid;
	}
	/**
	 * @param orderid the orderid to set
	 */
	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}
	/**
	 * @return the ordertype
	 */
	public int getOrdertype() {
		return ordertype;
	}
	/**
	 * @param ordertype the ordertype to set
	 */
	public void setOrdertype(int ordertype) {
		this.ordertype = ordertype;
	}
	/**
	 * @return the orderamount
	 */
	public int getOrderamount() {
		return orderamount;
	}
	/**
	 * @param orderamount the orderamount to set
	 */
	public void setOrderamount(int orderamount) {
		this.orderamount = orderamount;
	}
	/**
	 * @return the orderperson
	 */
	public int getOrderperson() {
		return orderperson;
	}
	/**
	 * @param orderperson the orderperson to set
	 */
	public void setOrderperson(int orderperson) {
		this.orderperson = orderperson;
	}
	/**
	 * @return the starttime
	 */
	public Date getStarttime() {
		return starttime;
	}
	/**
	 * @param starttime the starttime to set
	 */
	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	
	
	

}
