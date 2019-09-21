package com.videoweb.ying.controller;

import com.videoweb.base.BaseController;
import com.videoweb.utils.PropertiesUtil;
import com.videoweb.ying.po.CaricatureBookcase;
import com.videoweb.ying.service.CaricatureBookcaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author lbh
 * Date 2019-07-27
 * 书架控制器
 */
@RestController
@RequestMapping("/ying/apiCaricatureBookcase")
public class ApiCaricatureBookcaseController extends BaseController {

    @Autowired
    private CaricatureBookcaseService caricatureBookcaseService;

    @PostMapping("/save")
    public Object saveCaricatureBookcase(ModelMap modelMap, HttpServletRequest request, @RequestBody Map<String, Object> params) {

        logger.info("添加我的书架参数为:{}",params);

        if (params == null || "".equals(params)) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "参数异常");
            return setSuccessResponse(modelMap);
        }

        String caricatureId = (String) params.get("caricatureId");

        if (StringUtils.isEmpty(caricatureId)) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "参数异常");
            return setSuccessResponse(modelMap);
        }

        //获取用户id
        Integer memberId = getCurrUser(request);

        CaricatureBookcase caricatureBookcase = new CaricatureBookcase();
        caricatureBookcase.setMemberId(memberId);
        caricatureBookcase.setCaricatureId(Integer.parseInt(caricatureId));

        CaricatureBookcase formCaricatureBookcase = caricatureBookcaseService.selectOne(caricatureBookcase);

        //不存在则添加
        if(formCaricatureBookcase == null){
            caricatureBookcase.setCreateTime(new Date());
            caricatureBookcaseService.add(caricatureBookcase);
        }

        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "操作成功");

        return setSuccessResponse(modelMap);
    }


    @PostMapping("/del")
    public Object delCaricatureBookcase(ModelMap modelMap, HttpServletRequest request, @RequestBody List<Integer> caricatureIds) {

        logger.info("删除章节数据参数为:{}",caricatureIds);

        if (caricatureIds == null || caricatureIds.size() == 0) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "参数异常");
            return setSuccessResponse(modelMap);
        }

        //获取用户id
        Integer memberId = getCurrUser(request);
        logger.info("当前用户id是{}",memberId);

        for (Integer caricatureId : caricatureIds) {

            CaricatureBookcase caricatureBookcase = new CaricatureBookcase();
            caricatureBookcase.setMemberId(memberId);
            caricatureBookcase.setCaricatureId(caricatureId);
            caricatureBookcaseService.deleteByEntity(caricatureBookcase);

        }

        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "操作成功");

        return setSuccessResponse(modelMap);
    }


    @PostMapping("/getCaricatureBookcaseList")
    public Object getCaricatureBookcaseList(ModelMap modelMap, HttpServletRequest request) {


        Map<String,Object> result = new HashMap<>();

        //获取用户id
        Integer memberId = getCurrUser(request);

        List<Map<String, Object>> caricatureBookcaseList = caricatureBookcaseService.getCaricatureBookcaseListByMeberId(memberId);

        for (Map<String, Object> caricatureBookcase : caricatureBookcaseList) {
            //处理图片
            if ("1".equals(caricatureBookcase.get("coverType"))) {
                caricatureBookcase.put("cover", PropertiesUtil.getString("remote.file.uri.prefix") + caricatureBookcase.get("cover"));
            }

            //处理播放量
            Integer watchNum = (Integer)caricatureBookcase.get("watchNum");

            if ((watchNum / 10000) > 0) {
                double tk1 = new BigDecimal((float) watchNum / 10000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                caricatureBookcase.put("watchNum", tk1 + "万");
            } else {
                caricatureBookcase.put("watchNum", watchNum + "");
            }

            caricatureBookcase.remove("coverType");
        }


        result.put("caricatureBookcaseList",caricatureBookcaseList);

        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "查询成功");

        return setSuccessResponse(modelMap,result);
    }




}
