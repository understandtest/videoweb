package com.videoweb.ying.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.plugins.Page;
import com.videoweb.ying.po.TStar;
import com.videoweb.base.BaseMapper;


public interface TStarMapper extends BaseMapper<TStar> {

	public List<Map<String,Object>> selectListPage(Page<Map<String, Object>> page,@Param("cm") Map<String, Object> paramMap);
	
	public List<Map<String,Object>> getStarList();
	
	public Map<String,Object> getStarInfoByVideo(Map<String,Object> params);
}