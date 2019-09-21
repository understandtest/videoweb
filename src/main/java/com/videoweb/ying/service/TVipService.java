package com.videoweb.ying.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.videoweb.base.BaseService;
import com.videoweb.ying.dao.TVipMapper;
import com.videoweb.ying.po.TVip;


@Service("tVipService")
public class TVipService extends BaseService<TVip> {
	
	public List<Map<String,Object>> getVipList()
	{
		return ((TVipMapper)mapper).getVipList();
	}

	public TVip getVipByCartType(Integer cartType){
		return ((TVipMapper)mapper).getVipByCartType(cartType);
	}
}