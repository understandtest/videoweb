package com.videoweb.ying.service;

import com.videoweb.base.BaseService;
import com.videoweb.ying.po.FreeViewNumberSetting;
import org.springframework.stereotype.Service;

/**
 * @Author lbh
 * @Date 19-8-17
 */
@Service
public class FreeViewNumberSettingService extends BaseService<FreeViewNumberSetting> {

    public FreeViewNumberSetting findOne(Integer id) {
        return mapper.selectById(id);
    }



}
