package com.videoweb.ying.service;


import com.videoweb.base.BaseService;
import com.videoweb.ying.dao.TDateMapper;
import com.videoweb.ying.po.TDate;
import org.codehaus.jackson.map.ser.std.DateSerializer;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author hong
 * @Date 19-8-24
 */
@Service
public class TDateService extends BaseService<TDate> {

    /**
     * 查看当前日期是否在数据库已存在
     * @param date
     * @return
     */
    public boolean findDateIsExists(Date date){

        Integer count = ((TDateMapper)mapper).countByDate(date);

        return count > 0;
    }

    /**
     * 添加日期
     * @param date
     */
    public void add(TDate date){
        mapper.insert(date);
    }

}
