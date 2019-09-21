package com.videoweb.ying.service;

import com.videoweb.base.BaseService;
import com.videoweb.ying.po.IntegralVipHistory;
import org.springframework.stereotype.Service;

/**
 * @Author hong
 * @Date 19-8-26
 */
@Service
public class IntegralVipHistoryService extends BaseService<IntegralVipHistory> {

    public void add(IntegralVipHistory integralVipHistory){
        mapper.insert(integralVipHistory);
    }
}
