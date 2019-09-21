package com.videoweb.ying.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.videoweb.base.BaseController;
import com.videoweb.utils.PropertiesUtil;
import com.videoweb.ying.service.TDataKeyService;
import com.videoweb.ying.service.TStarService;
import com.videoweb.ying.service.TVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class ApiStarController extends BaseController {

    @Autowired
    private TStarService tStarService;

    @Autowired
    private TDataKeyService tDataKeyService;

    @Autowired
    private TVideoService tVideoService;


    @PostMapping("/ying/getStarPage")
    public Object getStarPage(ModelMap modelMap, HttpServletRequest request, @RequestBody Map<String, Object> params) {
        //罩杯信息
        List<Map<String, Object>> tf = new ArrayList<Map<String, Object>>();
        tf = tDataKeyService.getCupInfo();
        List<Map<String, Object>> tf2 = new ArrayList<Map<String, Object>>();
        Map<String, Object> tcup = new HashMap<String, Object>();
        tcup.put("value", "-1");
        tcup.put("name", "全部");

        tf2.add(tcup);
        tf2.addAll(tf);
        modelMap.put("cupList", tf2);

        // 用户id
        Integer memberId = getCurrUser(request);
        params.put("memberId",memberId);
        //明星数据
        Page<?> list = tStarService.selectListPage(params);
        if (list.getRecords() != null && !"".equals(list.getRecords())) {
            for (Map<String, Object> tfk : (List<Map<String, Object>>) list.getRecords()) {
                if ("1".equals(tfk.get("picType").toString())) {
                    tfk.put("headpic", PropertiesUtil.getString("remote.file.uri.prefix") + tfk.get("headpic"));
                }
            }
        }
        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "查询成功");
        return setSuccessResponse(modelMap, list);
    }

    @PostMapping("/openapi/getStarInfo")
    public Object getStarInfo(ModelMap modelMap, HttpServletRequest request, @RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<String, Object>();
        Page<?> list = tStarService.selectListPage(params);
        if (list != null && !"".equals(list) && list.getRecords() != null && !"".equals(list.getRecords())) {
            List<Map<String, Object>> tf = new ArrayList<Map<String, Object>>();
            tf = (List<Map<String, Object>>) list.getRecords();
            if (tf != null && tf.size() > 0) {
                result = tf.get(0);
                if (result != null && !"".equals(result)) {
                    if ("1".equals(result.get("picType").toString())) {
                        result.put("headpic", PropertiesUtil.getString("remote.file.uri.prefix") + result.get("headpic"));
                    }
                }
            }

        }
        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "查询成功");
        return setSuccessResponse(modelMap, result);
    }

    @PostMapping("/getCupInfo")
    public Object getCupInfo(ModelMap modelMap, HttpServletRequest request) {
        List<Map<String, Object>> tf = new ArrayList<Map<String, Object>>();
        tf = tDataKeyService.getCupInfo();
        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "查询成功");
        return setSuccessResponse(modelMap, tf);
    }

}
