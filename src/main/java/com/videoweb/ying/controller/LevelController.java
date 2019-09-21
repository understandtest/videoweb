package com.videoweb.ying.controller;

import com.videoweb.base.BaseController;
import com.videoweb.ying.po.TExtensionHistory;
import com.videoweb.ying.po.TMember;
import com.videoweb.ying.service.TExtensionHistoryService;
import com.videoweb.ying.service.TLevelService;
import com.videoweb.ying.service.TMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author lbh
 * Date 2019/6/14
 */
@RestController
@RequestMapping("/ying/level")
public class LevelController extends BaseController {

    @Autowired
    private TLevelService levelService;

    @Autowired
    private TMemberService memberService;

    @Autowired
    private TExtensionHistoryService tExtensionHistoryService;

    @PostMapping("/getLevels")
    public Object getLevels(ModelMap modelMap,HttpServletRequest request){

        if(null == request){
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "参数错误");
            return setSuccessResponse(modelMap);
        }

        //获取用户信息
        Integer memberId= getCurrUser(request);
        TMember mem = new TMember();
        mem.setId(memberId);
        mem= memberService.selectOne(mem);

        if(mem == null)
        {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "参数错误");
            return setSuccessResponse(modelMap);
        }

        //获取用户已拉人数
        Map<String,Object> params = new HashMap<>();
        params.put("memberId",memberId);
        List<Map<String, Object>> mapList = tExtensionHistoryService.getExtensionHistory(params);

        Integer exNum = mapList.size();
        List<Map> levels = levelService.findLevels();

        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "查询成功");
        modelMap.put("levels",levels); //推广规则
        modelMap.put("exNum",exNum); //已推广人数

        return setSuccessResponse(modelMap);
    }



}
