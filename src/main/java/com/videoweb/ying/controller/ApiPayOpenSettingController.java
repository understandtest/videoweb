package com.videoweb.ying.controller;

import com.videoweb.base.BaseController;
import com.videoweb.ying.po.TPayOpenSetting;
import com.videoweb.ying.service.TPayOpenSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author lbh
 * Date 2019/7/11
 * Describe
 */
@RestController
@RequestMapping("/ying/payOpenSetting")
public class ApiPayOpenSettingController extends BaseController {

    @Autowired
    private TPayOpenSettingService tPayOpenSettingService;

    @PostMapping("/findIsOpenPay")
    public Object findIsOpenPay(ModelMap modelMap){

        TPayOpenSetting payOpenSetting = tPayOpenSettingService.findOne();

        modelMap.put("isOpen",payOpenSetting.getIsOpen());
        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "查询成功");
        return  setSuccessResponse(modelMap);
    }



}
