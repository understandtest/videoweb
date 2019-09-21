package com.videoweb.ying.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.videoweb.base.BaseController;
import com.videoweb.utils.DoubleUtil;
import com.videoweb.utils.redis.CacheUtil;
import com.videoweb.ying.po.TCashApply;
import com.videoweb.ying.po.TMember;
import com.videoweb.ying.service.TCashApplyService;
import com.videoweb.ying.service.TExtendPriceService;
import com.videoweb.ying.service.TMemberService;
import com.videoweb.ying.service.TRateService;


@RestController
@RequestMapping("/ying")
public class ApiCashApplyController extends BaseController{

	@Autowired
	private TCashApplyService tCashApplyService;
	
	@Autowired
	private TExtendPriceService tExtendPriceService;
	
	@Autowired
	private TMemberService tMemberService;
	
	@Autowired
	private TRateService tRateService;
	
	
	/**
	 * 获取用户钻石数
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@PostMapping("/getApplyMemberCron")
	public Object getApplyMemberCron(ModelMap modelMap, HttpServletRequest request)
	{
		//获取用户当前的钻石数
		Integer memberId = getCurrUser(request);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("extendId", memberId);
		TMember tm = new TMember();
		tm.setId(memberId);
		tm = tMemberService.selectOne(tm);
		Integer currentCronNum = tm.getCronNum();
		modelMap.put("currentCronNum", currentCronNum);
		//获取用户全部的钻石数
		Integer totalCronNum = tExtendPriceService.getTotalCronNum(params);
		modelMap.put("totalCronNum", totalCronNum);
		//获取提现手续费
		Map<String,Object> rateResult = new HashMap<String,Object>();
		rateResult = tRateService.selectRate();
		if(rateResult != null && !"".equals(rateResult))
		{
			Double chargeFee = Double.valueOf(rateResult.get("chargeFee").toString());
			modelMap.put("chargeFee", chargeFee+"%");
			//折算成提现金额
			Double  cronRate= Double.valueOf(rateResult.get("cronRate").toString());
			Double cc = new BigDecimal(1/cronRate).doubleValue();
			Double price = DoubleUtil.multiplyForRoundHalfUp(Double.valueOf(currentCronNum), cc, 2);
			modelMap.put("price", price);
			
			modelMap.put("mostPrice", rateResult.get("mostPrice").toString());
			Integer mostPrice = Double.valueOf(rateResult.get("mostPrice").toString()).intValue();
			Integer cronRateInt = cronRate.intValue();
			Integer tf =mostPrice*cronRateInt;
/*			modelMap.put("mostCron", tf+"");
			modelMap.put("mostPrice", mostPrice+"")*/;
			//获取最大能抵扣的钻石数
			if(currentCronNum>=tf)
			{
				modelMap.put("mostCron", tf+"");
				modelMap.put("mostPrice", Double.valueOf(rateResult.get("mostPrice").toString())+"");
			}
			else
			{
				modelMap.put("mostCron", currentCronNum+"");
				modelMap.put("mostPrice", price+"");
			}
		}
		modelMap.put("retCode", "1");
		modelMap.put("retMsg", "获取数据成功");
		return  setSuccessResponse(modelMap);
	}
	
	
	@PostMapping("/submitApplyCron")
	public Object submitApplyCron(ModelMap modelMap, HttpServletRequest request,@RequestBody Map<String,Object> params)
	{
		if(params == null || "".equals(params))
		{
			 modelMap.put("retCode", "-1");
			 modelMap.put("retMsg", "提交失败，参数异常");
			 return  setSuccessResponse(modelMap);
		}
		if(params.get("price") == null || "".equals(params.get("price"))
				||params.get("name") == null || "".equals(params.get("name"))
				||params.get("cardNo") == null || "".equals(params.get("cardNo")))
		{
			 modelMap.put("retCode", "-1");
			 modelMap.put("retMsg", "提交失败，参数异常");
			 return  setSuccessResponse(modelMap);
		}
		//TODO 填写提现申请
		
		String name = String.valueOf(params.get("name"));
		String cardNo = String.valueOf(params.get("cardNo"));
		Double toPrice = Double.valueOf(params.get("price").toString());
		Integer memberId = getCurrUser(request);
		
		//验证当前的用户的金额是否满足
		Map<String,Object> paramsMap = new HashMap<String,Object>();
		paramsMap.put("extendId", memberId);
		TMember tm = new TMember();
		tm.setId(memberId);
		tm = tMemberService.selectOne(tm);
		Integer currentCronNum = tm.getCronNum();
		//获取用户全部的钻石数
		Integer totalCronNum = tExtendPriceService.getTotalCronNum(paramsMap);
		//获取提现手续费
		Map<String,Object> rateResult = new HashMap<String,Object>();
		rateResult = tRateService.selectRate();
		Double price=0d;
		Double chargeFee=0d;
		Double cronNum=0d;
		Double  cronRate=0d;
		if(rateResult != null && !"".equals(rateResult))
		{
			//提现手续费
			chargeFee = Double.valueOf(rateResult.get("chargeFee").toString());
			cronNum =Double.valueOf(rateResult.get("cronNum").toString());
			if(cronNum.intValue()>currentCronNum.intValue())
			{
				modelMap.put("retCode", "-1");
				modelMap.put("retMsg", "钻石数满足"+cronNum+"个，才可提现");
				return  setSuccessResponse(modelMap);
			}
			//钻石兑换比例=1/钻石数
			cronRate= Double.valueOf(rateResult.get("cronRate").toString());
			Double cc = new BigDecimal(1/cronRate).doubleValue();
			price = DoubleUtil.multiplyForRoundHalfUp(Double.valueOf(currentCronNum), cc, 2);
		}
		if(price.compareTo(toPrice)>=0)
		{
			if(CacheUtil.getLock("submitApplyCron_"+memberId))
			{
				TCashApply tca = new TCashApply();
				tca.setMemberId(memberId);
				tca.setCronNum(currentCronNum);
				tca.setCronRate(cronRate);
				tca.setChargeFee(chargeFee);
				tca.setUsePrice(toPrice);
				//获取可提现的金额=  提现金额- 提现金额*提现手续费 
				Double chargeFeeD = BigDecimal.valueOf(chargeFee).divide(new BigDecimal(100)).doubleValue();
				Double usePrice = DoubleUtil.subtractForRoundHalfUp(toPrice,DoubleUtil.multiplyForRoundHalfUp(chargeFeeD, toPrice, 2),2);
				tca.setGetPrice(usePrice);
				tca.setPayeeName(name);
				tca.setCardNo(cardNo);
				tca.setPayStatus(1);
				tca.setApplyTime(new Date());
				tCashApplyService.update(tca);
				//扣除钻石
				//将金额折算成钻石 =  提现金额*钻石比例
			    Double subCronNum=DoubleUtil.multiplyForRoundHalfUp(toPrice,cronRate,0);
			    int ss= currentCronNum.intValue()- subCronNum.intValue();
			    tm.setCronNum(ss);
			    tm = tMemberService.update(tm);
			    CacheUtil.unlock("submitApplyCron_"+memberId);
			}
		}
		else
		{
			modelMap.put("retCode", "-1");
			modelMap.put("retMsg", "提现余额不足");
			return  setSuccessResponse(modelMap);
		}
		modelMap.put("retCode", "1");
		modelMap.put("retMsg", "提现申请成功");
		return  setSuccessResponse(modelMap);
	}
	
	
	
}
