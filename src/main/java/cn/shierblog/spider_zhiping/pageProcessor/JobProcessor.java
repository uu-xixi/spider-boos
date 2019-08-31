package cn.shierblog.spider_zhiping.pageProcessor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author Albert
 * @Date 2019/03/20 19:46
 * @Description 页面分析
 **/
public class JobProcessor implements PageProcessor {
    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        //添加该页面所有连接
        List<String> javaUrl = html.xpath("//div[@class='job-list']//ul//li//div[@class='job-primary']//div[@class='company-text']//a").links().all();
        //添加该页面各个区域标签
        List<String> regionalLabelsUrls = html.xpath("//div[@class='condition-box']/dl[@class='condition-district  show-condition-district ']//dd//a").links().all();
        page.addTargetRequests(regionalLabelsUrls);
        //过滤掉公司主页
        List<String> companyUrl = javaUrl.stream().filter(url -> !url.contains("gongsi")).collect(Collectors.toList());
        page.addTargetRequests(companyUrl);

        //获取该页面下一页
        String nextPage = html.xpath("//div[@class='job-list']//div[@class='page']//a[@class='next']").links().toString();
        page.addTargetRequests(Arrays.asList(nextPage));

        //获取页面详情页地址
        String url = page.getUrl().toString();
        page.putField("url", url);
        //抽取公司名称
        String companyName = html.xpath("//div[@class='detail-content']//div[@class='job-sec']//div[@class='name']/text()").toString();
        if (StringUtils.isNotBlank(companyName)) {
            page.putField("companyName", companyName);
            //抽取薪资
            String salary = html.xpath("//div[@class='smallbanner']//span[@class='badge']/text()").toString();
            page.putField("salary", salary);
            //抽取招聘岗位
            String jobName = html.xpath("//div[@class='smallbanner']//div[@class='name']/h1/text()").toString();
            page.putField("jobName", jobName);
            //抽取招聘状态
            String status = html.xpath("//div[@class='job-banner']//div[@class='info-primary']/div/text()").toString();
            page.putField("status", status);
            // 城市(ctiy)经验(experience)学历(Degree)
            String ced = html.xpath("//div[@class='job-banner']//div[@class='info-primary']/p").toString();
            page.putField("ced", ced);
            //抽取招聘者(HR)
            String humanResources = html.xpath("//div[@class='job-box']//h2[@class='name']/text()").toString();
            page.putField("humanResources", humanResources);
            //抽取公司融资情况
            String financing = html.xpath("//div[@class='job-box']/div[@class='inner home-inner']/div[@class='job-sider']/div[@class='sider-company']/p[2]/text()").toString();

            page.putField("financing", financing);
            //公司规模
            String scale = html.xpath("//div[@class='job-box']//div[@class='sider-company']/p[3]/text()").toString();
            page.putField("scale", scale);
            //公司分类
            String detail = html.xpath("//div[@class='job-box']//div[@class='sider-company']/p/a/text()").toString();
            page.putField("detail", detail);
            //公司网址
            String website = html.xpath("//div[@class='job-box']//div[@class='sider-company']/p[5]/text()").toString();
            page.putField("website", website);
            //发布时间
            String publishTime = html.xpath("//div[@class='job-box']//div[@class='sider-company']/p[@class='gray']/text()").toString();
            page.putField("publishTime", publishTime);
            //岗位描述
            String workInfo = html.xpath("//div[@class='detail-content']//div[@class='text']/text()").toString();
            page.putField("workInfo", workInfo);
            //公司介绍
            String companyInfo = html.xpath("//div[@class='job-sec company-info']//div[@class='text']/text()").toString();
            page.putField("companyInfo", companyInfo);
            //法人代表
            String LegalPerson = html.xpath("//div[@class='level-list']/li[1]/text()").toString();
            page.putField("LegalPerson", LegalPerson);
            //注册资金
            String registeredFund = html.xpath("//div[@class='level-list']/li[2]/text()").toString();
            page.putField("registeredFund", registeredFund);
            //成立时间
            String establishedTime = html.xpath("//div[@class='level-list']/li[3]/text()").toString();
            page.putField("establishedTime", establishedTime);
            //企业类型
            String companyType = html.xpath("//div[@class='level-list']/li[4]/text()").toString();
            page.putField("companyType", companyType);
            //经营状态
            String manageState = html.xpath("//div[@class='level-list']/li[5]/text()").toString();
            page.putField("manageState", manageState);
            //公司地址
            String companyAddress = html.xpath("//div[@class='location-address']/text()").toString();
            page.putField("companyAddress", companyAddress);

        }
    }

    /**
     * Site.me() 可以对爬虫进行一些配置，包括编码，抓取间隔，超时时间，重试次数等
     */
    private Site site = Site.me()
            .setCharset("utf-8")
            .setTimeOut(5000)//设置超时时间 毫秒
            .setRetrySleepTime(5000)//设置重试的间隔时间，请求失败隔多少时间再次发送请求
            .setSleepTime(6000);//设置等待时间

    @Override
    public Site getSite() {
        return site;
    }


}
