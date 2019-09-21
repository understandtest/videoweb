package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;
import java.io.Serializable;



@TableName("t_video_comment_dianzan")
@SuppressWarnings("serial")
public class TVideoCommentDianzan extends BaseModel {

      /**
     * 视频ID
     */
         	   @TableField("video_comment_id")
        	private Integer videCommentId;
      /**
     * 会员ID
     */
         	   @TableField("member_id")
        	private Integer memberId;


	

	public Integer getVideCommentId() {
		return videCommentId;
	}

	public void setVideCommentId(Integer videCommentId) {
		this.videCommentId = videCommentId;
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

}