package com.videoweb.ying.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.videoweb.base.BaseService;
import com.videoweb.ying.dao.TRechargeCodeMapper;
import com.videoweb.ying.po.TRechargeCode;

/**
 * <p>
 * 充值码  服务实现�?
 * </p>
 *
 * @author haha
 * @since 2018-11-28
 */
@Service("tRechargeCodeService")
public class TRechargeCodeService extends BaseService<TRechargeCode> {
	
	
	public Map<String,Object> getRechargeCodeByCode(Map<String,Object> params)
	{
		return ((TRechargeCodeMapper)mapper).getRechargeCodeByCode(params);
	}
}