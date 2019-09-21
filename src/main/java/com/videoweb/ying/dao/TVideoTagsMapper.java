package com.videoweb.ying.dao;

import java.util.List;
import java.util.Map;

import com.videoweb.base.BaseMapper;
import com.videoweb.ying.po.TVideoTags;


public interface TVideoTagsMapper extends BaseMapper<TVideoTags> {

	public List<Map<String,Object>> getHotTags();
}