package com.videoweb.ying.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.videoweb.base.BaseService;
import com.videoweb.ying.dao.TSearchMapper;
import com.videoweb.ying.po.TSearch;


@Service("tSearchService")
public class TSearchService extends BaseService<TSearch> {

	  public List<Map<String,Object>> searchName()
	  {
		  return (List<Map<String, Object>>) ((TSearchMapper)mapper).searchName();	  
	   }
}