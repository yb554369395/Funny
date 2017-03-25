package com.yb.funny.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 资源实体类
 *
 * @author Yangbin
 */
public class Resource implements Serializable{

	// 资源ID
	private int resourceid;
	// 资源文字内容
	private String resourcetext;
	// 资源图片内容
	private String resourcepic;
	// 点赞数
	private long pointpraiseno;
	// 评论数
	private long commentno;
	// 发布时间
	private Date publishedtime;
	// 发布人ID
	private int publisher;
	// 发布人昵称
	private String publishername;
	// 发布人头像
	private String publishericon;
	// 从网上获取的资源的识别码
	private String hashid;

	/**
	 * @return the publishername
	 */
	public String getPublishername() {
		return publishername;
	}

	/**
	 * @param publishername
	 *            the publishername to set
	 */
	public void setPublishername(String publishername) {
		this.publishername = publishername;
	}

	/**
	 * @return the publishericon
	 */
	public String getPublishericon() {
		return publishericon;
	}

	/**
	 * @param publishericon
	 *            the publishericon to set
	 */
	public void setPublishericon(String publishericon) {
		this.publishericon = publishericon;
	}

	/**
	 * @return the hashid
	 */
	public String getHashid() {
		return hashid;
	}

	/**
	 * @param hashid
	 *            the hashid to set
	 */
	public void setHashid(String hashid) {
		this.hashid = hashid;
	}

	/**
	 * @return the resourceid
	 */
	public int getResourceid() {
		return resourceid;
	}

	/**
	 * @param resourceid
	 *            the resourceid to set
	 */
	public void setResourceid(int resourceid) {
		this.resourceid = resourceid;
	}

	/**
	 * @return the resourcetext
	 */
	public String getResourcetext() {
		return resourcetext;
	}

	/**
	 * @param resourcetext
	 *            the resourcetext to set
	 */
	public void setResourcetext(String resourcetext) {
		this.resourcetext = resourcetext;
	}

	/**
	 * @return the resourcepic
	 */
	public String getResourcepic() {
		return resourcepic;
	}

	/**
	 * @param resourcepic
	 *            the resourcepic to set
	 */
	public void setResourcepic(String resourcepic) {
		this.resourcepic = resourcepic;
	}

	/**
	 * @return the pointpraiseno
	 */
	public long getPointpraiseno() {
		return pointpraiseno;
	}

	/**
	 * @param pointpraiseno
	 *            the pointpraiseno to set
	 */
	public void setPointpraiseno(long pointpraiseno) {
		this.pointpraiseno = pointpraiseno;
	}

	/**
	 * @return the commentno
	 */
	public long getCommentno() {
		return commentno;
	}

	/**
	 * @param commentno
	 *            the commentno to set
	 */
	public void setCommentno(long commentno) {
		this.commentno = commentno;
	}

	/**
	 * @return the publishedtime
	 */
	public Date getPublishedtime() {
		return publishedtime;
	}

	/**
	 * @param publishedtime
	 *            the publishedtime to set
	 */
	public void setPublishedtime(Date publishedtime) {
		this.publishedtime = publishedtime;
	}

	/**
	 * @return the publisher
	 */
	public int getPublisher() {
		return publisher;
	}

	/**
	 * @param publisher
	 *            the publisher to set
	 */
	public void setPublisher(int publisher) {
		this.publisher = publisher;
	}

}
