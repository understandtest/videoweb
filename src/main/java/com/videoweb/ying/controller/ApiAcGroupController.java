package com.videoweb.ying.controller;

import com.videoweb.base.BaseController;
import com.videoweb.ying.po.AcGroup;
import com.videoweb.ying.service.AcGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author lbh
 * @Date 2019/8/7
 */
@RestController
@RequestMapping("/openapi/acGroup")
public class ApiAcGroupController extends BaseController {

    @Autowired
    private AcGroupService acGroupService;

    @PostMapping("/getAcGroup")
    public Object getAcGroup(ModelMap modelMap){

        Map<String,Object> acGroupMap = acGroupService.getAcGroup(1);

        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "获取数据成功");
        return  setSuccessResponse(modelMap,acGroupMap);
    }

}
