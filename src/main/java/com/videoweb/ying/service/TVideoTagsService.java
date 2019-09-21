package com.videoweb.ying.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.videoweb.base.BaseService;
import com.videoweb.ying.dao.TVideoTagsMapper;
import com.videoweb.ying.po.TVideoTags;


@Service("tVideoTagsService")
public class TVideoTagsService extends BaseService<TVideoTags> {
	
	public List<Map<String,Object>> getHotTags()
	{
		return ((TVideoTagsMapper)mapper).getHotTags();
	}
}