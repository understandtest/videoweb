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
@TableName("t_integral_history")
public class IntegralHistory extends BaseModel {

    /**
     * 积分列别id
     */
    @TableField("integral_classify_id")
    private Integer integralClassifyId;

    /**
     * 用户id
     */
    @TableField("member_id")
    private Integer memberId;

}
