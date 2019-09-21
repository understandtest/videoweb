package com.videoweb.ying.service;

import com.videoweb.base.BaseService;
import com.videoweb.ying.po.IntegralHistory;
import org.springframework.stereotype.Service;

/**
 * @Author hong
 * @Date 19-8-26
 */
@Service
public class IntegralHistoryService extends BaseService<IntegralHistory> {


    public void add(IntegralHistory history){
        mapper.insert(history);
    }

}
