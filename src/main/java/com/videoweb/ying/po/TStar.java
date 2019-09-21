package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;
import lombok.Data;

@Data
@TableName("t_star")
@SuppressWarnings("serial")
public class TStar extends BaseModel {

    /**
     * 头像
     */
    private String headpic;


    private Integer heat;

    /**
     * 名称
     */
    private String name;
    /**
     * 身高
     */
    @TableField("height_num")
    private String heightNum;
    /**
     * 三围
     */
    private String bwh;
    /**
     * 罩杯 基础数据
     */
    private String cup;
    /**
     * 简介
     */
    @TableField("brief_context")
    private String briefContext;



}