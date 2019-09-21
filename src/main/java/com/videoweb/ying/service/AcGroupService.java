package com.videoweb.ying.service;

import com.videoweb.base.BaseService;
import com.videoweb.ying.dao.AcGroupMapper;
import com.videoweb.ying.po.AcGroup;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @Author lbh
 * @Date 2019/8/7
 */
@Service
public class AcGroupService extends BaseService<AcGroup> {



    public Map<String,Object> getAcGroup(Integer id) {
        return ((AcGroupMapper)mapper).getAcGroup(id);
    }
}
