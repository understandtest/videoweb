package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;


/**
 * AUTHER lbh
 * Date 2019/5/21
 */
@TableName("t_video_pay")
public class TVideoPay extends BaseModel {

    @TableField("video_id")
    private Integer videoId;

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

}
