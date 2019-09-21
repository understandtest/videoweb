package com.videoweb.ying.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.codec.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.videoweb.base.BaseController;
import com.videoweb.utils.DoubleUtil;
import com.videoweb.utils.HashUtil;
import com.videoweb.utils.HttpConnectionUtil;
import com.videoweb.utils.HttpUtil;
import com.videoweb.utils.PropertiesUtil;
import com.videoweb.utils.WebUtil;
import com.videoweb.utils.redis.CacheUtil;
import com.videoweb.ying.po.TExtendPrice;
import com.videoweb.ying.po.TExtensionHistory;
import com.videoweb.ying.po.TMember;
import com.videoweb.ying.po.TMemberLogin;
import com.videoweb.ying.po.TPaySetting;
import com.videoweb.ying.po.TVip;
import com.videoweb.ying.po.TVipHistory;
import com.videoweb.ying.service.TExtendPriceService;
import com.videoweb.ying.service.TExtensionHistoryService;
import com.videoweb.ying.service.TMemberLoginService;
import com.videoweb.ying.service.TMemberService;
import com.videoweb.ying.service.TPaySettingService;
import com.videoweb.ying.service.TRateService;
import com.videoweb.ying.service.TVipHistoryService;
import com.videoweb.ying.service.TVipService;

@RestController
public class ApiPayController extends BaseController{

	@Autowired
	private TVipService tVipService;
	
	@Autowired
	private TPaySettingService tPaySettingService;
	
	@Autowired
	private TVipHistoryService tVipHistoryService;
	
	@Autowired
	private TMemberService tMemberService;
	
	@Autowired
	private TMemberLoginService tMemberLoginService;
	
	/*@Autowired
	private TReportService tReportService;
	*/
	
	 @Autowired
	private TRateService tRateService;
    
    @Autowired
    private TExtensionHistoryService tExtensionHistoryService;
    
    @Autowired
    private TExtendPriceService tExtendPriceService;

	
	@PostMapping("/openapi/getPayType")
    public Object getPayType(ModelMap modelMap, HttpServletRequest request)
    {
		List<Map<String,Object>> tPaySetting = new ArrayList<Map<String,Object>>();
		tPaySetting = tPaySettingService.getPayType();
		if(tPaySetting != null && tPaySetting.size()>0)
		{
			for(Map<String,Object> tf:tPaySetting)
			{
				if("1".equals(tf.get("payImgType").toString()))
				{
					tf.put("payImg", PropertiesUtil.getString("remote.file.uri.prefix")+tf.get("payImg"));
				}
			}
		}
		modelMap.put("retCode", "1");
	    modelMap.put("retMsg", "充值方式!");
	    return setSuccessResponse(modelMap,tPaySetting);
    }
	
