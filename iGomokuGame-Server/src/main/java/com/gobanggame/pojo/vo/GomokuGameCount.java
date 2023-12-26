package com.gobanggame.pojo.vo;

import lombok.Data;

import java.time.LocalDate;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-20 11:08
 * @description: 网站后台棋局信息模块的VO
 **/
@Data
public class GomokuGameCount {
    private Long id;
    private LocalDate recordDate;
    private Integer aiGameCounts;
    private Integer humanGameCount;
}
