package com.videoweb.ying.dao;

import com.videoweb.base.BaseMapper;
import com.videoweb.ying.po.CaricatureBookcase;

import java.util.List;
import java.util.Map;

/**
 * Author lbh
 * Date 2019-07-27
 */
public interface CaricatureBookcaseMapper extends BaseMapper<CaricatureBookcase> {
    List<Map<String, Object>> findChapterImgList(Integer memberId);
}
