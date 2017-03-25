package com.yb.funny.entity;


/**
 * 奖品实体类
 *
 * @author Yangbin
 */
public class Prize {
	
	//奖品id
	private int prizeid;
	//奖品数额
	private int prizeamount;
	//奖品信息
	private String prizeinfo;
	//兑换所需积分
	private int requiredpoints;
	//奖品类型 ：1、话费 ，2、流量
	private int prizetype;
	/**
	 * @return the prizeid
	 */
	public int getPrizeid() {
		return prizeid;
	}
	/**
	 * @param prizeid the prizeid to set
	 */
	public void setPrizeid(int prizeid) {
		this.prizeid = prizeid;
	}

	/**
	 * @return the prizeamount
	 */
	public int getPrizeamount() {
		return prizeamount;
	}
	/**
	 * @param prizeamount the prizeamount to set
	 */
	public void setPrizeamount(int prizeamount) {
		this.prizeamount = prizeamount;
	}
	/**
	 * @return the prizeinfo
	 */
	public String getPrizeinfo() {
		return prizeinfo;
	}
	/**
	 * @param prizeinfo the prizeinfo to set
	 */
	public void setPrizeinfo(String prizeinfo) {
		this.prizeinfo = prizeinfo;
	}
	/**
	 * @return the requiredpoints
	 */
	public int getRequiredpoints() {
		return requiredpoints;
	}
	/**
	 * @param requiredpoints the requiredpoints to set
	 */
	public void setRequiredpoints(int requiredpoints) {
		this.requiredpoints = requiredpoints;
	}
	/**
	 * @return the prizetype
	 */
	public int getPrizetype() {
		return prizetype;
	}
	/**
	 * @param prizetype the prizetype to set
	 */
	public void setPrizetype(int prizetype) {
		this.prizetype = prizetype;
	}
	
	
	

}
