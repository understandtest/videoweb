package com.videoweb.ying.dao;

import java.util.List;
import java.util.Map;

import com.videoweb.base.BaseMapper;
import com.videoweb.ying.po.TExtendPrice;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author haha
 * @since 2018-11-28
 */
public interface TExtendPriceMapper extends BaseMapper<TExtendPrice> {

	
	public Integer getTotalCronNum(Map<String,Object> params);
	
	public List<Map<String,Object>> getCronHistory(Map<String,Object> params);
}