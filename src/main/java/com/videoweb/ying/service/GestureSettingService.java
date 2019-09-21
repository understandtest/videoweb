package com.videoweb.ying.service;

import com.videoweb.base.BaseService;
import com.videoweb.ying.po.GestureSetting;
import org.springframework.stereotype.Service;

/**
 * @Author hong
 * @Date 19-8-26
 */
@Service
public class GestureSettingService extends BaseService<GestureSetting> {

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public GestureSetting findOne(Integer id){
        return mapper.selectById(id);
    }

}
