package com.videoweb.ying.controller;

import com.videoweb.base.BaseController;
import com.videoweb.ying.po.TMember;
import com.videoweb.ying.service.CaricatureBookcaseService;
import com.videoweb.ying.service.CaricatureChapterImgService;
import com.videoweb.ying.service.CaricatureChapterService;
import com.videoweb.ying.service.TMemberService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author lbh
 * Date 2019-07-27
 */
@RestController
public class ApiCaricatureChapterController extends BaseController {

    @Autowired
    private CaricatureChapterService caricatureChapterService;

    @Autowired
    private CaricatureChapterImgService caricatureChapterImgService;


    @Autowired
    private TMemberService memberService;

    @PostMapping("/openapi/caricatureChapter/getListByCaricatureId")
    public Object getListByCaricatureId(ModelMap modelMap, @RequestBody Map<String,Object> param){

        logger.info("章节查询接收参数:{}",param);

        String caricatureId = (String) param.get("caricatureId");

        if(StringUtils.isEmpty(caricatureId)){
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "查询失败，参数异常");
            return setSuccessResponse(modelMap);
        }

        Map<String,Object> result = new HashMap<>();

        List<Map<String, Object>> chapterMapList = caricatureChapterService.getListByCaricatureId(Integer.parseInt(caricatureId));

        result.put("chapterMapList",chapterMapList);

        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "查询成功");

        return setSuccessResponse(modelMap, result);
    }

    /**
     * 获取章节详细信息
     * @param modelMap
     * @param param
     * @return
     */
    @PostMapping("/ying/caricatureChapter/getChapterDetail")
    public Object getChapterDetail(ModelMap modelMap, @RequestBody Map<String,Object> params, HttpServletRequest request){

        logger.info("章节详情参数:{}",params);


        String chapterId = (String)params.get("chapterId");
        if (StringUtils.isEmpty(chapterId)) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "查询失败，参数异常");
            return setSuccessResponse(modelMap);
        }

        Integer memberId = getCurrUser(request);

        if(memberId == null){
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "查询失败，参数异常");
            return setSuccessResponse(modelMap);
        }


        TMember member = new TMember();
        member.setId(memberId);
        member = memberService.selectOne(member);

        if(member == null){
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "用户不存在");
            return setSuccessResponse(modelMap);
        }

        Integer chapterIdInt = Integer.parseInt(chapterId);

        Map<String,Object> result = new HashMap<>();
        //查询章节详细信息
        Map<String, Object> caricatureChapterMap = caricatureChapterService.findOne(chapterIdInt);

        //取出上一章和下一章id查看是否为空
        Integer upId = (Integer) caricatureChapterMap.get("upId");
        Integer nextId = (Integer) caricatureChapterMap.get("nextId");

        if(nextId == null){
            caricatureChapterMap.put("nextId",-1);
        }

        if(upId == null){
            caricatureChapterMap.put("upId",-1);
        }

        //查询是否是会员
        Integer isVip = member.getIsVip();
        //查看当前章节是否收费
        String isCollectFees = (String) caricatureChapterMap.get("isCollectFees");

        if("1".equals(isCollectFees) && isVip != 1){
            modelMap.put("retCode", "-2");
            modelMap.put("retMsg", "当前用户不是会员");
            return setSuccessResponse(modelMap);
        }

        //查询所属图片
        List<Map<String,Object>> chapterImgList = caricatureChapterImgService.findChapterImgList(chapterIdInt);

        caricatureChapterMap.put("chapterImgList",chapterImgList); //保存图片信息
        result.put("caricatureChapterMap",caricatureChapterMap);

        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "查询成功");
        return setSuccessResponse(modelMap, result);
    }

}
