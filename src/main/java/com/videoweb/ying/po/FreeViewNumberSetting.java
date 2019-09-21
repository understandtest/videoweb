package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;
import lombok.Data;

/**
 * @Author lbh
 * @Date 19-8-17
 * 免费播放次数
 */
@Data
@TableName("t_free_view_number_setting")
public class FreeViewNumberSetting extends BaseModel {

    /**
     * 免费播放次数
     */
    @TableField("free_number")
    private Integer freeNumber;

}
