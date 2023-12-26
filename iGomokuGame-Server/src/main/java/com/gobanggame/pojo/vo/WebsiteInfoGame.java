package com.gobanggame.pojo.vo;

import lombok.Data;

import java.time.LocalDate;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-20 11:13
 * @description: 网站每天访客量的vo，后台管理中—网站用户的分析VO
 **/
@Data
public class WebsiteInfoGame {
    private Long id;
    private Integer newUserCount;
    private Integer visitorCount;
    private Integer websiteClicks;
    private LocalDate recordDate;
}
