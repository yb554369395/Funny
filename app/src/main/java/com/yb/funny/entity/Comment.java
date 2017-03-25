package com.yb.funny.entity;

import java.util.Date;

/**
 * 评论实体类
 * 
 * @author Yangbin
 *
 */
public class Comment {

	// 评论ID
	private int commentid;
	// 评论人ID
	private int commentatorid;
	// 评论人昵称
	private String commentatorname;
	// 评论人头像
	private String commentatoricon;
	// 所属资源ID
	private int belongid;
	// 评论内容
	private String comments;
	// 评论时间
	private Date commenttime;

	/**
	 * @return the commentatorname
	 */
	public String getCommentatorname() {
		return commentatorname;
	}

	/**
	 * @param commentatorname
	 *            the commentatorname to set
	 */
	public void setCommentatorname(String commentatorname) {
		this.commentatorname = commentatorname;
	}

	/**
	 * @return the commentatoricon
	 */
	public String getCommentatoricon() {
		return commentatoricon;
	}

	/**
	 * @param commentatoricon
	 *            the commentatoricon to set
	 */
	public void setCommentatoricon(String commentatoricon) {
		this.commentatoricon = commentatoricon;
	}

	/**
	 * @return the commentid
	 */
	public int getCommentid() {
		return commentid;
	}

	/**
	 * @param commentid
	 *            the commentid to set
	 */
	public void setCommentid(int commentid) {
		this.commentid = commentid;
	}

	/**
	 * @return the commentatorid
	 */
	public int getCommentatorid() {
		return commentatorid;
	}

	/**
	 * @param commentatorid
	 *            the commentatorid to set
	 */
	public void setCommentatorid(int commentatorid) {
		this.commentatorid = commentatorid;
	}

	/**
	 * @return the belongid
	 */
	public int getBelongid() {
		return belongid;
	}

	/**
	 * @param belongid
	 *            the belongid to set
	 */
	public void setBelongid(int belongid) {
		this.belongid = belongid;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments
	 *            the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return the commenttime
	 */
	public Date getCommenttime() {
		return commenttime;
	}

	/**
	 * @param commenttime
	 *            the commenttime to set
	 */
	public void setCommenttime(Date commenttime) {
		this.commenttime = commenttime;
	}

}
