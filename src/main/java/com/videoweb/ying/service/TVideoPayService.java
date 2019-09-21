package com.videoweb.ying.service;

import com.videoweb.base.BaseService;
import com.videoweb.ying.dao.TVideoMapper;
import com.videoweb.ying.dao.TVideoPayMapper;
import com.videoweb.ying.po.TVideoPay;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * AUTHER lbh
 * Date 2019/5/21
 */
@Service
public class TVideoPayService extends BaseService<TVideoPay> {


    /**
     * 获取每日的排行记录
     * @return
     */
    public List<Map<String,Object>> findEverydayPays(){
        return ((TVideoPayMapper)mapper).findEverydayPays();
    }

    /**
     * 获取每周的排行记录
     * @return
     */
    public List<Map<String,Object>> findWeeklyPays(){
        return ((TVideoPayMapper)mapper).findWeeklyPays();
    }

    /**
     * 获取每月的排行记录
     * @return
     */
    public List<Map<String,Object>> findMonthlyPays(){
        return ((TVideoPayMapper)mapper).findMonthlyPays();
    }


    /**
     * 保存视频播放记录
     */
    public void saveVideoPay(Map<String,Object> params){
        logger.info("添加播放记录，视频id为:" + params.get("videoId"));
        Integer count = ((TVideoPayMapper) mapper).addVideoPay(params);
        logger.info("添加成功,插入记录数:" + count);
    }
}
