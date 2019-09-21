package com.videoweb.ying.dao;

import java.util.List;
import java.util.Map;

import com.videoweb.base.BaseMapper;
import com.videoweb.ying.po.TQq;


public interface TQqMapper extends BaseMapper<TQq> {

	public List<Map<String,Object>> selectQQurl();
}