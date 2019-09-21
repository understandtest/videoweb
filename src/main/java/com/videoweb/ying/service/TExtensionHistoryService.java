package com.videoweb.ying.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.videoweb.base.BaseService;
import com.videoweb.ying.dao.TExtensionHistoryMapper;
import com.videoweb.ying.po.TExtensionHistory;


@Service("tExtensionHistoryService")
public class TExtensionHistoryService extends BaseService<TExtensionHistory> {
	
	
	public List<Map<String,Object>> getExtensionHistory(Map<String,Object> params)
	{
		return ((TExtensionHistoryMapper)mapper).getExtensionHistory(params);
	}
}