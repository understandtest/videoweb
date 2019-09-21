package com.videoweb.ying.dao;

import java.util.List;
import java.util.Map;

import com.videoweb.base.BaseMapper;
import com.videoweb.ying.po.TTagType;


public interface TTagTypeMapper extends BaseMapper<TTagType> {

	public List<Map<String,Object>> selectTagType();


	/**
	 * 查询分类列表集合
	 * @return
	 */
	List<Map<String, Object>> getTagTypes();
}