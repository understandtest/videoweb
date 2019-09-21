package com.videoweb.ying.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.plugins.Page;
import com.videoweb.ying.po.TVideo;
import com.videoweb.base.BaseMapper;

public interface TVideoMapper extends BaseMapper<TVideo> {

	public Map<String,Object> getVideoDetail(Map<String,Object> params);
	
	public List<Map<String,Object>> selectListPage(Page<Map<String, Object>> page,@Param("cm") Map<String, Object> paramMap);
	
	public List<Map<String,Object>> getVideoList(Map<String, Object> paramMap);
	
	public List<Map<String,Object>> newVideoPage(Page<Map<String, Object>> page,@Param("cm") Map<String, Object> paramMap);
	
	public List<Map<String,Object>> selectOPenVideo(Page<Map<String, Object>> page,@Param("cm") Map<String, Object> paramMap);
	
	public List<Map<String,Object>> getLikeVideoById(Map<String,Object> params);
	
	public List<Map<String,Object>> getStarVideoByStarId(Map<String,Object> params);
}