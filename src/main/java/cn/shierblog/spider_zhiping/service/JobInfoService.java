package cn.shierblog.spider_zhiping.service;

import cn.shierblog.spider_zhiping.Dao.JobInfoDao;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author Albert
 * @Date 2019/03/30 14:34
 * @Description
 **/
@Service
public class JobInfoService {
    @Autowired
    private JobInfoDao jobInfoDao;
    public int saveJobInfo(JSONObject jsonObject){
        return jobInfoDao.saveJobInfo(jsonObject);
    }
}
