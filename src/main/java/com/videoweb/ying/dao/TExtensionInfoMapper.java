package com.videoweb.ying.dao;

import java.util.Map;

import com.videoweb.ying.po.TExtensionInfo;
import com.videoweb.base.BaseMapper;


public interface TExtensionInfoMapper extends BaseMapper<TExtensionInfo> {
	
	public Map<String,Object> getExtensionInfo();
}