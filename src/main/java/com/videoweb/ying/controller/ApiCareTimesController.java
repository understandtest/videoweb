package com.videoweb.ying.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.videoweb.base.BaseController;
import com.videoweb.ying.po.TCareTimes;
import com.videoweb.ying.service.TCareTimesService;

@RestController
@RequestMapping("/ying")
public class ApiCareTimesController extends BaseController{

	@Autowired
	private TCareTimesService tCareTimesService;
	
	@PostMapping("/setCareTimes")
	public Object setCareTimes(ModelMap modelMap, HttpServletRequest request,@RequestBody Map<String,Object> params)
	{
		if(params == null || "".equals(params))
		{
			modelMap.put("retCode", "-1");
		    modelMap.put("retMsg", "参数异常");
			return  setSuccessResponse(modelMap);
		}
		if(params.get("videoId") == null || "".equals(params.get("videoId"))
				||params.get("careType") == null || "".equals(params.get("careType")))
		{
			modelMap.put("retCode", "-1");
		    modelMap.put("retMsg", "参数异常");
			return  setSuccessResponse(modelMap);
		}
		Integer memberId = getCurrUser(request);
		TCareTimes times = new TCareTimes();
		times.setMemberId(memberId);
		times.setCareType(Integer.valueOf(params.get("careType").toString()));
		times.setVideoId(Integer.valueOf(params.get("videoId").toString()));
		times = tCareTimesService.update(times);
		if(times == null || "".equals(times))
		{
			modelMap.put("retCode", "-1");
		    modelMap.put("retMsg", "数据更新失败");
			return  setSuccessResponse(modelMap);
		}
		modelMap.put("retCode", "1");
	    modelMap.put("retMsg", "数据更新成功");
		return  setSuccessResponse(modelMap);
	}
	
}
