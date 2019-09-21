package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;

/**
 * Author lbh
 * Date 2019-07-24
 */
@TableName("t_short_realm_name_setting")
public class ShortRealmNameSetting extends BaseModel {

    /**
     * 商家key
     */
    private String key;

    /**
     * 秘钥
     */
    private String secret;

    /**
     * 短域名描述
     */
    @TableField("short_realm_name_desc")
    private String shortRealmNameDesc;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getShortRealmNameDesc() {
        return shortRealmNameDesc;
    }

    public void setShortRealmNameDesc(String shortRealmNameDesc) {
        this.shortRealmNameDesc = shortRealmNameDesc;
    }
}
