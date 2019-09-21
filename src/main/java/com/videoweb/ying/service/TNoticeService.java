package com.videoweb.ying.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.videoweb.base.BaseService;
import com.videoweb.ying.dao.TNoticeMapper;
import com.videoweb.ying.po.TNotice;
import org.springframework.web.bind.annotation.RequestParam;


@Service("tNoticeService")
public class TNoticeService extends BaseService<TNotice> {
	
	public List<Map<String,Object>> selectListPage(){
        return ((TNoticeMapper)mapper).selectListPage();
	}
	
	public List<Map<String,Object>> getNoticeSub()
	{
		return ((TNoticeMapper)mapper).getNoticeSub();
	}
}