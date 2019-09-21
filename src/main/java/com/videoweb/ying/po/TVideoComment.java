package com.videoweb.ying.po;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;
import java.io.Serializable;



@TableName("t_video_comment")
@SuppressWarnings("serial")
public class TVideoComment extends BaseModel {

      /**
     * 视频ID
     */
         	   @TableField("video_id")
        	private Integer videoId;
      /**
     * 会员ID
     */
         	   @TableField("member_id")
        	private Integer memberId;
      /**
     * 评论内容
     */
         	   @TableField("com_content")
        	private String comContent;
      /**
     * 评论时间
     */
         	   @TableField("com_time")
        	private Date comTime;
    

	public Integer getVideoId() {
		return videoId;
	}

	public void setVideoId(Integer videoId) {
		this.videoId = videoId;
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public String getComContent() {
		return comContent;
	}

	public void setComContent(String comContent) {
		this.comContent = comContent;
	}

	public Date getComTime() {
		return comTime;
	}

	public void setComTime(Date comTime) {
		this.comTime = comTime;
	}
}