package com.videoweb.ying.dao;

import java.util.List;
import java.util.Map;

import com.videoweb.base.BaseMapper;
import com.videoweb.ying.po.TSearch;


public interface TSearchMapper extends BaseMapper<TSearch> {

	public List<Map<String,Object>> searchName();
}