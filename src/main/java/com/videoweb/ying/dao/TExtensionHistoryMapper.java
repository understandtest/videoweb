package com.videoweb.ying.dao;

import java.util.List;
import java.util.Map;

import com.videoweb.ying.po.TExtensionHistory;
import com.videoweb.base.BaseMapper;


public interface TExtensionHistoryMapper extends BaseMapper<TExtensionHistory> {

	public List<Map<String,Object>> getExtensionHistory(Map<String,Object> params);
}