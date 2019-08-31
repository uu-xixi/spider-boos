package cn.shierblog.spider_zhiping.controller;

import cn.shierblog.spider_zhiping.pageProcessor.JobProcessor;
import cn.shierblog.spider_zhiping.pipeline.JobResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.RedisScheduler;

/**
 * @Author Albert
 * @Date 2019/03/21 02:51
 * @Description
 **/
@RestController
public class run {
    @Autowired
    private JobResult jobResult;
    @GetMapping("/start")
    public void run(){
            Spider.create(new JobProcessor()) //创建我们的页面解析器
                    //爬虫从指定的页面开始获取数据
                    //长沙:https://www.zhipin.com/c101250100-p100101/?ka=search_100101
                    //全国:https://www.zhipin.com/c100010000-p100101/?ka=sel-city-100010000
                    //重庆:https://www.zhipin.com/c101040100-p100101/?ka=search_100101
                    //北京：https://www.zhipin.com/c101010100-p100101/
                    .addUrl("https://www.zhipin.com/job_detail/?query=&city=101040100&industry=&position=100101") //添加URL地址
                    .thread(3)//开启多少个线程执行
                    .setScheduler(new QueueScheduler())//使用内存队列保存Url地址
                    .addPipeline(jobResult)//自定义结果保存
                    //.setScheduler(new RedisScheduler(jedisPool)) //使用redis保存队列
                    .run();//执行爬虫
    }

}
