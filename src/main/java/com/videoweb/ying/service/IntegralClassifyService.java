package com.videoweb.ying.service;

import com.videoweb.base.BaseService;
import com.videoweb.utils.PropertiesUtil;
import com.videoweb.ying.dao.IntegralClassifyMapper;
import com.videoweb.ying.dao.IntegralHistoryMapper;
import com.videoweb.ying.dao.TMemberMapper;
import com.videoweb.ying.po.IntegralClassify;
import com.videoweb.ying.po.IntegralHistory;
import com.videoweb.ying.po.TMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author hong
 * @Date 19-8-26
 */
@Service
public class IntegralClassifyService extends BaseService<IntegralClassify> {


    @Autowired
    private IntegralHistoryMapper integralHistoryMapper;

    @Autowired
    private TMemberMapper memberMapper;


    /**
     * 用户添加积分
     *
     * @param memberId
     * @param integralId
     */
    @Transactional
    public boolean addIntegral(Integer memberId, Integer integralId) {

        //查询用户是否已获取过积分
        boolean userIsGetIntegral = findUserIsGetIntegral(memberId, integralId, true);

        //用户已经获取过，停止添加积分操作，防止用户重复刷分
        if (userIsGetIntegral) {
            return false;
        }

        //查询当前积分分类积分数量
        IntegralClassify integralClassify = mapper.selectById(integralId);

        //查询用户添加积分
        TMember member = memberMapper.selectById(memberId);
        Integer integralNumber = member.getIntegralNumber() + integralClassify.getNumber();
        member.setIntegralNumber(integralNumber);
        //更新到数据库
        memberMapper.updateById(member);

        //添加历史记录
        addIntegralHistory(memberId, integralId);


        return true;
    }

    /**
     * 添加积分获取历史记录数据
     *
     * @param memberId
     * @param integralId
     */
    private void addIntegralHistory(Integer memberId, Integer integralId) {

        IntegralHistory integralHistory = new IntegralHistory();
        integralHistory.setIntegralClassifyId(integralId);
        integralHistory.setMemberId(memberId);
        integralHistory.setCreateTime(new Date());
        integralHistory.setUpdateTime(new Date());

        integralHistoryMapper.insert(integralHistory);

    }

    /**
     * 查询用户是否获取过积分,防止重复操作
     *
     * @param memberId
     * @param integralId
     * @param isAddThatVeryDayDate 增加当天时间过滤
     * @return
     */
    private boolean findUserIsGetIntegral(Integer memberId, Integer integralId, boolean isAddThatVeryDayDate) {

        //查询参数
        Map<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);
        params.put("integralId", integralId);

        if (isAddThatVeryDayDate && integralId != 7) {  // 这里的7是分享群类别
            params.put("addThatVeryDayDate", "1");
        }


        Integer count = integralHistoryMapper.countByMemberIdAndIntegralId(params);

        return count > 0;
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public IntegralClassify findOne(Integer id) {
        return mapper.selectById(id);
    }

    /**
     * 查询所有的积分获取类别，并查看当前用户是否完成
     *
     * @param memberId
     * @return
     */
    public List<Map<String, Object>> findAllAndLookIsAccomplishHistory(Integer memberId) {

        List<Map<String, Object>> integralClassifyList = ((IntegralClassifyMapper) mapper).findAll();

        for (Map<String, Object> integralClassMap : integralClassifyList) {

            //处理图片
            if ("1".equals(integralClassMap.get("iconType"))) {
                integralClassMap.put("icon", PropertiesUtil.getString("remote.file.uri.prefix") + integralClassMap.get("icon"));
            }
            integralClassMap.remove("iconType");

            Integer integralId = (Integer) integralClassMap.get("id");

            //获取类别
            boolean isAddThatVeryDayDate = integralId == 1 || integralId == 2 || integralId == 3;

            boolean userIsGetIntegral = findUserIsGetIntegral(memberId, integralId, isAddThatVeryDayDate);

            integralClassMap.put("isAccomplish", userIsGetIntegral ? 1 : 0);
        }


        return integralClassifyList;

    }
}
