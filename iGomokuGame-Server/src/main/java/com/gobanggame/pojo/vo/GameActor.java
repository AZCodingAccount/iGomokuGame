package com.gobanggame.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-14 16:18
 * @description: 在新加入房间的时候广播消息变化的VO
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameActor {
    private Long id;
    private String role;
    private String nickname;
    private String imageUrl;
    private String userLevel;
}
