
package com.videoweb.ying.controller;

import com.alibaba.fastjson.JSON;
import com.videoweb.base.BaseController;
import com.videoweb.utils.*;
import com.videoweb.utils.redis.CacheUtil;
import com.videoweb.ying.po.*;
import com.videoweb.ying.service.*;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping("/openapi")
public class ApiLoginController extends BaseController {

    @Autowired
    private TMemberLoginService tMemberLoginService;

    @Autowired
    private FreeViewNumberSettingService freeViewNumberSettingService;

    @Autowired
    private TMemberService tMemberService;

    @Autowired
    private TLevelService tLevelService;

    @Autowired
    private IntegralClassifyService integralClassifyService;

    @Autowired
    private IntegralHistoryService integralHistoryService;

    @Autowired
    private TTouristService tTouristService;

    @Autowired
    private TExtensionHistoryService tExtensionHistoryService;

    @Autowired
    private TBannerService tBannerService;


    @Autowired
    private TVersionService tVersionService;

    @Autowired
    private TNoticeService tNoticeService;

    @Autowired
    private TVipService tVipService;

    @Autowired
    private SysSmsService sysSmsService;

    //设备注册接口
    @PostMapping("/deviceInfo")
    public Object deviceInfo(ModelMap modelMap, HttpServletRequest request, @RequestBody Map<String, Object> params) {
        SimpleDateFormat fformat = new SimpleDateFormat("yyyy-MM-dd");
        String dd = fformat.format(new Date());
        logger.info("设备注册，入参:" + params);
        if (params == null || "".equals(params)) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "设备绑定失败,入参不能为空!");
            return setSuccessResponse(modelMap);
        }
        //1=IOS 2=android
        if (params.get("versionType") == null || "".equals(params.get("versionType"))) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "软件版本类型缺失");
            return setSuccessResponse(modelMap);
        }
        Integer versionType = Integer.valueOf(params.get("versionType").toString());
        //获取最新的版本号
        Map<String, Object> versionMap = new HashMap<String, Object>();
        versionMap.put("type", versionType);
        List<Map<String, Object>> versionList = new ArrayList<Map<String, Object>>();
        versionList = tVersionService.getNewVersion(versionMap);
        Map<String, Object> versionMap1 = new HashMap<String, Object>();
        if (versionList != null && !"".equals(versionList) && versionList.size() > 0) {
            versionMap1 = versionList.get(0);
        }

        if (versionMap1 == null || "".equals(versionMap1) || versionMap1.isEmpty()) {
            versionMap1.put("isNew", "1"); //0---当前版本是当前最新
        } else {
            versionMap1.put("isNew", "0"); //0---当前版本不是当前最新
            if (params.get("version") != null && !"".equals(params.get("version"))) {
                //获取当前版本号，验证当前版本是否强制更新
                if (params.get("version").equals(versionMap1.get("versionCode"))) {
                    versionMap1.put("isNew", "1"); //1----当前版本是当前最新
                }
            }
        }
        modelMap.put("versionInfo", versionMap1);

        //获取当前的提醒公告
        List<Map<String, Object>> noticeList = new ArrayList<Map<String, Object>>();
        noticeList = tNoticeService.getNoticeSub();
        modelMap.put("noticeList", noticeList);
        if (params.get("token") != null && !"".equals(params.get("token"))) {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap = Jwt.validToken(params.get("token").toString());
            boolean flag = (boolean) resultMap.get("isSuccess");
            if (flag) {
                Integer memberId = null;
                if (resultMap.get("data") != null && !"".equals(resultMap.get("data"))) {
                    JSONObject obj = (JSONObject) resultMap.get("data");
                    if (obj.get("uid") != null && !"".equals(obj.get("uid"))) {
                        memberId = Integer.valueOf(obj.get("uid").toString());
                    }
                }
                //根据用户信息获取当前用户的观影次数
                TMemberLogin tLogin = new TMemberLogin();
                tLogin.setMemberId(memberId);
                tLogin = tMemberLoginService.selectOne(tLogin);
                if (tLogin != null && !"".equals(tLogin)) {
                    if (tLogin.getIsEnable().intValue() == 0) {
                        modelMap.put("retCode", "-1");
                        modelMap.put("retMsg", "账号已冻结，请联系客服!");
                        return setSuccessResponse(modelMap);
                    }
                    //根据登录日期，更新当前用户的观影次数
                    SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
                    String lastDate = format.format(tLogin.getUpdateTime());
                    String nowDate = format.format(new Date());
                    if (!lastDate.equals(nowDate)) {
                        TMember tMember = new TMember();
                        tMember.setId(tLogin.getMemberId());
                        tMember = tMemberService.selectOne(tMember);
                        tMember.setUsedViewNum(0);
                        tMember.setUsedCacheNum(0);
                        tMember.setTmpViewNum(0);
                        tMemberService.update(tMember);
                    }
                    tLogin.setUpdateTime(new Date());
                    tLogin.setLoginTime(new Date());
                    tMemberLoginService.update(tLogin);
                    TMember tMember2 = new TMember();
                    tMember2.setId(tLogin.getMemberId());
                    tMember2 = tMemberService.selectOne(tMember2);
                    int viewNum = tMember2.getViewNum() - tMember2.getUsedViewNum() + tMember2.getTmpViewNum();
                    int cacheNum = tMember2.getCacheNum() - tMember2.getUsedCacheNum();

                    if (tMember2.getIsVip() == 1) {
                        Date now = new Date();
                        if (now.after(tMember2.getVipDate())) {
                            //更改VIP状态
                            tMember2.setIsVip(0);
                            tMemberService.update(tMember2);
                        }
                    } else {
                        modelMap.put("viewNum", viewNum);
                        modelMap.put("cacheNum", cacheNum);
                    }
                    modelMap.put("retCode", "1");
                    modelMap.put("retMsg", "登录成功");
                    modelMap.put("token", params.get("token"));
                    modelMap.put("loginType", "1");
                    CacheUtil.getCache().set("USER_TYPE_" + tLogin.getMemberId(), "1");
                    //CacheUtils.getInstance().put("USER_TYPE_"+tLogin.getMemberId(), "1");
                    //创建登录
                    //报表缓存到redis中
                    this.getReportCache(2, tLogin.getFromCode(), "");
                    //获取启动页广告
                    Map<String, Object> ppf = new HashMap<String, Object>();
                    ppf.put("picType", "4");
                    List<Map<String, Object>> bannerList = new ArrayList<Map<String, Object>>();
                    bannerList = tBannerService.getBannerList(ppf);
                    if (bannerList != null && bannerList.size() > 0) {
                        for (Map<String, Object> mm : bannerList) {
                            if ("1".equals(mm.get("picFrom").toString())) {
                                mm.put("picUrl", PropertiesUtil.getString("remote.file.uri.prefix") + mm.get("picUrl"));
                            }
                        }
                    }
                    modelMap.put("bannerList", bannerList);
                    return setSuccessResponse(modelMap);
                }
            }
            //token超时，自动切换到设备登录
        }
        //获取用户登录类型
        if (params.get("deviceCode") == null || "".equals(params.get("deviceCode"))) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "设备绑定失败,参数异常!");
            return setSuccessResponse(modelMap);
        }

        //验证当前设备是否已存在
        TMemberLogin tl = new TMemberLogin();
        tl.setDeviceCode(String.valueOf(params.get("deviceCode")));
        tl = tMemberLoginService.selectOne(tl);


        //创建账户
        if (tl == null || "".equals(tl)) {
            TMember tm = new TMember();
            tm.setUpdateTime(new Date());
            //获取最低等级
            List<Map<String, Object>> levelList = new ArrayList<Map<String, Object>>();
            levelList = tLevelService.getLevelList();
            if (levelList != null && levelList.size() > 0) {
                Map<String, Object> pp = new HashMap<String, Object>();
                pp = levelList.get(0);
                if (pp != null && !"".equals(pp) && pp.get("id") != null && !"".equals(pp.get("id"))) {
                    tm.setLevelId(Integer.valueOf(pp.get("id").toString()));
                }
            }
            //创建推广码
            String extensionCode = getExtensionCode();

            //给用户添加免费次数
            FreeViewNumberSetting fvs = freeViewNumberSettingService.findOne(1);
            if (null != fvs) {
                //免费观影次数
                Integer freeNumber = fvs.getFreeNumber();
                logger.info("当前系统免费观影次数为:{}",freeNumber);
                tm.setViewNum(freeNumber);
            }

            //添加下载app奖励积分
            IntegralClassify integralClassify = integralClassifyService.findOne(4);

            //添加积分
            if(null != integralClassify){
                tm.setIntegralNumber(integralClassify.getNumber());
            }


            tm.setExtensionCode(extensionCode);
            tm = tMemberService.update(tm);
            if (tm == null || "".equals(tm)) {
                modelMap.put("retCode", "-1");
                modelMap.put("retMsg", "设备绑定失败!");
                return setSuccessResponse(modelMap);
            }

            if(tm.getIntegralNumber() > 0){
                //添加积分获取记录记录
                addIntegralHistory(tm.getId(),integralClassify.getId());
            }



            tl = new TMemberLogin();
            tl.setDeviceCode(String.valueOf(params.get("deviceCode")));
            tl.setMemberId(tm.getId());
            tl.setUpdateTime(new Date());
            tl.setIsDeviceEnable(1);
            tl.setIsEnable(1);



            //创建用户注册记录
            this.getReportCache(1, params.get("fromCode").toString(), "");
        } else {
            if (tl.getIsDeviceEnable().intValue() == 0) {
                modelMap.put("retCode", "-1");
                modelMap.put("retMsg", "设备已被禁用!");
                return setSuccessResponse(modelMap);
            }
        }


        if (params.get("fromCode") != null && !"".equals(params.get("fromCode"))) {
            tl.setFromCode(params.get("fromCode").toString());
        }
        tl.setLoginTime(new Date());
        tl = tMemberLoginService.update(tl);


        //查询当前用户是否已有被推广记录，防止重复推广
        TExtensionHistory t = new TExtensionHistory();
        t.setExtendId(tl.getMemberId());
        TExtensionHistory fromT = tExtensionHistoryService.selectOne(t);

        if(fromT == null){
            //建立推广记录
            TExtensionHistory tex = new TExtensionHistory();

            //获取推广码
            String memberExtensionCode = null;

            if (params.get("extensionCode") != null && !"".equals(params.get("extensionCode"))) {

                String extensionCodeJson = params.get("extensionCode").toString();
                logger.info("extensionCodeJson:" + extensionCodeJson);
                //转换为Map
                Map extensionCodeMap = JSON.parseObject(extensionCodeJson, Map.class);

                if (extensionCodeMap.containsKey("channelCode")) {
                    memberExtensionCode = (String) extensionCodeMap.get("channelCode");
                    logger.info("channelCode:" + memberExtensionCode);
                }else if(extensionCodeMap.containsKey("channelcode")){
                    memberExtensionCode = (String) extensionCodeMap.get("channelcode");
                    logger.info("channelcode:" + memberExtensionCode);
                }

            }

            //查询当前推广码是否存在推广记录
            if(memberExtensionCode != null && !"".equals(memberExtensionCode)){

                TMember tMember3 = new TMember();
                tMember3.setExtensionCode(memberExtensionCode);
                tMember3 = tMemberService.selectOne(tMember3);
                if (tMember3 == null || "".equals(tMember3)) {
                    modelMap.put("retCode", "-1");
                    modelMap.put("retMsg", "推广码无效");
                    return setSuccessResponse(modelMap);
                }

                TExtensionHistory tExtensionHistory = new TExtensionHistory();
                logger.info("ExtendId-----------------------------" + tl.getMemberId());
                tExtensionHistory.setExtendId(tl.getMemberId());
                tExtensionHistory.setMemberId(tMember3.getId());
                TExtensionHistory fromExtensionHistory = tExtensionHistoryService.selectOne(tExtensionHistory);


                //推广新用户增加对应的积分
                IntegralClassify integralClassify = integralClassifyService.findOne(5);

                //增加积分
               if(null != integralClassify){

                   Integer integralNumber = tMember3.getIntegralNumber() +  integralClassify.getNumber();
                   tMember3.setIntegralNumber(integralNumber);

                   tMemberService.updateById(tMember3);

                   addIntegralHistory(tMember3.getId(),integralClassify.getId());
               }


                if (fromExtensionHistory == null) {

//            num += 2;
                    tex.setMemberId(tMember3.getId());
                    String deviceCode = (String) params.get("deviceCode");

                    tex.setNickName("用户" + deviceCode.substring(deviceCode.length() - 4, deviceCode.length()));

//            tex.setTel(telString);
                    tex.setRegeditTime(new Date());
                }

                if (fromExtensionHistory == null) {
                    if (tex != null && !"".equals(tex)) {
                        logger.info("ExtendId-----------------------------" + tl.getMemberId());
                        tex.setExtendId(tl.getMemberId());
                        tExtensionHistoryService.update(tex);
                        //升级被推广用户等级
                        //获取推广人的等级，并验证是否符合下一级等级的需求
                        //获取推广人人当前等级
//                String extensionCode1 = params.get("extensionCode").toString();

                        Integer currentLevelId = tMember3.getLevelId();
                        TLevel levelInfo = new TLevel();
                        levelInfo.setId(currentLevelId);
                        levelInfo = tLevelService.selectOne(levelInfo);
                        if (levelInfo != null && !"".equals(levelInfo)) {
                            //获取排序
                            int sorNo = levelInfo.getSortNo();
                            if (sorNo < 5) {
                                sorNo++;
                                TLevel levelInfo2 = new TLevel();
                                levelInfo2.setSortNo(sorNo);
                                levelInfo2 = tLevelService.selectOne(levelInfo2);
                                Integer exNum = levelInfo2.getExNum();
                                List<TExtensionHistory> textendList = new ArrayList<TExtensionHistory>();
                        /*Map<String, Object> paramsMap = new HashMap<String, Object>();
                        paramsMap.put("memberId", tMember.getId());*/
                                textendList = tExtensionHistoryService.queryList(params);
                                if (textendList != null && textendList.size() > 0) {
                                    logger.info("当前邀请人用户等级" + levelInfo2.getSortNo());
                                    logger.info("当前邀请人用户已邀请人数:" + textendList.size() + ",当前等级需要邀请人数:" + exNum);
                                    if (textendList.size() >= exNum) {
                                        tMember3.setIsRemind(1);
                                        tMember3.setLevelId(levelInfo2.getId());
                                        //todo 增加会员天数
                                        /*Calendar ca = Calendar.getInstance();
                                        //防止空指针
                                        if (null == tMember3.getVipDate()) {
                                            tMember3.setVipDate(new Date());
                                        }

                                        ca.setTime(tMember3.getVipDate());
                                        //根据购买类型id增加天数
                                        ca.add(Calendar.DATE, levelInfo2.getDayNum());
                                        Date cc = ca.getTime();
                                        tMember3.setVipDate(cc);

                                        tMember3.setIsVip(1);*/
//            						   tMember3.setViewNum(levelInfo2.getViewNum());
//            						   tMember3.setCacheNum(levelInfo2.getCacheNum());
                                        tMemberService.updateById(tMember3);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }else{
            logger.info("用户id:{},已有推广记录",tl.getMemberId());
        }

        //建立推广记录
        logger.info("获取用户信息:" + Bean2StringUtil.fieldToString(tl));
        //tl = tMemberLoginService.selectOne(tl);
        //logger.info("获取用户信息2:"+Bean2StringUtil.fieldToString(tl));
        if (tl != null && !"".equals(tl)) {
            if (tl.getIsDeviceEnable().intValue() != 1) {
                modelMap.put("retCode", "-1");
                modelMap.put("retMsg", "该设备已被禁用!");
                return setSuccessResponse(modelMap);
            }
        }
        //创建设备登录信息
        String token = null;
        Map<String, Object> payload = new HashMap<String, Object>();
        Date date = new Date();
        payload.put("uid", tl.getMemberId());// 用户id
        payload.put("iat", date.getTime());// 生成时间
        payload.put("ext", date.getTime() + 1000 * 60 * 60 * 5);// 过期时间5小时
        try {
            token = Jwt.createToken(payload);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (tl != null && !"".equals(tl)) {
            //根据登录日期，更新当前用户的观影次数
            SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
            String lastDate = format.format(tl.getUpdateTime());
            String nowDate = format.format(new Date());
            if (!lastDate.equals(nowDate)) {
                TMember tMember = new TMember();
                tMember.setId(tl.getMemberId());
                tMember = tMemberService.selectOne(tMember);
                tMember.setUsedViewNum(0);
                tMember.setUsedCacheNum(0);
                tMemberService.updateById(tMember);
            }
            tl.setUpdateTime(new Date());
            tl.setLoginTime(new Date());
            tMemberLoginService.update(tl);
            TMember tMember2 = new TMember();
            tMember2.setId(tl.getMemberId());
            tMember2 = tMemberService.selectOne(tMember2);
            int viewNum = tMember2.getViewNum() - tMember2.getUsedViewNum() + tMember2.getTmpViewNum();
            int cacheNum = tMember2.getCacheNum() - tMember2.getUsedCacheNum();
            if (tMember2.getIsVip() == 1) {
                Date now = new Date();
                if (now.after(tMember2.getVipDate())) {
                    modelMap.put("viewNum", viewNum);
                    modelMap.put("cacheNum", cacheNum);
                    //更改VIP状态
                    tMember2.setIsVip(0);
                    tMemberService.update(tMember2);
                } /*else {
                    modelMap.put("viewNum", 999999999);
                    TVip tvip = new TVip();
                    tvip.setId(tMember2.getVipId());
                    tvip = tVipService.selectOne(tvip);
                    modelMap.put("cacheNum", tvip.getCacheNum());
                }*/
            } else {
                modelMap.put("viewNum", viewNum);
                modelMap.put("cacheNum", cacheNum);
            }
            CacheUtil.getCache().set("USER_TYPE_" + tl.getMemberId(), "2");
        }
        //获取启动页广告
        Map<String, Object> ppf = new HashMap<String, Object>();
        ppf.put("picType", "4");
        List<Map<String, Object>> bannerList = new ArrayList<Map<String, Object>>();
        bannerList = tBannerService.getBannerList(ppf);
        if (bannerList != null && bannerList.size() > 0) {
            for (Map<String, Object> mm : bannerList) {
                if ("1".equals(mm.get("picFrom").toString())) {
                    mm.put("picUrl", PropertiesUtil.getString("remote.file.uri.prefix") + mm.get("picUrl"));
                }

            }
        }
        modelMap.put("bannerList", bannerList);
        modelMap.put("token", token);
        modelMap.put("loginType", "2");
        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "设备注册成功!");

        //创建登录
        this.getReportCache(2, tl.getFromCode(), "");
        return setSuccessResponse(modelMap);
    }

    /**
     * 添加积分获取历史记录
     * @param id
     * @param id1
     */
    private void addIntegralHistory(Integer memberId, Integer integralId) {

        IntegralHistory integralHistory = new IntegralHistory();
        integralHistory.setMemberId(memberId);
        integralHistory.setIntegralClassifyId(integralId);
        integralHistory.setCreateTime(new Date());
        integralHistory.setUpdateTime(new Date());

        integralHistoryService.add(integralHistory);
    }


    /**
     * 用户登录
     *
     * @param modelMap
     * @param request
     * @param params
     * @return
     */
    @PostMapping("/login")
    public Object login(ModelMap modelMap, HttpServletRequest request, @RequestBody Map<String, Object> params) {

        SimpleDateFormat fformat = new SimpleDateFormat("yyyy-MM-dd");
        String dd = fformat.format(new Date());
        //分为三种登录方式
        if (params == null || "".equals(params)) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "登录失败,入参不能为空!");
            return setSuccessResponse(modelMap);
        }
        //获取用户登录类型
        if (params.get("tel") == null || "".equals(params.get("tel"))
                || params.get("pwd") == null || "".equals(params.get("pwd"))) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "登录失败,参数异常!");
            return setSuccessResponse(modelMap);
        }
        //登录名
        String tel = String.valueOf(params.get("tel"));
        //密码
        String pwd = String.valueOf(params.get("pwd"));
        String pwdNew = MD5Util.md5(pwd, SysConstants.MD5_SALT);


        //验证账户是否正确
        TMemberLogin tLogin = new TMemberLogin();
        tLogin.setTel(tel);
        tLogin.setPwd(pwdNew);
        tLogin = tMemberLoginService.selectOne(tLogin);

        if (tLogin == null || "".equals(tLogin)) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "登录失败,用户名或密码错误!");
            return setSuccessResponse(modelMap);
        }
        if (tLogin.getIsEnable().intValue() == 0) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "账号已冻结，请联系客服!");
            return setSuccessResponse(modelMap);
        }

        //查看当前用户是否在线
        Long ttl = CacheUtil.getCache().ttl("onLine:" + tel) / 1000;
        logger.info("当前用户{} 登录标识过期时间为{}",tel,ttl);
        if(ttl > 0){
            if(ttl > 60){
                ttl = 60l;
                // 设置过期时间为60
                CacheUtil.getCache().set("onLine:" + tel,"1",60);

                // 清理用户登
                CacheUtil.getCache().del("USER_TYPE_" + tLogin.getMemberId());
            }

            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "登录失败,当前用户已在别地登陆,"+ttl+"秒后重试");
            return setSuccessResponse(modelMap);
        }


        String token = null;
        Map<String, Object> payload = new HashMap<String, Object>();
        Date date = new Date();
        payload.put("uid", tLogin.getMemberId());// 用户id
        payload.put("iat", date.getTime());// 生成时间
        payload.put("ext", date.getTime() + 1000 * 60 * 60 * 5);// 过期时间5小时
        try {
            token = Jwt.createToken(payload);
        } catch (Exception e) {
            e.printStackTrace();
        }

        CacheUtil.getCache().set("onLine:" + tel,tel, 60 * 60 * 5); //5小时过期

        //根据登录日期，更新当前用户的观影次数
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
        String lastDate = format.format(tLogin.getUpdateTime());
        String nowDate = format.format(date);
        if (!lastDate.equals(nowDate)) {
            TMember tMember = new TMember();
            tMember.setId(tLogin.getMemberId());
            tMember = tMemberService.selectOne(tMember);
            tMember.setUsedViewNum(0);
            tMember.setUsedCacheNum(0);
            tMember.setTmpViewNum(0);
            tMemberService.update(tMember);
        }
        tLogin.setUpdateTime(date);
        tLogin.setLoginTime(date);
        tMemberLoginService.update(tLogin);
        TMember tMember2 = new TMember();
        tMember2.setId(tLogin.getMemberId());
        tMember2 = tMemberService.selectOne(tMember2);
        int viewNum = tMember2.getViewNum() + tMember2.getTmpViewNum();
        int cacheNum = tMember2.getCacheNum();
        TLevel level = new TLevel();
        level.setId(tMember2.getLevelId());
        level = tLevelService.selectOne(level);
       /* if (level != null && !"".equals(level)) {
            viewNum = viewNum + level.getViewNum();
            cacheNum = cacheNum + level.getCacheNum();
        }*/
        if (tMember2.getIsVip() == 1) {
            Date now = new Date();
            if (now.after(tMember2.getVipDate())) {
                modelMap.put("viewNum", viewNum);
                modelMap.put("cacheNum", cacheNum);
                //更改VIP状态
                tMember2.setIsVip(0);
                tMemberService.update(tMember2);
            } else {
                modelMap.put("viewNum", 999999999);
                TVip tvip = new TVip();
                tvip.setId(tMember2.getVipId());
                tvip = tVipService.selectOne(tvip);
                modelMap.put("cacheNum", tvip.getCacheNum());
            }
        } else {
            modelMap.put("viewNum", viewNum);
            modelMap.put("cacheNum", cacheNum);
        }


        CacheUtil.getCache().set("onLine:" + tel,tel,60 * 60 * 5); //5小时过期


        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "登录成功");
        modelMap.put("token", token);
        modelMap.put("loginType", "1");
        CacheUtil.getCache().set("USER_TYPE_" + tLogin.getMemberId(), "1");
        //创建登录
        this.getReportCache(2, tLogin.getFromCode(), "");
        return setSuccessResponse(modelMap);

    }



    /**
     * 手机注册
     *
     * @param modelMap
     * @param request
     * @param params
     * @return
     */
    @PostMapping("/regedit")
    @Transactional
    public Object regedit(ModelMap modelMap, HttpServletRequest request, @RequestBody Map<String, Object> params) {
        SimpleDateFormat fformat = new SimpleDateFormat("yyyy-MM-dd");
        String dd = fformat.format(new Date());
        if (params == null || "".equals(params)) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "请求参数异常");
            return setSuccessResponse(modelMap);
        }

        if (params.get("tel") == null || "".equals(params.get("tel"))
                || params.get("smsCode") == null || "".equals(params.get("smsCode"))
                || params.get("pwd") == null || "".equals(params.get("pwd"))
                || params.get("deviceCode") == null || "".equals(params.get("deviceCode"))) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "手机号或验证码不能为空");
            return setSuccessResponse(modelMap);
        }
        if (CacheUtil.getCache().get("SMS_" + params.get("tel")) != null
                && !"".equals(CacheUtil.getCache().get("SMS_" + params.get("tel")))) {
            String code = (String) CacheUtil.getCache().get("SMS_" + params.get("tel"));
            if (!code.equals(params.get("smsCode"))) {
                modelMap.put("retCode", "-1");
                modelMap.put("retMsg", "验证码不正确或失效，请重试");
                return setSuccessResponse(modelMap);
            }
            //验证当前设备号是否已绑定
            TMemberLogin tl = new TMemberLogin();
            tl.setDeviceCode(String.valueOf(params.get("deviceCode")));
            tl = tMemberLoginService.selectOne(tl);
            if (tl.getTel() != null && !"".equals(tl.getTel())) {
                modelMap.put("retCode", "-1");
                modelMap.put("retMsg", "该设备已被" + tl.getTel() + "绑定");
                return setSuccessResponse(modelMap);
            }

            //验证手机号是否已绑定
            TMemberLogin t2 = new TMemberLogin();
            t2.setTel(params.get("tel").toString());
            t2 = tMemberLoginService.selectOne(t2);
            if (t2 != null && !"".equals(t2) && t2.getId() != null && !"".equals(t2.getId())) {
                modelMap.put("retCode", "-1");
                modelMap.put("retMsg", "该设备已被绑定");
                return setSuccessResponse(modelMap);
            }

            //根据手机号获取会员信息
            TMember tMember = new TMember();
            tMember.setId(tl.getMemberId());
            tMember = tMemberService.selectOne(tMember);
            tMember.setTel(params.get("tel").toString());
            //获取最低等级
            List<Map<String, Object>> levelList = new ArrayList<Map<String, Object>>();
            levelList = tLevelService.getLevelList();
            if (levelList != null && levelList.size() > 0) {
                Map<String, Object> pp = new HashMap<String, Object>();
                pp = levelList.get(0);
                if (pp != null && !"".equals(pp) && pp.get("id") != null && !"".equals(pp.get("id"))) {
                    tMember.setLevelId(Integer.valueOf(pp.get("id").toString()));
                }
            }

//               String extensionCode =getExtensionCode();
//               tMember.setExtensionCode(extensionCode);
            tMember.setIsVip(0);
            tMember.setIsRemind(0);
            tMember.setSex(1);
            String telString = params.get("tel").toString().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
            String ttelString = params.get("tel").toString();
            tMember.setNickName("用户" + ttelString.substring(ttelString.length() - 4, ttelString.length()));
            //创建每日赠送观影次数
            int num = 4;


            //建立推广记录
            TExtensionHistory tex = new TExtensionHistory();
            if (params.get("extensionCode") != null && !"".equals(params.get("extensionCode"))) {
                String extensionCode = params.get("extensionCode").toString();
                TMember tMember3 = new TMember();
                tMember3.setExtensionCode(extensionCode);
                tMember3 = tMemberService.selectOne(tMember3);
                if (tMember3 == null || "".equals(tMember3)) {
                    modelMap.put("retCode", "-1");
                    modelMap.put("retMsg", "推广码无效");
                    return setSuccessResponse(modelMap);
                }
                //todo 推广用户

                //推广新用户增加对应的积分
                IntegralClassify integralClassify = integralClassifyService.findOne(5);

                //增加积分
                if(null != integralClassify){

                    Integer integralNumber = tMember3.getIntegralNumber() +  integralClassify.getNumber();
                    tMember3.setIntegralNumber(integralNumber);

                    tMemberService.updateById(tMember3);

                    addIntegralHistory(tMember3.getId(),integralClassify.getId());
                }


                num += 2;
                tex.setMemberId(tMember3.getId());
                tex.setNickName("用户" + ttelString.substring(ttelString.length() - 4, ttelString.length()));
                tex.setTel(telString);
                tex.setRegeditTime(new Date());
            }
            tMember.setViewNum(num);

            //添加绑定手机积分
            IntegralClassify integralClassify = integralClassifyService.findOne(6);

            if(null != integralClassify){
                tMember.setIntegralNumber(integralClassify.getNumber());
            }


            //创建用户
            tMember = tMemberService.update(tMember);
            if (tMember == null || "".equals(tMember)) {
                modelMap.put("retCode", "-1");
                modelMap.put("retMsg", "用户注册失败");
                return setSuccessResponse(modelMap);
            }

            //添加积分获取记录
            addIntegralHistory(tMember.getId(),integralClassify.getId());



            if (params.get("extensionCode") != null && !"".equals(params.get("extensionCode"))) {
                if (tex != null && !"".equals(tex)) {
                    tex.setExtendId(tMember.getId());
                    tExtensionHistoryService.update(tex);
                    //升级被推广用户等级
                    //获取推广人的等级，并验证是否符合下一级等级的需求
                    //获取推广人人当前等级
                    String extensionCode = params.get("extensionCode").toString();
                    TMember tMember3 = new TMember();
                    tMember3.setExtensionCode(extensionCode);
                    tMember3 = tMemberService.selectOne(tMember3);
                    Integer currentLevelId = tMember3.getLevelId();
                    TLevel levelInfo = new TLevel();
                    levelInfo.setId(currentLevelId);
                    levelInfo = tLevelService.selectOne(levelInfo);
                    if (levelInfo != null && !"".equals(levelInfo)) {
                        //获取排序
                        int sorNo = levelInfo.getSortNo();
                        if (sorNo < 5) {
                            sorNo++;
                            TLevel levelInfo2 = new TLevel();
                            levelInfo2.setSortNo(sorNo);
                            levelInfo2 = tLevelService.selectOne(levelInfo2);
                            Integer exNum = levelInfo2.getExNum();
                            List<TExtensionHistory> textendList = new ArrayList<TExtensionHistory>();
                            Map<String, Object> paramsMap = new HashMap<String, Object>();
                            paramsMap.put("memberId", tMember.getId());
                            textendList = tExtensionHistoryService.queryList(params);
                            if (textendList != null && textendList.size() > 0) {
                                if (textendList.size() >= exNum) {
                                  tMember3.setIsRemind(1);
                                    tMember3.setLevelId(levelInfo2.getId());
/*
                                    //todo 增加会员天数
                                    Calendar ca = Calendar.getInstance();
                                    ca.setTime(tMember3.getVipDate());
                                    //根据购买类型id增加天数
                                    ca.add(Calendar.DATE, levelInfo2.getDayNum());
                                    Date cc = ca.getTime();
                                    tMember3.setVipDate(cc);
//            						   tMember3.setViewNum(levelInfo2.getViewNum());
//            						   tMember3.setCacheNum(levelInfo2.getCacheNum());
                                    tMemberService.update(tMember3);*/

                                    /*tMember3.setIsRemind(1);
                                    tMember3.setLevelId(levelInfo2.getId());
                                    //todo 增加会员天数
                                    Calendar ca = Calendar.getInstance();
                                    //防止空指针
                                    if (null == tMember3.getVipDate()) {
                                        tMember3.setVipDate(new Date());
                                    }

                                    ca.setTime(tMember3.getVipDate());
                                    //根据购买类型id增加天数
                                    ca.add(Calendar.DATE, levelInfo2.getDayNum());
                                    Date cc = ca.getTime();
                                    tMember3.setVipDate(cc);

                                    tMember3.setIsVip(1);*/
//            						   tMember3.setViewNum(levelInfo2.getViewNum());
//            						   tMember3.setCacheNum(levelInfo2.getCacheNum());
                                    tMemberService.updateById(tMember3);
                                }
                            }
                        }
                    }
                }
            }


            String pwd = String.valueOf(params.get("pwd"));
            String pwdNew = MD5Util.md5(pwd, SysConstants.MD5_SALT);
            tl.setTel(tMember.getTel());
            tl.setPwd(pwdNew);
            tl.setDeviceCode(String.valueOf(params.get("deviceCode")));
            tl.setUpdateTime(new Date());
            tl = tMemberLoginService.update(tl);
            if (tl == null || "".equals(tl)) {
                modelMap.put("retCode", "-1");
                modelMap.put("retMsg", "用户注册失败");
                return setSuccessResponse(modelMap);
            }
            modelMap.put("retCode", "1");
            modelMap.put("retMsg", "注册成功");
            //创建手机号绑定埋点和用户登录埋点
            this.getReportCache(4, tl.getFromCode(), "");
            //创建登录
            this.getReportCache(2, tl.getFromCode(), "");
            //获取token
            String token = null;
            Map<String, Object> payload = new HashMap<String, Object>();
            Date date = new Date();
            payload.put("uid", tl.getMemberId());// 用户id
            payload.put("iat", date.getTime());// 生成时间
            payload.put("ext", date.getTime() + 1000 * 60 * 60 * 5);// 过期时间5小时
            try {
                token = Jwt.createToken(payload);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String tel = (String) params.get("tel");
            CacheUtil.getCache().set("onLine:" + tel,tel, 60 * 60 * 5); //5小时过期

            //获取当前等级的次数
            int viewNum = tMember.getViewNum();
            int cacheNum = tMember.getCacheNum();
            TLevel level = new TLevel();
            level.setId(tMember.getLevelId());
            level = tLevelService.selectOne(level);
//            if (level != null && !"".equals(level)) {
//                viewNum = viewNum + level.getViewNum();
//                cacheNum = cacheNum + level.getCacheNum();
//            }
            modelMap.put("token", token);
            modelMap.put("loginType", "1");
            modelMap.put("viewNum", viewNum);
            modelMap.put("cacheNum", cacheNum);
            CacheUtil.getCache().set("USER_TYPE_" + tl.getMemberId(), "1");
            return setSuccessResponse(modelMap);
        }
        modelMap.put("retCode", "-1");
        modelMap.put("retMsg", "注册失败");
        return setSuccessResponse(modelMap);
    }


    private String getExtensionCode() {
        //创建推广码
        String extensionCode = DataUtil.createExtensionCode();
        TMember tMember2 = new TMember();
        tMember2.setExtensionCode(extensionCode);
        tMember2 = tMemberService.selectOne(tMember2);
        if (tMember2 == null || "".equals(tMember2)) {
            return extensionCode;
        }
        return getExtensionCode();
    }

    @PostMapping("/getSmsCode")
    public Object getSmsCode(ModelMap modelMap, HttpServletRequest request, @RequestBody Map<String, Object> params) {
        if (params == null || "".equals(params)) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "请求参数异常");
            return setSuccessResponse(modelMap);
        }

        logger.info("-------------8888888888-------------"+params.get("tel"));
        logger.info("-----------------8888888---------"+params.get("type"));



        if (params.get("tel") == null || "".equals(params.get("tel")) ||
                params.get("type") == null || "".equals(params.get("type"))) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "手机号不能为空");
            return setSuccessResponse(modelMap);
        }
        if ("1".equals(params.get("type").toString())) {
            //验证该验证码是否已注册，如果已注册，提醒： 该手机号已注册，请登录
            TMemberLogin tml = new TMemberLogin();
            tml.setTel(params.get("tel").toString());
            tml = tMemberLoginService.selectOne(tml);
            if (tml != null && !"".equals(tml)) {
                modelMap.put("retCode", "-1");
                modelMap.put("retMsg", "该手机号已注册，请登录");
                return setSuccessResponse(modelMap);
            }
        }
        String code = DataUtil.createSmsCode();
        Map<String, Object> intoMap = new HashMap<String, Object>();
        if (CacheUtils.getInstance().get("sms_info") != null && !"".equals(CacheUtils.getInstance().get("sms_info"))) {
            intoMap = (Map<String, Object>) CacheUtils.getInstance().get("sms_info");
        } else {
            intoMap = sysSmsService.selectSms();
        }
        String result = MiaoDiSmsSender.smsSenderTo(params.get("tel").toString(), code, intoMap);
        if (result == null) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "发送验证码失败");
            return setSuccessResponse(modelMap);
        }
        CacheUtil.getCache().set("SMS_" + params.get("tel"), code);
        modelMap.put("retCode", "1");
        modelMap.put("retMsg", "发送验证码成功");
        return setSuccessResponse(modelMap);
    }


    @PostMapping("/loseTel")
    public Object loseTel(ModelMap modelMap, HttpServletRequest request, @RequestBody Map<String, Object> params) {
        if (params == null || "".equals(params)) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "请求参数异常");
            return setSuccessResponse(modelMap);
        }
        if (params.get("tel") == null || "".equals(params.get("tel"))
                || params.get("smsCode") == null || "".equals(params.get("smsCode"))
                || params.get("pwd") == null || "".equals(params.get("pwd"))) {
            modelMap.put("retCode", "-1");
            modelMap.put("retMsg", "手机号或验证码不能为空");
            return setSuccessResponse(modelMap);
        }
        if (CacheUtil.getCache().get("SMS_" + params.get("tel")) != null
                && !"".equals(CacheUtil.getCache().get("SMS_" + params.get("tel")))) {
            String code = (String) CacheUtil.getCache().get("SMS_" + params.get("tel"));
            if (!code.equals(params.get("smsCode"))) {
                modelMap.put("retCode", "-1");
                modelMap.put("retMsg", "验证码失效");
                return setSuccessResponse(modelMap);
            }
            String pwd = String.valueOf(params.get("pwd"));
            String pwdNew = MD5Util.md5(pwd, SysConstants.MD5_SALT);
            TMemberLogin tLogin = new TMemberLogin();
            tLogin.setTel(params.get("tel").toString());
            tLogin = tMemberLoginService.selectOne(tLogin);
            if (tLogin == null || "".equals(tLogin)) {
                modelMap.put("retCode", "-1");
                modelMap.put("retMsg", "该用户不存在");
                return setSuccessResponse(modelMap);
            }
            tLogin.setPwd(pwdNew);
            tLogin.setLoginTime(new Date());
            tMemberLoginService.update(tLogin);
            String token = null;
            Map<String, Object> payload = new HashMap<String, Object>();
            Date date = new Date();
            payload.put("uid", tLogin.getMemberId());// 用户id
            payload.put("iat", date.getTime());// 生成时间
            payload.put("ext", date.getTime() + 1000 * 60 * 60 * 5);// 过期时间5小时
            try {
                token = Jwt.createToken(payload);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String tel = params.get("tel").toString();

            CacheUtil.getCache().set("onLine:" + tel,tel, 60 * 60 * 5); //5小时过期

            SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
            String lastDate = format.format(tLogin.getUpdateTime());
            String nowDate = format.format(date);
            if (!lastDate.equals(nowDate)) {
                TMember tMember = new TMember();
                tMember.setId(tLogin.getMemberId());
                tMember = tMemberService.selectOne(tMember);
                tMember.setUsedViewNum(0);
                tMember.setUsedCacheNum(0);
                tMemberService.update(tMember);
            }
            TMember tMember2 = new TMember();
            tMember2.setId(tLogin.getMemberId());
            tMember2 = tMemberService.selectOne(tMember2);
            int viewNum = tMember2.getViewNum() + tMember2.getTmpViewNum();
            int cacheNum = tMember2.getCacheNum();
            TLevel level = new TLevel();
            level.setId(tMember2.getLevelId());
            level = tLevelService.selectOne(level);
            if (level != null && !"".equals(level)) {
                viewNum = viewNum + level.getViewNum();
                cacheNum = cacheNum + level.getCacheNum();
            }
            if (tMember2.getIsVip() == 1) {
                Date now = new Date();
                if (now.after(tMember2.getVipDate())) {
                    modelMap.put("viewNum", viewNum);
                    modelMap.put("cacheNum", cacheNum);
                    //更改VIP状态
                    tMember2.setIsVip(0);
                    tMemberService.update(tMember2);
                } else {
                    modelMap.put("viewNum", 999999999);
                    TVip tvip = new TVip();
                    tvip.setId(tMember2.getVipId());
                    tvip = tVipService.selectOne(tvip);
                    modelMap.put("cacheNum", tvip.getCacheNum());
                }
            } else {
                modelMap.put("viewNum", viewNum);
                modelMap.put("cacheNum", cacheNum);
            }
            CacheUtil.getCache().set("USER_TYPE_" + tLogin.getMemberId(), "1");
            modelMap.put("token", token);
            modelMap.put("loginType", "1");
            modelMap.put("retCode", "1");
            modelMap.put("retMsg", "修改密码成功!");
            return setSuccessResponse(modelMap);

        }
        modelMap.put("retCode", "-1");
        modelMap.put("retMsg", "修改密码失败");
        return setSuccessResponse(modelMap);
    }




    @PostMapping("/404")
    public Object noPage(ModelMap modelMap, HttpServletRequest request) {
        return setResponse(modelMap, HttpCode.BAD_REQUEST);
    }

    @PostMapping("/500")
    public Object noAction(ModelMap modelMap, HttpServletRequest request) {
        return setResponse(modelMap, HttpCode.INTERNAL_SERVER_ERROR);
    }
}
