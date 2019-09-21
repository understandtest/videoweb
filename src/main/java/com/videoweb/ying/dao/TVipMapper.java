package com.videoweb.ying.dao;

import java.util.List;
import java.util.Map;

import com.videoweb.ying.po.TVip;
import com.videoweb.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

public interface TVipMapper extends BaseMapper<TVip> {

    public List<Map<String, Object>> getVipList();


    TVip getVipByCartType(@Param("cartType") Integer cartType);
}