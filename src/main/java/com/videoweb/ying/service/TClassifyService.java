package com.videoweb.ying.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.videoweb.base.BaseService;
import com.videoweb.ying.dao.TClassifyMapper;
import com.videoweb.ying.po.TClassify;


@Service("tClassifyService")
public class TClassifyService extends BaseService<TClassify> {
	
	public List<Map<String,Object>> selectClassify()
	{
		return ((TClassifyMapper)mapper).selectClassify();
	}
}