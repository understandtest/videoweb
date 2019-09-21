package com.videoweb.ying.dao;

import java.util.List;
import java.util.Map;

import com.videoweb.ying.po.TPaySetting;
import com.videoweb.base.BaseMapper;


public interface TPaySettingMapper extends BaseMapper<TPaySetting> {

	public List<Map<String,Object>> getPayType();
}