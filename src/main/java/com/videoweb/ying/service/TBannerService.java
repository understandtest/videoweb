package com.videoweb.ying.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.videoweb.base.BaseService;
import com.videoweb.ying.dao.TBannerMapper;
import com.videoweb.ying.po.TBanner;


@Service("tBannerService")
public class TBannerService extends BaseService<TBanner> {

    public List<Map<String, Object>> getBannerList(Map<String, Object> params) {
        return ((TBannerMapper) mapper).getBannerList(params);
    }


    //@Scheduled(cron = "0 0 2 */3 * ?") //一分钟执行一次
    public void test() throws IOException {
        System.out.println("测试定时器~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        //调用第三方软件
        ProcessBuilder processBuilder = new ProcessBuilder();
        //测试具体命令
        processBuilder.command("reboot");
        //合并流
        processBuilder.redirectErrorStream(true);

        //执行命令
        Process process = processBuilder.start();
        //获取流信息
        InputStream inputStream = process.getInputStream();

        //转换成字符流
        InputStreamReader input = new InputStreamReader(inputStream, "GBK");

        char[] chars = new char[1024];

        int len = -1;

        //读取流转换成字符串
        while((len = input.read(chars)) != -1){
            String str = new String(chars, 0, len);
            System.out.println(str);
        }

        //关闭流
        input.close();
        inputStream.close();
    }
}