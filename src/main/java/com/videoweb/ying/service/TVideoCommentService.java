package com.videoweb.ying.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.videoweb.base.BaseService;
import com.videoweb.ying.dao.TVideoCommentMapper;
import com.videoweb.ying.po.TVideoComment;


@Service("tVideoCommentService")
public class TVideoCommentService extends BaseService<TVideoComment> {
	
	public List<Map<String,Object>> getVideoCommon(Map<String,Object> params)
	{
		return ((TVideoCommentMapper)mapper).getVideoCommon(params);
	}
	
	public Integer selectVideoCommentConut(Map<String,Object> params)
	{
		return  ((TVideoCommentMapper)mapper).selectVideoCommentConut(params);
	}
}