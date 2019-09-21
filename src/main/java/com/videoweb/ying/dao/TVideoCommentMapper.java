package com.videoweb.ying.dao;

import java.util.List;
import java.util.Map;

import com.videoweb.ying.po.TVideoComment;
import com.videoweb.base.BaseMapper;

public interface TVideoCommentMapper extends BaseMapper<TVideoComment> {

	public List<Map<String,Object>> getVideoCommon(Map<String,Object> params);
	
	public Integer selectVideoCommentConut(Map<String,Object> params);
}