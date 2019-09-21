package com.videoweb.ying.service;

import com.videoweb.base.BaseService;
import com.videoweb.ying.dao.CaricatureClassifyMapper;
import com.videoweb.ying.po.CaricatureClassify;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Author lbh
 * Date 2019-07-27
 */
@Service
public class CaricatureClassifyService extends BaseService<CaricatureClassify> {

    /**
     * 查询所有分类
     * @return
     */
    public List<Map<String, Object>> findList() {
        return ((CaricatureClassifyMapper)mapper).findList();
    }
}
