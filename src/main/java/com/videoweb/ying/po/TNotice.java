package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;
import java.io.Serializable;



@TableName("t_notice")
@SuppressWarnings("serial")
public class TNotice extends BaseModel {

      /**
     * 标题
     */
         	@TableField("notice_title")
        	private String noticeTitle;
      /**
     * 简介
     */
         	@TableField("notice_brief")
        	private String noticeBrief;
      /**
     * 内容
     */
         	@TableField("notice_content")
        	private String noticeContent;
      /**
     * 提醒有效时长
     */
         	@TableField("long_time")
        	private Float longTime;


	public String getNoticeTitle() {
		return noticeTitle;
	}

	public void setNoticeTitle(String noticeTitle) {
		this.noticeTitle = noticeTitle;
	}

	public String getNoticeBrief() {
		return noticeBrief;
	}

	public void setNoticeBrief(String noticeBrief) {
		this.noticeBrief = noticeBrief;
	}

	public String getNoticeContent() {
		return noticeContent;
	}

	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}

	public Float getLongTime() {
		return longTime;
	}

	public void setLongTime(Float longTime) {
		this.longTime = longTime;
	}

}