package com.videoweb.ying.controller;

import com.videoweb.base.BaseController;
import com.videoweb.ying.po.IntegralVip;
import com.videoweb.ying.po.TMember;
import com.videoweb.ying.service.IntegralVipService;
import com.videoweb.ying.service.TMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author hong
 * @Date 19-8-26
 */
@RestController
@RequestMapping("/ying/integralVip")
public class ApiIntegralVipController extends BaseController {

    @Autowired
    private TMemberService memberService;

    @Autowired
    private IntegralVipService integralVipService;


    @PostMapping("/exchange")
    public Object exchangeVip(ModelMap modelMap,HttpServletRequest request,Integer integralVipId){

        //获取用户id
        Integer memberId = getCurrUser(request);
        logger.info("兑换积分，当前用户id为{}，兑换积分id为{]",memberId,integralVipId);

        //校验参数是否合法
        TMember member = memberService.findOne(memberId);

        IntegralVip integralVip = integralVipService.findOne(integralVipId);

        if(null == member || null == integralVip){
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "非法操作，当前用户不存在");
            return setSuccessResponse(modelMap);
        }

        if(null == integralVipId){
                modelMap.put("retCode", "-1");
                modelMap.put("retMsg", "操作失败，参数异常");
                return setSuccessResponse(modelMap);
        }

        boolean isSuccess = integralVipService.exchangeVip(member,integralVipId);

        if(!isSuccess){
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "操作失败，积分不足");
            return setSuccessResponse(modelMap);
        }

        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "操作成功");
        return setSuccessResponse(modelMap);

    }

    /**
     * 查询所有的会员积分兑换
     * @return
     */
    @PostMapping("/findAll")
    public Object findAll(ModelMap modelMap){

        Map<String,Object> result = new HashMap<>();

        List<Map<String, Object>> integralVipList = integralVipService.findAll();
        result.put("integralVipList",integralVipList);

        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "操作成功");
        return setSuccessResponse(modelMap,result);
    }


}
