package com.videoweb.ying.service;

import com.videoweb.base.BaseService;
import com.videoweb.ying.dao.CaricatureChapterImgMapper;
import com.videoweb.ying.po.caricatureChapterImg;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Author lbh
 * Date 2019-07-27
 */
@Service
public class CaricatureChapterImgService extends BaseService<caricatureChapterImg> {


    public List<Map<String, Object>> findChapterImgList(Integer chapterId) {

        return ((CaricatureChapterImgMapper)mapper).findChapterImgList(chapterId);
    }
}
