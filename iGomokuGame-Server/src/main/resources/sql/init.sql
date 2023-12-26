create database gomoku_online;

use gomoku_online;

# type字段是为了后续扩展，实际是我当时设计的时候有问题~~~
# admin表
create table admin
(
    id         bigint auto_increment
        primary key,
    username   varchar(15)                                                                                         not null,
    nickname   varchar(15)  default '不知名管理员'                                                                 not null,
    password   varchar(15)                                                                                         not null,
    avatar     varchar(100) default 'https://th.bing.com/th/id/OIP.o0eECfDkWwVfz3-3lwKd5AAAAA?rs=1&pid=ImgDetMain' not null,
    type       varchar(15)  default "admin"                                                                        not null,    
    status     tinyint(1)   default 1                                                                              null,
    createTime datetime     default CURRENT_TIMESTAMP                                                              null,
    constraint username
        unique (username)
);

# feedback表，存储用户反馈
create table feedback
(
    id               bigint auto_increment
        primary key,
    user_id          bigint               not null,
    feedback_content varchar(500)         not null,
    feedback_time    datetime             null,
    fixed            tinyint(1) default 0 null,
    fixed_time       datetime             null
);

# 游戏细节表，本项目没有用上，后续扩展棋局复现的时候可以用
create table game_details
(
    id         bigint auto_increment
        primary key,
    game_id    bigint  not null,
    move_x     tinyint not null,
    move_y     tinyint not null,
    color      tinyint not null,
    step_order int     not null
);

# 游戏历史表，本项目也没用上，后续可以给用户一个查询的接口
create table game_history
(
    id          bigint auto_increment
        primary key,
    black_id    bigint            not null,
    white_id    bigint            not null,
    begin_time  datetime          null,
    end_time    datetime          null,
    game_result tinyint default 2 not null,
    room_id     mediumtext        null
);

# 用户表，存储用户相关信息

create table user
(
    id                    bigint auto_increment
        primary key,
    username              varchar(15)                                                                                                        not null,
    password              varchar(15)                                                                                                        not null,
    nickname              varchar(15)  default '不知名小将'                                                                                  not null,
    gender                char         default '男'                                                                                          not null,
    age                   int          default 18                                                                                            not null,
    description           varchar(50)  default '这个人很懒，还没有描述哦'                                                                     null,
    social_account        varchar(30)  default '还没有添加社交账号'                                                                          null,
    image_url             varchar(200) default 'https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/defaultavatar.png' not null,
    user_level            varchar(10)  default '初入江湖'                                                                                    not null,
    user_score            int          default 0                                                                                             not null,
    game_total_counts     int          default 0                                                                                             not null,
    game_person_counts    int          default 0                                                                                             not null,
    game_ai_counts        int          default 0                                                                                             not null,
    game_success_counts   int          default 0                                                                                             not null,
    game_fail_counts      int          default 0                                                                                             not null,
    game_dead_heat_counts int          default 0                                                                                             not null,
    online                varchar(3)   default '不在线'                                                                                      not null,
    last_online_time      datetime                                                                                                           null,
    deleted               tinyint      default 0                                                                                             not null,
    create_time           datetime                                                                                                           not null,
    update_time           datetime                                                                                                           not null,
    constraint username
        unique (username)
);

# 用户好友表，存储用户与该用户对应的好友
create table user_friends
(
    id           bigint auto_increment
        primary key,
    user_id      bigint            not null,
    friend_id    bigint            not null,
    deleted      tinyint default 0 not null,
    create_time  datetime          null,
    deleted_time datetime          null
);

# 用户好友消息表，存储聊天的消息
create table user_messages
(
    id           bigint auto_increment
        primary key,
    user_id      bigint                             not null,
    friend_id    bigint                             not null,
    message      varchar(500)                       not null,
    message_time datetime default CURRENT_TIMESTAMP not null,
    readed       tinyint                            null comment '标识信息是否已读（0已读 1未读）'
);

# 网站每天的信息，数据分析时使用
create table website_day_info
(
    id               bigint auto_increment
        primary key,
    new_user_count   int default 0 null,
    visitor_count    int default 0 not null,
    website_clicks   int default 0 not null,
    ai_game_counts   int default 0 not null,
    human_game_count int default 0 not null,
    record_date      date          null
);

# 网站访客详细信息，数据可视化大屏
create table website_visitor_details_info
(
    id          bigint auto_increment
        primary key,
    name        varchar(100) default '未知用户'        null,
    ip          varchar(40)  default '未知ip'          null,
    address     varchar(30)  default '未知地址'        null,
    access_time datetime     default CURRENT_TIMESTAMP null
);


# 给管理员插入一条初始化数据，其他尽量不要乱插。业务逻辑有时候会出问题，数据库层面没有约束
insert into admin(username,password) values("admin","admin");   