package com.videoweb.ying.dao;

import com.videoweb.base.BaseMapper;
import com.videoweb.ying.po.IntegralHistory;

import java.util.Map;

/**
 * @Author hong
 * @Date 19-8-26
 */
public interface IntegralHistoryMapper extends BaseMapper<IntegralHistory> {

    /**
     * 根据用户id和积分id统计数量查看是否已获取过积分
     * @param params
     * @return
     */
    Integer countByMemberIdAndIntegralId(Map<String, Object> params);
}
