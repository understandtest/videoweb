
package com.videoweb.ying.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.videoweb.base.BaseController;
import com.videoweb.ying.po.TSuggestion;
import com.videoweb.ying.service.TSuggestionService;


@RestController
@RequestMapping("/ying")
public class ApiSuggestionController extends BaseController{

	@Autowired
	private TSuggestionService tSuggestionService;
	
	@PostMapping("/saveSuggest")
	public Object saveSuggest(ModelMap modelMap, HttpServletRequest request,@RequestBody TSuggestion tSuggestion)
	{
		Integer memberId = getCurrUser(request);
		if(tSuggestion.getSuggestionContent() ==null ||"".equals(tSuggestion.getSuggestionContent()))
		{
			modelMap.put("retCode", "-1");
		    modelMap.put("retMsg", "提交建议失败");
			return  setSuccessResponse(modelMap);
		}
		tSuggestion.setMemberId(memberId);
		tSuggestion =tSuggestionService.update(tSuggestion);
		if(tSuggestion ==null ||"".equals(tSuggestion))
		{
			modelMap.put("retCode", "-1");
		    modelMap.put("retMsg", "提交建议失败");
			return  setSuccessResponse(modelMap);
		}
		modelMap.put("retCode", "1");
	    modelMap.put("retMsg", "提交成功");
		return  setSuccessResponse(modelMap);
	}

}
