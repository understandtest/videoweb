package com.videoweb.ying.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.videoweb.base.BaseService;
import com.videoweb.ying.dao.TLevelMapper;
import com.videoweb.ying.po.TLevel;


@Service("tLevelService")
public class TLevelService extends BaseService<TLevel> {
	
	public List<Map<String,Object>> getLevelList()
	{
		return ((TLevelMapper)mapper).getLevelList();
	}
	
	public Map<String,Object> getNextLevel(Map<String,Object> params)
	{
		return ((TLevelMapper)mapper).getNextLevel(params);
	}

	/**
	 * 查询所有的等级规则
	 * @return
	 */
	public List<Map> findLevels(){
		return ((TLevelMapper)mapper).findLevels();
	}
}