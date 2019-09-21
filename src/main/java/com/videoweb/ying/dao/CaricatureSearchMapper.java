package com.videoweb.ying.dao;

import com.videoweb.base.BaseMapper;
import com.videoweb.ying.po.CaricatureSearch;

import java.util.List;

/**
 * Author lbh
 * Date 2019-07-27
 */
public interface CaricatureSearchMapper extends BaseMapper<CaricatureSearch> {
    List<String> getHotSearchName();
}
