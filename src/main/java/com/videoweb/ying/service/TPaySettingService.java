package com.videoweb.ying.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.videoweb.base.BaseService;
import com.videoweb.ying.dao.TPaySettingMapper;
import com.videoweb.ying.po.TPaySetting;


@Service("tPaySettingService")
public class TPaySettingService extends BaseService<TPaySetting> {
	
	public List<Map<String,Object>> getPayType()
	{
		return ((TPaySettingMapper)mapper).getPayType();
	}
}