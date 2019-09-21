package com.videoweb.ying.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.videoweb.base.BaseService;
import com.videoweb.ying.dao.TDataKeyMapper;
import com.videoweb.ying.po.TDataKey;


@Service("tDataKeyService")
public class TDataKeyService extends BaseService<TDataKey> {
	
	public List<Map<String,Object>> getCupInfo()
	{
		return ((TDataKeyMapper)mapper).getCupInfo();
	}
}