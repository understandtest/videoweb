package com.videoweb.ying.dao;

import java.util.Map;

import com.videoweb.base.BaseMapper;
import com.videoweb.ying.po.SysSms;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author haha
 * @since 2019-03-24
 */
public interface SysSmsMapper extends BaseMapper<SysSms> {

	public Map<String,Object> selectSms();
}