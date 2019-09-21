package com.videoweb.ying.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.videoweb.base.BaseModel;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_member")
@SuppressWarnings("serial")
public class TMember extends BaseModel {

    /**
     * 手机号
     */
    private String tel;
    /**
     * 头像
     */
    private String headpic;

    /**
     * 积分数量
     */
    @TableField("integral_number")
    private Integer integralNumber;

    /**
     * 昵称
     */
    @TableField("nick_name")
    private String nickName;
    /**
     * 级别ID
     */
    @TableField("level_id")
    private Integer levelId;
    /**
     * 每日观影次数
     */
    @TableField("view_num")
    private Integer viewNum;
    /**
     * 已使用次数
     */
    @TableField("used_view_num")
    private Integer usedViewNum;
    /**
     * 每日缓存次数
     */
    @TableField("cache_num")
    private Integer cacheNum;
    /**
     * 已使用缓存次数
     */
    @TableField("used_cache_num")
    private Integer usedCacheNum;
    /**
     * 是否为VIP  0=否 1=是
     */
    @TableField("is_vip")
    private Integer isVip;

    @TableField("vip_id")
    private Integer vipId;
    /**
     * VIP截止时间
     */
    @TableField("vip_date")
    private Date vipDate;
    /**
     * 推广码
     */
    @TableField("extension_code")
    private String extensionCode;
    /**
     * 1= 男 2=女 3=未知
     */
    private Integer sex;
    /**
     * 是否强制提醒升级
     */
    @TableField("is_remind")
    private Integer isRemind;

    /**
     * 临时观影次数
     */
    @TableField("tmp_view_num")
    private Integer tmpViewNum;
    /**
     * 是否保存二维码
     */
    @TableField("is_save_qr")
    private Integer isSaveQr;


    /**
     * 钻石数
     */
    @TableField("cron_num")
    private Integer cronNum;





}