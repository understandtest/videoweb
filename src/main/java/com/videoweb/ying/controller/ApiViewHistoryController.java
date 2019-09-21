
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
import com.videoweb.utils.PropertiesUtil;
import com.videoweb.ying.service.TViewsHistoryService;


@RestController
@RequestMapping("/ying")
public class ApiViewHistoryController  extends BaseController{


	@Autowired
	private TViewsHistoryService tViewsHistoryService;
	
	@PostMapping("/getMemberViewHistoryMore")
	public Object getMemberViewHistoryMore(ModelMap modelMap, HttpServletRequest request)
	{
		List<Map<String,Object>> viewList = new ArrayList<Map<String,Object>>();
		Map<String,Object> result = new HashMap<String,Object>();
		Integer memberId = getCurrUser(request);
		result.put("memberId", memberId);
		viewList = tViewsHistoryService.selectViewHistory(result);
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
	 * 删除我的访问记录
	 * @param modelMap
	 * @param request
	 * @param params
	 * @return
	 */
	@PostMapping("/deleteViewHistory")
	public Object deleteViewHistory(ModelMap modelMap, HttpServletRequest request,@RequestBody Map<String,Object> params)
	{
		if(params == null || "".equals(params))
		{
			modelMap.put("retCode", "-1");
		    modelMap.put("retMsg", "删除访问记录异常");
		    return setSuccessResponse(modelMap);
		}
		if(params.get("ids") == null || "".equals(params.get("ids")))
		{
			modelMap.put("retCode", "-1");
		    modelMap.put("retMsg", "删除访问记录异常");
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
                tViewsHistoryService.delete(id);
            }
            logger.info("删除访问记录结束");
         }
		modelMap.put("retCode", "1");
	    modelMap.put("retMsg", "删除访问记录成功");
		return setSuccessResponse(modelMap);
	}

}
