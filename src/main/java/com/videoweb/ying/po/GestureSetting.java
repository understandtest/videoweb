package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;
import lombok.Data;

/**
 * @Author hong
 * @Date 19-8-26
 */
@Data
@TableName("t_gesture_setting")
public class GestureSetting extends BaseModel {

    /**
     * 手势状态:1开启,0关闭
     */
    private String status;

}
