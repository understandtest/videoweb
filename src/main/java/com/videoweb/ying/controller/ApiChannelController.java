package com.videoweb.ying.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import com.videoweb.ying.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.videoweb.base.BaseController;
import com.videoweb.utils.PropertiesUtil;

@RestController
@RequestMapping("/openapi")
public class ApiChannelController extends BaseController{
	
	
	@Autowired
	private TBannerService tBannerService;
	
	@Autowired
	private TVideoTagsService tVideoTagsService;
	
	@Autowired
	private TTagsService tTagsService;

	@Autowired
	private TStarService tStarService;

	@Autowired
	private TVideoService tVideoService;
	
	
	@PostMapping("/channelInfo")
	public Object channelInfo(ModelMap modelMap, HttpServletRequest request)
	{
		//轮播图
		Map<String,Object> result = new HashMap<String,Object>();
		Map<String,Object> pp = new HashMap<String,Object>();
		pp.put("picType", "2");
		List<Map<String,Object>> bannerList = new ArrayList<Map<String,Object>>();
		bannerList = tBannerService.getBannerList(pp);
		result.put("bannerList", bannerList);
		if(bannerList != null && bannerList.size()>0)
		{
			for(Map<String,Object> mm:bannerList)
			{
				if("1".equals(mm.get("picFrom").toString()))
				{
					mm.put("picUrl", PropertiesUtil.getString("remote.file.uri.prefix")+mm.get("picUrl"));
				}
			}
		}
		
		//热门标签 8个
		List<Map<String,Object>> hotTagList = new ArrayList<Map<String,Object>>();
		hotTagList = tVideoTagsService.getHotTags();
		if(hotTagList != null && hotTagList.size()>0)
		{
			for(Map<String,Object> pk : hotTagList)
			{
				if("1".equals(pk.get("picType").toString()))
				{
					pk.put("picUrl", PropertiesUtil.getString("remote.file.uri.prefix")+pk.get("picUrl"));
				}
				
			}
		}
		result.put("hotTagList", hotTagList);
		//猜你喜欢的标签 4个
		List<Map<String,Object>> careTagList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> careTagList2 = new ArrayList<Map<String,Object>>();
		careTagList = tTagsService.selectTagsByRandom();
		if(careTagList != null && careTagList.size()>0)
		{
			careTagList2 = getRandom(careTagList);
			for(Map<String,Object> pk : careTagList2)
			{
				if("1".equals(pk.get("picType").toString()))
				{
					pk.put("picUrl", PropertiesUtil.getString("remote.file.uri.prefix")+pk.get("picUrl"));
				}
				
			}
		}
		result.put("careTagList", careTagList2);


		//获取人气明星
		List<Map<String,Object>> starList  = new ArrayList<Map<String,Object>>();
		starList = tStarService.getStarList();
		for(Map<String,Object> mm:starList)
		{
			if("1".equals(mm.get("picType").toString()))
			{
				mm.put("headpic", PropertiesUtil.getString("remote.file.uri.prefix")+mm.get("headpic"));
			}

			//根据明星id查询对应的视频信息
			Map<String,Object> params = new HashMap<>();
			params.put("starId",mm.get("id"));
			//查询对应的视频
			List<Map<String, Object>> videos = tVideoService.getStarVideoByStarId(params);
			//关联到人气明星map中
			mm.put("videos",videos);
		}

		result.put("starList", starList);


		modelMap.put("retCode", "1");
	    modelMap.put("retMsg", "获取数据成功");
		return  setSuccessResponse(modelMap,result);
	}
	
	private List<Map<String,Object>> getRandom(List<Map<String,Object>> careTagList)
	{
		List<Map<String,Object>> careTagList2 = new ArrayList<Map<String,Object>>();
		for(int i=0;i<4;i++)
		{
			Random random = new Random();
			int n = random.nextInt(careTagList.size());
			careTagList2.add(careTagList.get(n));
			careTagList.remove(n);
		}
		return careTagList2;
	}
}
