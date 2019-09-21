package com.videoweb.ying.dao;

import java.util.Map;

import com.videoweb.base.BaseMapper;
import com.videoweb.ying.po.TRechargeCode;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author haha
 * @since 2018-11-28
 */
public interface TRechargeCodeMapper extends BaseMapper<TRechargeCode> {

	
	public Map<String,Object> getRechargeCodeByCode(Map<String,Object> params);
}