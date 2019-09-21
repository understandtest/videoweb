package com.videoweb.ying.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.plugins.Page;
import com.videoweb.base.BaseMapper;
import com.videoweb.ying.po.TCareHistory;


public interface TCareHistoryMapper extends BaseMapper<TCareHistory> {

	public List<Map<String,Object>> selectCareHistoryPage(Page<Map<String, Object>> page,@Param("cm") Map<String, Object> paramMap);
	
	public List<Map<String,Object>> selectCareHistory(Map<String, Object> paramMap);
}