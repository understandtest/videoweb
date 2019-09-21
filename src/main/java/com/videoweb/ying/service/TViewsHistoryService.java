package com.videoweb.ying.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.videoweb.base.BaseService;
import com.videoweb.ying.dao.TViewsHistoryMapper;
import com.videoweb.ying.po.TViewsHistory;


@Service("tViewsHistoryService")
public class TViewsHistoryService extends BaseService<TViewsHistory> {
	
	
	public List<Map<String,Object>> selectViewHistory(Map<String,Object> params)
	{
		return ((TViewsHistoryMapper)mapper).selectViewHistory(params);
	}
}