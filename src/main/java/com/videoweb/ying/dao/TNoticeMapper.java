package com.videoweb.ying.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.plugins.Page;
import com.videoweb.ying.po.TNotice;
import com.videoweb.base.BaseMapper;


public interface TNoticeMapper extends BaseMapper<TNotice> {

	public List<Map<String,Object>> selectListPage();
	
	public List<Map<String,Object>> getNoticeSub();
}