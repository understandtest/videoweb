package com.videoweb.ying.controller;

import com.videoweb.base.BaseController;
import com.videoweb.utils.*;
import com.videoweb.utils.fastdfs.FileModel;
import com.videoweb.utils.redis.CacheUtil;
import com.videoweb.utils.word.WordFilter;
import com.videoweb.ying.po.ShortRealmNameSetting;
import com.videoweb.ying.po.TLevel;
import com.videoweb.ying.po.TMember;
import com.videoweb.ying.po.TVip;
import com.videoweb.ying.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/ying")
public class ApiMemberController extends BaseController {

    @Autowired
    private TMemberService tMemberService;

    @Autowired
    private TLevelService tLevelService;

    @Autowired
    private TViewsHistoryService tViewsHistoryService;

    @Autowired
    private TCareHistoryService tCareHistoryService;

    @Autowired
    private TBannerService bannerService;

    @Autowired
    private TTouristService tTouristService;

    @Autowired
    private TVersionService tVersionService;

    @Autowired
    private TQqService tQqService;

    @Autowired
    private TExtensionInfoService tExtensionInfoService;

    @Autowired
    private TVipService tVipService;

    @Autowired
    private ShortRealmNameSettingService shortRealmNameSettingService;

    /**
     * 获取个人信息
     *
     * @param modelMap
     * @param request
     * @return
     */
    @PostMapping("/getMemberInfo")
    public Object getMemberInfo(ModelMap modelMap, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        Integer memberId = getCurrUser(request);
        //获取用户类型
        if (CacheUtil.getCache().get("USER_TYPE_" + memberId) != null
                && !"".equals(CacheUtil.getCache().get("USER_TYPE_" + memberId))) {
            String userType = (String) CacheUtil.getCache().get("USER_TYPE_" + memberId);
            //获取用户信息
            Map<String, Object> tm = new HashMap<String, Object>();
            tm = tMemberService.queryMemberInfo(memberId);
            if (tm == null || "".equals(tm)) {
                modelMap.put("retCode", "-1");
                modelMap.put("retMsg", "未查询到结果");
                return setSuccessResponse(modelMap);
            }
            if (tm.get("headpic") != null && !"".equals(tm.get("headpic"))) {
                tm.put("headpic", PropertiesUtil.getString("remote.file.uri.prefix") + tm.get("headpic"));
            } else {
                tm.put("headpic", "");
            }
            if (tm.get("nickName") == null || "".equals(tm.get("nickName"))) {
                tm.put("nickName", "游客");
            }
            //获取等级相差人数
            if (tm.get("sortNo") != null && !"".equals(tm.get("sortNo"))) {
                Map<String, Object> tf = new HashMap<String, Object>();
                Map<String, Object> intoPrams = new HashMap<String, Object>();
                Integer sortNo = Integer.valueOf(tm.get("sortNo").toString());
                sortNo += 1;
                if (sortNo <= 6) {
                    intoPrams.put("sortNo", sortNo);
                    tf = tLevelService.getNextLevel(intoPrams);
                    if (tf != null && !"".equals(tf)) {
                        //tm.put("nextIcon", root+tf.get("leIcon"));
                        Integer exNum = Integer.valueOf(tf.get("exNum").toString());
                        Integer currentExNum = Integer.valueOf(tm.get("exNum").toString());
                        Integer ccNum = exNum - currentExNum;
                        tm.put("ccExNum", ccNum);
                        tm.put("nextSortNo", sortNo);
                        tm.put("nexName", tf.get("nexName"));
                        double tk1 = new BigDecimal((float) currentExNum / exNum).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        tm.put("percentExNum", tk1);
                    }
                } else {
                    tm.put("ccExNum", 0);
                    tm.put("nextSortNo", (sortNo - 1));
                    tm.put("nexName", tm.get("levleName"));
                }
            }
            //获取浏览记录
            List<Map<String, Object>> viewHistoryList = new ArrayList<Map<String, Object>>();
            Map<String, Object> tcc = new HashMap<String, Object>();
            tcc.put("memberId", tm.get("id"));
            if (CacheUtil.getCache().get("viewHistoryList_" + tm.get("id")) != null
                    && !"".equals(CacheUtil.getCache().get("viewHistoryList_" + tm.get("id")))) {
                viewHistoryList = (List<Map<String, Object>>) CacheUtil.getCache().get("viewHistoryList_" + tm.get("id"));
            } else {
                viewHistoryList = tViewsHistoryService.selectViewHistory(tcc);
                for (Map<String, Object> mm : viewHistoryList) {
                    //todo 发生空指针异常
                    if (mm != null && "1".equals(mm.get("videoCoverType").toString())) {
                        mm.put("videoCover", PropertiesUtil.getString("remote.file.uri.prefix") + mm.get("videoCover"));
                    }
                }
                CacheUtil.getCache().set("viewHistoryList_" + tm.get("id"), (Serializable) viewHistoryList);
            }
            result.put("viewHistoryList", viewHistoryList);
            int viewNum = Integer.valueOf(tm.get("viewNum").toString()) + Integer.valueOf(tm.get("tmpViewNum").toString());
            int cacheNum = Integer.valueOf(tm.get("cacheNum").toString());
            TLevel level = new TLevel();
            level.setId(Integer.valueOf(tm.get("levelId").toString()));
            level = tLevelService.selectOne(level);
      	   /* if(level != null && !"".equals(level))
      	    {
      	    	viewNum = viewNum+level.getViewNum();
      	    	cacheNum = cacheNum+level.getCacheNum();
      	     }*/

            //todo vip结束时间
            tm.put("vipDate", tm.get("vipDate"));
      	    /*if("1".equals(tm.get("isVip").toString()))
      	    {
      	    	viewNum=999999999;
      	    	tm.put("viewNum", viewNum);
      	    	TVip tvip = new TVip();
				tvip.setId(Integer.valueOf(tm.get("vipId").toString()));
				tvip = tVipService.selectOne(tvip);
				tm.put("cacheNum", tvip.getCacheNum());
      	    	
      	    }*/
      	    /*else
      	    {
      	    	tm.put("viewNum", viewNum);
           	    tm.put("cacheNum", cacheNum);
			}*/
            if ("1".equals(userType)) {
                result.put("memberInfo", tm);
                //获取我喜欢的记录
                List<Map<String, Object>> careHistoryList = new ArrayList<Map<String, Object>>();
                if (CacheUtil.getCache().get("careHistoryList_" + tm.get("id")) != null && !"".equals("careHistoryList_" + tm.get("id"))) {
                    careHistoryList = (List<Map<String, Object>>) CacheUtil.getCache().get("careHistoryList_" + tm.get("id"));
                } else {
                    careHistoryList = tCareHistoryService.selectCareHistory(tcc);
                    for (Map<String, Object> mm : careHistoryList) {
                        if ("1".equals(mm.get("videoCoverType").toString())) {
                            mm.put("videoCover", PropertiesUtil.getString("remote.file.uri.prefix") + mm.get("videoCover"));
                        }
                    }
                    CacheUtil.getCache().set("careHistoryList_" + tm.get("id"), (Serializable) careHistoryList);
                }
                result.put("careHistoryList", careHistoryList);
                //将强制提醒关闭
                if (tm.get("isRemind") != null && !"".equals(tm.get("isRemind"))) {
                    Integer isRemind = Integer.valueOf(tm.get("isRemind").toString());
                    if (isRemind.intValue() == 1) {
                        TMember tmf = new TMember();
                        tmf.setId(memberId);
                        tmf.setIsRemind(0);
                        tMemberService.update(tmf);
                    }
                }
            } else {
                Map<String, Object> tmk = new HashMap<String, Object>();
//				SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//				String  dateString = format.format(new Date());
//				if( CacheUtils.getInstance().get("USER_TYPE_"+memberId+"_"+dateString+"_viewNum")!= null &&
//						!"".equals(CacheUtils.getInstance().get("USER_TYPE_"+memberId+"_"+dateString+"_viewNum"))&&
//						CacheUtils.getInstance().get("USER_TYPE_"+memberId+"_"+dateString+"_cacheNum")!= null &&
//						!"".equals(CacheUtils.getInstance().get("USER_TYPE_"+memberId+"_"+dateString+"_cacheNum")))
//				{
//					Integer viewNum = Integer.valueOf(CacheUtils.getInstance().get("USER_TYPE_"+memberId+"_"+dateString+"_viewNum").toString());
//					Integer cacheNum = Integer.valueOf(CacheUtils.getInstance().get("USER_TYPE_"+memberId+"_"+dateString+"_cacheNum").toString());
//					List<TTourist> tk = new ArrayList<TTourist>();
//				    Map<String,Object> pp = new HashMap<String,Object>();
//				    tk = tTouristService.queryList(pp);
//				    if(tk != null && tk.size()>0)
//				    {
//				    	TTourist tkk = new TTourist();
//				    	tkk = tk.get(0);
//				    	
//				    }
                tmk.put("memberId", memberId);
                tmk.put("viewNum", viewNum);
                tmk.put("cacheNum", cacheNum);
                tmk.put("usedViewNum", Integer.valueOf(String.valueOf(tm.get("usedViewNum"))));
                tmk.put("usedCacheNum", Integer.valueOf(String.valueOf(tm.get("usedCacheNum"))));
                tmk.put("ccExNum", tm.get("ccExNum"));
                tmk.put("nextSortNo", tm.get("nextSortNo"));
                tmk.put("nexName", tm.get("nexName"));
                tmk.put("sortNo", tm.get("sortNo"));
                tmk.put("levleName", tm.get("levleName"));
                tmk.put("percentExNum", tm.get("percentExNum"));
                tmk.put("nickName", tm.get("nickName"));
                tmk.put("extensionCode", tm.get("extensionCode"));
                tmk.put("headpic", tm.get("headpic"));
                tmk.put("isVip", tm.get("isVip"));
                tmk.put("integralNumber", tm.get("integralNumber"));
                tmk.put("vipDate", tm.get("vipDate"));
//				}
                result.put("memberInfo", tmk);
            }
            //获取推广信息接口
            Map<String, Object> getExtensionInfo = new HashMap<String, Object>();
            getExtensionInfo = tExtensionInfoService.getExtensionInfo();
            if (getExtensionInfo != null && !"".equals(getExtensionInfo)) {
                String extensionUrl = getExtensionInfo.get("extensionUrl").toString() + "?code=" + tm.get("extensionCode");


                getExtensionInfo.put("extensionUrl", extensionUrl);
            }
            if (getExtensionInfo.get("extensionContext") != null && !"".equals(getExtensionInfo.get("extensionContext"))) {
                String extensionUrl = getExtensionInfo.get("extensionUrl").toString();
                String extensionContext = String.valueOf(getExtensionInfo.get("extensionContext"));
                String tkl = extensionContext.replaceAll("#\\{code}", tm.get("extensionCode").toString()).replaceAll("#\\{url}", extensionUrl);
                getExtensionInfo.put("extensionContext", tkl);
            }
            result.put("extensionInfo", getExtensionInfo);
        } else {
            return setResponse(modelMap, HttpCode.TIMEOUTTOKEN);
        }
        //火爆交流群
        String qqurlString = "";
        if (CacheUtil.getCache().get("tQ") != null && !"".equals(CacheUtil.getCache().get("tQ"))) {
            qqurlString = (String) CacheUtil.getCache().get("tQ");
        } else {
            List<Map<String, Object>> tQ = new ArrayList<Map<String, Object>>();
            tQ = tQqService.selectQQurl();
            if (tQ != null && !"".equals(tQ) && tQ.size() > 0) {
                Map<String, Object> tf = new HashMap<String, Object>();
                tf = tQ.get(0);
                qqurlString = String.valueOf(tf.get("name"));
            }
            CacheUtil.getCache().set("tQ", qqurlString);
        }


        result.put("qqurl", qqurlString);

        //查询用户中心页面广告
        Map<String, Object> params = new HashMap<>();
        params.put("picType", 10);
        List<Map<String, Object>> bannerList = bannerService.getBannerList(params);

        result.put("bannerList", bannerList);
        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "查询成功");

