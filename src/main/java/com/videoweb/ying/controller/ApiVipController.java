
package com.videoweb.ying.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.videoweb.base.BaseController;
import com.videoweb.ying.po.TMember;
import com.videoweb.ying.po.TVip;
import com.videoweb.ying.service.TMemberService;
import com.videoweb.ying.service.TVipService;


@RestController
@RequestMapping("/ying")
public class ApiVipController extends BaseController{

	@Autowired
	private TVipService tVipService;

	@Autowired
	private TMemberService tMemberService;

	@PostMapping("/getVipList")
	public Object getVipList(ModelMap modelMap, HttpServletRequest request)
	{
		List<Map<String,Object>> viewList = new ArrayList<Map<String,Object>>();
		viewList = tVipService.getVipList();
		/*if(viewList != null && viewList.size()>0)
		{
			for(Map<String,Object> pm:viewList)
			{
				String days="0";
				if("1".equals(pm.get("cardType").toString()))
				{
					days = "30";
				}
				else if("2".equals(pm.get("cardType").toString()))
				{
					days = "90";
				}
				else
				{
					days = "365";
				}
				pm.put("describe", days+"天每日观影次数无限，高清专属资源，缓存次数"+pm.get("cacheNum"));
			}
		}*/

		//获取当前人的
		/*Integer memberId= getCurrUser(request);
		TMember mem = new TMember();
		mem.setId(memberId);
		mem= tMemberService.selectOne(mem);
		if(mem != null && !"".equals(mem))
		{
			if(mem.getIsVip() == 1)
			{
				//获取VIP信息
				String cardTypeName="";
				TVip vip = new TVip();
				vip.setId(mem.getVipId());
				vip = tVipService.selectOne(vip);
				if(vip != null && !"".equals(vip))
				{
					switch (vip.getCardType().intValue()){
						case 1: {
							cardTypeName="天卡";
							break;
						}
						case 2: {
							cardTypeName="月卡";
							break;
						}
						case 3: {
							cardTypeName="半年卡";
							break;
						}
						case 4: {
							cardTypeName="年卡";
							break;
						}
						case 5: {
							cardTypeName="永久卡";
							break;
						}
					}
				}
				//到期日期
				SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
				String dateFormat = format.format(mem.getVipDate());
				//todo vip到期
				String memberVipInfo="您的VIP【"+cardTypeName+"】会员到期时间为"+dateFormat;
				modelMap.put("memberVipInfo", memberVipInfo);
			}
			else
			{
				modelMap.put("memberVipInfo", "");
			}
		}
		else
		{
			modelMap.put("memberVipInfo", "");
		}*/
		modelMap.put("retCode", "1");

	    modelMap.put("retMsg", "查询成功");
		return setSuccessResponse(modelMap,viewList);
	}

}
