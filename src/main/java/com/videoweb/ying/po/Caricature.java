package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;

/**
 * Author lbh
 * Date 2019-07-25
 * 漫画实体类
 */
@TableName("t_caricature")
public class Caricature extends BaseModel {

    private String name;

    private String cover;

    @TableField("desc_cover")
    private String descCover; //详情封面

    @TableField("desc_cover_type")
    private String descCoverType; //详情封面类型

    @TableField("cover_type")
    private String coverType;

    @TableField("is_push")
    private String isPush; //是否上架

    @TableField("caricature_desc")
    private String caricatureDesc; //漫画描述

    @TableField("watch_num")
    private Integer watchNum; //观看次数

    @TableField("chapter_num")
    private Integer chapterNum; //章节次数

    @TableField("caricature_classify_id")
    private Integer caricatureClassifyId; //分类id

    public String getDescCover() {
        return descCover;
    }

    public void setDescCover(String descCover) {
        this.descCover = descCover;
    }

    public String getDescCoverType() {
        return descCoverType;
    }

    public void setDescCoverType(String descCoverType) {
        this.descCoverType = descCoverType;
    }

    public String getCoverType() {
        return coverType;
    }

    public void setCoverType(String coverType) {
        this.coverType = coverType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getIsPush() {
        return isPush;
    }

    public void setIsPush(String isPush) {
        this.isPush = isPush;
    }

    public String getCaricatureDesc() {
        return caricatureDesc;
    }

    public void setCaricatureDesc(String caricatureDesc) {
        this.caricatureDesc = caricatureDesc;
    }

    public Integer getWatchNum() {
        return watchNum;
    }

    public void setWatchNum(Integer watchNum) {
        this.watchNum = watchNum;
    }

    public Integer getChapterNum() {
        return chapterNum;
    }

    public void setChapterNum(Integer chapterNum) {
        this.chapterNum = chapterNum;
    }

    public Integer getCaricatureClassifyId() {
        return caricatureClassifyId;
    }

    public void setCaricatureClassifyId(Integer caricatureClassifyId) {
        this.caricatureClassifyId = caricatureClassifyId;
    }
}
