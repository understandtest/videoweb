package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;
import lombok.Data;

/**
 * @Author hong
 * @Date 19-9-10
 */
@Data
@TableName("t_member_star")
public class MemberStar extends BaseModel {

    @TableField("member_id")
    private Integer memberId;

    @TableField("star_id")
    private Integer starId;

}
