package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;

/**
 * Author lbh
 * Date 2019-07-25
 */
@TableName("t_caricature_chapter_img")
public class caricatureChapterImg extends BaseModel {

    @TableField("sort_no")
    private Integer sortNo; //排序

    @TableField("caricature_chapter_id")
    private Integer caricatureChapterId; //所属章节

    @TableField("img_url")
    private String imgUrl; //图片地址

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public Integer getCaricatureChapterId() {
        return caricatureChapterId;
    }

    public void setCaricatureChapterId(Integer caricatureChapterId) {
        this.caricatureChapterId = caricatureChapterId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
