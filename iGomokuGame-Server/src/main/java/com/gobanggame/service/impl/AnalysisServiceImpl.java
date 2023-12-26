package com.gobanggame.service.impl;

import com.gobanggame.mapper.WebsiteDayInfoMapper;
import com.gobanggame.mapper.WebsiteVisitorDetailsInfoMapper;
import com.gobanggame.pojo.entity.WebsiteDayInfo;
import com.gobanggame.pojo.entity.WebsiteVisitorDetailsInfo;
import com.gobanggame.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-19 23:35
 * @description:
 **/
@Service
public class AnalysisServiceImpl implements AnalysisService {

    @Autowired
    private WebsiteVisitorDetailsInfoMapper websiteVisitorDetailsInfoMapper;

    @Autowired
    private WebsiteDayInfoMapper websiteDayMapper;

    /**
     * @param ipAddr
     * @param cityInfoByFile
     * @return void
     * @author AlbertZhang
     * @description 添加网站访客
     * @date 2023-12-19 23:39
     **/
    @Override
    public void WebsiteClick(String ipAddr, String cityInfoByFile) {

        // 往网站访客详细表插入一条数据。
        websiteVisitorDetailsInfoMapper.WebsiteClick(ipAddr, cityInfoByFile, LocalDateTime.now());
        // 获取当前日期，然后看看每日网站访客表有没有匹配的，有就不管了，等后续登录或注册的时候处理逻辑，没有就创建今天的数据
        LocalDate now = LocalDate.now();
        WebsiteDayInfo websiteDayInfo = websiteDayMapper.selectByDate(now);
        if (websiteDayInfo == null) {   // 不存在就插入数据
            // 初始化，访问者赋值为0，点击数赋值为1（我们认为只有在网站有账号的人 才计入访问数量的计算）
            WebsiteDayInfo websiteDayInfo1 = WebsiteDayInfo.builder().visitorCount(0)
                    .websiteClicks(1)
                    .recordDate(now).build();
            websiteDayMapper.insert(websiteDayInfo1);
        }
    }
}
