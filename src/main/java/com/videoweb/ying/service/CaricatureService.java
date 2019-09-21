package com.videoweb.ying.service;

import com.videoweb.base.BaseService;
import com.videoweb.ying.dao.CaricatureMapper;
import com.videoweb.ying.po.Caricature;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Author lbh
 * Date 2019-07-27
 */
@Service
public class CaricatureService extends BaseService<Caricature>{


    /**
     * 动漫数据查询
     * @return
     */
    public List<Map<String,Object>> findCaricatureList(Map<String, Object> params){

        return  ((CaricatureMapper)mapper).findCaricatureList(params);

    }

    public Map<String,Object> findOne(int id) {
        return ((CaricatureMapper)mapper).findOne(id);
    }

    public void updateWatchNum(int id) {
        ((CaricatureMapper)mapper).updateWatchNum(id);
    }
}
