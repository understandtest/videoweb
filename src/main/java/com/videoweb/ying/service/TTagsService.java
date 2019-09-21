package com.videoweb.ying.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.videoweb.base.BaseService;
import com.videoweb.ying.dao.TTagsMapper;
import com.videoweb.ying.po.TTags;


@Service("tTagsService")
public class TTagsService extends BaseService<TTags> {
	
	public List<Map<String,Object>> selectTagsByType(Map<String,Object> params)
	{
		return ((TTagsMapper)mapper).selectTagsByType(params);
	}
	
	public List<Map<String,Object>> selectTagsByRandom()
	{
		return ((TTagsMapper)mapper).selectTagsByRandom();
	}

	/**
	 *
	 * @param tpId tagTypeId
	 * @return
	 */
	public List<Map<String, Object>> getTags(Integer tpId) {
		return ((TTagsMapper)mapper).getTags(tpId);
	}
}