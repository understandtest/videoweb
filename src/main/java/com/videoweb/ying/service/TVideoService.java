package com.videoweb.ying.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.videoweb.base.BaseService;
import com.videoweb.ying.dao.TNoticeMapper;
import com.videoweb.ying.dao.TVideoMapper;
import com.videoweb.ying.po.TVideo;


@Service("tVideoService")
public class TVideoService extends BaseService<TVideo> {
	
	
	public Map<String,Object> getVideoDetail(Map<String,Object> params)
	{
		return ((TVideoMapper)mapper).getVideoDetail(params);
	}
	
	public Page<Map<String, Object>> selectListPage(Map<String, Object> paramMap){
        Page<Map<String, Object>> page = getPageMap(paramMap);
        page.setRecords(((TVideoMapper)mapper).selectListPage(page, paramMap));
        return page;
	}
	
	public List<Map<String,Object>> getVideoList(Map<String, Object> paramMap)
	{
		return ((TVideoMapper)mapper).getVideoList(paramMap);
	}
	
	
	public Page<Map<String, Object>> newVideoPage(Map<String, Object> paramMap){
        Page<Map<String, Object>> page = getPageMap(paramMap);
        page.setRecords(((TVideoMapper)mapper).newVideoPage(page, paramMap));
        return page;
	}
	
	public Page<Map<String, Object>> selectOPenVideo(Map<String, Object> paramMap){
        Page<Map<String, Object>> page = getPageMap(paramMap);
        page.setRecords(((TVideoMapper)mapper).selectOPenVideo(page, paramMap));
        return page;
	}
	
	public List<Map<String,Object>> getLikeVideoById(Map<String, Object> paramMap)
	{
		return ((TVideoMapper)mapper).getLikeVideoById(paramMap);
	}
	
	public List<Map<String,Object>> getStarVideoByStarId(Map<String, Object> paramMap)
	{
		return ((TVideoMapper)mapper).getStarVideoByStarId(paramMap);
	}
}