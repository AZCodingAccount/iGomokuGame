package com.gobanggame.pojo.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-14 14:14
 * @description: 在线五子棋前端发送消息接收类
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OnlineGomokuMessage {
    private Integer type;
    private String role;
    private Integer stepOrder;
    private Long gameId;
    private String message;
}
