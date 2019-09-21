
package com.videoweb.ying.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.minidev.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.videoweb.base.BaseController;
import com.videoweb.utils.HttpCode;
import com.videoweb.utils.Jwt;
import com.videoweb.ying.service.TExtensionHistoryService;


@RestController
@RequestMapping("/ying")
public class ApiExtensionController extends BaseController{

	@Autowired
	private TExtensionHistoryService tExtensionHistoryService;
	
	
	@PostMapping("/getExtensionHistory")
	public Object getExtensionHistory(ModelMap modelMap, HttpServletRequest request)
	{
		List<Map<String,Object>> viewList = new ArrayList<Map<String,Object>>();
		Map<String,Object> result = new HashMap<String,Object>();
		String token = request.getHeader("token");
		if(token  == null || "".equals(token))
        {
			return  setSuccessResponse(modelMap,HttpCode.LOSTTOKEN);
        }
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap= Jwt.validToken(token);
		JSONObject obj= (JSONObject) resultMap.get("data");
		Integer memberId = Integer.valueOf(obj.get("uid").toString());
		result.put("memberId", memberId);
		viewList = tExtensionHistoryService.getExtensionHistory(result);
		modelMap.put("retCode", "1");
	    modelMap.put("retMsg", "查询成功");
		return setSuccessResponse(modelMap,viewList);
	}

}
