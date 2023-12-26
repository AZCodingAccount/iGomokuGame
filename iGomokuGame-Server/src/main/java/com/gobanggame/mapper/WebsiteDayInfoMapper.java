package com.gobanggame.mapper;

import com.gobanggame.pojo.vo.GomokuGameCount;
import com.gobanggame.pojo.entity.WebsiteDayInfo;
import com.gobanggame.pojo.vo.WebsiteInfoGame;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-19 23:58
 * @description:
 **/
@Mapper
public interface WebsiteDayInfoMapper {
    /**
     * @param now
     * @return com.gobanggame.pojo.entity.WebsiteDayInfo
     * @author AlbertZhang
     * @description 根据日期获取websiteDay对象
     * @date 2023-12-20 0:03
     **/
    @Select("select * from website_day_info where record_date=#{now} limit 1;")
    WebsiteDayInfo selectByDate(LocalDate now);

    /**
     * @param websiteDayInfo1
     * @return void
     * @author AlbertZhang
     * @description 向网站每日信息表新增一条数据
     * @date 2023-12-20 0:08
     **/
    void insert(WebsiteDayInfo websiteDayInfo1);

    /**
     * @param websiteDayInfo1
     * @return void
     * @author AlbertZhang
     * @description 更新每天的网站信息
     * @date 2023-12-20 0:17
     **/
    void update(WebsiteDayInfo websiteDayInfo1);

    /**
     * @param
     * @return java.util.List<com.gobanggame.pojo.vo.GomokuGameCount>
     * @author AlbertZhang
     * @description 获取网站棋局的相关信息（每天的）
     * @date 2023-12-20 11:09
     **/
    @Select("select * from website_day_info")
    List<GomokuGameCount> selectGameCount();

    /**
     * @param
     * @return java.util.List<com.gobanggame.pojo.vo.WebsiteInfoGame>
     * @author AlbertZhang
     * @description 获取每天网站的相关信息
     * @date 2023-12-20 11:16
     **/
    @Select("select * from website_day_info")
    List<WebsiteInfoGame> selectWebsiteInfo();

    
}
