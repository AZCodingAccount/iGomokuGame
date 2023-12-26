package com.gobanggame.pojo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @program: ExampleProgram
 * @description: WebsiteDayInfo实体类
 **/
@Data
@Schema(description = "WebsiteDayInfo实体类")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebsiteDayInfo {

    @Schema(description = "网站信息id", example = "1")
    private Long id;

    @Schema(description = "新注册用户数量", example = "100")
    private Integer newUserCount;

    @Schema(description = "当天访客人数", example = "150")
    private Integer visitorCount;

    @Schema(description = "网站点击量", example = "1000")
    private Integer websiteClicks;

    @Schema(description = "网站所开AI棋局数", example = "50")
    private Integer aiGameCounts;

    @Schema(description = "网站所开人类棋局数", example = "20")
    private Integer humanGameCount;

    @Schema(description = "记录日期", example = "2023-12-17")
    private LocalDate recordDate;
}
