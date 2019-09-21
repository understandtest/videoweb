package com.videoweb.ying.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.videoweb.base.BaseService;
import com.videoweb.ying.dao.TVersionMapper;
import com.videoweb.ying.po.TVersion;


@Service("tVersionService")
public class TVersionService extends BaseService<TVersion> {
	
	public List<Map<String,Object>> getNewVersion(Map<String,Object> params)
	{
		return ((TVersionMapper)mapper).getNewVersion(params);
	}
	
}