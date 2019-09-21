package com.videoweb.ying.dao;

import com.videoweb.base.BaseMapper;
import com.videoweb.ying.po.MemberStar;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author hong
 * @Date 19-9-10
 */
public interface MemberStarMapper extends BaseMapper<MemberStar> {
    /**
     * 根据用户id查询对应的数据
     *
     * @param memberId
     * @return
     */
    List<MemberStar> findByMemberId(@Param("memberId") Integer memberId);

    List<Map<String, Object>> getMemberCollect(@Param("memberId") Integer memberId);

    Integer delByMemberIdAndMsId(@Param("msId") Integer msId, @Param("memberId") Integer memberId);
}
