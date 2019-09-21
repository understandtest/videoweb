package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;

/**
 * Author lbh
 * Date 2019-07-25
 */
@TableName("t_caricature_chapter")
public class CaricatureChapter extends BaseModel {

    private String cover;

    @TableField("caricature_id")
    private Integer caricatureId;

    private String name;

    @TableField("sort_no")
    private String sortNo;

    @TableField("cover_type")
    private String coverType;

    @TableField("is_collect_fees")
    private String isCollectFees;

    public String getCoverType() {
        return coverType;
    }

    public void setCoverType(String coverType) {
        this.coverType = coverType;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Integer getCaricatureId() {
        return caricatureId;
    }

    public void setCaricatureId(Integer caricatureId) {
        this.caricatureId = caricatureId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortNo() {
        return sortNo;
    }

    public void setSortNo(String sortNo) {
        this.sortNo = sortNo;
    }

    public String getIsCollectFees() {
        return isCollectFees;
    }

    public void setIsCollectFees(String isCollectFees) {
        this.isCollectFees = isCollectFees;
    }
}
