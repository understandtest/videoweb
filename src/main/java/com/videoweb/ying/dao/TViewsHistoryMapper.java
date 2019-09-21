package com.videoweb.ying.dao;

import java.util.List;
import java.util.Map;

import com.videoweb.base.BaseMapper;
import com.videoweb.ying.po.TViewsHistory;


public interface TViewsHistoryMapper extends BaseMapper<TViewsHistory> {

	public List<Map<String,Object>> selectViewHistory(Map<String,Object> params);
}