package com.videoweb.ying.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.videoweb.ying.po.TVip;
import com.videoweb.ying.service.TVipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.videoweb.base.BaseController;
import com.videoweb.utils.redis.CacheUtil;
import com.videoweb.ying.po.TMember;
import com.videoweb.ying.po.TRechargeCode;
import com.videoweb.ying.service.TMemberService;
import com.videoweb.ying.service.TRechargeCodeService;


@RestController
@RequestMapping("/ying")
public class ApiRechargeCodeController extends BaseController{

	
	@Autowired
	private TRechargeCodeService tRechargeCodeService;

	@Autowired
	private TVipService tVipService;
	
	@Autowired
	private TMemberService tMemberService;
	
	/**
	 * 使用充值码
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@PostMapping("/useRechargeCode")
	public Object useRechargeCode(ModelMap modelMap, HttpServletRequest request,@RequestBody Map<String,Object> params)
	{
		if(params == null || "".equals(params))
		{
			 modelMap.put("retCode", "-1");
			 modelMap.put("retMsg", "查询失败，参数异常");
			 return  setSuccessResponse(modelMap);
		}
		if(params.get("rechargeCode") == null || "".equals(params.get("rechargeCode")))
		{
			 modelMap.put("retCode", "-1");
			 modelMap.put("retMsg", "查询失败，参数异常");
			 return  setSuccessResponse(modelMap);
		}
		String rechargeCode = String.valueOf(params.get("rechargeCode"));
		//验证当前充值码的有效性
		if(CacheUtil.getLock("rechargeCode_"+rechargeCode))
		{
			Map<String,Object> result = new HashMap<String,Object>();
			result = tRechargeCodeService.getRechargeCodeByCode(params);
			if(result == null ||"".equals(result))
			{
				modelMap.put("retCode", "-1");
				modelMap.put("retMsg", "当前充值码已失效");
				CacheUtil.unlock("rechargeCode_"+rechargeCode);
				return  setSuccessResponse(modelMap);
			}
			//如果未失效，则进行充值
			Integer id = Integer.valueOf(result.get("id").toString());
			TRechargeCode trc = new TRechargeCode();
			trc.setId(id);
			//获取用户ID
			Integer memberId = getCurrUser(request);
			trc.setActivityTime(new Date());
			trc.setMemberId(memberId);
			trc.setIsActivity(2);
			trc = tRechargeCodeService.update(trc);
			//更新用户的VIP到期时间
			TMember tm = new TMember();
			tm.setId(memberId);
			tm = tMemberService.selectOne(tm);
			if(tm != null && !"".equals(tm))
			{
				tm.setIsVip(1);
				tm.setVipId(trc.getVipId());
				Date oldDay = tm.getVipDate();
				if(oldDay == null || "".equals(oldDay))
				{
					oldDay = new Date();
				}
				Calendar ca = Calendar.getInstance();
				ca.setTime(oldDay);

				TVip vip = tVipService.getVipByCartType(trc.getCardType());

				if(null == vip){
					modelMap.put("retCode", "-1");
					modelMap.put("retMsg", "充值码发生错误");
					return  setSuccessResponse(modelMap);
				}

				ca.add(Calendar.DATE, vip.getDayNum());
				Date cc = ca.getTime();
				tm.setVipDate(cc);
				tMemberService.update(tm);
			}
			CacheUtil.unlock("rechargeCode_"+rechargeCode);
		}
		modelMap.put("retCode", "1");
		modelMap.put("retMsg", "充值成功");
		return  setSuccessResponse(modelMap);
	}
	
	
}
