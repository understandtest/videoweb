package com.videoweb.ying.dao;

import com.videoweb.base.BaseMapper;
import com.videoweb.ying.po.Caricature;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Author lbh
 * Date 2019-07-25
 */

public interface CaricatureMapper extends BaseMapper<Caricature> {


    List<Map<String, Object>> findCaricatureList(Map<String, Object> params);

    Map<String, Object> findOne(@Param("id") int id);

    void updateWatchNum(@Param("id") int id);
}
