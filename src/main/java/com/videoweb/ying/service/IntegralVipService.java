package com.videoweb.ying.service;

import com.videoweb.base.BaseService;
import com.videoweb.utils.PropertiesUtil;
import com.videoweb.ying.dao.IntegralVipHistoryMapper;
import com.videoweb.ying.dao.IntegralVipMapper;
import com.videoweb.ying.dao.TMemberMapper;
import com.videoweb.ying.po.IntegralVip;
import com.videoweb.ying.po.IntegralVipHistory;
import com.videoweb.ying.po.TMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author hong
 * @Date 19-8-26
 */
@Service
public class IntegralVipService extends BaseService<IntegralVip> {

    @Autowired
    private TMemberMapper memberMapper;

    @Autowired
    private IntegralVipHistoryMapper integralVipHistoryMapper;

    public IntegralVip findOne(Integer id){
        return mapper.selectById(id);
    }

    /**
     * 积分兑换会员
     * @param member
     * @param integralVipId
     * @return
     */
    @Transactional
    public boolean exchangeVip(TMember member, Integer integralVipId) {

        //查询当前兑换需要多少积分
        IntegralVip integralVip = mapper.selectById(integralVipId);

        Integer integralNumber = integralVip.getIntegralNumber();

        //查看当前会员积分是否大于需要兑换的会员积分
        Integer memberIntegralNumber = member.getIntegralNumber();

        //积分不足兑换失败
        if(integralNumber > memberIntegralNumber){
            return false;
        }

        //兑换,增加会员天数
        Calendar ca = Calendar.getInstance();
        //防止空指针
        if (null == member.getVipDate()) {
            member.setVipDate(new Date());
        }

        ca.setTime(member.getVipDate());
        //根据购买类型id增加天数
        ca.add(Calendar.DATE, integralVip.getDayNumber());
        Date cc = ca.getTime();
            member.setVipDate(cc);
        member.setIsVip(1);
        //扣除积分
        member.setIntegralNumber(memberIntegralNumber - integralNumber);

        //保存到数据库
        memberMapper.updateById(member);


        //添加兑换记录
        addIntegralVipHistory(member.getId(),integralVipId);

        return true;

    }

    /**
     * 添加兑换记录
     * @param memberId
     * @param integralVipId
     */
    private void addIntegralVipHistory(Integer memberId, Integer integralVipId) {

        IntegralVipHistory integralVipHistory = new IntegralVipHistory();
        integralVipHistory.setIntegralVip(integralVipId);
        integralVipHistory.setMemberId(memberId);
        integralVipHistory.setUpdateTime(new Date());
        integralVipHistory.setCreateTime(new Date());

        integralVipHistoryMapper.insert(integralVipHistory);

    }


    /**
     * 查询所有的会员积分兑换规则
     * @return
     */
    public List<Map<String,Object>> findAll(){

        List<Map<String,Object>> integralVipList = ((IntegralVipMapper)mapper).findAll();

        //处理图片
        for (Map<String, Object> integralVipMap : integralVipList) {

            //处理图片
            if ("1".equals(integralVipMap.get("iconType"))) {
                integralVipMap.put("icon", PropertiesUtil.getString("remote.file.uri.prefix") + integralVipMap.get("icon"));
            }
            integralVipMap.remove("iconType");

        }

        return integralVipList;
    }
}
