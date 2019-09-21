
package com.videoweb.ying.controller;

import java.util.ArrayList;
import java.util.Date;
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

import com.baomidou.mybatisplus.plugins.Page;
import com.videoweb.base.BaseController;
import com.videoweb.utils.PropertiesUtil;
import com.videoweb.utils.redis.CacheUtil;
import com.videoweb.ying.po.TCareHistory;
import com.videoweb.ying.service.TCareHistoryService;


@RestController
@RequestMapping("/ying")
public class ApiViewCareHistoryController extends BaseController{

	@Autowired
	private TCareHistoryService tCareHistoryService;

	@PostMapping("/getMemberCareHistoryMore")
	public Object getMemberCareHistoryMore(ModelMap modelMap, HttpServletRequest request)
	{
		List<Map<String,Object>> viewList = new ArrayList<Map<String,Object>>();
		Map<String,Object> result = new HashMap<String,Object>();
		Integer memberId = getCurrUser(request);
		result.put("memberId", memberId);
		viewList = tCareHistoryService.selectCareHistory(result);
		if(viewList != null && viewList.size()>0)
		{
			for(Map<String,Object> mm:viewList)
			{
				if("1".equals(mm.get("videoCoverType").toString()))
				 {
					mm.put("videoCover",PropertiesUtil.getString("remote.file.uri.prefix")+mm.get("videoCover"));
				 }
			}

		}
		modelMap.put("retCode", "1");
	    modelMap.put("retMsg", "查询成功");
		return setSuccessResponse(modelMap,viewList);
	}

	/**
	 * 我的喜欢分页
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@PostMapping("/getMemberCareHistoryPage")
	public Object getMemberCareHistoryPage(ModelMap modelMap, HttpServletRequest request)
	{
		Map<String,Object> result = new HashMap<String,Object>();
		Integer memberId = getCurrUser(request);
		result.put("memberId", memberId);
		Page<?>  viewList = tCareHistoryService.selectCareHistoryPage(result);
		if(viewList.getRecords() != null && viewList.getRecords().size()>0)
		{
			for(Map<String,Object> mm:(List<Map<String,Object>>)viewList.getRecords())
			{
				if("1".equals(mm.get("videoCoverType").toString()))
				 {
					mm.put("videoCover",PropertiesUtil.getString("remote.file.uri.prefix")+mm.get("videoCover"));
				 }
			}
		}
		modelMap.put("retCode", "1");
	    modelMap.put("retMsg", "查询成功");
		return setSuccessResponse(modelMap,viewList);
	}

	@PostMapping("/setCareHistory")
	public Object setCareHistory(ModelMap modelMap, HttpServletRequest request,@RequestBody Map<String,Object> params)
	{

		if(params == null || "".equals(params))
		{
			modelMap.put("retCode", "-1");
		    modelMap.put("retMsg", "参数异常");
			return  setSuccessResponse(modelMap);
		}
		if(params.get("videoId") == null || "".equals(params.get("videoId")))
		{
			modelMap.put("retCode", "-1");
		    modelMap.put("retMsg", "参数异常");
			return  setSuccessResponse(modelMap);
		}
		Integer memberId = getCurrUser(request);
		TCareHistory careHistory = new TCareHistory();
		careHistory.setMemberId(memberId);
		careHistory.setVideoId(Integer.valueOf(params.get("videoId").toString()));
		careHistory = tCareHistoryService.selectOne(careHistory);
		if(careHistory == null || "".equals(careHistory))
		{
			careHistory = new TCareHistory();
			careHistory.setMemberId(memberId);
			careHistory.setVideoId(Integer.valueOf(params.get("videoId").toString()));
		}
		careHistory.setDianTime(new Date());
		careHistory = tCareHistoryService.update(careHistory);
		if(careHistory == null || "".equals(careHistory))
		{
			modelMap.put("retCode", "-1");
		    modelMap.put("retMsg", "数据更新失败");
			return setSuccessResponse(modelMap);
		}
		//删除原有缓存历史
		CacheUtil.getCache().del("careHistoryList_"+memberId);
		modelMap.put("retCode", "1");
	    modelMap.put("retMsg", "数据更新成功");
		return setSuccessResponse(modelMap);
	}



	@PostMapping("/deleteCareHistory")
	public Object deleteCareHistory(ModelMap modelMap, HttpServletRequest request,@RequestBody Map<String,Object> params)
	{
		if(params == null || "".equals(params))
		{
			modelMap.put("retCode", "-1");
		    modelMap.put("retMsg", "删除我的喜欢记录异常");
		    return setSuccessResponse(modelMap);
		}
		if(params.get("ids") == null || "".equals(params.get("ids")))
		{
			modelMap.put("retCode", "-1");
		    modelMap.put("retMsg", "删除我的喜欢记录异常");
		    return setSuccessResponse(modelMap);
		}
		List<Integer> idList = new ArrayList<Integer>();
	     String[] str = params.get("ids").toString().split(",");
         if (str != null && !"".equals(str) && str.length > 0) {
            for (String ss : str) {
                if (ss != null && !"".equals(ss)) {
                    // 获取选中的单个用户ID
                    Integer tt = Integer.valueOf(ss);
                    idList.add(tt);
                }
            }
         }
        if(idList != null &&  idList.size()>0)
        {
            //遍历ID删除对应数据
            for (int i = 0; i < idList.size(); i++) {
                Integer id=idList.get(i);
                tCareHistoryService.delete(id);
            }
            logger.info("删除我的喜欢记录结束");
         }
		modelMap.put("retCode", "1");
	    modelMap.put("retMsg", "删除我的喜欢记录成功");
		return setSuccessResponse(modelMap);
	}
}
