package com.videoweb.ying.dao;

import java.util.List;
import java.util.Map;

import com.videoweb.ying.po.TBanner;
import com.videoweb.base.BaseMapper;


public interface TBannerMapper extends BaseMapper<TBanner> {

	public List<Map<String,Object>> getBannerList(Map<String,Object> params);
}