
package com.videoweb.ying.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.videoweb.base.BaseController;
import com.videoweb.utils.PropertiesUtil;
import com.videoweb.utils.redis.CacheUtil;
import com.videoweb.ying.service.TBannerService;
import com.videoweb.ying.service.TClassifyService;
import com.videoweb.ying.service.TStarService;
import com.videoweb.ying.service.TVideoService;


@RestController
@RequestMapping("/openapi")
public class ApiIndexController extends BaseController {

    @Autowired
    private TBannerService tBannerService;

    @Autowired
    private TClassifyService tClassifyService;

    @Autowired
    private TVideoService tVideoService;
    @Autowired
    private TStarService tStarService;

    @Autowired
    private TBannerService bannerService;

    @PostMapping("/indexInfo")
    public Object indexInfo(ModelMap modelMap, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (CacheUtil.getCache().get("indexInfo") != null && !"".equals(CacheUtil.getCache().get("indexInfo"))) {
            result = (Map<String, Object>) CacheUtil.getCache().get("indexInfo");
        } else {
            //获取轮播图
            Map<String, Object> pp = new HashMap<String, Object>();
            pp.put("picType", "1");
            List<Map<String, Object>> bannerList = new ArrayList<Map<String, Object>>();
            bannerList = tBannerService.getBannerList(pp);
            result.put("bannerList", bannerList);

            if (bannerList != null && bannerList.size() > 0) {
                for (Map<String, Object> mm : bannerList) {
                    if ("1".equals(mm.get("picFrom").toString())) {
                        mm.put("picUrl", PropertiesUtil.getString("remote.file.uri.prefix") + mm.get("picUrl"));
                    }
                }
            }
            //获取分类
            List<Map<String, Object>> classifyList = new ArrayList<Map<String, Object>>();
            classifyList = tClassifyService.selectClassify();
            for (Map<String, Object> mm : classifyList) {
                if ("1".equals(mm.get("iconType").toString())) {
                    mm.put("classifyIcon", PropertiesUtil.getString("remote.file.uri.prefix") + mm.get("classifyIcon"));
                }
            }
            result.put("classifyList", classifyList);
            //获取最新片源4个
            Map<String, Object> tkMap = new HashMap<String, Object>();
            tkMap.put("newView", "1");
            List<Map<String, Object>> newVideoList = new ArrayList<Map<String, Object>>();
            newVideoList = tVideoService.getVideoList(tkMap);
            for (Map<String, Object> mm : newVideoList) {
                if ("1".equals(mm.get("videoCoverType").toString())) {
                    mm.put("videoCover", PropertiesUtil.getString("remote.file.uri.prefix") + mm.get("videoCover"));
                }
            }
            result.put("newVideoList", newVideoList);
            //重磅热播
            tkMap.clear();
            tkMap.put("mostView", "1");
            List<Map<String, Object>> mostVideoList = new ArrayList<Map<String, Object>>();
            mostVideoList = tVideoService.getVideoList(tkMap);
            for (Map<String, Object> mm : mostVideoList) {
                if ("1".equals(mm.get("videoCoverType").toString())) {
                    mm.put("videoCover", PropertiesUtil.getString("remote.file.uri.prefix") + mm.get("videoCover"));
                }
            }
            result.put("mostVideoList", mostVideoList);
            //获取人气明星
            List<Map<String, Object>> starList = new ArrayList<Map<String, Object>>();
            starList = tStarService.getStarList();
            for (Map<String, Object> mm : starList) {
                if ("1".equals(mm.get("picType").toString())) {
                    mm.put("headpic", PropertiesUtil.getString("remote.file.uri.prefix") + mm.get("headpic"));
                }
            }
            result.put("starList", starList);
            //分类数据
            List<Map<String, Object>> classifyListCollect = new ArrayList<Map<String, Object>>();
            if (classifyList != null && classifyList.size() > 0) {
                for (Map<String, Object> tgh : classifyList) {
                    Map<String, Object> resultMap = new HashMap<String, Object>();
                    List<Map<String, Object>> videoll = new ArrayList<Map<String, Object>>();
                    Map<String, Object> gg = new HashMap<String, Object>();
                    gg.put("classifyId", tgh.get("id"));
                    gg.put("newView", "1");
                    videoll = tVideoService.getVideoList(gg);
                    resultMap.put("id", tgh.get("id"));
                    resultMap.put("name", tgh.get("name"));
                    for (Map<String, Object> mm : videoll) {
                        if ("1".equals(mm.get("videoCoverType").toString())) {
                            mm.put("videoCover", PropertiesUtil.getString("remote.file.uri.prefix") + mm.get("videoCover"));
                        }
                    }
                    resultMap.put("videoList", videoll);
                    classifyListCollect.add(resultMap);
                }
            }
            result.put("classifyListCollect", classifyListCollect);
            //查询首页所有广告
            Map indexBanners = getIndexBanners();
            result.put("indexBanners", indexBanners);
            CacheUtil.getCache().set("indexInfo", (Serializable) result);
        }
        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "查询成功");
        return setSuccessResponse(modelMap, result);
    }

    //获取首页广告
    private Map getIndexBanners() {
        //存储首页广告
        Map<String, Object> bannerMap = new HashMap<>();
        //首页广告1
        bannerMap.put("indexBanner1", getBannersByCid(6));
        //首页广告2
        bannerMap.put("indexBanner2", getBannersByCid(7));
        //首页广告3
        bannerMap.put("indexBanner3", getBannersByCid(8));
        //首页广告4
        bannerMap.put("indexBanner4", getBannersByCid(9));

        return bannerMap;

    }

    private List<Map<String, Object>> getBannersByCid(Integer cId) {
        Map<String, Object> params = new HashMap<>();
        params.put("picType", cId);
        List<Map<String, Object>> bannerList = bannerService.getBannerList(params);
        return bannerList;
    }


}
