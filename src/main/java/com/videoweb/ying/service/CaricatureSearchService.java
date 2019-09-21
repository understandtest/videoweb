package com.videoweb.ying.service;

import com.videoweb.base.BaseService;
import com.videoweb.ying.dao.CaricatureSearchMapper;
import com.videoweb.ying.po.CaricatureSearch;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author lbh
 * Date 2019-07-27
 */
@Service
public class CaricatureSearchService extends BaseService<CaricatureSearch> {


    public List<String> getHotSearchName() {
        return ((CaricatureSearchMapper)mapper).getHotSearchName();
    }
}
