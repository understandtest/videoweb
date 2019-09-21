package com.videoweb.ying.controller;

import com.videoweb.base.BaseController;
import com.videoweb.utils.PropertiesUtil;
import com.videoweb.ying.service.TBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AUTHER lbh
 * Date 2019/5/17
 */
@RestController
@RequestMapping("/openapi")
public class ApiBannerController extends BaseController {

    @Autowired
    private TBannerService bannerService;

    /**
     * 查询广告根据类别
     * @return
     */
    @PostMapping("/findBanner")
    public Object findBanner(Integer cId, ModelMap modelMap){

        Map<String,Object> params = new HashMap<>();
        params.put("picType",cId);
        List<Map<String, Object>> bannerList = bannerService.getBannerList(params);

        for (Map<String, Object> mm : bannerList) {
            if ("1".equals(mm.get("picFrom").toString())) {
                mm.put("picUrl", PropertiesUtil.getString("remote.file.uri.prefix") + mm.get("picUrl"));
            }
        }

        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "获取数据成功");
        return  setSuccessResponse(modelMap,bannerList);
    }

}