	/**
	 * 充值接口
	 * @param modelMap
	 * @param request
	 * @param params
	 * @return
	 */
	@PostMapping("/ying/payRecharge")
	public Object payRecharge(ModelMap modelMap, HttpServletRequest request, @RequestBody Map<String, Object> params)
	{
		Integer memberId = getCurrUser(request);
		if(params == null || "".equals(params))
		{
			modelMap.put("retCode", "-1");
		    modelMap.put("retMsg", "充值失败，参数异常!");
		    return setSuccessResponse(modelMap);
		}
		
		if(params.get("vipId") == null || "".equals(params.get("vipId"))
				||params.get("payType") == null || "".equals(params.get("payType")))
		{
			modelMap.put("retCode", "-1");
		    modelMap.put("retMsg", "充值失败，参数异常!");
		    return setSuccessResponse(modelMap);
		}
		
		//根据vipId取出充值价格
		TVip vip= new TVip();
		vip.setId(Integer.valueOf(params.get("vipId").toString()));
		vip = tVipService.selectOne(vip);
		if(vip == null || "".equals(vip))
		{
			modelMap.put("retCode", "-1");
		    modelMap.put("retMsg", "充值失败，无此产品!");
		    return setSuccessResponse(modelMap);
		}
		Double price = vip.getPrice();
		int useCron = 0;
		boolean selectflag = false;
		if(params.get("selectType") != null && !"".equals(params.get("selectType")))
		{
			//当选择钻石抵扣方式时
			if("1".equals(params.get("selectType")))
			{
				selectflag = true;
				//将个人钻石数放进缓存
				TMember tmeb = new TMember();
				tmeb.setId(memberId);
				tmeb = tMemberService.selectOne(tmeb);
				//获取能够折算的金额和钻石
				Integer currentCronNum = tmeb.getCronNum();
				//获取提现手续费
				Map<String,Object> rateResult = new HashMap<String,Object>();
				rateResult = tRateService.selectRate();
				if(rateResult != null && !"".equals(rateResult))
				{
					Double  cronRate= Double.valueOf(rateResult.get("cronRate").toString());
					Double cc = new BigDecimal(1/cronRate).doubleValue();
					Double tkprice = DoubleUtil.multiplyForRoundHalfUp(Double.valueOf(currentCronNum), cc, 2);
					
					Integer mostPrice = Double.valueOf(rateResult.get("mostPrice").toString()).intValue();
					Integer cronRateInt = cronRate.intValue();
					Integer tf =mostPrice*cronRateInt;
					//获取最大能抵扣的钻石数
					Double usePrice = 0d;
					if(currentCronNum>=tf)
					{
						useCron = tf;
						usePrice = Double.valueOf(rateResult.get("mostPrice").toString());
					}
					else
					{
						useCron = currentCronNum;
						usePrice = tkprice;
					}
					//获取扣减后的充值价格
					price = DoubleUtil.subtractForRoundHalfUp(price,usePrice,2);
				}
			}
		}
		Integer payType = Integer.valueOf(params.get("payType").toString());
		if(payType == 1)
		{
			//支付宝
			//查询可用的支付宝账户
			TPaySetting paySetting = new TPaySetting();
			paySetting.setPayType(payType);
			paySetting.setIsEnable(1);
			paySetting = tPaySettingService.selectOne(paySetting);
			if(paySetting == null && "".equals(paySetting))
			{
				modelMap.put("retCode", "-1");
			    modelMap.put("retMsg", "充值失败，第三方支付接口异常!");
			    return setSuccessResponse(modelMap);
			}
			Map<String,String> payParams = new HashMap<String,String>();
			payParams.put("merchant_no", paySetting.getPayAccount());//商户号
			SimpleDateFormat tf = new SimpleDateFormat("yyyyMMddHHmmss");
			String merchantOrderNo =IdWorker.get32UUID() ;
			payParams.put("merchant_order_no",merchantOrderNo);//商户订单号
			payParams.put("notify_url", PropertiesUtil.getString("payBackUrl")); //回调地址
			payParams.put("start_time", tf.format(new Date()));
			Double tprice = DoubleUtil.getTwoDecimal(price);
			payParams.put("trade_amount", tprice+"");
			String goodsName="";


			switch (vip.getCardType()){
				case 1: {
					goodsName="天卡";
					break;
				}
				case 2: {
					goodsName="月卡";
					break;
				}
				case 3: {
					goodsName="半年卡";
					break;
				}
				case 4: {
					goodsName="年卡";
					break;
				}
				case 5: {
					goodsName="永久卡";
					break;
				}
			}

			payParams.put("goods_name",goodsName);
			payParams.put("goods_desc", "购买"+goodsName);
			String attach = memberId+"ying"+price+"ying"+vip.getId()+"ying"+vip.getCardType();
			payParams.put("attach", attach);
			payParams.put("sign_type", "1");
			String sign = createSign(payParams,paySetting.getPayKey());
			payParams.put("sign", sign);
			String cc = packageSign(payParams,false);
			String res = HttpUtil.post(paySetting.getPayUrl(),cc);
			logger.info("支付宝支付信息返回:"+res);
			JSONObject  myJson = JSONObject.fromObject(res);
			String status = myJson.getString("status");
			if("1".equals(status))
			{
				JSONObject mcc = myJson.getJSONObject("data");
				modelMap.put("payUrl", mcc.getString("pay_url"));
				modelMap.put("tradeNo", merchantOrderNo);
				modelMap.put("retCode", "1");
			    modelMap.put("retMsg", "发起支付成功!");
			    
			    //创建订单信息
			    TVipHistory tvh = new TVipHistory();
			    tvh.setMemberId(memberId);
			    tvh.setPayPrice(price);
			    tvh.setVipId(vip.getId());
			    tvh.setPayNo(merchantOrderNo);
			    if(selectflag)
			    {
			    	 tvh.setIsUseCron(2);
			    	 tvh.setUseCron(useCron);
			    }

			   	//设置有效期
				tvh.setDateValidity(vip.getDayNum());


			    tvh.setPayStatus(1);//待支付
			    tvh.setPayType(1);
			    tVipHistoryService.update(tvh);
			    
			    return setSuccessResponse(modelMap);
			}
			modelMap.put("retMsg", "发起支付失败,失败原因："+new String(myJson.get("info").toString()));
			
		}
		else if(payType == 2)
		{
			TPaySetting paySetting = new TPaySetting();
			paySetting.setPayType(payType);
			paySetting.setIsEnable(1);
			paySetting = tPaySettingService.selectOne(paySetting);
			if(paySetting == null && "".equals(paySetting))
			{
				modelMap.put("retCode", "-1");
			    modelMap.put("retMsg", "充值失败，第三方支付接口异常!");
			    return setSuccessResponse(modelMap);
			}
			Map<String,String> payParams = new HashMap<String,String>();
			payParams.put("mch_id", paySetting.getPayAccount());
			String body="购买VIP充值";
			payParams.put("body", body);
			payParams.put("total_fee",  DoubleUtil.changeY2F(String.valueOf(price)));
			SimpleDateFormat tf = new SimpleDateFormat("yyyyMMdd");
			//String attach = tf.format(new Date())+"ying"+memberId+"ying"+vip.getId()+"ying"+vip.getCardType();
			String merchantOrderNo =IdWorker.get32UUID() ;
			payParams.put("out_trade_no", merchantOrderNo);
			payParams.put("trade_type", "WX_WAP");//WX_SCAN
			payParams.put("spbill_create_ip", WebUtil.getHost(request));
			payParams.put("notify_url", PropertiesUtil.getString("weixinpayBackUrl"));
			String sign = createSign(payParams,paySetting.getPayKey());
			payParams.put("sign", sign);
			String cc = packageSign(payParams,false);
			try {
				String res = HttpConnectionUtil.get(paySetting.getPayUrl(),cc);
				System.out.println("微信支付信息返回:"+res);
				if(res != null && !"".equals(res))
				{
					JSONObject  myJson = JSONObject.fromObject(res);
					modelMap.put("payUrl", myJson.getString("wx_wap_url"));
					modelMap.put("tradeNo", merchantOrderNo);
					modelMap.put("retCode", "1");
				    modelMap.put("retMsg", "发起支付成功!");
				    //创建订单信息
				    TVipHistory tvh = new TVipHistory();
				    tvh.setMemberId(memberId);
				    tvh.setPayPrice(price);
				    tvh.setVipId(vip.getId());
				    tvh.setPayNo(merchantOrderNo);
				    if(selectflag)
				    {
				    	 tvh.setIsUseCron(2);
				    	 tvh.setUseCron(useCron);
				    }

					//设置有效期
					tvh.setDateValidity(vip.getDayNum());

				    tvh.setPayStatus(1);//待支付
				    tvh.setPayType(2);
				    tVipHistoryService.update(tvh);
				    return setSuccessResponse(modelMap);
				}
				
			} catch (Exception e) {
				logger.info("----微信支付异常，异常信息----"+e.getMessage());
				modelMap.put("retCode", "-1");
				modelMap.put("retMsg", "发起支付失败!");
			    return setSuccessResponse(modelMap);
			}
		}
		modelMap.put("retCode", "-1");
		modelMap.put("retMsg", "发起支付失败!");
	    return setSuccessResponse(modelMap);
	}
	
	
	@PostMapping("/openapi/payBack")
	public Object  payBack(ModelMap modelMap, HttpServletRequest request, @RequestBody Map<String, Object> params)
	{
		logger.info("支付宝回调参数:"+params);
		if(params != null && !"".equals(params))
		{
			if("Success".equals(params.get("status")))
			{
				String merchantOrderNo = params.get("merchant_order_no").toString();
				String tradeNo = params.get("tradeNo").toString();
				String attach = params.get("attach").toString();
				String[] sf = attach.split("ying");
				TVipHistory tvh = new TVipHistory();
				tvh.setPayNo(merchantOrderNo);
				tvh.setPayStatus(1);
				tvh = tVipHistoryService.selectOne(tvh);
				if(tvh == null ||"".equals(tvh))
				{
					return "fail";
				}
				tvh.setPayTime(new Date());
				tvh.setPayStatus(2);
				tvh.setPayOutNo(tradeNo);
				tVipHistoryService.update(tvh);
				//更新用户VIP，及其使用期限
				TMember tmb = new TMember();
				tmb.setId(Integer.valueOf(sf[0]));
				tmb = tMemberService.selectOne(tmb);
				tmb.setIsVip(1);
				tmb.setVipId(Integer.valueOf(sf[2]));
				Date oldDay = tmb.getVipDate();
				if(oldDay == null || "".equals(oldDay))
				{
					oldDay = new Date();
				}
				Calendar ca = Calendar.getInstance();
				ca.setTime(oldDay);

				Integer cardType = Integer.valueOf(sf[3]);

				//根据购买类型id增加天数
				TVip vip = tVipService.getVipByCartType(cardType);

				if(null != vip){
					ca.add(Calendar.DATE, vip.getDayNum());
				}

				Date cc = ca.getTime();
				tmb.setVipDate(cc);
				//todo 增加缓存次数
				tmb.setCacheNum(tmb.getCacheNum() + vip.getCacheNum());
				//扣除钻石数
				if(tvh.getIsUseCron() == 2)
				{
					if(tmb.getCronNum().intValue()>tvh.getUseCron().intValue())
					{
						int newCrom = tmb.getCronNum()-tvh.getUseCron();
						tmb.setCronNum(newCrom);
					}
				}
				tMemberService.update(tmb);
				//更改充值
				//获取渠道码
				TMemberLogin ml = new TMemberLogin();
				ml.setMemberId(Integer.valueOf(sf[0]));
				ml = tMemberLoginService.selectOne(ml);
				this.getReportCache(3, ml.getFromCode(), String.valueOf(tvh.getPayPrice()));
				
				//进行充值分配钻石
	       		//获取后台比例
	       		Map<String,Object> tf = new HashMap<String,Object>();
	    		tf = tRateService.selectRate();
	    		if(tf != null && !"".equals(tf))
	    		{
	    			if(tf.get("rechargeRate") != null && !"".equals(tf.get("rechargeRate")))
	    			{
	    				Double rechargeRate = Double.valueOf(tf.get("rechargeRate").toString());
	    				Double cronNumDouble= DoubleUtil.multiplyForRoundHalfUp(tvh.getPayPrice(),rechargeRate,0);
	    				int cronNumNew =cronNumDouble.intValue();
	    				//获取被推广人的金币
	    				TExtensionHistory tex = new TExtensionHistory();
	    				tex.setExtendId(tvh.getMemberId());
	    				tex=tExtensionHistoryService.selectOne(tex);
	    				if(tex != null && !"".equals(tex))
	    				{
	    					TMember extendMember = new TMember();
	    					extendMember.setId(tex.getMemberId());
	    					extendMember = tMemberService.selectOne(extendMember);
	    					int newCronNum= extendMember.getCronNum()+cronNumNew;
	    					extendMember.setCronNum(newCronNum);
	    					tMemberService.update(extendMember);
	    					//建立推广
	    					TExtendPrice tdp = new TExtendPrice();
	    					tdp.setCronNum(cronNumNew);
	    					tdp.setMemberId(tvh.getMemberId());
	    					tdp.setVipHistoryId(tvh.getId());
	    					tdp.setExtendId(tex.getMemberId());
	    					tdp.setPrice(tvh.getPayPrice());
	    					tdp.setRechargeRate(rechargeRate);
	    					tdp.setName(tex.getNickName());
	    					tdp.setTel(tex.getTel());
	    					tdp.setRechargeTime(new Date());
	    					tExtendPriceService.update(tdp);
	    				}
	    			}
	    		}
				
				return "success";
			}
		}
		return "fail";
	}
	

