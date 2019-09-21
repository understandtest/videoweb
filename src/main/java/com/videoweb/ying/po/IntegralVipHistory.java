package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;
import lombok.Data;

/**
 * @Author hong
 * @Date 19-8-26
 */
@Data
@TableName("t_integral_vip_history")
public class IntegralVipHistory extends BaseModel {

    @TableField("member_id")
    private Integer memberId;

    @TableField("integral_vip")
    private Integer integralVip;

}
