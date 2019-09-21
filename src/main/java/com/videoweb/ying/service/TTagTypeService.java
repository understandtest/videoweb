package com.videoweb.ying.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.videoweb.base.BaseService;
import com.videoweb.ying.dao.TTagTypeMapper;
import com.videoweb.ying.po.TTagType;


@Service("tTagTypeService")
public class TTagTypeService extends BaseService<TTagType> {
	
	public List<Map<String,Object>> selectTagType()
	{
		return  ((TTagTypeMapper)mapper).selectTagType();
	}

	public List<Map<String, Object>> getTagTypes() {
		return ((TTagTypeMapper)mapper).getTagTypes();
	}
}