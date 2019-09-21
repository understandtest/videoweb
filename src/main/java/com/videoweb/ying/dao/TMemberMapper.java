package com.videoweb.ying.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.videoweb.ying.po.TMember;
import com.videoweb.base.BaseMapper;


public interface TMemberMapper extends BaseMapper<TMember> {

	public Map<String,Object> queryMemberInfo(@Param("memberId") Integer memberId);

    void clearNumber();

}