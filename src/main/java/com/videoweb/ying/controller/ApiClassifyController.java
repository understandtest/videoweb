package com.videoweb.ying.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.videoweb.base.BaseController;
import com.videoweb.utils.PropertiesUtil;
import com.videoweb.ying.service.TClassifyService;

@RestController
@RequestMapping("/openapi")
public class ApiClassifyController extends BaseController{

	@Autowired
	private TClassifyService tClassifyService;
	
	/**
	 * 查看分类
	 * @param modelMap
	 * @param request
	 * @param params
	 * @return
	 */
	@PostMapping("/selectClassify")
	public Object selectClassify(ModelMap modelMap, HttpServletRequest request)
	{
		
		List<Map<String,Object>> classifyList = new ArrayList<Map<String,Object>>();
		classifyList = tClassifyService.selectClassify();
		if(classifyList != null && !"".equals(classifyList) && classifyList.size()>0)
		{
			modelMap.put("retCode", "1");
			for(Map<String,Object> mm:classifyList)
			{
				if("1".equals(mm.get("iconType").toString()))
				{
					mm.put("classifyIcon", PropertiesUtil.getString("remote.file.uri.prefix")+mm.get("classifyIcon"));
				}
			}
		    modelMap.put("retMsg", "查询成功");
		    return  setSuccessResponse(modelMap,classifyList);
		}
		modelMap.put("retCode", "-1");
	    modelMap.put("retMsg", "未查询到结果");
		return  setSuccessResponse(modelMap);
	}
}
