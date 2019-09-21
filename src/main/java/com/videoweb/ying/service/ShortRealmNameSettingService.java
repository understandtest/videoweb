package com.videoweb.ying.service;

import com.videoweb.base.BaseService;
import com.videoweb.ying.po.ShortRealmNameSetting;
import org.springframework.stereotype.Service;

/**
 * Author lbh
 * Date 2019-07-24
 */
@Service
public class ShortRealmNameSettingService extends BaseService<ShortRealmNameSetting> {

    /**
     * 查询短域名设置信息
     * @return
     */
    public ShortRealmNameSetting findOne(){
        return mapper.selectById(1);
    }



}
