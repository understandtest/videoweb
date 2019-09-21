package com.videoweb.ying.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.videoweb.base.BaseController;
import com.videoweb.ying.service.TExtendPriceService;

@RestController
@RequestMapping("/ying")
public class ApiExtendPriceController extends BaseController{

	@Autowired
	private TExtendPriceService tExtendPriceService;
	
	
	@PostMapping("/myincome")
	public Object myincome(ModelMap modelMap, HttpServletRequest request)
	{
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		Map<String,Object> params  = new HashMap<String,Object>();
		Integer memberId = getCurrUser(request);
		params.put("extendId", memberId);
		result = tExtendPriceService.getCronHistory(params);
		int toIncome=0;
		if(result != null && !"".equals(result))
		{
			for(Map<String,Object> tf :result)
			{
				int tk = Integer.valueOf(tf.get("income").toString()); 
				toIncome+=tk;
			}
		}
		modelMap.put("toIncome", toIncome);
		modelMap.put("retCode", "1");
		modelMap.put("retMsg", "获取信息成功");
		return  setSuccessResponse(modelMap,result);
	}
}
