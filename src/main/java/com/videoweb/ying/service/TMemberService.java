package com.videoweb.ying.service;

import com.videoweb.base.BaseService;
import com.videoweb.ying.dao.TMemberMapper;
import com.videoweb.ying.po.TMember;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("tMemberService")
public class TMemberService extends BaseService<TMember> {

    public Map<String, Object> queryMemberInfo(Integer memberId) {
        return ((TMemberMapper) mapper).queryMemberInfo(memberId);
    }

    public void updateById(TMember member){
        mapper.updateById(member);
    }

    /**
     * 清理各种播放次数
     */
    public void clearNumber() {
        ((TMemberMapper) mapper).clearNumber();
    }


    /**
     * 根据id查询
     * @param id
     * @return
     */
    public TMember findOne(Integer id){
        return mapper.selectById(id);
    }
}