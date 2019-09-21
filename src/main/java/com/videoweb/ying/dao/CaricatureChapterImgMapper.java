package com.videoweb.ying.dao;

import com.videoweb.base.BaseMapper;
import com.videoweb.ying.po.caricatureChapterImg;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Author lbh
 * Date 2019-07-25
 */
public interface CaricatureChapterImgMapper extends BaseMapper<caricatureChapterImg> {

    List<Map<String, Object>> findChapterImgList(@Param("chapterId") Integer chapterId);
}
