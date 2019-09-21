package com.videoweb.ying.service;

import com.nimbusds.jose.crypto.MACSigner;
import com.videoweb.base.BaseService;
import com.videoweb.ying.dao.CaricatureBookcaseMapper;
import com.videoweb.ying.dao.CaricatureChapterImgMapper;
import com.videoweb.ying.po.CaricatureBookcase;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Author lbh
 * Date 2019-07-27
 */
@Service
public class CaricatureBookcaseService extends BaseService<CaricatureBookcase> {



    public void add(CaricatureBookcase caricatureBookcase) {

        mapper.insert(caricatureBookcase);
    }

    public List<Map<String,Object>> getCaricatureBookcaseListByMeberId(Integer memberId) {

        return ((CaricatureBookcaseMapper)mapper).findChapterImgList(memberId);
    }
}
