package com.videoweb.ying.controller;

import com.videoweb.base.BaseController;
import com.videoweb.ying.dao.TVideoPayMapper;
import com.videoweb.ying.service.TVideoPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * AUTHER lbh
 * Date 2019/5/21
 */

@RestController
@RequestMapping("/openapi/videoPay")
public class ApiVideoPayController extends BaseController {


    @Autowired
    private TVideoPayService videoPayService;


    /**
     * 获取每日的排行记录
     *
     * @return
     */
    @GetMapping("/findEverydayPays")
    public Object findEverydayPays(ModelMap modelMap) {

        List<Map<String, Object>> videoPays = videoPayService.findEverydayPays();
        //处理评论比例
        batchHandleCentByCareNums(videoPays);
        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "查询成功");
        return setSuccessResponse(modelMap, videoPays);
    }


    /**
     * 获取每周的排行记录
     *
     * @return
     */
    @GetMapping("/findWeeklyPays")
    public Object findWeeklyPays(ModelMap modelMap) {
        List<Map<String, Object>> videoPays = videoPayService.findWeeklyPays();
        //处理评论比例
        batchHandleCentByCareNums(videoPays);
        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "查询成功");
        return setSuccessResponse(modelMap, videoPays);
    }

    /**
     * 获取每月的排行记录
     *
     * @return
     */
    @GetMapping("/findMonthlyPays")
    public Object findMonthlyPays(ModelMap modelMap) {
        List<Map<String, Object>> videoPays = videoPayService.findMonthlyPays();
        //处理评论比例
        batchHandleCentByCareNums(videoPays);
        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "查询成功");
        return setSuccessResponse(modelMap, videoPays);
    }


    /**
     * 批量根据赞点数获取点赞比例
     */
    private void batchHandleCentByCareNums(List<Map<String, Object>> videoPays) {

        for (Map<String, Object> videoPay : videoPays) {
            handleCentByCareNum(videoPay);
            //删除不必要的数据
            videoPay.remove("dislikeNum");
            videoPay.remove("careNum");
        }

    }

    public void handleCentByCareNum(Map<String, Object> videoPay) {
        if (videoPay.get("careNum") != null && !"".equals(videoPay.get("careNum"))
                && videoPay.get("dislikeNum") != null && !"".equals(videoPay.get("dislikeNum"))) {
            Integer careNum = Integer.valueOf(videoPay.get("careNum").toString());
            Integer dislikeNum = Integer.valueOf(videoPay.get("dislikeNum").toString());
            if ((careNum + dislikeNum) != 0) {
                Integer tf = (careNum + dislikeNum);
                double tk1 = new BigDecimal((float) careNum / tf).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                videoPay.put("cent", tk1 * 100 + "%");
            } else {
                videoPay.put("cent", "0%");
            }
        } else {
            videoPay.put("cent", "0%");
        }
    }


}
