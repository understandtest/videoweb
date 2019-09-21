package com.videoweb.ying.controller;

import com.videoweb.base.BaseController;
import com.videoweb.ying.po.GestureSetting;
import com.videoweb.ying.service.GestureSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author hong
 * @Date 19-8-26
 */
@RestController
public class GestureSettingController extends BaseController {

    @Autowired
    private GestureSettingService gestureSettingService;

    @PostMapping("/openapi/gestureSetting")
    public Object gestureSetting(ModelMap modelMap){

        Map<String,Object> result = new HashMap<>();

        GestureSetting gestureSetting = gestureSettingService.findOne(1);

        result.put("gestureSetting",gestureSetting);
        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "查询成功");
        return setSuccessResponse(modelMap,result);
    }

}
