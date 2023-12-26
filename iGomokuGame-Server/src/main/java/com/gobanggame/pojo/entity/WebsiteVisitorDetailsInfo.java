package com.gobanggame.pojo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @program: ExampleProgram
 * @description: WebsiteDetailsInfo实体类
 **/
@Data
@Schema(description = "WebsiteDetailsInfo实体类")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebsiteVisitorDetailsInfo {

    @Schema(description = "记录id", example = "1")
    private Long id;

    @Schema(description = "用户标识", example = "Nickname123")
    private String name;

    @Schema(description = "访客ip", example = "192.168.1.1")
    private String ip;

    @Schema(description = "访客地址", example = "未知地址")
    private String address;

    @Schema(description = "网站访问时间", example = "2023-12-17T10:15:00")
    private LocalDateTime accessTime;
}
