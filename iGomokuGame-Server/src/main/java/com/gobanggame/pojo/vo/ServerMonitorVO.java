package com.gobanggame.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-23 17:34
 * @description: 服务器性能监控VO类
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "服务器性能相关指标")
public class ServerMonitorVO {

    @Schema(description = "当前房间数", example = "1")
    private Integer roomCount;
    @Schema(description = "超时已关闭房间数", example = "1")
    private Integer closedRoomCount;
    @Schema(description = "网站维护的session数量", example = "1")
    private Integer SessionCount;
}
