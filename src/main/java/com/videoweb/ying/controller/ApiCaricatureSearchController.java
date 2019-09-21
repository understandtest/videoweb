package com.videoweb.ying.controller;

import com.videoweb.base.BaseController;
import com.videoweb.ying.service.CaricatureSearchService;
import com.videoweb.ying.service.CaricatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author lbh
 * Date 2019-07-27
 */
@RestController
@RequestMapping("/openapi/caricatureSearch")
public class ApiCaricatureSearchController extends BaseController {

    @Autowired
    private CaricatureSearchService caricatureSearchService;

    /**
     * 获取热门搜索数据
     * @return
     */
    @PostMapping("/getHotSearchName")
    public Object getHotSearchName(ModelMap modelMap){
        //返回结果map
        Map<String,Object> result = new HashMap<>();

        List<String> searchNameList = caricatureSearchService.getHotSearchName();

        result.put("searchNameList",searchNameList);

        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "查询成功");

        return setSuccessResponse(modelMap, result);
    }

}
