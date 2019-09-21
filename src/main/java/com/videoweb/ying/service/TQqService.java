package com.videoweb.ying.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.videoweb.base.BaseService;
import com.videoweb.ying.dao.TQqMapper;
import com.videoweb.ying.po.TQq;


@Service("tQqService")
public class TQqService extends BaseService<TQq> {
	
	public List<Map<String,Object>> selectQQurl()
	{
		return ((TQqMapper)mapper).selectQQurl();
	}
}