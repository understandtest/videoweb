package com.videoweb.ying.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.videoweb.base.BaseService;
import com.videoweb.ying.dao.TExtensionInfoMapper;
import com.videoweb.ying.po.TExtensionInfo;


@Service("tExtensionInfoService")
public class TExtensionInfoService extends BaseService<TExtensionInfo> {
	
	public Map<String,Object> getExtensionInfo()
	{
		return ((TExtensionInfoMapper)mapper).getExtensionInfo();
	}
}