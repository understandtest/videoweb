package com.videoweb.ying.task;

import com.videoweb.utils.redis.CacheUtil;
import com.videoweb.ying.po.TDate;
import com.videoweb.ying.po.TMember;
import com.videoweb.ying.service.TDateService;
import com.videoweb.ying.service.TMemberService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @Author lbh
 * @Date 2019-07-16
 */
@Component
public class ClearMemberNumTask {

    protected final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    private TMemberService memberService;

    @Autowired
    private TDateService dateService;

    /**
     * 清空播放次数和缓存次数
     * @throws IOException
     *
     */
    @Scheduled(cron = "0 0 0 */1 * ?") //每天凌晨12点执行任务
    @Transactional
    public void clearCacheNumAndUsedViewNum() {
        logger.info("执行定时清理播放次数任务");
        memberService.clearNumber();
        //清空缓存中播放记录
        CacheUtil.getCache().delAll("USER_VIDEO_PLAY*");


        addDate();

    }

    /**
     * 添加日期
     */
    private void addDate() {
        logger.info("添加日期~~~~~~~~~~~~~~~~~~~~~~~~");
        TDate date = new TDate();

        date.setDate(new Date());
        date.setUpdateTime(new Date());
        date.setCreateTime(new Date());

        dateService.add(date);
    }


}
