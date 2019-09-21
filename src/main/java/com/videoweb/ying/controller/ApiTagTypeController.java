package com.videoweb.ying.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.videoweb.ying.po.TTagType;
import com.videoweb.ying.service.TTagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.videoweb.base.BaseController;
import com.videoweb.ying.service.TTagTypeService;

@RestController
@RequestMapping("/openapi")
public class ApiTagTypeController extends BaseController{

	@Autowired
	private TTagTypeService tTagTypeService;

	@Autowired
	private TTagsService tagsService;
	
	 /**
	 * 获取标签类型
	 * @param modelMap
	 * @param request
	 * @return
	 */
	 @PostMapping("/selectTagType")
	 public Object selectTagType(ModelMap modelMap, HttpServletRequest request) {
		 List<Map<String,Object>> tagTypeList = new ArrayList<Map<String,Object>>();
		 List<Map<String,Object>> tagTypeList2 = new ArrayList<Map<String,Object>>();
		 Map<String,Object> mpp = new HashMap<String, Object>();
		 mpp.put("name", "全部");
		 mpp.put("id", -1);
		 tagTypeList2.add(mpp);
		 tagTypeList = tTagTypeService.selectTagType();
		if(tagTypeList != null && tagTypeList.size()>0)
		 {
			tagTypeList2.addAll(tagTypeList);
//			 for(Map<String,Object> mm:tagTypeList)
//			 {
//				 mm.put("picUrl", root+mm.get("picUrl"));
//			 }
			 modelMap.put("retCode", "1");
		     modelMap.put("retMsg", "查询成功");
		     return  setSuccessResponse(modelMap,tagTypeList2);
		 }
		 modelMap.put("retCode", "-1");
	     modelMap.put("retMsg", "未查询到结果");
		 return  setSuccessResponse(modelMap);
	 }



	/**
	 * 获取标签分类
	 * @param modelMap
	 * @return
	 */
	@PostMapping("getTagTypes")
	public Object getTagTypes(ModelMap modelMap){

		List<Map<String,Object>> tagTypeMaps= tTagTypeService.getTagTypes();

		for (Map<String, Object> tagTypeMap : tagTypeMaps) {

			Integer id = (Integer) tagTypeMap.get("id");

			//根据标签id查询所属分类
			List<Map<String,Object>> tagMaps = tagsService.getTags(id);

			tagTypeMap.put("tagMaps",tagMaps);

		}

		modelMap.put("retCode", "1");
		modelMap.put("retMsg", "查询成功");
		return setSuccessResponse(modelMap,tagTypeMaps);
	}

}
