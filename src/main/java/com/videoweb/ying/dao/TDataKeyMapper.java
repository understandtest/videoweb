package com.videoweb.ying.dao;

import java.util.List;
import java.util.Map;

import com.videoweb.ying.po.TDataKey;
import com.videoweb.base.BaseMapper;


public interface TDataKeyMapper extends BaseMapper<TDataKey> {

	public List<Map<String,Object>> getCupInfo();
}