        return setSuccessResponse(modelMap, result);
    }


    @PostMapping("/saveMemberPic")
    public Object saveMemberPic(ModelMap modelMap, HttpServletRequest request, @RequestParam("picFile") MultipartFile picFile) {

        Integer memberId = getCurrUser(request);
        TMember tm = new TMember();
        tm.setId(memberId);
        tm = tMemberService.selectOne(tm);
        if (tm == null || "".equals(tm)) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "更新失败，接口数据异常");
            return setSuccessResponse(modelMap);
        }
        if (picFile != null && !"".equals(picFile)) {
            //更新头像
            String filePath = UploadUtil.uploadImage(picFile, request);
            if (!"".equals(filePath)) {
                String objectId = "";
                FileModel fm = UploadUtil.remove2DFS("sysUser", objectId, filePath);
                String fileUrl = fm.getReturnPath();
                if (fileUrl == null || "".equals(fileUrl)) {
                    modelMap.put("retCode", "1");
                    modelMap.put("retMsg", "更新成功");
                    return setSuccessResponse(modelMap);
                }
                tm.setHeadpic(fileUrl);
                tMemberService.update(tm);
                modelMap.put("url", PropertiesUtil.getString("remote.file.uri.prefix") + fm.getReturnPath());
            }
            //String url = FileUtil.uploadImgOnly(request,picFile);

        }
        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "更新成功");
        return setSuccessResponse(modelMap);
    }


    @PostMapping("/saveMemberInfo")
    public Object saveMemberInfo(ModelMap modelMap, HttpServletRequest request, @RequestBody Map<String, Object> params) {

        Integer memberId = getCurrUser(request);
        TMember tm = new TMember();
        tm.setId(memberId);
        tm = tMemberService.selectOne(tm);
        if (tm == null || "".equals(tm)) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "更新失败，接口数据异常");
            return setSuccessResponse(modelMap);
        }
        if (params != null && !"".equals(params)) {
            if (params.get("nickName") != null && !"".equals(params.get("nickName"))) {
                String nickName = String.valueOf(params.get("nickName"));
                if (WordFilter.isContains(nickName)) {
                    modelMap.put("retCode", "-1");
                    modelMap.put("retMsg", "昵称存在敏感词汇，请修改");
                    return setSuccessResponse(modelMap);
                }
                tm.setNickName(String.valueOf(params.get("nickName")));
            }
            if (params.get("sex") != null && !"".equals(params.get("nickName"))) {
                tm.setSex(Integer.valueOf(params.get("sex").toString()));
            }
            tMemberService.update(tm);
        }
        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "更新成功");
        return setSuccessResponse(modelMap);
    }


    @PostMapping("/selectMemberInfo")
    public Object selectMemberInfo(ModelMap modelMap, HttpServletRequest request) {
        Integer memberId = getCurrUser(request);
        TMember tm = new TMember();
        tm.setId(memberId);
        tm = tMemberService.selectOne(tm);
        if (tm == null || "".equals(tm)) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "更新失败，接口数据异常");
            return setSuccessResponse(modelMap);
        }
        String pic = tm.getHeadpic();
        tm.setHeadpic(PropertiesUtil.getString("remote.file.uri.prefix") + pic);
        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "更新成功");
        return setSuccessResponse(modelMap, tm);
    }

    /**
     * 获取推广验证码
     *
     * @param modelMap
     * @param request
     * @return
     */
    @PostMapping("/getExtensionCode")
    public Object getExtensionCode(ModelMap modelMap, HttpServletRequest request, @RequestBody Map<String, Object> params) {
        if (params == null || "".equals(params)) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "获取推广码，接口数据异常");
            return setSuccessResponse(modelMap);
        }
        if (params.get("type") == null || "".equals(params.get("type"))) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "获取推广码，接口数据异常");
            return setSuccessResponse(modelMap);
        }

