package cn.shierblog.spider_zhiping.Dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author Albert
 * @Date 2019/03/30 14:36
 * @Description
 **/
@Mapper
public interface JobInfoDao {
    public int saveJobInfo(JSONObject jsonObject);
}
