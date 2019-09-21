
package com.videoweb.ying.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.videoweb.base.BaseController;
import com.videoweb.ying.po.TNotice;
import com.videoweb.ying.service.TNoticeService;


@RestController
@RequestMapping("/openapi")
public class ApiNoticeController extends BaseController{

	@Autowired
	private TNoticeService tNoticeService;
	
	
	@PostMapping("/getNoticeList")
	public Object getNoticeList(ModelMap modelMap)
	{
		List<Map<String, Object>> list = tNoticeService.selectListPage();
		modelMap.put("retCode", "1");
		 modelMap.put("retMsg", "查询成功");
		 return  setSuccessResponse(modelMap,list);
	}
	
	@PostMapping("/getNoticeDetail")
	public Object getNoticeDetail(ModelMap modelMap, HttpServletRequest request,@RequestBody Map<String,Object> params)
	{
		if(params == null || "".equals(params))
		{
			 modelMap.put("retCode", "-1");
			 modelMap.put("retMsg", "查询失败，参数异常");
			 return  setSuccessResponse(modelMap);
		}
		if(params.get("id") == null || "".equals(params.get("id")))
		{
			 modelMap.put("retCode", "-1");
			 modelMap.put("retMsg", "查询失败，参数异常");
			 return  setSuccessResponse(modelMap);
		}
		Integer id = Integer.valueOf(params.get("id").toString());
		TNotice tn = new TNotice();
		tn = tNoticeService.queryById(id);
		modelMap.put("retCode", "1");
		modelMap.put("retMsg", "查询成功");
		return  setSuccessResponse(modelMap,tn);
	}

}
