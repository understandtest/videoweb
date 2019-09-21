package com.videoweb.ying.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.videoweb.base.BaseController;
import com.videoweb.utils.PropertiesUtil;
import com.videoweb.ying.service.TTagsService;

@RestController
@RequestMapping("/openapi")
public class ApiTagsController extends BaseController{

	@Autowired
	private TTagsService tTagsService;
	
	/**
	 * 根据标签类型获取标签
	 * @param modelMap
	 * @param request
	 * @param params
	 * @return
	 */
	@PostMapping("/selectTagsByType")
	public Object selectTagsByType(ModelMap modelMap, HttpServletRequest request,@RequestBody Map<String,Object> params)
	{
		
		if(params == null || "".equals(params))
		{
			 modelMap.put("retCode", "-1");
		     modelMap.put("retMsg", "请求参数异常");
		     return  setSuccessResponse(modelMap);
		}
		if(params.get("id") == null || "".equals(params.get("id")))
		{
			 modelMap.put("retCode", "-1");
		     modelMap.put("retMsg", "请求参数异常");
		     return  setSuccessResponse(modelMap);
		}
		List<Map<String,Object>> tagsList = new ArrayList<Map<String,Object>>();
		tagsList = tTagsService.selectTagsByType(params);
		if(tagsList != null && !"".equals(tagsList) && tagsList.size()>0)
		{
			 for(Map<String,Object> mm:tagsList)
			 {
				 if("1".equals(mm.get("picType").toString()))
				 {
					 mm.put("picUrl", PropertiesUtil.getString("remote.file.uri.prefix")+mm.get("picUrl"));
				 }
			 }
			modelMap.put("retCode", "1");
		    modelMap.put("retMsg", "查询成功");
		    return  setSuccessResponse(modelMap,tagsList);
		}
		modelMap.put("retCode", "-1");
	    modelMap.put("retMsg", "未查询到结果");
		return  setSuccessResponse(modelMap);
	}


}
