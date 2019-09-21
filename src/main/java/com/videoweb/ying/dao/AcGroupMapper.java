package com.videoweb.ying.dao;


import com.videoweb.base.BaseMapper;
import com.videoweb.ying.po.AcGroup;

import java.util.Map;

/**
 * @Author lbh
 * @Date 2019/8/7
 */
public interface AcGroupMapper extends BaseMapper<AcGroup> {
    Map<String, Object> getAcGroup(Integer id);
}
