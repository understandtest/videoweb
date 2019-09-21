package com.videoweb.ying.po;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;

import java.io.Serializable;


@TableName("t_video")
@SuppressWarnings("serial")
public class TVideo extends BaseModel {

    /**
     * 视频名称
     */
    @TableField("video_name")
    private String videoName;
    /**
     * 视频链接
     */
    @TableField("video_url")
    private String videoUrl;
    /**
     * 明星ID
     */
    @TableField("star_id")
    private Integer starId;
    /**
     * 上映时间
     */
    @TableField("push_time")
    private Date pushTime;

    /**
     * 视频时长
     */
    @TableField("play_time")
    private Double playTime;
    /**
     * 简介
     */
    @TableField("brief_content")
    private String briefContent;
    /**
     * 封面
     */
    @TableField("video_cover")
    private String videoCover;
    /**
     * 播放次数
     */
    @TableField("play_num")
    private Integer playNum;
    /**
     * 失效次数
     */
    @TableField("lose_num")
    private Integer loseNum;

    @TableField("classify_id")
    private Integer classifyId;

    /**
     * 是否上架
     */
    @TableField("is_push")
    private Integer isPush;

    /**
     * 渠道ID
     */
    @TableField("channel_id")
    private Integer channelId;

    public Double getPlayTime() {
        return playTime;
    }

    public void setPlayTime(Double playTime) {
        this.playTime = playTime;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Integer getStarId() {
        return starId;
    }

    public void setStarId(Integer starId) {
        this.starId = starId;
    }

    public Date getPushTime() {
        return pushTime;
    }

    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
    }

    public String getBriefContent() {
        return briefContent;
    }

    public void setBriefContent(String briefContent) {
        this.briefContent = briefContent;
    }

    public String getVideoCover() {
        return videoCover;
    }

    public void setVideoCover(String videoCover) {
        this.videoCover = videoCover;
    }

    public Integer getPlayNum() {
        return playNum;
    }

    public void setPlayNum(Integer playNum) {
        this.playNum = playNum;
    }


    public Integer getLoseNum() {
        return loseNum;
    }

    public void setLoseNum(Integer loseNum) {
        this.loseNum = loseNum;
    }

    public Integer getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(Integer classifyId) {
        this.classifyId = classifyId;
    }

    public Integer getIsPush() {
        return isPush;
    }

    public void setIsPush(Integer isPush) {
        this.isPush = isPush;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }


}