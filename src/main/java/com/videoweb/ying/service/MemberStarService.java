package com.videoweb.ying.service;

import com.videoweb.base.BaseService;
import com.videoweb.utils.PropertiesUtil;
import com.videoweb.ying.dao.MemberStarMapper;
import com.videoweb.ying.po.MemberStar;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author hong
 * @Date 19-9-10
 */
@Service
public class MemberStarService extends BaseService<MemberStar> {


    /**
     * 查询用户收藏的明星数据
     * @param memberId
     * @return
     */
    public List<Map<String, Object>> getMemberCollect(Integer memberId) {

        List<Map<String, Object>> memberCollectList = ((MemberStarMapper) mapper).getMemberCollect(memberId);

        //处理图片
        for (Map<String, Object> map : memberCollectList) {
            if ("1".equals(map.get("picType"))) {
                map.put("headpic", PropertiesUtil.getString("remote.file.uri.prefix") + map.get("headpic"));
            }

            map.remove("picType");
        }

        return memberCollectList;
    }

    /**
     * 添加收藏
     * @param memberStar
     */
    public void addCooect(MemberStar memberStar) {
        mapper.insert(memberStar);
    }

    /**
     * 删除用户收藏
     * @param memberStarIdList
     * @param memberId 用户id
     */
    public void delMemberCollect(List<Integer> memberStarIdList, Integer memberId) {

        for (Integer msId : memberStarIdList) {
            ((MemberStarMapper)mapper).delByMemberIdAndMsId(msId,memberId);
        }

    }
}
