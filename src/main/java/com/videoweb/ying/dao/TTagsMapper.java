package com.videoweb.ying.dao;

import java.util.List;
import java.util.Map;

import com.videoweb.ying.po.TTags;
import com.videoweb.base.BaseMapper;
import org.apache.ibatis.annotations.Param;


public interface TTagsMapper extends BaseMapper<TTags> {

	public List<Map<String, Object>> selectTagsByType(Map<String, Object> params);

	public List<Map<String, Object>> selectTagsByRandom();

    List<Map<String, Object>> getTags(@Param("tpId") Integer tpId);
}