package com.videoweb.ying.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.videoweb.base.BaseService;
import com.videoweb.ying.dao.TExtendPriceMapper;
import com.videoweb.ying.po.TExtendPrice;

/**
 * <p>
 * 推广充值收益  服务实现�?
 * </p>
 *
 * @author haha
 * @since 2018-11-28
 */
@Service("tExtendPriceService")
public class TExtendPriceService extends BaseService<TExtendPrice> {
	
	public Integer getTotalCronNum(Map<String,Object> params)
	{
		return ((TExtendPriceMapper)mapper).getTotalCronNum(params);
	}
	
	public List<Map<String,Object>> getCronHistory(Map<String,Object> params)
	{
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		result = ((TExtendPriceMapper)mapper).getCronHistory(params);
		return result;
	}
}