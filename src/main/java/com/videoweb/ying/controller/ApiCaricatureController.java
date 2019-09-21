package com.videoweb.ying.controller;

import com.videoweb.base.BaseController;
import com.videoweb.utils.PropertiesUtil;
import com.videoweb.ying.po.*;
import com.videoweb.ying.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author lbh
 * Date 2019-07-27
 */
@RestController
public class ApiCaricatureController extends BaseController {

    @Autowired
    private CaricatureService caricatureService;

    @Autowired
    private CaricatureClassifyService caricatureClassifyService;

    @Autowired
    private CaricatureSearchService caricatureSearchService;

    @Autowired
    private TMemberService memberService;

    @Autowired
    private CaricatureBookcaseService caricatureBookcaseService;

    /**
     * 获取漫画数据
     * @param modelMap
     * @return
     */
    @PostMapping("/openapi/caricature/getCaricatureInfo")
    public Object getCaricatureInfo(ModelMap modelMap,@RequestBody Map<String, Object> params){

        logger.info("获取漫画列表参数为:{}" , params );

        //返回结果map
        Map<String,Object> result = new HashMap<>();

        //查询所有的分类
        List<Map<String,Object>> classifyList = caricatureClassifyService.findList();

        //排序规则
        List<OrderType> orderTypes = new ArrayList<>();
        orderTypes.add(new OrderType(0,"综合"));
        orderTypes.add(new OrderType(1,"最多播放"));
        orderTypes.add(new OrderType(2,"最近更新"));
        orderTypes.add(new OrderType(3,"最多喜欢"));

        List<Map<String, Object>> caricatureList = caricatureService.findCaricatureList(params);

        //处理搜索
        String searchName = (String) params.get("name");

        handleSearch(searchName);


        for (Map<String, Object> mm : caricatureList) {
            if ("1".equals(mm.get("coverType"))) {
                mm.put("cover", PropertiesUtil.getString("remote.file.uri.prefix") + mm.get("cover"));
            }
            mm.remove("coverType");
        }

        String orderType = (String)params.get("orderType");
        if(StringUtils.isEmpty(orderType)){
            orderType = "0"; //默认为综合查询
        }

        String classifyId = (String)params.get("classifyId");
        if(StringUtils.isEmpty(classifyId)){
            classifyId = "-1"; //默认为全部查询
        }

        result.put("classifyId",Integer.parseInt(classifyId));
        result.put("caricatureList",caricatureList);
        result.put("orderType",orderType);
        result.put("orderTypes",orderTypes);
        result.put("classifyList",classifyList);

        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "查询成功");

        return setSuccessResponse(modelMap, result);

    }

    @PostMapping("/ying/caricature/getCaricatureDetail")
    public Object getCaricature(ModelMap modelMap, @RequestBody Map<String, Object> params, HttpServletRequest request){

        logger.info("获取漫画详情参数为:{}",params);

        if (params == null || "".equals(params)) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "查询失败，参数异常");
            return setSuccessResponse(modelMap);
        }


        String caricatureId = (String)params.get("caricatureId");
        if (caricatureId == null || "".equals(caricatureId)) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "查询失败，参数异常");
            return setSuccessResponse(modelMap);
        }

        //获取用户
        Integer memberId = getCurrUser(request);
        TMember member = new TMember();
        member.setId(memberId);

        if(!(memberId != null && memberService.selectOne(member) != null)){
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "用户不存在");
            return setSuccessResponse(modelMap);
        }

        Map<String,Object> result = new HashMap<>();

        //查询具体数据
        Map<String, Object> caricatureMap = caricatureService.findOne(Integer.parseInt(caricatureId));

        //处理图片
        if ("1".equals(caricatureMap.get("coverType"))) {
            caricatureMap.put("cover", PropertiesUtil.getString("remote.file.uri.prefix") + caricatureMap.get("cover"));
        }

        if ("1".equals(caricatureMap.get("descCoverType"))) {
            caricatureMap.put("descCover", PropertiesUtil.getString("remote.file.uri.prefix") + caricatureMap.get("descCover"));
        }
        //清理key
        caricatureMap.remove("coverType");
        caricatureMap.remove("descCoverType");

        Integer watchNum = (Integer)caricatureMap.get("watchNum");

        if ((watchNum / 10000) > 0) {
            double tk1 = new BigDecimal((float) watchNum / 10000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            caricatureMap.put("watchNum", tk1 + "万");
        } else {
            caricatureMap.put("watchNum", watchNum + "");
        }

        //查询是否加入书架
        String isCollect = findIsCollect(memberId,Integer.parseInt(caricatureId))?"0":"1";

        //更新漫画观看数量
        caricatureService.updateWatchNum(Integer.parseInt(caricatureId));

        result.put("isCollect",isCollect);
        result.put("caricatureMap",caricatureMap);

        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "查询成功");

        return setSuccessResponse(modelMap, result);
    }

    /**
     * 查询是否加入书架
     * @param memberId
     * @param caricatureId
     * @return
     */
    private boolean findIsCollect(Integer memberId, Integer caricatureId) {
        CaricatureBookcase caricatureBookcase = new CaricatureBookcase();
        caricatureBookcase.setMemberId(memberId);
        caricatureBookcase.setCaricatureId(caricatureId);
        caricatureBookcase = caricatureBookcaseService.selectOne(caricatureBookcase);

        return caricatureBookcase == null;

    }

    /**
     * 搜索处理方法
     * @param searchName
     */
    private void handleSearch(String searchName) {

        if(StringUtils.isEmpty(searchName)){
            return;
        }

        CaricatureSearch caricatureSearch = new CaricatureSearch();
        caricatureSearch.setSearchName(searchName);
        caricatureSearch = caricatureSearchService.selectOne(caricatureSearch);

        if (caricatureSearch == null) {
            caricatureSearch = new CaricatureSearch();
            caricatureSearch.setSearchName(searchName);
            caricatureSearch.setSearchNum(1);
        } else {
            int searchNum = caricatureSearch.getSearchNum();
            searchNum++;
            caricatureSearch.setSearchNum(searchNum);
        }

        caricatureSearchService.update(caricatureSearch);

    }

}
