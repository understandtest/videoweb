package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;

import java.io.Serializable;


@TableName("t_level")
@SuppressWarnings("serial")
public class TLevel extends BaseModel {

    /**
     * 等级名称
     */
    private String name;
    /**
     * 等级排序
     */
    @TableField("sort_no")
    private Integer sortNo;
    /**
     * 等级图标
     */
    @TableField("le_icon")
    private String leIcon;
    /**
     * 推广几人
     */
    @TableField("ex_num")
    private Integer exNum;
    /**
     * 观影次数
     */
    @TableField("view_num")
    private Integer viewNum;
    /**
     * 缓存次数
     */
    @TableField("cache_num")
    private Integer cacheNum;

    /**
     * 增加会员天数
     */
    @TableField("day_num")
    private Integer dayNum;

    public Integer getDayNum() {
        return dayNum;
    }

    public void setDayNum(Integer dayNum) {
        this.dayNum = dayNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public String getLeIcon() {
        return leIcon;
    }

    public void setLeIcon(String leIcon) {
        this.leIcon = leIcon;
    }

    public Integer getExNum() {
        return exNum;
    }

    public void setExNum(Integer exNum) {
        this.exNum = exNum;
    }

    public Integer getViewNum() {
        return viewNum;
    }

    public void setViewNum(Integer viewNum) {
        this.viewNum = viewNum;
    }

    public Integer getCacheNum() {
        return cacheNum;
    }

    public void setCacheNum(Integer cacheNum) {
        this.cacheNum = cacheNum;
    }

}