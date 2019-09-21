package com.videoweb.ying.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.videoweb.base.BaseMapper;
import com.videoweb.ying.po.CaricatureChapter;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Author lbh
 * Date 2019-07-25
 */
public interface CaricatureChapterMapper extends BaseMapper<CaricatureChapter> {
    /**
     * 根据漫画id查询所属章节
     *
     * @param caricatureId
     * @return
     */
    List<Map<String, Object>> getListByCaricatureId(@Param("caricatureId") Integer caricatureId);

    Map<String, Object> findOne(@Param("id") Integer id);
}
