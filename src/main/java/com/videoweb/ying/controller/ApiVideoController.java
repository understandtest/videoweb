
package com.videoweb.ying.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.videoweb.ying.po.*;
import com.videoweb.ying.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.videoweb.base.BaseController;
import com.videoweb.utils.DateUtil;
import com.videoweb.utils.PropertiesUtil;
import com.videoweb.utils.redis.CacheUtil;
import com.videoweb.utils.word.WordFilter;


@RestController
public class ApiVideoController extends BaseController {


    @Autowired
    private TVideoService tVideoService;

    @Autowired
    private TCareTimesService tCareTimesService;

    @Autowired
    private TCareHistoryService tCareHistoryService;

    @Autowired
    private TVideoCommentService tVideoCommentService;

    @Autowired
    private TVideoCommentDianzanService tVideoCommentDianzanService;

    @Autowired
    private TViewsHistoryService tViewsHistoryService;

    @Autowired
    private TMemberService tMemberService;

    @Autowired
    private TBannerService tBannerService;

    @Autowired
    private TClassifyService tClassifyService;

    @Autowired
    private TSearchService tSearchService;

    @Autowired
    private TStarService tStarService;

    @Autowired
    private TExtensionInfoService tExtensionInfoService;

    @Autowired
    private TMemberLoginService tMemberLoginService;

    @Autowired
    private TLevelService tLevelService;

    @Autowired
    private TVipService tVipService;

    @Autowired
    private VideoDurationService videoDurationService;


    @Autowired
    private TVideoPayService videoPayService;