	@PostMapping("/openapi/weixinpayBack")
	public Object  weixinpayBack(ModelMap modelMap, HttpServletRequest request)
	{
		String paramsString ="";
		InputStreamReader inputStreamReader;
		try {
			inputStreamReader = new InputStreamReader(request.getInputStream(),"UTF-8");
			 BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	 			String str = null;  
	 			StringBuilder builder=new StringBuilder();
	            while ((str = bufferedReader.readLine()) != null) {  
	            	builder.append(str);  
	            }  
	            bufferedReader.close();  
	            inputStreamReader.close();  
	            logger.info("支付回调内容:"+builder.toString());
	            paramsString = builder.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
		String[] tff = paramsString.split("\\&");
		Map<String,Object> params = new HashMap<String,Object>();
		for(String tk:tff)
		{
			String[] ykStrings = tk.split("=");
			params.put(ykStrings[0], ykStrings[1]);
		}
		TVipHistory tvh = new TVipHistory();
		if(params != null && !"".equals(params))
		{
			if(params.get("out_trade_no") != null && !"".equals(params.get("out_trade_no")))
			{
				String attach = String.valueOf(params.get("out_trade_no"));
				String tradeNo = String.valueOf(params.get("trade_no"));
				tvh.setPayNo(attach);
				tvh.setPayStatus(1);
				tvh = tVipHistoryService.selectOne(tvh);
				if(tvh == null ||"".equals(tvh))
				{
					return "fail";
				}
				tvh.setPayTime(new Date());
				tvh.setPayStatus(2);
				tvh.setPayOutNo(tradeNo);
				tVipHistoryService.update(tvh);
			}
			if(tvh != null && !"".equals(tvh))
			{
				//更新用户VIP，及其使用期限
				
				TMember tmb = new TMember();
				tmb.setId(tvh.getMemberId());
				tmb = tMemberService.selectOne(tmb);
				tmb.setIsVip(1);
				tmb.setVipId(tvh.getVipId());
				//根据VIP获取相关信息
				TVip tVip = new TVip();
				tVip.setId(tvh.getVipId());
				tVip = tVipService.selectOne(tVip);
				Date oldDay = tmb.getVipDate();
				if(oldDay == null || "".equals(oldDay))
				{
					oldDay = new Date();
				}
				Calendar ca = Calendar.getInstance();
				ca.setTime(oldDay);

				//根据购买类型id增加天数
				ca.add(Calendar.DATE, tVip.getDayNum());

				Date cc = ca.getTime();
				tmb.setVipDate(cc);

				//todo 增加缓存次数
				tmb.setCacheNum(tmb.getCacheNum() + tVip.getCacheNum());

				//扣除钻石数
				if(tvh.getIsUseCron() == 2)
				{
					if(tmb.getCronNum().intValue()>tvh.getUseCron().intValue())
					{
						int newCrom = tmb.getCronNum()-tvh.getUseCron();
						tmb.setCronNum(newCrom);
					}
					
				}
				tMemberService.update(tmb);
				//更改充值
				TMemberLogin ml = new TMemberLogin();
				ml.setMemberId(tvh.getMemberId());
				ml = tMemberLoginService.selectOne(ml);
				this.getReportCache(3, ml.getFromCode(), String.valueOf(tvh.getPayPrice()));
				//进行充值分配钻石
	       		//获取后台比例
	       		Map<String,Object> tf = new HashMap<String,Object>();
	    		tf = tRateService.selectRate();
	    		if(tf != null && !"".equals(tf))
	    		{
	    			if(tf.get("rechargeRate") != null && !"".equals(tf.get("rechargeRate")))
	    			{
	    				Double rechargeRate = Double.valueOf(tf.get("rechargeRate").toString());
	    				Double cronNumDouble= DoubleUtil.multiplyForRoundHalfUp(tvh.getPayPrice(),rechargeRate,0);
	    				int cronNumNew =cronNumDouble.intValue();
	    				//获取被推广人的金币
	    				TExtensionHistory tex = new TExtensionHistory();
	    				tex.setExtendId(tvh.getMemberId());
	    				tex=tExtensionHistoryService.selectOne(tex);
	    				if(tex != null && !"".equals(tex))
	    				{
	    					TMember extendMember = new TMember();
	    					extendMember.setId(tex.getMemberId());
	    					extendMember = tMemberService.selectOne(extendMember);
	    					int newCronNum= extendMember.getCronNum()+cronNumNew;
	    					extendMember.setCronNum(newCronNum);
	    					tMemberService.update(extendMember);
	    					//建立推广
	    					TExtendPrice tdp = new TExtendPrice();
	    					tdp.setCronNum(cronNumNew);
	    					tdp.setMemberId(tvh.getMemberId());
	    					tdp.setVipHistoryId(tvh.getId());
	    					tdp.setExtendId(tex.getMemberId());
	    					tdp.setPrice(tvh.getPayPrice());
	    					tdp.setRechargeRate(rechargeRate);
	    					tdp.setName(tex.getNickName());
	    					tdp.setTel(tex.getTel());
	    					tdp.setRechargeTime(new Date());
	    					tExtendPriceService.update(tdp);
	    				}
	    			}
	    		}
				return "success";
			}
		}
		return "fail";
	}
	
	
	
	@PostMapping("/ying/getPayStatus")
	public Object getPayStatus(ModelMap modelMap, HttpServletRequest request, @RequestBody Map<String, Object> params)
	{
		if(params == null || "".equals(params))
		{
			modelMap.put("retCode", "-1");
		    modelMap.put("retMsg", "充值失败，参数异常!");
		    return setSuccessResponse(modelMap);
		}
		if(params.get("tradeNo") == null || "".equals(params.get("tradeNo")))
		{
			modelMap.put("retCode", "-1");
		    modelMap.put("retMsg", "充值失败，参数异常!");
		    return setSuccessResponse(modelMap);
		}
		TVipHistory tvh = new TVipHistory();
		tvh.setPayNo(params.get("tradeNo").toString());
		tvh = tVipHistoryService.selectOne(tvh);
		
		if(tvh == null || "".equals(tvh))
		{
			modelMap.put("retCode", "-1");
			modelMap.put("retMsg", "无此充值记录!");
			return setSuccessResponse(modelMap);
		}
		if(tvh.getPayStatus().intValue()==2)
		{
			modelMap.put("retCode", "1");
			modelMap.put("retMsg", "充值成功!");
			return setSuccessResponse(modelMap);
		}
		modelMap.put("retCode", "-1");
		modelMap.put("retMsg", "充值暂未成功!");
		return setSuccessResponse(modelMap);
		
	}
	
	
	public static String createSign(Map<String, String> params, String partnerKey) {
		// 生成签名前先去除sign
		String stringA = packageSign(params, false);
		String stringSignTemp = stringA + "&key=" + partnerKey;
		return HashUtil.md5(stringSignTemp);
	}
	
	/**
	 * 组装签名的字段
	 * 
	 * @param params
	 *            参数
	 * @param urlEncoder
	 *            是否urlEncoder
	 * @return String
	 */
	public static String packageSign(Map<String, String> params, boolean urlEncoder) {
		// 先将参数以其参数名的字典序升序进行排序
		TreeMap<String, String> sortedParams = new TreeMap<String, String>(params);
		// 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Entry<String, String> param : sortedParams.entrySet()) {
			String value = param.getValue();
			if (StringUtils.isBlank(value)) {
				continue;
			}
			if (first) {
				first = false;
			} else {
				sb.append("&");
			}
			sb.append(param.getKey()).append("=");
			if (urlEncoder) {
				try {
					value = urlEncode(value);
				} catch (UnsupportedEncodingException e) {
				}
			}
			sb.append(value);
		}
		return sb.toString();
	}
	
	 /* urlEncode
	 * 
	 * @param src
	 *            微信参数
	 * @return String
	 * @throws UnsupportedEncodingException
	 *             编码错误
	 */
	public static String urlEncode(String src) throws UnsupportedEncodingException {
		return URLEncoder.encode(src, Charsets.UTF_8.name()).replace("+", "%20");
	}
}
