package com.videoweb.ying.dao;

import java.util.List;
import java.util.Map;

import com.videoweb.ying.po.TVersion;
import com.videoweb.base.BaseMapper;


public interface TVersionMapper extends BaseMapper<TVersion> {

	public List<Map<String,Object>> getNewVersion(Map<String,Object> params);
}