    /**
     * 获取视频详细信息接口
     *
     * @param modelMap
     * @param request
     * @param params
     * @return
     */
    @PostMapping("/ying/getVideoDetail")
    public Object getVideoDetail(ModelMap modelMap, HttpServletRequest request, @RequestBody Map<String, Object> params) {
        if (params == null || "".equals(params)) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "查询失败，参数异常");
            return setSuccessResponse(modelMap);
        }
        if (params.get("videoId") == null || "".equals(params.get("videoId"))) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "查询失败，参数异常");
            return setSuccessResponse(modelMap);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result = tVideoService.getVideoDetail(params);
        if (result == null || "".equals(result)) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "查询失败,该ID不存在");
            return setSuccessResponse(modelMap);
        }
        if ("1".equals(result.get("videoCoverType").toString())) {
            result.put("videoCover", PropertiesUtil.getString("remote.file.uri.prefix") + result.get("videoCover"));
        }
        if ("1".equals(result.get("videoType").toString())) {
            result.put("videoUrl", PropertiesUtil.getString("remote.file.uri.prefix") + result.get("videoUrl"));
        }

        if (result.get("careNum") != null && !"".equals(result.get("careNum"))
                && result.get("dislikeNum") != null && !"".equals(result.get("dislikeNum"))) {
            Integer careNum = Integer.valueOf(result.get("careNum").toString());
            Integer dislikeNum = Integer.valueOf(result.get("dislikeNum").toString());
            if ((careNum + dislikeNum) != 0) {
                Integer tf = (careNum + dislikeNum);
                double tk1 = new BigDecimal((float) careNum / tf).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                result.put("cent", tk1 * 100 + "%");
            } else {
                result.put("cent", "0%");
            }
        } else {
            result.put("cent", "0%");
        }

        if (result.get("playNum") != null && !"".equals(result.get("playNum"))) {
            int playNum = Integer.valueOf(result.get("playNum").toString());
            int tk = playNum / 10000;
            double tk1 = new BigDecimal((float) playNum / 10000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            if (tk > 0) {
                result.put("playNum", tk1 + "万");
            } else {
                result.put("playNum", result.get("playNum") + "");
            }
        } else {
            result.put("playNum", "0");
        }

        Map<String, Object> pp = new HashMap<String, Object>();
        pp.put("picType", "3");
        List<Map<String, Object>> bannerList = new ArrayList<Map<String, Object>>();
        bannerList = tBannerService.getBannerList(pp);
        if (bannerList != null && bannerList.size() > 0) {
            for (Map<String, Object> mm : bannerList) {
                if ("1".equals(mm.get("picFrom").toString())) {
                    mm.put("picUrl", PropertiesUtil.getString("remote.file.uri.prefix") + mm.get("picUrl"));
                }
            }
        }
        result.put("bannerList", bannerList);

        Map<String, Object> pp2 = new HashMap<String, Object>();
        pp2.put("picType", "5");
        List<Map<String, Object>> bannerList2 = new ArrayList<Map<String, Object>>();
        bannerList2 = tBannerService.getBannerList(pp2);
        if (bannerList2 != null && bannerList2.size() > 0) {
            for (Map<String, Object> mm : bannerList2) {
                mm.put("picUrl", PropertiesUtil.getString("remote.file.uri.prefix") + mm.get("picUrl"));
            }
        }
        result.put("bannerListHead", bannerList2);

        //验证当前人是否点赞
        Integer memberId = getCurrUser(request);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String dateString = format.format(new Date());
        TCareTimes careTimes = new TCareTimes();
        careTimes.setVideoId(Integer.valueOf(result.get("id").toString()));
        careTimes.setMemberId(memberId);
        careTimes = tCareTimesService.selectOne(careTimes);
        if (careTimes == null || "".equals(careTimes)) {
            result.put("isLike", "0");
        } else {
            result.put("isLike", careTimes.getCareType() + "");
        }
        //获取是否喜欢
        TCareHistory careHistory = new TCareHistory();
        careHistory.setMemberId(memberId);
        careHistory.setVideoId(Integer.valueOf(result.get("id").toString()));
        careHistory = tCareHistoryService.selectOne(careHistory);
        if (careHistory == null || "".equals(careHistory)) {
            result.put("isCare", "0");
        } else {
            result.put("isCare", "1");
        }
        //新增点击观看接口
        TViewsHistory viewHistory = new TViewsHistory();
        viewHistory.setMemberId(memberId);
        viewHistory.setVideoId(Integer.valueOf(params.get("videoId").toString()));
        viewHistory = tViewsHistoryService.selectOne(viewHistory);
        if (viewHistory == null || "".equals(viewHistory)) {
            viewHistory = new TViewsHistory();
            viewHistory.setMemberId(memberId);
            viewHistory.setVideoId(Integer.valueOf(params.get("videoId").toString()));
        }
        viewHistory.setViewTime(new Date());
        tViewsHistoryService.update(viewHistory);
        //删除原有缓存
        CacheUtil.getCache().del("viewHistoryList_" + memberId);

        //查看是否是会员
        modelMap.put("useView", "1"); //表示是有权限播放

        //查询可以播放时长
        VideoDuration videoDuration = videoDurationService.queryById(1);

        if (null != videoDuration) {
            modelMap.put("payDuration", videoDuration.getPlayDuration());
        }


        //todo 需要校验是否是会员，具体校验会员是否过期
        TMember tm = new TMember();
        tm.setId(memberId);
        tm = tMemberService.selectOne(tm);

        //是否是会员
        if (null != tm) {
            //当前用户是会员
            if(1 == tm.getIsVip()){

                //会员过期时间
                Date vipDate = tm.getVipDate();
                //当前系统时间
                Date now = new Date();

                //会员已过期
                if(now.after(vipDate)){
                    tm.setIsVip(0);

                    tMemberService.updateById(tm);
                }
                modelMap.put("isVip",tm.getIsVip());
            }

            //当前用户不是会员
            if (1 != tm.getIsVip()) {

                Integer viewNum = tm.getViewNum(); //免费观影次数
                Integer usedViewNum = tm.getUsedViewNum(); //已经观影次数

                Integer videoId = Integer.parseInt((String) params.get("videoId")); //当前播放id

                //当前视频放入缓存中
                String cacheKey = "USER_VIDEO_PLAY:" + memberId + "_video" + videoId; //存储key

                if(viewNum > usedViewNum){ //当前观影次数大于已观影次数
                    //查询当前视频是否播放过
                    Boolean exists = CacheUtil.getCache().exists(cacheKey);

                    if(!exists){
                        //减少当前用户观影次数
                        usedViewNum += 1;
                        tm.setUsedViewNum(usedViewNum);
                        //存入缓存中
                        CacheUtil.getCache().set(cacheKey,memberId + "_" + videoId);
                        //更新到数据库
                        tMemberService.updateById(tm);
                    }

                    modelMap.put("isVip",1);
                }else {
                    //查询当前视频是否播放过
                    Boolean exists = CacheUtil.getCache().exists(cacheKey);

                    if(exists){
                        modelMap.put("isVip",1);
                    }else{
                        modelMap.put("isVip",0);
                    }

                }

            }

        }

        //获取评论数
        Integer videoCommentId = tVideoCommentService.selectVideoCommentConut(params);
        result.put("videoCommentNum", videoCommentId);
        //获取明星影片
        List<Map<String, Object>> starVideoList = new ArrayList<Map<String, Object>>();
        Map<String, Object> starInto = new HashMap<String, Object>();
        starInto.put("starId", result.get("starId"));
        starInto.put("ppSize", 15);
        starVideoList = tVideoService.getStarVideoByStarId(starInto);
        if (starVideoList != null && starVideoList.size() > 0) {
            for (Map<String, Object> thfMap : starVideoList) {
                if ("1".equals(thfMap.get("videoCoverType").toString())) {
                    thfMap.put("videoCover", PropertiesUtil.getString("remote.file.uri.prefix") + thfMap.get("videoCover"));
                }
            }
        }
        result.put("starVideoList", starVideoList);
        //猜你喜欢的
        List<Map<String, Object>> likeVideoList = new ArrayList<Map<String, Object>>();
        Map<String, Object> likeInto = new HashMap<String, Object>();
        likeInto.put("videoId", result.get("id"));
        likeInto.put("ppSize", 15);
        likeVideoList = tVideoService.getLikeVideoById(likeInto);
        if (likeVideoList != null && likeVideoList.size() > 0) {
            for (Map<String, Object> mm : likeVideoList) {
                int playNum = Integer.valueOf(mm.get("playNum").toString());
                if ((playNum / 10000) > 0) {
                    double tk1 = new BigDecimal((float) playNum / 10000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    mm.put("playNum", tk1 + "万");
                } else {
                    mm.put("playNum", mm.get("playNum") + "");
                }

                if ("1".equals(mm.get("videoCoverType").toString())) {
                    mm.put("videoCover", PropertiesUtil.getString("remote.file.uri.prefix") + mm.get("videoCover"));
                }
            }
        }
        result.put("likeVideoList", likeVideoList);
        //更新播放次数
        TVideo tv = new TVideo();
        tv.setId(Integer.valueOf(params.get("videoId").toString()));
        tv = tVideoService.selectOne(tv);
        if (tv != null && !"".equals(tv)) {
            Integer playNum = tv.getPlayNum();
            playNum++;
            tv.setPlayNum(playNum);
            tVideoService.update(tv);
        }
        //推广链接

        Map<String, Object> getExtensionInfo = new HashMap<String, Object>();
        getExtensionInfo = tExtensionInfoService.getExtensionInfo();
        if (getExtensionInfo != null && !"".equals(getExtensionInfo)) {
            String extensionUrl = getExtensionInfo.get("extensionUrl").toString() + "?code=" + tm.getExtensionCode();
            getExtensionInfo.put("extensionUrl", extensionUrl);
        }
        if (getExtensionInfo.get("extensionContext") != null && !"".equals(getExtensionInfo.get("extensionContext"))) {
            String extensionUrl = getExtensionInfo.get("extensionUrl").toString();
            String extensionContext = String.valueOf(getExtensionInfo.get("extensionContext"));
            String thjString = extensionContext.replaceAll("#\\{code}", tm.getExtensionCode().toString()).replaceAll("#\\{url}", extensionUrl);
            getExtensionInfo.put("extensionContext", thjString);
        }
        result.put("extensionInfo", getExtensionInfo);


        //观看记录表增加记录
        Map<String, Object> payParams = new HashMap<>();
        payParams.put("videoId", params.get("videoId"));
        payParams.put("createTime", new Date());
        payParams.put("memberId", memberId);
        videoPayService.saveVideoPay(payParams);

        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "查询成功");

        System.out.println(JSON.toJSONString(setSuccessResponse(modelMap, result)));

        return setSuccessResponse(modelMap, result);
    }


    @PostMapping("/openapi/getVideoByStarId")
    public Object getVideoByStarId(ModelMap modelMap, HttpServletRequest request, @RequestBody Map<String, Object> params) {
        List<Map<String, Object>> classifyList = new ArrayList<Map<String, Object>>();
        classifyList = tClassifyService.selectClassify();
        if (classifyList != null && !"".equals(classifyList) && classifyList.size() > 0) {
            List<Map<String, Object>> classifyList2 = new ArrayList<Map<String, Object>>();
           /* Map<String, Object> tf = new HashMap<String, Object>();
            tf.put("id", -1);
            tf.put("name", "全部");
            tf.put("classifyIcon", "");
            classifyList2.add(tf);*/
            for (Map<String, Object> mm : classifyList) {
                if ("1".equals(mm.get("iconType").toString())) {
                    mm.put("classifyIcon", PropertiesUtil.getString("remote.file.uri.prefix") + mm.get("classifyIcon"));
                }
                classifyList2.add(mm);
            }
            modelMap.put("classifyList", classifyList2);
        }
        Page<?> list = tVideoService.selectListPage(params);
        if (list.getRecords() != null && list.getRecords().size() > 0) {
            for (Map<String, Object> mm : (List<Map<String, Object>>) list.getRecords()) {
                int playNum = Integer.valueOf(mm.get("playNum").toString());
                if ((playNum / 10000) > 0) {
                    double tk1 = new BigDecimal((float) playNum / 10000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    mm.put("playNum", tk1 + "万");
                } else {
                    mm.put("playNum", mm.get("playNum") + "");
                }

                if ("1".equals(mm.get("videoCoverType").toString())) {
                    mm.put("videoCover", PropertiesUtil.getString("remote.file.uri.prefix") + mm.get("videoCover"));
                }
                if ("1".equals(mm.get("videoType").toString())) {
                    mm.put("videoUrl", PropertiesUtil.getString("remote.file.uri.prefix") + mm.get("videoUrl"));
                }
            }
        }
        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "查询成功");
        return setSuccessResponse(modelMap, list);
    }


    @PostMapping("/ying/getFindVideo")
    public Object getFindVideo(ModelMap modelMap, HttpServletRequest request, @RequestBody Map<String, Object> params) {
        Integer memberId = getCurrUser(request);
        params.put("memberId", memberId);
        int page = 0;
        if (params.get("pageNum") != null && !"".equals(params.get("pageNum"))) {
            page = Integer.valueOf(params.get("pageNum").toString());
        }
        Page<?> list = null;
        if (CacheUtil.getCache().get("getFindVideo_" + page + "_" + memberId) != null && !"".equals(CacheUtil.getCache().get("getFindVideo_" + page + "_" + memberId))) {
            list = (Page<?>) CacheUtil.getCache().get("getFindVideo_" + page + "_" + memberId);
            modelMap.put("extensionInfo", CacheUtil.getCache().get("getFindVideo_ex_" + page + "_" + memberId));
        } else {
            list = tVideoService.selectListPage(params);
            if (list.getRecords() != null && list.getRecords().size() > 0) {
                for (Map<String, Object> mm : (List<Map<String, Object>>) list.getRecords()) {
                    int playNum = Integer.valueOf(mm.get("playNum").toString());
                    if ((playNum / 10000) > 0) {
                        double tk1 = new BigDecimal((float) playNum / 10000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        mm.put("playNum", tk1 + "万");
                    } else {
                        mm.put("playNum", mm.get("playNum") + "");
                    }

                    if ("1".equals(mm.get("videoCoverType").toString())) {
                        mm.put("videoCover", PropertiesUtil.getString("remote.file.uri.prefix") + mm.get("videoCover"));
                    }
                    if ("1".equals(mm.get("videoType").toString())) {
                        mm.put("videoUrl", PropertiesUtil.getString("remote.file.uri.prefix") + mm.get("videoUrl"));
                    }
                }
            }
            TMember tm = new TMember();
            tm.setId(memberId);
            tm = tMemberService.selectOne(tm);
            Map<String, Object> getExtensionInfo = new HashMap<String, Object>();
            getExtensionInfo = tExtensionInfoService.getExtensionInfo();
            if (getExtensionInfo != null && !"".equals(getExtensionInfo)) {
                if (getExtensionInfo.get("extensionUrl") != null && !"".equals(getExtensionInfo.get("extensionUrl"))) {
                    String extensionUrl = getExtensionInfo.get("extensionUrl").toString() + "?code=" + tm.getExtensionCode();
                    getExtensionInfo.put("extensionUrl", extensionUrl);
                }
                if (getExtensionInfo.get("extensionContext") != null && !"".equals(getExtensionInfo.get("extensionContext"))) {
                    String extensionUrl = getExtensionInfo.get("extensionUrl").toString();
                    String extensionContext = String.valueOf(getExtensionInfo.get("extensionContext"));
                    String yhString = extensionContext.replaceAll("#\\{code}", tm.getExtensionCode().toString()).replaceAll("#\\{url}", extensionUrl);
                    getExtensionInfo.put("extensionContext", yhString);
                }
            }
            modelMap.put("extensionInfo", getExtensionInfo);
            CacheUtil.getCache().set("getFindVideo_" + page + "_" + memberId, (Serializable) list);
            CacheUtil.getCache().set("getFindVideo_ex_" + page + "_" + memberId, (Serializable) getExtensionInfo);
        }
        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "查询成功");
        return setSuccessResponse(modelMap, list);
    }

    /**
     * 获取评论列表
     *
     * @param modelMap
     * @param request
     * @param params
     * @return
     */
    @PostMapping("/ying/getVideoCommon")
    public Object getVideoCommon(ModelMap modelMap, HttpServletRequest request, @RequestBody Map<String, Object> params) {
        if (params.get("videoId") == null || "".equals(params.get("videoId"))) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "获取评论列表失败，参数异常");
            return setSuccessResponse(modelMap);
        }
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        Integer memberId = getCurrUser(request);
        params.put("memberId", memberId);
        result = tVideoCommentService.getVideoCommon(params);
        if (result != null && result.size() > 0) {
            for (Map<String, Object> mf : result) {
                mf.put("headpic", PropertiesUtil.getString("remote.file.uri.prefix") + mf.get("headpic"));
                try {
                    mf.put("comTime", DateUtil.cgformat((Date) mf.get("comTime")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }


        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "查询成功");
        return setSuccessResponse(modelMap, result);
    }


    @PostMapping("/ying/saveVideoCommon")
    public Object saveVideoCommon(ModelMap modelMap, HttpServletRequest request, @RequestBody Map<String, Object> params) {
        Integer memberId = getCurrUser(request);
        //验证当前用户是否被封设备还是封号
        TMemberLogin tmlogin = new TMemberLogin();
        tmlogin.setMemberId(memberId);
        tmlogin = tMemberLoginService.selectOne(tmlogin);
        if (tmlogin != null && !"".equals(tmlogin)) {
            if (tmlogin.getIsEnable() == 0) {
                modelMap.put("retCode", "-1");
                modelMap.put("retMsg", "账号已冻结，请联系客服!");
                return setSuccessResponse(modelMap);
            }
            if (tmlogin.getIsDeviceEnable() == 0) {
                modelMap.put("retCode", "-1");
                modelMap.put("retMsg", "设备已冻结，请联系客服!");
                return setSuccessResponse(modelMap);
            }
        }
        if (params == null || "".equals(params)) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "新增数据失败，参数异常");
            return setSuccessResponse(modelMap);
        }

        if (params.get("comContent") == null || "".equals(params.get("comContent"))
                || params.get("videoId") == null || "".equals(params.get("videoId"))) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "新增数据失败，参数异常");
            return setSuccessResponse(modelMap);
        }
        TVideoComment tvc = new TVideoComment();
        tvc.setVideoId(Integer.valueOf(params.get("videoId").toString()));
        tvc.setMemberId(memberId);
        tvc.setComTime(new Date());
        String comContent = params.get("comContent").toString();
        if (comContent != null && !"".equals(comContent)) {
            comContent = WordFilter.doFilter(comContent);
        }
        tvc.setComContent(comContent);
        tvc = tVideoCommentService.update(tvc);
        if (tvc == null || "".equals(tvc)) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "评论失败");
            return setSuccessResponse(modelMap);
        }
        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "评论成功");
        return setSuccessResponse(modelMap);
    }


    @PostMapping("/ying/saveVideoCommonDianzan")
    public Object saveVideoCommonDianzan(ModelMap modelMap, HttpServletRequest request, @RequestBody Map<String, Object> params) {
        Integer memberId = getCurrUser(request);
        if (params == null || "".equals(params)) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "新增数据失败，参数异常");
            return setSuccessResponse(modelMap);
        }

        if (params.get("videCommentId") == null || "".equals(params.get("videCommentId"))) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "新增数据失败，参数异常");
            return setSuccessResponse(modelMap);
        }
        TVideoCommentDianzan tvc = new TVideoCommentDianzan();
        tvc.setVideCommentId(Integer.valueOf(params.get("videCommentId").toString()));
        tvc.setMemberId(memberId);
        tvc = tVideoCommentDianzanService.update(tvc);
        if (tvc == null || "".equals(tvc)) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "评论失败");
            return setSuccessResponse(modelMap);
        }
        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "查询成功");
        return setSuccessResponse(modelMap);
    }

    @PostMapping("/openapi/newVideoPage")
    public Object newVideoPage(ModelMap modelMap, HttpServletRequest request, @RequestBody Map<String, Object> params) {
        Page<?> list = tVideoService.newVideoPage(params);
        if (list.getRecords() != null && list.getRecords().size() > 0) {
            for (Map<String, Object> mm : (List<Map<String, Object>>) list.getRecords()) {
                int playNum = Integer.valueOf(mm.get("playNum").toString());
                if ((playNum / 10000) > 0) {
                    double tk1 = new BigDecimal((float) playNum / 10000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    mm.put("playNum", tk1 + "万");
                } else {
                    mm.put("playNum", mm.get("playNum") + "");
                }
                if ("1".equals(mm.get("videoCoverType").toString())) {
                    mm.put("videoCover", PropertiesUtil.getString("remote.file.uri.prefix") + mm.get("videoCover"));
                }
                if ("1".equals(mm.get("videoType").toString())) {
                    mm.put("videoUrl", PropertiesUtil.getString("remote.file.uri.prefix") + mm.get("videoUrl"));
                }
            }
        }
        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "查询成功");
        return setSuccessResponse(modelMap, list);
    }

    /**
     * 搜索
     *
     * @param modelMap
     * @param request
     * @param params
     * @return
     */
    @PostMapping("/openapi/selectOPenVideo")
    public Object selectOPenVideo(ModelMap modelMap, HttpServletRequest request, @RequestBody Map<String, Object> params) {
        //获取搜索数据
        if (params == null || "".equals(params)) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "搜索数据异常，参数异常");
            return setSuccessResponse(modelMap);
        }

        if (params.get("searchName") == null || "".equals(params.get("searchName"))) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "搜索数据异常，参数异常");
            return setSuccessResponse(modelMap);
        }


        TSearch search = new TSearch();
        search.setSearchName(params.get("searchName").toString());
        search = tSearchService.selectOne(search);
        if (search == null || "".equals(search)) {
            search = new TSearch();
            search.setSearchName(params.get("searchName").toString());
        } else {
            int searchNum = search.getSearchNum();
            searchNum++;
            search.setSearchNum(searchNum);
        }
        tSearchService.update(search);


        Page<?> list = tVideoService.selectOPenVideo(params);
        if (list.getRecords() != null && list.getRecords().size() > 0) {
            for (Map<String, Object> mm : (List<Map<String, Object>>) list.getRecords()) {
                int playNum = Integer.valueOf(mm.get("playNum").toString());
                if ((playNum / 10000) > 0) {
                    double tk1 = new BigDecimal((float) playNum / 10000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    mm.put("playNum", tk1 + "万");
                } else {
                    mm.put("playNum", mm.get("playNum") + "");
                }

                if ("1".equals(mm.get("videoCoverType").toString())) {
                    mm.put("videoCover", PropertiesUtil.getString("remote.file.uri.prefix") + mm.get("videoCover"));
                }
                if ("1".equals(mm.get("videoType").toString())) {
                    mm.put("videoUrl", PropertiesUtil.getString("remote.file.uri.prefix") + mm.get("videoUrl"));
                }
            }
        }
        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "查询成功");
        return setSuccessResponse(modelMap, list);
    }

    /**
     * 热门搜索
     *
     * @param modelMap
     * @param request
     * @return
     */
    @PostMapping("/openapi/searchSearchName")
    public Object searchSearchName(ModelMap modelMap, HttpServletRequest request) {
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        if (CacheUtil.getCache().get("searchSearchName") != null && !"".equals(CacheUtil.getCache().get("searchSearchName"))) {
            resultList = (List<Map<String, Object>>) CacheUtil.getCache().get("searchSearchName");
        } else {
            resultList = tSearchService.searchName();
            CacheUtil.getCache().set("searchSearchName", (Serializable) resultList, 180);
        }
        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "查询成功");
        return setSuccessResponse(modelMap, resultList);
    }

    /**
     * 提交失效次数
     *
     * @param modelMap
     * @param request
     * @param params
     * @return
     */
    @PostMapping("/openapi/saveloseVideo")
    public Object saveloseVideo(ModelMap modelMap, HttpServletRequest request, @RequestBody Map<String, Object> params) {
        if (params == null || "".equals(params)) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "提交失效次数失败，参数异常");
            return setSuccessResponse(modelMap);
        }

        if (params.get("videoId") == null || "".equals(params.get("videoId"))) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "提交失效次数失败，参数异常");
            return setSuccessResponse(modelMap);
        }
        TVideo videoIde = new TVideo();
        videoIde.setId(Integer.valueOf(params.get("videoId").toString()));
        videoIde = tVideoService.selectOne(videoIde);
        if (videoIde != null && !"".equals(videoIde)) {
            int losNum = videoIde.getLoseNum().intValue();
            losNum++;
            videoIde.setLoseNum(losNum);
            tVideoService.update(videoIde);
            modelMap.put("retCode", "1");
            modelMap.put("retMsg", "提交失效成功");
            return setSuccessResponse(modelMap);
        }
        modelMap.put("retCode", "-1");
        modelMap.put("retMsg", "提交失效失败");
        return setSuccessResponse(modelMap);
    }

    /**
     * 点击广告新增观影次数
     *
     * @param modelMap
     * @param request
     * @param params
     * @return
     */

    @PostMapping("/ying/clickAd")
    public Object clickAd(ModelMap modelMap, HttpServletRequest request, @RequestBody Map<String, Object> params) {
        Integer memberId = getCurrUser(request);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String dateString = format.format(new Date());
        if (params == null || "".equals(params)) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "新增数据失败，参数异常");
            return setSuccessResponse(modelMap);
        }
        if (params.get("id") == null || "".equals(params.get("id"))) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "新增数据失败，参数异常");
            return setSuccessResponse(modelMap);
        }

        if (CacheUtil.getCache().get("ad_" + memberId + "_" + dateString + "_" + params.get("id")) == null ||
                "".equals(CacheUtil.getCache().get("ad_" + memberId + "_" + dateString + "_" + params.get("id")))) {
            //获取当前人的登录类型
            if (CacheUtil.getCache().get("USER_TYPE_" + memberId) != null
                    && !"".equals(CacheUtil.getCache().get("USER_TYPE_" + memberId))) {
//				String userType = (String) CacheUtils.getInstance().get("USER_TYPE_"+memberId);
                //当是用户登录的情况下
//				if("1".equals(userType))
//				{

                //新增临时缓存次数
                TMember tmeber = new TMember();
                tmeber.setId(memberId);
                tmeber = tMemberService.selectOne(tmeber);
                if (tmeber == null || "".equals(tmeber)) {
                    modelMap.put("retCode", "-1");
                    modelMap.put("retMsg", "token失效，新增观影次数失败，请重新登录");
                    return setSuccessResponse(modelMap);
                }
                Integer oldTmp = tmeber.getTmpViewNum() == null ? 0 : tmeber.getTmpViewNum();
                oldTmp = oldTmp + 1;
                tmeber.setTmpViewNum(oldTmp);
                tMemberService.update(tmeber);
//				}
//				else
//				{
//					
//					if( CacheUtils.getInstance().get("USER_TYPE_"+memberId+"_"+dateString+"_viewNum")!= null &&
//							!"".equals(CacheUtils.getInstance().get("USER_TYPE_"+memberId+"_"+dateString+"_viewNum")))
//					{
//						Integer viewNum = Integer.valueOf(CacheUtils.getInstance().get("USER_TYPE_"+memberId+"_"+dateString+"_viewNum").toString());
//						viewNum++;
//						CacheUtils.getInstance().put("USER_TYPE_"+memberId+"_"+dateString+"_viewNum",viewNum);
//					}
//				}
            }
            CacheUtil.getCache().set("ad_" + memberId + "_" + dateString + "_" + params.get("id"), "1");
        }
        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "新增缓存次数完成");
        return setSuccessResponse(modelMap);
    }


    @PostMapping("/ying/saveQrcode")
    public Object saveQrcode(ModelMap modelMap, HttpServletRequest request) {
        Integer memberId = getCurrUser(request);
        //验证当前人是否已经保存过二维码
        TMember tmeber = new TMember();
        tmeber.setId(memberId);
        tmeber = tMemberService.selectOne(tmeber);
        if (tmeber == null || "".equals(tmeber)) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "token失效，新增观影次数失败，请重新登录");
            return setSuccessResponse(modelMap);
        }
        if (tmeber.getIsSaveQr().intValue() != 0) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "您已存储过二维码，无法再次获取观影次数奖励");
            return setSuccessResponse(modelMap);
        }
        //获取当前人的登录类型
        if (CacheUtil.getCache().get("USER_TYPE_" + memberId) != null
                && !"".equals(CacheUtil.getCache().get("USER_TYPE_" + memberId))) {
//			String userType = (String) CacheUtils.getInstance().get("USER_TYPE_"+memberId);
            //当是用户登录的情况下
//			if("1".equals(userType))
//			{
            //新增临时缓存次数
            tmeber.setIsSaveQr(1);
            int view = tmeber.getViewNum() == null ? 0 : tmeber.getViewNum();
            view++;
            tmeber.setViewNum(view);
            tMemberService.update(tmeber);
//			}
			/*else
			{
				
				if( CacheUtils.getInstance().get("USER_TYPE_"+memberId+"_"+dateString+"_viewNum")!= null &&
						!"".equals(CacheUtils.getInstance().get("USER_TYPE_"+memberId+"_"+dateString+"_viewNum")))
				{
					Integer viewNum = Integer.valueOf(CacheUtils.getInstance().get("USER_TYPE_"+memberId+"_"+dateString+"_viewNum").toString());
					viewNum++;
					CacheUtils.getInstance().put("USER_TYPE_"+memberId+"_"+dateString+"_viewNum",viewNum);
				}
			}*/
        }
        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "新增缓存次数完成");
        return setSuccessResponse(modelMap);
    }
}
