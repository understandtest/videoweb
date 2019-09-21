package com.videoweb.ying.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.videoweb.base.BaseMapper;
import com.videoweb.ying.po.CaricatureClassify;

import java.util.List;
import java.util.Map;

/**
 * Author lbh
 * Date 2019-07-25
 */
public interface CaricatureClassifyMapper extends BaseMapper<CaricatureClassify> {

    List<Map<String, Object>> findList();

}
