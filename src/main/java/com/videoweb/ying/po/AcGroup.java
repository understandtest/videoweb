package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;

/**
 * @Author lbh
 * @Date 2019/8/7
 * 交流群
 */
@TableName("t_ac_group")
public class AcGroup extends BaseModel {

    @TableField("href_url")
    private String hrefUrl;

    public String getHrefUrl() {
        return hrefUrl;
    }

    public void setHrefUrl(String hrefUrl) {
        this.hrefUrl = hrefUrl;
    }
}
