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
import com.videoweb.ying.service.TQqService;

@RestController
@RequestMapping("/openapi")
public class ApiQQController extends BaseController{

	@Autowired
	private TQqService tQqService;
	
	/**
	 * 获取QQ链接
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@PostMapping("/getQqInfo")
	public Object getQqInfo(ModelMap modelMap, HttpServletRequest request)
	{
		 List<Map<String,Object>> tQ = new ArrayList<Map<String,Object>>();
		 tQ = tQqService.selectQQurl();
		 if(tQ != null && !"".equals(tQ)&&tQ.size()>0)
		 {
			 Map<String,Object> tf = new HashMap<String,Object>();
			 tf = tQ.get(0);
			 modelMap.put("qq", tf.get("name"));
		 }
		 modelMap.put("retCode", "1");
		 modelMap.put("retMsg", "查询成功");
		 return  setSuccessResponse(modelMap);
	}
}
