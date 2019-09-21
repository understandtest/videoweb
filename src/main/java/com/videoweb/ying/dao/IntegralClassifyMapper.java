package com.videoweb.ying.dao;

import com.videoweb.base.BaseMapper;
import com.videoweb.ying.po.IntegralClassify;

import java.util.List;
import java.util.Map;

/**
 * @Author hong
 * @Date 19-8-26
 */
public interface IntegralClassifyMapper extends BaseMapper<IntegralClassify> {
    List<Map<String, Object>> findAll();
}
