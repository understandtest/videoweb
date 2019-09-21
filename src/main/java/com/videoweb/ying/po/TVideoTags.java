package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;
import java.io.Serializable;



@TableName("t_video_tags")
@SuppressWarnings("serial")
public class TVideoTags extends BaseModel {

      /**
     * 视频ID
     */
         	   @TableField("video_id")
        	private Integer videoId;
      /**
     * 标签ID
     */
         	   @TableField("tag_id")
        	private Integer tagId;


	public Integer getVideoId() {
		return videoId;
	}

	public void setVideoId(Integer videoId) {
		this.videoId = videoId;
	}

	public Integer getTagId() {
		return tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}

}