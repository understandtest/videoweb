package com.videoweb.ying.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.videoweb.base.BaseService;
import com.videoweb.ying.dao.MemberStarMapper;
import com.videoweb.ying.dao.TStarMapper;
import com.videoweb.ying.po.MemberStar;
import com.videoweb.ying.po.TStar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("tStarService")
public class TStarService extends BaseService<TStar> {

	@Autowired
	private MemberStarMapper memberStarMapper;

	public Page<Map<String, Object>> selectListPage(Map<String, Object> paramMap){
        Page<Map<String, Object>> page = getPageMap(paramMap);

		List<Map<String, Object>> starList = ((TStarMapper) mapper).selectListPage(page, paramMap);

		// 取出用户id
		Integer memberId= (Integer)paramMap.get("memberId");

		// 处理用户收藏数据
		if(null != memberId){
			handleMemberCollect(memberId,starList);
		}


		page.setRecords(starList);
        return page;
	}

	/**
	 * 处理用户收藏数据
	 * @param memberId
	 * @param starList
	 */
	private void handleMemberCollect(Integer memberId, List<Map<String, Object>> starList) {

		// 查询与该用户关联的收藏数据
		List<MemberStar> memberStarList = memberStarMapper.findByMemberId(memberId);

		for (Map<String, Object> map : starList) {

			Integer starId = (Integer)map.get("id");
			// 查询当前明星是否被用户收藏
			long count = memberStarList.stream().filter(ms -> ms.getStarId().equals(starId)).count();

			String isCollect = count > 0? "1":"0";
			map.put("isCollect",isCollect);
		}

	}

	public List<Map<String,Object>> getStarList()
	{
		return ((TStarMapper)mapper).getStarList();
	}
	/**
	 * 获取明星信息
	 * @param params
	 * @return
	 */
	public Map<String,Object> getStarInfoByVideo(Map<String,Object> params)
	{
		return ((TStarMapper)mapper).getStarInfoByVideo(params);
	}



}