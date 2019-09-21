package com.videoweb.ying.controller;

import com.videoweb.base.BaseController;
import com.videoweb.ying.po.MemberStar;
import com.videoweb.ying.service.MemberStarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author hong
 * @Date 19-9-10
 */
@RestController
@RequestMapping("/ying/memberStar")
public class ApiMemberStarController extends BaseController {

    @Autowired
    private MemberStarService memberStarService;

    /**
     * 获取用户收藏数据
     * @return
     */
    @PostMapping("/getMemberCollect")
    public Object getMemberCollect(HttpServletRequest request, ModelMap modelMap){

        Integer memberId = getCurrUser(request);

        List<Map<String,Object>> memberCollectList = memberStarService.getMemberCollect(memberId);

        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "查询成功");
        return setSuccessResponse(modelMap, memberCollectList);
    }


    /**
     * 保存用户收藏
     * @param modelMap
     * @param request
     * @param params
     * @return
     */
    @PostMapping("/saveMemberCollect")
    public Object saveMemberCollect(ModelMap modelMap, HttpServletRequest request, Integer starId) {

        if (null == starId) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "参数异常");
            return setSuccessResponse(modelMap);
        }

        //获取用户id
        Integer memberId = getCurrUser(request);

        MemberStar memberStar = new MemberStar();
        memberStar.setMemberId(memberId);
        memberStar.setStarId(starId);

        MemberStar formMemberStar = memberStarService.selectOne(memberStar);

        //不存在则添加
        if(null == formMemberStar){
            memberStar.setCreateTime(new Date());
            memberStarService.addCooect(memberStar);
        }

        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "操作成功");

        return setSuccessResponse(modelMap);
    }

    /**
     * 删除收藏
     * @param modelMap
     * @param request
     * @param memberStarIds
     * @return
     */
    @PostMapping("/delMemberCollect")
    public Object delMemberCollect(ModelMap modelMap, HttpServletRequest request, @RequestBody List<Integer> memberStarIds) {

        //获取用户id
        Integer memberId = getCurrUser(request);

        memberStarService.delMemberCollect(memberStarIds,memberId);

        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "操作成功");

        return setSuccessResponse(modelMap);
    }


}
