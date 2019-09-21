package com.videoweb.ying.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.videoweb.base.BaseService;
import com.videoweb.ying.dao.TCareHistoryMapper;
import com.videoweb.ying.dao.TVideoMapper;
import com.videoweb.ying.po.TCareHistory;


@Service("tCareHistoryService")
public class TCareHistoryService extends BaseService<TCareHistory> {

	public Page<Map<String, Object>> selectCareHistoryPage(Map<String,Object> params)
	{
		Page<Map<String, Object>> page = getPageMap(params);
		page.setRecords(((TCareHistoryMapper)mapper).selectCareHistoryPage(page, params));
		return page;
	}
	
	public List<Map<String, Object>> selectCareHistory(Map<String,Object> params)
	{
		return ((TCareHistoryMapper)mapper).selectCareHistory(params);
	}
}