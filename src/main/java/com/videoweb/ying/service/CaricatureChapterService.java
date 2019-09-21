package com.videoweb.ying.service;

import com.videoweb.base.BaseService;
import com.videoweb.ying.dao.CaricatureChapterMapper;
import com.videoweb.ying.dao.CaricatureMapper;
import com.videoweb.ying.po.CaricatureChapter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Author lbh
 * Date 2019-07-27
 */
@Service
public class CaricatureChapterService extends BaseService<CaricatureChapter> {


    /**
     * 根据漫画id查询所属章节列表
     * @param caricatureId
     * @return
     */
    public List<Map<String,Object>> getListByCaricatureId(Integer caricatureId) {
        return ((CaricatureChapterMapper)mapper).getListByCaricatureId(caricatureId);
    }

    public Map<String,Object> findOne(Integer id) {

        return ((CaricatureChapterMapper)mapper).findOne(id);


    }
}