//		List<Map<String,Object>> versionList = new ArrayList<Map<String,Object>>();
//		versionList = tVersionService.getNewVersion(params);
//		if(versionList != null && !"".equals(versionList)&&versionList.size()>0)
//		{
//			Map<String,Object>  versionMap= new HashMap<String,Object>();
//			versionMap = versionList.get(0);
//			modelMap.put("extensionUrl",versionMap.get("versionUrl"));
//		}
        Integer memeberId = getCurrUser(request);
        TMember tm = new TMember();
        tm.setId(memeberId);
        tm = tMemberService.selectOne(tm);
        if (tm == null || "".equals(tm)) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "获取推广码，接口数据异常");
            return setSuccessResponse(modelMap);
        }
        modelMap.put("extensionCode", tm.getExtensionCode());
        Map<String, Object> getExtensionInfo = new HashMap<String, Object>();
        getExtensionInfo = tExtensionInfoService.getExtensionInfo();
        if (getExtensionInfo != null && !"".equals(getExtensionInfo)) {
            String extensionUrl = getExtensionInfo.get("extensionUrl").toString() + "?channelCode=" + tm.getExtensionCode();

            //获取短域名需要的参数
            ShortRealmNameSetting shortRealmNameSetting = shortRealmNameSettingService.findOne();

            String shortRealmNameUrl = ShortRealmNameSettingUtils.getShortRealmNameSetting(extensionUrl, shortRealmNameSetting);

            if (!StringUtils.isEmpty(shortRealmNameUrl)) {
                extensionUrl = shortRealmNameUrl;
            }

            modelMap.put("extensionUrl", extensionUrl);
            String extensionContext = String.valueOf(getExtensionInfo.get("extensionContext"));
            String tfh = extensionContext.replaceAll("#\\{channelCode}", tm.getExtensionCode()).replaceAll("#\\{url}", extensionUrl);
            modelMap.put("extensionContext", tfh);
        }
        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "获取推广码成功");
        return setSuccessResponse(modelMap);
    }


    /**
     * 获取缓存次数
     *
     * @param modelMap
     * @param request
     * @param params
     * @return
     */
    @PostMapping("/getCacheNum")
    public Object getCacheNum(ModelMap modelMap, HttpServletRequest request) {
        Integer memberId = getCurrUser(request);
        //用户登录
        TMember mb = new TMember();
        mb.setId(memberId);
        mb = tMemberService.selectOne(mb);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String dateString = format.format(new Date());
//   	 if(CacheUtil.getCache().get("USER_TYPE_Cache_"+memberId+"_"+dateString+"_video_"+params.get("videoId"))== null ||
//				"".equals(CacheUtil.getCache().get("USER_TYPE_Cache_"+memberId+"_"+dateString+"_video_"+params.get("videoId")))	)
//			{
        TLevel level = new TLevel();
        level.setId(mb.getLevelId());
        level = tLevelService.selectOne(level);
        int cacheNum = 0;
        if (level != null && !"".equals(level)) {
            cacheNum = level.getCacheNum();
        }
        if (mb.getIsVip() == 1) {
            Date now = new Date();
            if (now.before(mb.getVipDate())) {
                int usedCache = mb.getUsedCacheNum();
                TVip tvip = new TVip();
                tvip.setId(mb.getVipId());
                tvip = tVipService.selectOne(tvip);
                int cache = tvip.getCacheNum();
                if (cache != 999999999) {
                    if (cache - usedCache <= 0) {
                        modelMap.put("retCode", "-2");
                        modelMap.put("retMsg", "次数已用完，请充值");
                        return setSuccessResponse(modelMap);
                    }
                }
            } else {
                cacheNum += mb.getCacheNum();
                int usedCacheNum = mb.getUsedCacheNum();
                if ((cacheNum - usedCacheNum) <= 0) {
                    modelMap.put("retCode", "-2");
                    modelMap.put("retMsg", "次数已用完，请充值");
                    return setSuccessResponse(modelMap);
                }
            }
        } else {
            cacheNum += mb.getCacheNum();
            int usedCacheNum = mb.getUsedCacheNum();
            if ((cacheNum - usedCacheNum) <= 0) {
                modelMap.put("retCode", "-2");
                modelMap.put("retMsg", "次数已用完，请充值");
                return setSuccessResponse(modelMap);
            }
        }
//		}		
        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "获取缓存次数成功");
        return setSuccessResponse(modelMap);
    }


    @PostMapping("/usedViewOrCacheNum")
    public Object getCacheNum(ModelMap modelMap, HttpServletRequest request, @RequestBody Map<String, Object> params) {

        Integer memberId = getCurrUser(request);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String dateString = format.format(new Date());
        if (params == null || "".equals(params)) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "参数异常");
            return setSuccessResponse(modelMap);
        }

        if (params.get("type") == null || "".equals(params.get("type"))
                || params.get("videoId") == null || "".equals(params.get("videoId"))) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "参数异常");
            return setSuccessResponse(modelMap);
        }
        if ("1".equals(params.get("type").toString())) {
            //更新观看次数
            TMember tm = new TMember();
            tm.setId(memberId);
            tm = tMemberService.selectOne(tm);
            TLevel level = new TLevel();
            level.setId(tm.getLevelId());
            level = tLevelService.selectOne(level);
            int view = 0;
       	    /*if(level != null && !"".equals(level))
       	    {
       	    	view = level.getViewNum();
       	    }*/
            if (tm != null && !"".equals(tm)) {
                //当观看过，则不进行扣减
                if (CacheUtil.getCache().get("USER_TYPE_" + memberId + "_" + dateString + "_video_" + params.get("videoId")) == null ||
                        "".equals(CacheUtil.getCache().get("USER_TYPE_" + memberId + "_" + dateString + "_video_" + params.get("videoId")))) {
                    //VIP情况下
                    if (tm.getIsVip() == 1) {
                        Date now = new Date();
                        if (now.after(tm.getVipDate())) {
                            view += (tm.getViewNum() + tm.getTmpViewNum());
                            int usedView = tm.getUsedViewNum();
                            if (view > usedView) {
                                usedView++;
                                tm.setUsedViewNum(usedView);
                                tm = tMemberService.update(tm);
                                CacheUtil.getCache().set("USER_TYPE_" + memberId + "_" + dateString + "_video_" + params.get("videoId"), "1");
                            } else {
                                modelMap.put("retCode", "-2");
                                modelMap.put("retMsg", "次数已用完，请充值");
                                return setSuccessResponse(modelMap);
                            }
                        }
                    } else {
                        view += (tm.getViewNum() + tm.getTmpViewNum());
                        int usedView = tm.getUsedViewNum();
                        if (view > usedView) {
                            usedView++;
                            tm.setUsedViewNum(usedView);
                            tm = tMemberService.update(tm);
                            CacheUtil.getCache().set("USER_TYPE_" + memberId + "_" + dateString + "_video_" + params.get("videoId"), "1");
                        } else {
                            modelMap.put("retCode", "-2");
                            modelMap.put("retMsg", "次数已用完，请充值");
                            return setSuccessResponse(modelMap);
                        }
                    }
                }
            }
        } else if ("2".equals(params.get("type").toString())) {
            //用户登录
            TMember mb = new TMember();
            mb.setId(memberId);
            mb = tMemberService.selectOne(mb);
//			if(CacheUtil.getCache().get("USER_TYPE_Cache_"+memberId+"_"+dateString+"_video_"+params.get("videoId"))== null ||
//					"".equals(CacheUtil.getCache().get("USER_TYPE_Cache_"+memberId+"_"+dateString+"_video_"+params.get("videoId")))	)
//			{
            TLevel level = new TLevel();
            level.setId(mb.getLevelId());
            level = tLevelService.selectOne(level);
            int cacheNum = 0;
            if (level != null && !"".equals(level)) {
                cacheNum = level.getCacheNum();
            }
            int usedCacheNum = mb.getUsedCacheNum();
            if (mb.getIsVip() == 1) {
                Date now = new Date();
                if (now.before(mb.getVipDate())) {
                    int userCacheNum = mb.getUsedCacheNum();
                    TVip tvip = new TVip();
                    tvip.setId(mb.getVipId());
                    tvip = tVipService.selectOne(tvip);
                    int cache = tvip.getCacheNum();
                    if (cache != 999999999) {
                        if (cache - userCacheNum > 0) {
                            usedCacheNum++;
                            mb.setUsedCacheNum(usedCacheNum);
                            tMemberService.update(mb);
                            //CacheUtil.getCache().set("USER_TYPE_Cache_"+memberId+"_"+dateString+"_video_"+params.get("videoId"),"1");
                        } else {
                            modelMap.put("retCode", "-2");
                            modelMap.put("retMsg", "次数已用完，请充值");
                            return setSuccessResponse(modelMap);
                        }
                    }
                } else {
                    cacheNum += mb.getCacheNum();
                    if ((cacheNum - usedCacheNum) <= 0) {
                        modelMap.put("retCode", "-2");
                        modelMap.put("retMsg", "次数已用完，请充值");
                        return setSuccessResponse(modelMap);
                    } else {
                        usedCacheNum++;
                        mb.setUsedCacheNum(usedCacheNum);
                        tMemberService.update(mb);
                        //CacheUtil.getCache().set("USER_TYPE_Cache_"+memberId+"_"+dateString+"_video_"+params.get("videoId"),"1");
                    }
                }
            } else {
                cacheNum += mb.getCacheNum();
                if ((cacheNum - usedCacheNum) > 0) {
                    usedCacheNum++;
                    mb.setUsedCacheNum(usedCacheNum);
                    tMemberService.update(mb);
                    //CacheUtil.getCache().set("USER_TYPE_Cache_"+memberId+"_"+dateString+"_video_"+params.get("videoId"),"1");
                } else {
                    modelMap.put("retCode", "-2");
                    modelMap.put("retMsg", "次数已用完，请充值");
                    return setSuccessResponse(modelMap);
                }
            }
        } else {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "参数异常");
            return setSuccessResponse(modelMap);
        }
        //}
        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "扣减成功");
        return setSuccessResponse(modelMap);
    }


    /**
     * 获取观影次数
     *
     * @return
     */
    @PostMapping("/valiateViewNum")
    public Object valiateViewNum(ModelMap modelMap, HttpServletRequest request, @RequestBody Map<String, Object> params) {
		/*Integer memberId = getCurrUser(request);
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String  dateString = format.format(new Date());
		if(params == null || "".equals(params))
		{
			modelMap.put("retCode", "-1");
		    modelMap.put("retMsg", "参数异常");
			return setSuccessResponse(modelMap);
		}
		
		if(params.get("type") == null ||"".equals(params.get("type"))
				||params.get("videoId") == null||"".equals(params.get("videoId")))
		{
			modelMap.put("retCode", "-1");
		    modelMap.put("retMsg", "参数异常");
			return setSuccessResponse(modelMap);
		}
		//观影次数
		if("1".equals(params.get("type").toString()))
		{
			//更新观看次数
			TMember tm = new TMember();
			tm.setId(memberId);
			tm = tMemberService.selectOne(tm);
			TLevel level = new TLevel();
       	    level.setId(tm.getLevelId());
       	    level=tLevelService.selectOne(level);
       	    int view=0;
       	    if(level != null && !"".equals(level))
       	    {
       	    	view = level.getViewNum();
       	    }
			if(tm != null && !"".equals(tm))
			{
				//当观看过，则不进行扣减
				if(CacheUtil.getCache().get("USER_TYPE_"+memberId+"_"+dateString+"_video_"+params.get("videoId"))== null ||
					"".equals(CacheUtil.getCache().get("USER_TYPE_"+memberId+"_"+dateString+"_video_"+params.get("videoId")))	)
				{
					//VIP情况下
					if(tm.getIsVip() ==1)
					{
						Date now = new Date();
						if(now.after(tm.getVipDate()))
						{
							view +=tm.getViewNum()+tm.getTmpViewNum();
							int usedView = tm.getUsedViewNum();
							if(view-usedView<=0)
							{
								modelMap.put("retCode", "-2");
							    modelMap.put("retMsg", "次数已用完，请充值");	    
								return setSuccessResponse(modelMap);
							}
						}
					}
					else
					{
						view +=tm.getViewNum()+tm.getTmpViewNum();
						int usedView = tm.getUsedViewNum();
						if(view-usedView<=0)
						{
							modelMap.put("retCode", "-2");
						    modelMap.put("retMsg", "次数已用完，请充值");	    
							return setSuccessResponse(modelMap);
						}
					}
				}
			}
			else
			{
				return  setResponse(modelMap,HttpCode.TIMEOUTTOKEN);
			}
		}
		//缓存次数
		else if("2".equals(params.get("type").toString()))
		{
			//用户登录
			TMember mb = new TMember();
			mb.setId(memberId);
			mb = tMemberService.selectOne(mb);
//			if(CacheUtil.getCache().get("USER_TYPE_Cache_"+memberId+"_"+dateString+"_video_"+params.get("videoId"))== null ||
//					"".equals(CacheUtil.getCache().get("USER_TYPE_Cache_"+memberId+"_"+dateString+"_video_"+params.get("videoId")))	)
//				{
				TLevel level = new TLevel();
	       	    level.setId(mb.getLevelId());
	       	    level=tLevelService.selectOne(level);
	       	    int cacheNum=0;
	       	    if(level != null && !"".equals(level))
	       	    {
	       	    	cacheNum = level.getCacheNum();
	       	    }
	       	    if(mb.getIsVip() ==1)
				{
	       	    	Date now = new Date();
					if(now.before(mb.getVipDate()))
					{
						int usedCache = mb.getUsedCacheNum();
						TVip tvip = new TVip();
						tvip.setId(mb.getVipId());
						tvip = tVipService.selectOne(tvip);
						int cache = tvip.getCacheNum();
						if(cache !=999999999)
						{
							if(cache-usedCache<=0)
							{
								modelMap.put("retCode", "-2");
							    modelMap.put("retMsg", "次数已用完，请充值");	    
								return setSuccessResponse(modelMap);
							}
						}
					}
					else 
					{
						cacheNum +=mb.getCacheNum(); 
						int usedCacheNum = mb.getUsedCacheNum();
						if((cacheNum-usedCacheNum)<=0)
						{
							modelMap.put("retCode", "-2");
							modelMap.put("retMsg", "次数已用完，请充值");	   
						    return setSuccessResponse(modelMap);
						}	
					}
				}
	       	    else
	       	    {
	       	    	cacheNum +=mb.getCacheNum(); 
	    			int usedCacheNum = mb.getUsedCacheNum();
	    			if((cacheNum-usedCacheNum)<=0)
	    			{
	    				modelMap.put("retCode", "-2");
	    				modelMap.put("retMsg", "次数已用完，请充值");	   
	    			    return setSuccessResponse(modelMap);
	    			}		
				}
//			}		
		}
		else
		{
			modelMap.put("retCode", "-1");
		    modelMap.put("retMsg", "参数异常");
			return setSuccessResponse(modelMap);
		}*/
        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "存在可以次数");
        return setSuccessResponse(modelMap);
    }
}
