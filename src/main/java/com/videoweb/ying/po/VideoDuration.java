package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;

/**
 * Author lbh
 * Date 2019/6/14
 */
@TableName("t_video_duration")
public class VideoDuration extends BaseModel {

    /**
     * 播放时长
     */
    @TableField("play_duration")
    private int playDuration;

    public int getPlayDuration() {
        return playDuration;
    }

    public void setPlayDuration(int playDuration) {
        this.playDuration = playDuration;
    }
}
