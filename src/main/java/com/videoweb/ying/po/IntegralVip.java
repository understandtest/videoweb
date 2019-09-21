package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;
import lombok.Data;

/**
 * @Author hong
 * @Date 19-8-26
 * 积分vip实体类
 */
@Data
@TableName("t_integral_vip")
public class IntegralVip extends BaseModel {

    private String icon;

    @TableField("icon_type")
    private String iconType;

    @TableField("integral_number")
    private Integer integralNumber;

    @TableField("day_number")
    private Integer dayNumber;

}
