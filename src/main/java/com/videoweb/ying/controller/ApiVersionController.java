
package com.videoweb.ying.controller;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.videoweb.ying.po.TProtocol;
import com.videoweb.ying.service.TProtocolService;
import com.videoweb.ying.service.TVersionService;

@RestController
@RequestMapping("/openapi")
public class ApiVersionController extends BaseController{

	@Autowired
	private TVersionService tVersionService;
	
	@Autowired
	private TProtocolService tProtocolService;
	
	@PostMapping("/getVersionNew")
	public Object getVersionNew(ModelMap modelMap, HttpServletRequest request,@RequestBody Map<String, Object> params)
	{
		
		List<Map<String,Object>> versionList = new ArrayList<Map<String,Object>>();
		versionList = tVersionService.getNewVersion(params);
		Map<String,Object> versionMap = new HashMap<String,Object>();
		if(versionList != null && !"".equals(versionList) && versionList.size()>0)
		{
			
			versionMap = versionList.get(0);
			versionMap.put("isNew", "0"); //0---当前版本不是当前最新
			if(params.get("version") != null && !"".equals(params.get("version")))
			{
				//获取当前版本号，验证当前版本是否强制更新
				 if(params.get("version").equals(versionMap.get("versionCode")))
				 {
					 versionMap.put("isNew", "1"); //1----当前版本是当前最新
				 }
			 }
		}
		else
		{
			versionMap.put("isNew", "1"); //0---当前版本是当前最新
		}
		modelMap.put("versionInfo", versionMap);
		modelMap.put("retCode", "1");
	    modelMap.put("retMsg", "获取版本号成功!");
	    return setSuccessResponse(modelMap);
	}

	
	@PostMapping("/getProtocol")
	public Object getProtocol(ModelMap modelMap, HttpServletRequest request)
	{
		Map<String,Object> params = new HashMap<String,Object>();
		List<TProtocol> versionList = new ArrayList<TProtocol>();
		versionList = tProtocolService.queryList(params);
		if(versionList != null && !"".equals(versionList) && versionList.size()>0)
		{
			TProtocol versionMap = new TProtocol();
			versionMap = versionList.get(0);
			modelMap.put("protocolText", versionMap.getProtocolText());
		}
		modelMap.put("retCode", "1");
	    modelMap.put("retMsg", "获取版本号成功!");
	    return setSuccessResponse(modelMap);
	}
}
