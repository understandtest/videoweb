package com.videoweb.ying.service;

import com.videoweb.base.BaseService;
import com.videoweb.ying.dao.ModuleSettingMapper;
import com.videoweb.ying.po.ModuleSetting;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author hong
 * @Date 19-9-10
 */
@Service
public class ModuleSettingService extends BaseService<ModuleSetting> {


    /**
     * 根据id查询
     * @param id
     * @return
     */
    public Map<String, Object> findOne(Integer id){
        return ((ModuleSettingMapper)mapper).findOne(id);
    }



}
