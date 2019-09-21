package com.videoweb.ying.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.videoweb.base.BaseService;
import com.videoweb.ying.dao.TRateMapper;
import com.videoweb.ying.po.TRate;

/**
 * <p>
 * 汇率设置  服务实现�?
 * </p>
 *
 * @author haha
 * @since 2018-11-28
 */
@Service("tRateService")
public class TRateService extends BaseService<TRate> {
		
	public Map<String,Object> selectRate()
	{
		return ((TRateMapper)mapper).selectRate();
	}
	
	
}