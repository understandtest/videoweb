package com.videoweb.ying.dao;

import java.util.List;
import java.util.Map;

import com.videoweb.ying.po.TClassify;
import com.videoweb.base.BaseMapper;


public interface TClassifyMapper extends BaseMapper<TClassify> {

	public List<Map<String,Object>> selectClassify();
}