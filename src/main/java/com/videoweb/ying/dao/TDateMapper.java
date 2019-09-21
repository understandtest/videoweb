package com.videoweb.ying.dao;


import com.videoweb.base.BaseMapper;
import com.videoweb.ying.po.TDate;

import java.util.Date;

/**
 * @Author hong
 * @Date 19-8-24
 */
public interface TDateMapper extends BaseMapper<TDate> {
    Integer countByDate(Date date);
}
