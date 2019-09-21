package com.videoweb.ying.controller;

import com.videoweb.base.BaseController;
import com.videoweb.ying.po.TMember;
import com.videoweb.ying.service.IntegralClassifyService;
import com.videoweb.ying.service.TMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author hong
 * @Date 19-8-26
 */
@RestController
@RequestMapping("/ying/integralClassify")
public class ApiIntegralClassifyController extends BaseController {

    @Autowired
    private IntegralClassifyService integralClassifyService;

    @Autowired
    private TMemberService memberService;

    /**
     * 添加积分
     * @param modelMap
     * @param integralId 积分类别Id
     * @return
     */
        @PostMapping("/addIntegral")
    public Object addIntegral(ModelMap modelMap, HttpServletRequest request,Integer integralId){

        //获取用户id
        Integer memberId = getCurrUser(request);

        //校验参数是否合法
        TMember member = memberService.findOne(memberId);

        if(null == member){
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "非法操作，当前用户不存在");
            return setSuccessResponse(modelMap);
        }

        //系统添加积分接口只能添加id为1,2,3类型的
        boolean integralIdIsLegal = integralId == 1 || integralId == 2 || integralId == 3 || integralId == 7;

        if(!integralIdIsLegal){
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "操作失败，参数异常");
            return setSuccessResponse(modelMap);
        }

        boolean isSuccess = integralClassifyService.addIntegral(memberId, integralId);

        //成功添加积分
        if(!isSuccess){
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "非法操作，您已获取过积分，请勿重复操作");
            return setSuccessResponse(modelMap);
        }

        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "添加积分成功");
        return setSuccessResponse(modelMap);
    }


    /**
     * 查询所有的积分类别
     * @return
     */
    @PostMapping("findAll")
    public Object findAll(HttpServletRequest request,ModelMap modelMap){

        //获取用户id
        Integer memberId = getCurrUser(request);

        //校验参数是否合法
        TMember member = memberService.findOne(memberId);

        if(null == member){
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "非法操作，当前用户不存在");
            return setSuccessResponse(modelMap);
        }

        List<Map<String, Object>> integralClassifyList = integralClassifyService.findAllAndLookIsAccomplishHistory(memberId);

        modelMap.put("isVip",member.getIsVip());
        modelMap.put("integralNumber",member.getIntegralNumber());
        modelMap.put("integralClassifyList",integralClassifyList);
        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "查询成功");
        return setSuccessResponse(modelMap);
    }


}
