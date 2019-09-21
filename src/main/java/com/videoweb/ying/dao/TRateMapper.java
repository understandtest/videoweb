package com.videoweb.ying.dao;

import java.util.Map;

import com.videoweb.base.BaseMapper;
import com.videoweb.ying.po.TRate;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author haha
 * @since 2018-11-28
 */
public interface TRateMapper extends BaseMapper<TRate> {

	public Map<String,Object> selectRate();
}