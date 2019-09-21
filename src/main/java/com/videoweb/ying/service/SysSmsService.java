package com.videoweb.ying.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.videoweb.base.BaseService;
import com.videoweb.ying.dao.SysSmsMapper;
import com.videoweb.ying.po.SysSms;

/**
 * <p>
 *   服务实现�?
 * </p>
 *
 * @author haha
 * @since 2019-03-24
 */
@Service("sysSmsService")
public class SysSmsService extends BaseService<SysSms> {
	
	
	public Map<String,Object> selectSms()
	{
		return ((SysSmsMapper)mapper).selectSms();
	}
}