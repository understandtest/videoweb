package com.videoweb.ying.dao;

import com.videoweb.base.BaseMapper;
import com.videoweb.ying.po.TVideoPay;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * AUTHER lbh
 * Date 2019/5/21
 */
public interface TVideoPayMapper extends BaseMapper<TVideoPay> {

    /**
     * 获取每日的排行记录
     * @return
     */
    List<Map<String,Object>> findEverydayPays();

    /**
     * 获取每周的排行记录
     * @return
     */
    List<Map<String,Object>> findWeeklyPays();

    /**
     * 获取每月的排行记录
     * @return
     */
    List<Map<String,Object>> findMonthlyPays();

    /**
     * 添加播放机录
     * @param paramMap
     * @return
     */
    Integer addVideoPay(@Param("cm") Map<String, Object> paramMap);

}
