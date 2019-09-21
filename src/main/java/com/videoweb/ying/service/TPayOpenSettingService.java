package com.videoweb.ying.service;

import com.videoweb.base.BaseService;
import com.videoweb.ying.po.TPayOpenSetting;
import org.springframework.stereotype.Service;

/**
 * Author lbh
 * Date 2019/7/11
 * Describe
 */
@Service
public class TPayOpenSettingService extends BaseService<TPayOpenSetting> {


    /**
     * 查询是否开启支付功能
     * @return
     */
    public TPayOpenSetting findOne(){
        return mapper.selectById(1);
    }


}
