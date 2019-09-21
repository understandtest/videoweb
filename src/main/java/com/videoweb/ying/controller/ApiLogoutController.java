package com.videoweb.ying.controller;

import com.videoweb.base.BaseController;
import com.videoweb.utils.redis.CacheUtil;
import com.videoweb.ying.po.TMember;
import com.videoweb.ying.service.TMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Author lbh
 * Date 2019-07-27
 */
@RestController
@RequestMapping("/ying")
public class ApiLogoutController extends BaseController {

    @Autowired
    private TMemberService memberService;

    @PostMapping("/logout")
    public Object logout(ModelMap modelMap,HttpServletRequest request){

        //获取用户信息
        Integer memberId = getCurrUser(request);

        logger.info("用户id为:{}退出登陆",memberId);

        //查询用户
        TMember member = new TMember();
        member.setId(memberId);
        member = memberService.selectOne(member);

        if(member != null){
            //获取手机号
            String tel = member.getTel();
            CacheUtil.getCache().del("onLine:" + tel);
        }


        return setSuccessResponse(modelMap);
    }
}
