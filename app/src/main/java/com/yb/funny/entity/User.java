package com.yb.funny.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 用户实体类
 *
 * @author Yangbin
 */
public class User implements Serializable{
	// 用户ID
	private int userid;
	// 用户名
	private String username;
	// 密码
	private String password;
	// 性别
	private int sex;
	// 积分
	private int integral;
	// 用户昵称
	private String name;
	// 用户头像
	private String icon;
	// 用户简介
	private String introduction;
    //用户发表的内容条数
    private int resourcecount;
	// 最后一次登录时间
	private Date lastlogin;

	/**
	 * @return the userid
	 */
	public int getUserid() {
		return userid;
	}

	/**
	 * @param userid
	 *            the userid to set
	 */
	public void setUserid(int userid) {
		this.userid = userid;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the sex
	 */
	public int getSex() {
		return sex;
	}

	/**
	 * @param sex
	 *            the sex to set
	 */
	public void setSex(int sex) {
		this.sex = sex;
	}

	/**
	 * @return the integral
	 */
	public int getIntegral() {
		return integral;
	}

	/**
	 * @param integral
	 *            the integral to set
	 */
	public void setIntegral(int integral) {
		this.integral = integral;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon
	 *            the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * @return the introduction
	 */
	public String getIntroduction() {
		return introduction;
	}

	/**
	 * @param introduction
	 *            the introduction to set
	 */
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

    public int getResourcecount() {
        return resourcecount;
    }

    public void setResourcecount(int resourcecount) {
        this.resourcecount = resourcecount;
    }

    public Date getLastlogin() {
        return lastlogin;
    }

    public void setLastlogin(Date lastlogin) {
        this.lastlogin = lastlogin;
    }

    /**
     * 增加积分
     */
	public void addIntegral(int integral){
        this.integral += integral;
    }

    /**
     * 减少积分
     */
	public void lessIntegral(int integral){
        this.integral -= integral;
    }

    /**
     * 增加发表的内容条数
     */
    public void addResourceCount(){
        this.resourcecount++;
    }


}
