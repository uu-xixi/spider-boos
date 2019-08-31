package cn.shierblog.spider_zhiping.pipeline;

import cn.shierblog.spider_zhiping.service.JobInfoService;
import com.alibaba.fastjson.JSONObject;
import com.sun.xml.internal.bind.v2.runtime.output.SAXOutput;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Author Albert
 * @Date 2019/03/21 02:54
 * @Description
 **/
@Component
public class JobResult implements Pipeline {
    @Autowired
    private JobInfoService jobInfoService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        if (StringUtils.isNotBlank(resultItems.get("companyName"))) {
            //获取页面详情页地址
            String url = resultItems.get("url");
            //获取公司名称
            String companyName = resultItems.get("companyName");
            //获取总薪资
            String salary = resultItems.get("salary");
            //最小薪资
            int leastMoney = Integer.parseInt(StringUtils.substringBefore(salary, "-"));
            //最大薪资
            int maxMoney = Integer.parseInt(StringUtils.substringBetween(salary, "-", "K"));
            //获取工作岗位
            String jobName = resultItems.get("jobName");
            //获取招聘状态
            String status = resultItems.get("status");
            // 城市(ctiy)经验(experience)学历(Degree)
            String ced = resultItems.get("ced");
            //获取城市
            String city = ced.substring(StringUtils.ordinalIndexOf(ced, ">", 1) + 1, StringUtils.ordinalIndexOf(ced, "<", 2));
            //获取开发经验
            String experience = ced.substring(StringUtils.ordinalIndexOf(ced, ">", 3) + 1, StringUtils.ordinalIndexOf(ced, "<", 4));
            //最少开发经验
            int leastExperience = 0;
            //最大开发经验
            int maxExperience = 0;
            if (!"应届生".equals(experience) && !"10年以上".equals(experience) && !"1年以内".equals(experience) && !"经验不限".equals(experience)) {
                leastExperience = Integer.parseInt(experience.substring(0, StringUtils.ordinalIndexOf(experience, "-", 1)));
                maxExperience = Integer.parseInt(experience.substring(StringUtils.ordinalIndexOf(experience, "-", 1) + 1, StringUtils.ordinalIndexOf(experience, "年", 1)));
            } else {
                if ("应届生".equals(experience) || "经验不限".equals(experience)) {
                    leastExperience = 0;
                    maxExperience = leastExperience;
                    return;
                }
                leastExperience = Integer.parseInt(experience.substring(0, StringUtils.ordinalIndexOf(experience, "年", 1)));
                maxExperience = leastExperience;
            }
            //获取学历
            String degree = ced.substring(StringUtils.ordinalIndexOf(ced, ">", 5) + 1, StringUtils.ordinalIndexOf(ced, "<", 6));
            //获取招聘人
            String humanResources = resultItems.get("humanResources");
            //公司融资情况
            String financing = resultItems.get("financing");
            //判断是否是公司融资数据，如果不是则为空
            if (financing.contains("人") || StringUtils.isBlank(financing)) {
                financing = "";
            }
            //公司规模
            String scale = resultItems.get("scale");
            int leastPeople = 0;
            int maxPeople = 0;
            if (StringUtils.isNotBlank(scale) && !"10000人以上".equals(scale)) {
                int indexScale = StringUtils.ordinalIndexOf(scale, "-", 1);
                //最小公司人数
                leastPeople = Integer.parseInt(scale.substring(0, indexScale));
                //最大公司人数
                maxPeople = Integer.parseInt(scale.substring(indexScale + 1, StringUtils.ordinalIndexOf(scale, "人", 1)));
            } else {
                if (StringUtils.isBlank(scale)) {
                    leastPeople = 0;
                    maxPeople = leastPeople;
                } else {
                    leastPeople = Integer.parseInt(scale.substring(0, StringUtils.ordinalIndexOf(scale, "人", 1)));
                    maxPeople = leastPeople;
                }
            }

            //公司分类
            String detail = resultItems.get("detail");
            //公司网站
            String website = resultItems.get("website");
            //判断如果该字符串包含 http://xxxx.xxxx.xxx 表示该公司详情页有公司网站一栏，否则为空
            if (StringUtils.isBlank(website) || !website.contains("h")) {
                website = "";
            }
            //招聘发布时间
            String publishTime = resultItems.get("publishTime");

            //岗位职责
            String workInfo = resultItems.get("workInfo");
            //公司介绍
            String companyInfo = resultItems.get("companyInfo");
            //判断如果公司介绍为null则添加字符串为空
            if (StringUtils.isBlank(companyInfo)) {
                companyInfo = "";
            }
            //法人代表
            String legalPerson = resultItems.get("LegalPerson");
            //注册资金
            String registeredFund = resultItems.get("registeredFund");
            //成立时间
            String establishedTime = resultItems.get("establishedTime");
            if (StringUtils.isBlank(establishedTime)) {
                establishedTime = "";
            }
            //企业类型
            String companyType = resultItems.get("companyType");
            //经营状态
            String manageState = resultItems.get("manageState");
            //公司地址
            String companyAddress = resultItems.get("companyAddress");

            JSONObject result = new JSONObject();
            //添加uuid取消“-”符号并转换为小写
            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
            result.put("uuid", uuid);
            result.put("companyName", companyName);
            result.put("salary", salary);
            result.put("maxExperience", maxExperience);
            result.put("leastExperience", leastExperience);
            result.put("city", city);
            result.put("leastPeople", leastPeople);
            result.put("maxPeople", maxPeople);
            result.put("leastMoney", leastMoney);
            result.put("maxMoney", maxMoney);
            result.put("experience", experience);
            result.put("degree", degree);
            result.put("status", status);
            result.put("jobName", jobName);
            result.put("humanResources", humanResources);
            result.put("financing", financing);
            result.put("scale", scale);
            result.put("detail", detail);
            result.put("website", website);
            result.put("publishTime", publishTime);
            result.put("workInfo", workInfo);
            result.put("companyInfo", companyInfo);
            result.put("legalPerson", legalPerson);
            result.put("registeredFund", registeredFund);
            result.put("establishedTime", establishedTime);
            result.put("companyType", companyType);
            result.put("manageState", manageState);
            result.put("companyAddress", companyAddress);
            result.put("url", url);
            //持久化数据到数据库
            //如需保存到数据库请打开注释
//            jobInfoService.saveJobInfo(result);
            System.out.println("uuid：" + uuid);
            System.out.println("公司名称：" + companyName);
            System.out.println("城市：" + city);
            System.out.println("开发经验：" + experience);
            System.out.println("薪资" + salary);
            System.out.println("工作岗位：" + jobName);
            System.out.println("招聘状态：" + status);
            System.out.println("融资情况：" + financing);
            System.out.println("公司规模：" + scale);
            System.out.println("岗位信息：" + workInfo);
            System.out.println("招聘人：" + humanResources);
            System.out.println("公司分类：" + detail);
            System.out.println("公司网址：" + website);
            System.out.println("发布时间：" + publishTime);
            System.out.println("公司信息：" + companyInfo);
            System.out.println("法人代表：" + legalPerson);
            System.out.println("注册信息：" + registeredFund);
            System.out.println("注册时间：" + establishedTime);
            System.out.println("公司类型：" + companyType);
            System.out.println("经营状态：" + manageState);
            System.out.println("公司地址：" + companyAddress);
            System.out.println("详情页面：" + url);
            System.out.println("----------------------------------------------------分界线----------------------------------------------------");


        }
    }
}
