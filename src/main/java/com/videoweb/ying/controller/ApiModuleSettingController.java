package com.videoweb.ying.controller;

import com.videoweb.base.BaseController;
import com.videoweb.ying.service.ModuleSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author hong
 * @Date 19-9-10
 */
@RestController
@RequestMapping("/openapi/moduleSetting")
public class ApiModuleSettingController extends BaseController {


    @Autowired
    private ModuleSettingService moduleSettingService;

    @PostMapping("/getModuleSetting")
    public Object getModuleSetting(ModelMap modelMap){

        Map<String,Object> result = new HashMap<>();


        // 查询对应模块的开启状态
        Map<String, Object> caricatureModuleSetting = moduleSettingService.findOne(2);
        Map<String, Object> starModuleSetting = moduleSettingService.findOne(1);

        result.put("caricatureModuleSetting",caricatureModuleSetting);
        result.put("starModuleSetting",starModuleSetting);

        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "查询成功");
        return  setSuccessResponse(modelMap,result);

    }



}
