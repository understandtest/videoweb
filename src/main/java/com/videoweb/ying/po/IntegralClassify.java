package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;
import lombok.Data;

/**
 * @Author hong
 * @Date 19-8-26
 * 积分类型实体类
 */
@Data
@TableName("t_integral_classify")
public class IntegralClassify extends BaseModel {

    /**
     * 图标
     */
    private String icon;

    @TableField("icon_type")
    private String iconType;

    /**
     * 积分名称
     */
    private String name;

    /**
     * 积分数量
     */
    private Integer number;


    /**
     * 积分描述
     */
    @TableField("integral_desc")
    private String integralDesc;




}
