package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;

/**
 * Author lbh
 * Date 2019-07-27
 * 动漫收藏表
 */
@TableName("t_caricature_bookcase")
public class CaricatureBookcase extends BaseModel {

    @TableField("caricature_id")
    private Integer caricatureId;

    @TableField("member_id")
    private Integer memberId;

    public Integer getCaricatureId() {
        return caricatureId;
    }

    public void setCaricatureId(Integer caricatureId) {
        this.caricatureId = caricatureId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }
}
