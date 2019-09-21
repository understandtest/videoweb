package com.videoweb.ying.dao;

import java.util.List;
import java.util.Map;

import com.videoweb.ying.po.TLevel;
import com.videoweb.base.BaseMapper;


public interface TLevelMapper extends BaseMapper<TLevel> {

	public List<Map<String,Object>> getLevelList();
	
	public Map<String,Object> getNextLevel(Map<String,Object> params);


	List<Map> findLevels();
}