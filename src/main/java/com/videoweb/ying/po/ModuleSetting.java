package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;
import lombok.Data;

/**
 * @Author hong
 * @Date 19-9-10
 * 功能模块实体类  id:1 为明星模块  2: 漫画后台
 */
@Data
@TableName("t_module_setting")
public class ModuleSetting extends BaseModel {


    @TableField("is_enable")
    private String isEnable;



}
