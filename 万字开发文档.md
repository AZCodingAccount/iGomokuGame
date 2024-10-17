# 五子棋项目

## 项目亮点

(1)本项目采用vue3+springboot为主要开发技术。

(2)使用spring task技术实现定时任务,自动关闭房间。

(3)使用WebSocket技术实现用户聊天与联机对战的实现。

(4)使用博弈树+极大极小搜索+Alpha-Beta剪枝+评估函数实现人机对战

(4)使用Echarts和DataView实现数据可视化

(5)HTTP短轮询实现数据可视化中数据的更新

(6)使用前端的Excel包实现数据的导入导出

(7)使用animate.css实现游戏结束时动画的播放。

(8)使用TTS技术实现文本转语音完成AI与用户互动的功能。

(9)使用Ip2Region实现根据IP获取用户地址

(10) 开发过程规范，项目工作量饱满

## 项目需求分析

**项目模块如图**：

![image-20240104232637761](https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/image-20240104232637761.png)

这里对这些模块进行简单的说明

- 用户模块

  用户可以在平台拥有属于自己的账号，可以设置头像，个人说明。通过搜索用户账号可以添加好友，从而进行对战。在进行人机对战，在线对战这些过程中可以为自己累计一定的经验，从而升级。平台还提供排行榜的展示功能，增加用户粘性。

- 人机对战模块

  用户可以设置人机对战难度（分为level1-3，分别对应alpha-beta剪枝函数的depth为1-2与自行设置的恶一个评估函数）。进去以后UI会显示时间，当前轮数。同时后端的评估函数会动态判断当前局势从而给出一些互动语句。

- 在线对战模块

  用户通过输入房间号来进行对战开局。在局内可以进行实时的聊天。后面进来的用户会询问是否进行观战转成观战用户。

- 后台管理

  在后台管理模块。分为数据分析和用户管理

  - 程序提供数据可视化大屏来显示当前网站访客详细信息、访客地域分布、用户年龄分布、用户等级分布。
  - 提供点击量，访问量，访问量与点击量比值，平台每日新增用户数。
  - 棋局分析。分为网站近日五子棋棋局数与联机棋局数分析

  用户管理

  - 前台提供反馈设置，在后台反馈管理模块可以进行反馈的解决。
  - 同时后台提供用户管理，可以查看用户信息、根据条件查询用户信息，用户账号的禁用与启用。
  - 后台还提供一键导出数据到excel表格的功能

## **开发周期🚀🚀🚀**

**预计开发周期**

![image-20231209151601692](https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/image-20231209151601692.png)

**实际开发周期**

真的没有摸鱼，每天都爆肝8小时以上，工作量真的大

![image-20240104235444759](https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/image-20240104235444759.png)

## **技术选型**

**前端**

1. Vue3技术，采用Element Plus这个字节开源的组件库。
2. Apache-echarts+datav进行网站后台的可视化统计
3. HTTP短轮询实现数据的"实时"更新
4. 采用Excel包导出数据

**后端**

1. 采用springboot技术开发接口
2. springboot-starter-websocket(集成websocket)实现聊天+对战+观战功能
3. 使用ip2region获取访问用户ip相关信息
4. spring-task实现定时任务

**数据库**

​	使用mysql存用户，好友，聊天记录一些信息，用一下spring-cache(集成redis)存储排行榜的一些信息

**算法**

1. 采用博弈树+极大极小搜索+Alpha-Beta剪枝+评估函数

## 主要业务流程

![image-20231211180102564](https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/image-20231211180102564.png)

​																						好友聊天模块时序图

![image-20231209165418635](https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/image-20231209165418635.png)

​																在线对战模块websocket执行流程顺序图

## 前端页面开发

### **友商前端页面调研**

![image-20231120175743810](https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/image-20231120175743810.png)

<img src="https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/image-20231120180150150.png" alt="image-20231120180150150" style="zoom:50%;" />



<img src="https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/image-20231120180851305.png" alt="image-20231120180851305" style="zoom:50%;" />

<img src="https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/image-20231120181713041.png" alt="image-20231120181713041" style="zoom:50%;" />

![image-20231120181843311](https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/image-20231120181843311.png)

<img src="https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/image-20231120181849802.png" alt="image-20231120181849802" style="zoom:50%;" />

### 详细界面开发

**ok,该借鉴的都已经借鉴了，现在开始我们的前端页面开发。**

这里就不详细截图了，主要就是几个页面

**前台模块**

- 主页
- 前台登录注册页面
- 排行榜页面
- 个人信息模块页面
- 好友模块的页面
- 人机对战前置页面
- 人机对战页面
- 联机对战前置页面
- 联机对战页面

**管理员模块**

- 管理员登录页面
- 首页
- 用户管理页面
- 反馈管理页面
- 五子棋棋局分析页面
- 网站动态分析页面、
- 数据可视化大屏页面

## **后端**开发

### 数据库表设计

**user表**：存储用户相关信息

| 字段名                | 字段类型     | 约束         | 备注                             |
| --------------------- | ------------ | ------------ | -------------------------------- |
| id                    | bigint       | 主键自增     |                                  |
| username              | varchar(15)  | 非空、唯一   | 用户名                           |
| password              | varchar(15)  | 非空         | 密码                             |
| nickname              | varchar(15)  |              | 用户昵称（若未设置显示username） |
| gender                | tinyint      | 非空、默认1  | 1男性、0女性                     |
| age                   | int          | 非空、默认18 | 用户年龄                         |
| description           | varchar(50)  | 默认字符串   | 用户个人描述                     |
| social_account        | varchar(30)  | 默认字符串   | 用户社交媒体联系方式             |
| image_url             | varchar(200) | 非空         | 头像url                          |
| user_level            | varchar(200) | 默认字符串   | 用户棋力等级(冗余字段)           |
| user_score            | int          | 非空、默认0  | 用户分数                         |
| game_total_counts     | int          | 非空、默认0  | 用户已下的总棋局数               |
| game_person_counts    | int          | 非空、默认0  | 用户跟人下的棋局数               |
| game_ai_counts        | int          | 非空、默认0  | 用户跟人机下的棋局数             |
| game_success_counts   | int          | 非空、默认0  | 用户胜利棋局数                   |
| game_fail_counts      | int          | 非空、默认0  | 用户失败棋局数                   |
| game_dead_heat_counts | int          | 非空、默认0  | 用户平局棋局数                   |
| online                | tinyint      | 非空、默认0  | 1在线、0非在线                   |
| last_online_time      | datetime     |              | 最后一次在线时间                 |
| deleted               | tinyint      | 非空、默认0  | 逻辑删除字段                     |
| create_time           | datetime     |              | 创建时间                         |
| update_time           | datetime     |              | 修改时间                         |

**备注**：棋力等级设置为1-5，分别对应前端页面显示的初入江湖、略知一二、小有所成、登峰造极、胜天半子，再高就是渡劫飞升

对应的user_score可以是100、500、2000、5000、10000

TODO: 后续一些字段可以抽出来一个单独的表作为热表

!!!**我为了简便把gender、user_level、online都改成字符串了，这样不用映射。**

**user_friends表**：存储用户关联的好友相关信息

| 字段名       | 字段类型 | 约束        | 备注         |
| ------------ | -------- | ----------- | ------------ |
| id           | bigint   | 主键，自增  |              |
| user_id      | bigint   | 非空        | 用户id       |
| friend_id    | bigint   | 非空        | 好友id       |
| deleted      | tinyint  | 非空、默认0 | 逻辑删除字段 |
| create_time  | datetime |             | 创建时间     |
| deleted_time | datetime |             | 删除时间     |

**friends_message表**：存储聊天相关信息

| 字段名       | 字段类型     | 约束                 | 备注       |
| ------------ | ------------ | -------------------- | ---------- |
| id           | bigint       | 主键、自增           |            |
| user_id      | bigint       | 非空                 | 用户id     |
| friend_id    | bigint       | 非空                 | 好友id     |
| message      | varchar(500) | 非空                 | 发送的消息 |
| message_time | datetime     | 非空、默认为当前时间 | 消息时间戳 |

**game_history**：存储棋局历史信息

| 字段名      | 字段类型 | 约束         | 备注                        |
| ----------- | -------- | ------------ | --------------------------- |
| id          | bigint   | 主键非空自增 | 棋局id                      |
| black_id    | bigint   | 非空         | 黑棋用户id                  |
| white_id    | bigint   | 非空         | 白棋用户id（0代表是AI对战） |
| begin_time  | datetime |              | 游戏开始时间                |
| end_time    | datetime |              | 游戏结束时间                |
| game_result | tinyint  | 非空、默认2  | 0 黑胜 1 白胜 2平           |

**game_details**:存储棋局历史信息

| 字段名     | 字段类型 | 约束         | 备注        |
| ---------- | -------- | ------------ | ----------- |
| id         | bigint   | 主键非空自增 | 历史信息id  |
| game_id    | bigint   | 非空         | 棋局id      |
| move_x     | tinyint  | 非空         | 移动的x坐标 |
| move_y     | tinyint  | 非空         | 移动的y坐标 |
| color      | tinyint  | 非空         | 0黑色 1白色 |
| step_order | int      | 非空         | 移动的顺序  |

备注：TODO: 这个表用于后续扩展复盘，前端请求这些数据可以使用交互的方式为下棋者进行棋局的复现。

**admin：后台管理员表**

| 字段名     | 字段类型     | 约束         | 备注             |
| ---------- | ------------ | ------------ | ---------------- |
| id         | bigint       | 主键自增     | 管理员id         |
| username   | varchar(15)  | 非空、唯一   | 用户名           |
| nickname   | varchar(15)  | 非空，默认值 | 昵称             |
| password   | varchar(15)  | 非空         | 密码             |
| avatar     | varchar(100) | 非空，默认值 | 头像url          |
| type       | varchar(15)  | 非空         | 账号权限         |
| status     | boolean      | 默认true     | 当前员工账号状态 |
| createTime | DateTime     | 默认当前时间 | 账号创建时间     |

**user_feedback：用户反馈表**

| 字段名           | 字段类型     | 约束        | 备注             |
| ---------------- | ------------ | ----------- | ---------------- |
| id               | bigint       | 主键、自增  | 反馈id           |
| user_id          | bigint       | 非空        | 反馈的用户       |
| feedback_content | varchar(500) | 非空        | 反馈信息         |
| feedback_time    | datetime     |             | 反馈时间         |
| fixed_emp_id     | bigint       | 非空，默认1 | 解决问题的员工id |
| fixed_time       | daetime      |             | 问题解决时间     |

**website_day_info：存储网站相关信息**

| 字段名           | 字段类型 | 约束        | 备注             |
| ---------------- | -------- | ----------- | ---------------- |
| id               | bigint   | 主键、自增  | 网站信息id       |
| new_user_count   | int      | 主键，自增  |                  |
| visitor_count    | int      | 非空、默认0 | 当天访客人数     |
| website_clicks   | int      | 非空、默认0 | 网站点击量       |
| ai_game_counts   | int      | 非空、默认0 | 网站所开ai棋局数 |
| human_game_count | int      | 非空、默认0 | 网站所开ai棋局数 |
| record_date      | datetime |             | 记录的日期       |

**website_visitor_details_info：存储网站访客详细信息**

| 字段名      | 字段类型     | 约束           | 备注                           |
| ----------- | ------------ | -------------- | ------------------------------ |
| id          | bigint       | 主键自增       |                                |
| name        | varchar(100) | 默认"未知用户" | 用户标识，登录用户存储的是昵称 |
| ip          | varchar(40)  | 默认"未知ip"   | 访客ip                         |
| address     | varchat(30)  | 默认"未知地址" | 访客地址                       |
| access_time | Datetime     | 默认当前时间   | 网站访问时间                   |



### 接口设计

前端后端数据交互统一格式

| 名称 | 类型   | 是否必须 | 备注                      |
| ---- | ------ | -------- | ------------------------- |
| code | number | 必须     | 响应码, 1 成功 ;，0  失败 |
| msg  | string | 非必须   | 提示信息                  |
| data | string | 必须     | 返回的数据                |

ℹ️:下述接口的请求参数使用对象传值时都是json数据、不再显式列出、统一在请求头Content-Type:application/json添加即可

ℹ️:code为0即为出现异常，msg信息中会附加异常的详细信息，使用vue响应拦截器拦截这类返回数据展示弹窗即可

#### 登录注册

1. 注册请求：

   >请求路径：/user/register
   >
   >请求方式：POST
   >
   >接口描述：该接口用于注册用户，用户输入用户名和密码点击注册按钮即可将数据提交到后端。

请求参数：

```json
{
    "username":"demo123456",
    "password":"demo123456"
}
```

响应参数：

```json
{
    "code":1,
    "msg":"success",
    "data":null
}
```

<hr>

2. 登录请求：

   >请求路径：/user/login
   >
   >请求方式：POST
   >
   >接口描述：该接口用于用户登录，用户输入用户名和密码，服务端为用户端下发token，以后的每次请求都需要携带token，否则服务端拒绝返回数据。

请求参数：

```json
{
    "username":"demo123456",
    "password":"demo123456"
}
```

响应参数：

```json
{
    "code":1,
    "msg":"success",
    "data":{
        "id":2,
       	"username":"demo123456",
        "nickname":"这是示例昵称",
        "userLevel":"胜天半子",
        "rankingList":[         {"id":1,"nickname":"user1","imageUrl":"https://picture1/1.png","gameSuccessCounts":100,"gameFailCounts":2,"gameDeadHeatCounts":0,"userScore":5800},
{"id":2,"nickname":"user2","imageUrl":"https://picture1/2.png","gameSuccessCounts":200,"gameFailCounts":2,"gameDeadHeatCounts":0,"userScore":8800},    
// ......省略8条
        ]
        "jwt":"asabsabds.hjbhsjsa.saj182292282029.hshsuahsak"          
    }
}
```

**对数据的简单说明：**

返回的数据可使用JS中的一个对象映射一下存起来，在template里面直接调就行

1. id作为用户的唯一标识，创建房间，查询个人资料都能用到。(可以把下面的jwt，还有用户信息那些存到vuex和pinia里面)

2. username、nickname、userLevel是用于主页用户个人资料的展示（详细资料在点击头像进入单独页面时触发请求返回）

3. rankingList是一个长度为10的数组，用于显示排行榜中用户的一些个人信息。

4. jwt是用户的唯一标识，后端拿着它可以解析出用户的相关信息。

#### 用户个人资料

3. **根据用户id查询用户信息**

>请求路径：/user/{id}
>
>请求方式：GET
>
>接口描述：根据登录时返回的用户id查询出来用户数据，用于个人资料的回显操作

请求参数：路径参数

响应数据

   ```json
   {
       "code":1,
       "msg":"success",
       "data":{
           "id":2,
           "username":"demo123",
           "nickname":"示例昵称",
           "gender":"男",
           "age":18,
           "decription":"这是一段示例自我介绍",
           "socialAccount":"我的qq账号是111111111",
           "imageUrl":"https://jshdshjdc/1.png",
           "userLevel":"胜天半子",
           "userScore":5888,
           "gameTotalCounts": 0,
           "gamePersonCounts": 0,
           "gameAiCounts": 0,
           "gameSuccessCounts": 0,
           "gameFailCounts": 0,
           "gameDeadHeatCounts": 0
       }
   }
   ```

**对数据的简单说明：**

这个数据是用于个人资料的回显操作，其中

username不允许修改(可选择不展示)，userLevel和userScore这两个字段是用于展示等级，不可修改。

<hr>
4. **上传头像**

>请求路径：/user/upload
>
>请求方式：POST
>
>接口描述：用于上传图片，上传成功以后返回图片访问url

请求参数：Headers：**Content-Type：multipart/form-data**		请求体类型：**file**

返回数据

```json
{
    "code":1,
    "msg":"success",
    "data":"http://www.alioss.com/1.png"
}
```



5. **更新用户信息**

>请求路径：/user
>
>请求方式：PUT
>
>接口描述：根据修改后的用户信息更新数据

请求参数：

```json
{
    "id":2,
    "username":"demo123",
    "password":"demo123",
    "nickname":"更新的示例昵称",
    "gender":"男",
    "age":18,
    "decription":"这是一段更新的示例自我介绍",
    "socialAccount":"我的qq账号是111111111",
    "imageUrl":"https://jshdshjdc/1.png"
}
```

响应数据：

```json
{
    "code":1,
    "msg":"success",
    "data":null
}
```

<hr>
6. **用户注销**

>请求路径：/user/{id}
>
>请求方式：DELETE
>
>接口描述：用户注销账户

请求参数：路径参数

响应数据：

```json
{
    "code":1,
    "msg":"success",
    "data":null
}
```

#### 好友模块

💡：之前的接口设计有疏漏，忘记了可以从jwt里面获取当前用户id，下面的接口设计均不需传递当前用户id

7. 查询所有好友

>请求路径：/user/friend/list
>
>请求方式：GET
>
>接口描述：查询出用户的所有好友

请求参数：无（在jwt中就可获取id）

响应数据：

```json
{
    "code":1,
    "msg":"success",
    "data":[
       { "id":1,"nickname":"张三","age":18,"description":"自我介绍~","socialAccount":"我的qq账号是11111"
,"imageUrl":"https://ssdddddh/1.png","userLevel":"胜天半子","userScore":58888 , "game_total_counts": 0,"game_person_counts": 0,  "game_ai_counts": 0, "game_success_counts": 0,  "game_fail_counts": 0, "game_dead_heat_counts": 0 ,isOnline:"在线",lastOnlineTime:"2023-11-28"} ,
         { "id":1,"nickname":"张三","age":18,"description":"自我介绍~","socialAccount":"我的qq账号是11111"
,"imageUrl":"https://ssdddddh/1.png","userLevel":"胜天半子","userScore":58888 , "game_total_counts": 0,"game_person_counts": 0,  "game_ai_counts": 0, "game_success_counts": 0,  "game_fail_counts": 0, "game_dead_heat_counts": 0 ,isOnline:"不在线",lastOnlineTime:"2023-11-28"} 
        //......以下省略好友列表]
}
```

**简单描述：**前端渲染主页面时不必都渲染，挑几个需要的字段进行渲染，但需要封装到一个数组中，当用户点击**查看详细信息**时，直接根据id从数组中拿而不必再次发请求。

<hr>
8. 添加好友

>请求路径：/user/friend
>
>请求方式：POST
>
>接口描述：用户添加好友，输入昵称或者用户名，点击添加按钮即可添加

请求数据：

```json
{
    "username":"demo123"
    // "nickname":"示例昵称"
}
```

返回数据：

```json
{
    "code":1,
    "msg":"success",
    "data":null
}
```

<hr>

##### 好友聊天(websocket请求)

请求路径：ws://localhost:8080/user/friend/{userId}       (这个请求在登录完成时就需要创建连接)

接口描述：服务端会为每个id生成一个sesscion。每次通信，消息的传输格式遵循下面的规范：

发送数据

   ```json
   {
       "userId":1,	// 这个为了保证清晰才传的
       "targetUserId":3,
       "message":"你好，世界！"
   }
   ```

服务端发送数据（发送的数据不是同一个客户机，而是另一个通话的给他发送了消息）

   ```json
   {
       "userId":3,	
       "targetUserId":1,
       "nickname":"小张",	// 用于弹框显示来自谁的消息
       "message":"你也好"
   }
   ```

**简单描述**：前端为了简化，就不提供多个会话之间的切换功能了。这就意味着前端在实现过程中可以接收到数据直接进行渲染，而不必维护每个会话(判断当前前端是哪个聊天室)。因此这个targetUserId和这个userId如果服务端不抽风的话其实是多余的。

- 进入聊天页面，发送请求获取历史聊天数据，接口`8`

- 另外，实现这样一个功能，收到数据以后，判断一下当前在哪个页面（vue-router里面的this.$route.path可判断）。如果前台显示的不是聊天页面，**弹出一个弹窗提示收到了来自谁的消息**

- 最后一个需求是有未读的消息在好友页面会显示红点(简化需求，只有当用户不在线时我们才认为他的消息是未读，否则都是已读)。在建立完毕ws连接以后，前端向服务器发送一个请求获取当前未读的数量（接口`9`）

- 修改未读信息。只要点击聊天窗口，就把所有历史信息更新成已读(接口`10`)

9. 请求聊天数据（使用ws或者使用http请求都可以，这里采用http）

> 请求路径：/user/friend/list/{friendId}
>
> 请求方式：GET
>
> 接口描述：该接口用于从数据库中请求数据，默认返回10条

请求参数：路径参数

响应数据

```json
// 假设当前id为1的用户要跟id为3的用户聊天
{
    "code": 1,
    "msg": "success",
    "data": [
        {
            "id": 20,
            "userId": 1,
            "imageUrl": "http://sjdojdid/1.png",
            "nickname": "User1Nick",
            "friendId": 3,
            "message": "你好",
            "messageTime": "2023-12-11 12:37:40"
        },
        {
            "id": 21,
            "userId": 3,
            "imageUrl": "http://sjdojdid/3.png",
            "nickname": "User3Nick",
            "friendId": 1,
            "message": "你也好啊",
            "messageTime": "2023-12-11 12:37:41"
        },
        {
            "id": 22,
            "userId": 1,
            "imageUrl": "http://sjdojdid/1.png",
            "nickname": "User1Nick",
            "friendId": 3,
            "message": "最近怎么样",
            "messageTime": "2023-12-11 12:37:42"
        },
        {
            "id": 23,
            "userId": 3,
            "imageUrl": "http://sjdojdid/3.png",
            "nickname": "User3Nick",
            "friendId": 1,
            "message": "挺好的",
            "messageTime": "2023-12-11 12:37:42"
        }
        // 以下省略6条数据
    ]
}
```

简单描述：注意，这里渲染的话就头像和昵称，然后聊天信息和对应时间。所需数据已在接口中返回。

ℹ️前端渲染的时候可以这样渲染，判断返回的数据的userId和当前自己的userId是不是对应，对应的话把消息放右边，否则放左边。

<hr>

10.  获取当前消息未读的信息


>请求路径：/user/friend/unread/message/list
>
>请求方式：GET
>
>接口描述：通过请求这个接口获取总的未读消息的数量和每个好友未读消息的数量

请求参数：无

响应数据

```json
{
    "code":1,
    "msg":"success",
    "data":{
        "totalCount":122,
        "userMessageResultList":[{"userId":1,"friendId":3,"count":99},	
        {"userId":1,"friendId":3,"count":88},
        {"userId":1,"friendId":3,"count":11},
        // .......]
    }
}
```

简单描述：登录成功以后，totalCount这个数据展示在主页面上（先定义一个对象存起来）。点击进入好友界面时，把这个对象中的数组的数据带到好友界面中去（当然也可以进入好友界面时再发一次请求），根据userId和friendId找到那个好友，在聊天框旁边显示一个未读的数字（参考qq）。

💡也就是说当登录成功时需要做两件事。首先建立websocket连接。然后发送请求获取当前用户未读信息数。

<hr>
11. 修改当前未读的信息

>请求路径：/user/friend/message/{friendId}
>
>请求方式：PUT
>
>接口描述：请求这个接口将所有未读信息设置成已读

请求参数：路径参数

响应数据：

```json
{
    "code":1,
    "msg":"success",
    "data":null
}
```

简单描述：用户点击进入好友界面，每次进入聊天页面时向后端发送这个请求，意味着当前这个会话的所有信息都已读。

💡也就是说当进入好友聊天界面需要发送两个请求，一个是获取聊天记录（接口`8`），一个是修改未读信息数(接口`10`)（不需要关心有没有未读信息，后端自动处理）

<hr>

12. 删除好友

>请求路径：/user/friend/{id}
>
>请求方式：DELETE
>
>接口描述：用户删除好友，进入好友界面，点击好友右侧删除按钮，删除好友  

请求参数：路径参数（好友id而非当前用户id）

响应数据：

```json
{
    "code":1,
    "msg":"success",
    "data":null
}
```

#### 五子棋模块

##### AI五子棋

**简单构思**

​	点击人机对战，出现dialog弹出框，选择人机对战难度，点击确认首先向服务器发送请求，请求用户的基本信息用于界面的用户信息展示（`接口3`），添加棋局（`接口13`）。用户是黑棋，黑棋先走占优，现在棋盘状态的传输有两种选择：

- 在每一次通信中都将一个二维数组转成json数据，发到后端，后端接口接收到这个数据塞到AI算法里面运算得出AI下一个落子坐标、当前状态（如AI胜利，玩家胜利），友好互动（我活四了哈哈，你居然活四了）、返回给前端。

  - 这种方法的优点是实现简单

  - 缺点是每次传输的数据比较多（前端采用15*15二维数组存储棋子状态，转换成json数据是225+240=465个字节）0.45KB左右

- 在每一次通信的时候前端跟服务端建立一个websocket连接，服务端负责维护这个session和一个绑定这个session的二维数组，前端每次传输时只需要传输当前棋子坐标。服务端仍进行运算，返回的数据相同。

  - 优点是传输的数据少了，只需要7个字节。
  - 缺点是实现复杂，对服务端配置要求也比较高（需要负责维护每个session，每个session都需要维护一个Bytes类型的数组）

这里我们采用第一种方案，绝对不是因为懒得实现（就好像redis用跳表不是红黑树是因为安特雷兹不会实现红黑树那样😂 ）。是因为每次传输数据很少，不到0.5KB，没必要为了节省那一些传输的数据增加服务端的额外开销。

13. ai端添加棋局

>请求路径：/game/ai/games
>
>请求方式：POST
>
>接口描述：当进入对局的时候向服务端发送一个这个请求，目的是告诉服务端要添加一个棋局

请求参数：无

返回数据

```json
{
    "code":1,
    "msg":"success",
    "data":100	// 返回的是棋局id，后续发送请求需要携带这个id
}
```



14. 人机对战棋盘数据传输

>请求路径：/game/ai/pieces
>
>请求方式：POST
>
>接口描述：通过这个接口跟服务端进行通信，获取AI下一步要走哪里且获取棋局的一些信息。

请求参数：

```json
{
    "playerMoves":{
        "x":10,	// 行
        "y":11	// 列	// 注意这里对应平面直角坐标系正好相反，注意辨别，且是从0
    },	// 当前玩家落子的坐标，用于存储到数据库
    "stepOrder":11,	// 当前轮数，用于存储到数据库，从第一轮开始
    "level":3, // 1-4 代表当前棋局的难度，用于AI生成结果
    "gameId":100,	// 用于表示当前是哪个棋局，用于存储到数据库，在前面请求中返回了
    "boardStates": [
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 2, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
    ]	// 棋盘状态数组，0 未落子 1 玩家落子(黑)	2 AI落子(白) 
}
```

响应数据：

```json
{
    "code":1,
    "msg":"success",
    "data":{
		"AIPieces":{
            "x":10,	// 第10行   
        	"y":11	// 第11列
        },
        "gameStatus":2,		// 0 用户胜利 1 AI胜利 2 平局 3棋局进行中
        "message":"你居然活四了",	// 后端对棋局状态的判断发出的互动语句，为null代表未发出。这个可以使用前端的技术进行语音播报。
        "thinkTime":10	// AI思考时间（秒）
    }
}
```

简单描述：该接口用于向服务器请求AI落子的下一步坐标，💡对于所有逻辑的判断均在后端进行，当游戏结束后，后端会自动修改棋局的状态和更新用户表的对战信息，及返回数据中gameStatus也有体现。

前端实现时可以搞两个图片，一个黑子一个白子。当数组值改变以后，根据v-if指令判断数组位置的值渲染哪一张图片，定义一个isWait变量判断当前是谁的回合。

为了增加互动性，根据战场的局势我们进行了一些判断

| 棋局状态                                        | 消息内容                                     |
| ----------------------------------------------- | -------------------------------------------- |
| AI胜利                                          | 哈哈哈，我赢了，小伙子还得练                 |
| AI双活四                                        | 防守吧，我看你能防得住我吗                   |
| AI活四                                          | 这把你能赢，除非太阳打西边出来               |
| AI双活三                                        | 我感觉游戏要结束了，铁子                     |
| AI活三                                          | 你要小心了，老铁                             |
| 玩家胜利                                        | 我就说多读点书还是有用的吧，你赢啦           |
| 玩家活四                                        | 你是职业选手吗老铁？阿尔法狗来了都得甘拜下风 |
| 玩家双活四                                      | 我的天啊，太佩服你了，我完犊子了             |
| 玩家双活三                                      | 我承认你很强，我想认输了，老铁               |
| 玩家活三                                        | 没办法啊，我必须得防你了                     |
| 平局                                            | 我们不相上下，你想再来一把吗，哈哈           |
| 局势焦灼(轮数在12,16,20,24且没有其他信息时提示) | 局势有点焦灼啊                               |



啊啊

| 棋局状态   | 消息内容                                       |
| ---------- | ---------------------------------------------- |
| AI胜利     | 这把我赢了，挑战者请不要灰心继续努力           |
| AI双活四   | 对战者，你好像要输了，呜呜呜                   |
| AI活四     | 我下了这步棋，哥哥不会怪我吧，呜呜呜           |
| AI双活三   | 慢慢来，可能还是有机会的哦                     |
| AI活三     | 你要小心了，挑战者，注意观察棋局局势           |
| 玩家胜利   | 恭喜你赢啦，在成为棋圣的路上又近了一步         |
| 玩家活四   | 你是职业选手吗挑战者？阿尔法狗来了都得甘拜下风 |
| 玩家双活四 | 哇，这次我防不住了，怎么办呢                   |
| 玩家双活三 | 你这步棋下的实在是太妙了，我真的下不过你       |
| 玩家活三   | 我这步需要防你，可不能让你得逞了               |
| 平局       | 我们不相上下，你想再来一把吗，我随时奉陪哦     |
| 局势焦灼   | 现在局势有点焦灼啊，该怎么破局呢？             |



##### 联机五子棋

**简单构思**

开发这样一个联机五子棋首先要解决的是实时性问题，肯定不能像AI那样使用http了。因此鉴于这个项目还是一个单体架构的项目，就采用websocket进行会话的维护。流程是这样的。服务端时序图参考**技术选型**部分。

<img src="C:\Users\Albert han\Desktop\iGomoku前端代码\image-20231214120206570.png" alt="image-20231214120206570" style="zoom:50%;" />

**服务端具体实现细节**

下面是下面的消息要用到的数字代表的含义

（需要注意的是如果不作为观战者身份继续加入的话，前端不需要给后端发送任何消息，点击取消关闭提示框时，设置session自动关闭即可）

| 值   | 类型                                         |
| ---- | -------------------------------------------- |
| 0    | 警告消息（表示当前房间用户超过两人了）——后端 |
| 1    | 确认作为观战者身份继续加入房间——前端         |
| 2    | 标识本次返回的是用户的角色—后端              |
| 3    | 聊天消息                                     |
| 4    | 下棋消息                                     |
| 5    | 互动消息                                     |
| 6    | 房间人数变化消息                             |



用户输入房间号，前端向后端请求建立websocket连接请求url如下`ws://localhost:8080/game/online/gomoku/{roomId}/{userId}`。后端根据携带的房间号去concurrentHashMap里进行查询，

—>如果这里面的长度大于2，返回json数据，前端提示是否仍要加入房间？加入，把对应数据添加到map里面。不加入，销毁session，重新输入房间号

服务端返回的消息

```json
{
    "type":0
}
```

前端发送的消息

```json
{
    "type":1	// 表示确认要继续
}
```



—>如果查询不到，将房间号作为键，map<Long,Actor>作为值。（Actor是一个实体类，保存的是用户角色、用户session，用户棋盘数据）

—>如果只有一个，将对应数据加入到session里面

**连接成功后**

- 跟人机对战一样，前端向后端发送一个请求用于请求用户信息（`接口3`）。
- 后端会根据roomId添加一个棋局，

- 服务器并向客户端立即返回当前用户的类型（观战者、执黑棋者、执白棋者）。用于客户端针对不同用户身份设置不同的权限和页面（如执黑棋者点击超链接下黑棋，下完棋要锁住棋盘）观战者不能下棋

```json
{
    "type":2,
    "role":"执黑棋者",
    "gameId":100
}
```



数据发送的格式(用户id在jwt中、roomId在websocket url中)

```json
{
    "type":4,	// 表明当前数据的通信类型，分别有聊天消息、互动消息、下棋消息
    "role":"执黑棋者",	// 表明当前用户的类型，分别有观战者、执黑棋者、执白棋者
    "stepOrder":12,	  // 轮数，用于存储到数据库
    "gameId":100,	// 用于表示当前是哪个棋局，用于存储到数据库，在前面请求中返回了
    "message":"(1,2)" // 要传输的消息，统一格式，都是字符串。有聊天消息、互动消息（挑衅、求饶、赞赏）、下棋消息(1,2)代表2行3列的棋子下了
}
```

传输格式(向前端广播)

```json
{
    "type":4,	// 表明当前数据的通信类型，分别有聊天消息、互动消息、下棋消息
    "id":10,	// 用户id
    "role":"执黑棋者",	// 表明当前用户的类型，分别有观战者、执黑棋者、执白棋者
    "message":"(1,2)"	// 要传输的消息，统一格式，都是字符串。有聊天消息、互动消息（挑衅、求饶、赞赏）、下棋消息(1,2)代表2行3列的棋子下了	   ,
    "isGameOver":0	// 0未结束，1黑棋胜，2白棋胜，3平
}
```

在**游戏进行中时**，服务端做的工作有

- 判断棋局状态并将消息转发广播
- 维护game_details表。
- 如果有**新的玩家加入**进来，还需要对这个房间的其他人广播当前房间里面的所有玩家。用于界面左侧列表的展示。

- 当观战者退出时，从集合中移除session，同时广播现在的所有玩家，当对战者退出时，移除所有session。

传输数据格式为

```json
{
    "type":6,
    "actors":[
        {"id":"1","role":"执黑棋者","nickname":"老张头","imageUrl":"https://alioss/1.png","userLevel":"渡劫飞升"},
        {"id":"2","role":"执白棋者","nickname":"老李头","imageUrl":"https://alioss/1.png","userLevel":"胜天半子"},
        {"id":"3","role":"观战者","nickname":"老王头","imageUrl":"https://alioss/1.png","userLevel":"小有所成"}
    ]
}
```

在**游戏结束后**。

- 服务端需要做的工作有更新user表、game_history表。
- 移除这个房间中的所有session

另外，为了提升服务端性能，当一个**room存活时间超过60分钟**时，服务端会将该session回收，同时对应的棋局信息也将会被清除。



### 后台模块

​	其实这个系统后台也不太必要，但是为了完成要求的文件下载工作，还是添加上了。所谓后台管理系统，管理什么呢？对于不同应用场景的具体需求也不一样。

​	一个典型的场景就是订票场景，用户要订票，在你这个网站买了票，预订了一些航班。同时航班机长、服务人员都需要知道这个航班的具体信息，是否延误、目前航班所预订数。且**管理人员**得实时的掌控这个航班的信息，要知道航班的准点率、哪些航班受欢迎等等，同时对于机长和服务人员账号权限进行控制。这样就是一个典型的后台管理系统实践。不难看出，后台管理系统的一些功能：

1. 权限控制。针对不同的账号显示不同的内容，赋予不同的权限
2. 数据展示。通过前台网站用户的具体信息来进行统计展示
3. 数据可视化分析。对前台网站的信息进行进一步数据分析和挖掘，同时还需要支持导出数据。

我这个系统的后台初步构思三个功能：

1. 一键导出用户数据，查看及禁用用户账号
2. 棋局数据的导出和查看
3. 网站反馈管理
4. 网站可视化分析。网站访客量，点击量，AI棋局的开局量，联机棋局的开局量（均为每天）
5. 可视化大屏。用户性别，年龄，对局时间分布。使用的用户地域分布
6. 服务器性能监控。CPU使用率，硬盘使用率

**技术选型**

为了保证和前端的一致性，本次仍采用vue3生态开发。本次我们采用一些成熟的后台管理系统模版添加。下面是我选择的原因

- 若依。这个是国内某大佬写的一个前后端的框架，Gitee上现在是排名第3，对于写后台管理系统实在是再好不过了。完整的CRUD代码生成，开发效率非常快，奈何他这个后端是需要根据模版结构的，由于我之前的后端已经开发过，再迁移的话非常麻烦，且这个我个人觉得一般是自己人用的系统，B2B的那种使用这个开发。

- vue-pure-admin。这个框架是前端框架，作者也用node写了一套后端，改一下就可以，我不选择这个的原因是这个项目的ts实在是太规范了，我对ts的了解实在是不太多，就没选择。

- Geeker-admin。这个也是使用ts写的，这个有个很香的功能就是**数据可视化大屏**，可惜那些api还是看不懂。

- vue-navie-admin。这个香是香，挺规范的，但是功能太少了，表格我没看到有，就没选。

- vue-manage-system。最后是这个，感觉功能挺完善的，各种该有的功能都有，不知道star为什么那么少，只有几十个，但是他没有导入导出功能，且状态管理工具用的vuex，且**没有文档！！！**

-  Vue3-Antd-Plus。最后选择了这个，使用vue3+js。可视化大屏也有，文档和该有的组件也都有，作者看着是个肝帝，10分好评。

![image-20231217172741863](https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/image-20231217172741863.png)



15. 获取反馈列表

>请求路径：/management/feedbacks
>
>请求方式：GET
>
>接口描述：后端向这个请求发送请求获取反馈列表

请求参数：无

响应数据：

```json
{
    "code":1.
    "msg":"success",
    "data":[
    {"id":1,"userId":3,"feedbackContent":"这是反馈内容","feedbackTime":"2023-12-18 11:14:00","fixed":true,"fixedTime":"2023-12-18 11:14:00"},
{"id":1,"userId":3,"feedbackContent":"这是反馈内容","feedbackTime":"2023-12-18 11:14:00","fixed":true,"fixedTime":"2023-12-18 11:14:00"},
{"id":1,"userId":3,"feedbackContent":"这是反馈内容","feedbackTime":"2023-12-18 11:14:00","fixed":true,"fixedTime":"2023-12-18 11:14:00"}
    ]
}
```

16. 解决反馈

>请求路径：/management/feedbacks
>
>请求方式：PUT
>
>接口描述：用于更新feedback表，一般是用于管理员解决反馈

请求参数（可选）

```json
{
    "id":1,
    "userId":3,
    "feedbackContent":"这是反馈内容",
    "feedbackTime":"2023-12-18 11:14:00",
    "fixed":true,
    "fixedTime":"2023-12-18 11:14:00"
}
```

返回数据

```json
{
    "code":1.
    "msg":"success",
    "data":null
}
```

17. 解决反馈

>请求路径：/management/feedbacks
>
>请求方式：POST
>
>接口描述：用于添加feedback表，一般是用于用户添加反馈

请求参数

```json
{
    "feedbackContent":"这是反馈内容",
    "feedbackTime":"2023-12-18 11:14:00"
}
```

返回数据

```json
{
    "code":1.
    "msg":"success",
    "data":null
}
```





### 详细功能细节

登录注册业务功能：

1. 注册

   点击注册按钮，后端使用DTO接收数据，首先向数据库发出查询语句，查询当前用户名是否唯一。

   唯一：将用户名和密码插入到数据库中，并给user表中部分字段赋值

   不唯一：向前端页面返回异常，异常信息为**用户名已经被占用了哦**

2. 登录

   点击登录按钮，后端使用登录的DTO接收到数据，向数据库发出查询语句。

   未查询到数据，向前端返回异常信息：**用户名或密码错误**

   查询到数据，将数据根据算法生成令牌，将jwt令牌和其他信息封装到一个VO对象中返回，令牌有效期为7天。

<hr>

用户其他业务功能

1. 根据id查询信息。

   查询结果封装到VO里面，返回给前端

2. 用户更新信息

   用户填写更新数据表单，点击提交。数据从前端传过来使用一个DTO接收，首先查询昵称是否更改，更改的话查询昵称是否重复，重复就抛异常：**昵称已经被占用了哦**。条件更新、向数据库发出SQL语句，是否更新成功

   更新成功：返回成功信息

   更新失败：向前端返回异常信息：**更新失败**

3. 用户注销账户

   点击注销按钮，从前端传过来一个用户id，接收信息，将逻辑删除字段设置为1、填充id字段。调用update方法即可。

   更新成功：返回成功信息

   注销失败：向前端返回异常信息：**注销失败**

<hr>

用户好友模块

1. 查询所有好友

   ​	在点击进入好友页面的时候，需要向后端发送请求，请求到所有好友数据。后端通过jwt解析出用户id，进行user表和user_friends表多表联查，好友数据封装到一个数组中进行返回，前端进行渲染。

2. 用户添加好友

   点击添加好友按钮，出现输入框让用户输入用户名/昵称，下面几种情况：

   - 输入自己的用户名或昵称。异常信息：**不允许添加自己为好友**
   - 输入的用户名或昵称查询出来的用户deleted字段为1。异常信息：**用户已注销**
   - 输入的用户名或昵称查询不到用户。异常信息：**用户名或昵称不存在**

   添加成功：返回成功信息

3. 好友聊天

   ​	每个好友旁边有一个聊天按钮，点击（弹出一个聊天框或跳转到聊天页面）这时需要新建一个websocket连接。目前仅支持在线聊天（但前端进入聊天框时需要获取历史聊天数据），默认获取10条。

### SQL建表语句

```sql
create database gomoku_online
use gomoku_online
# user表
CREATE TABLE user
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(15) NOT NULL UNIQUE,
    password VARCHAR(15) NOT NULL,
    nickname VARCHAR(15) DEFAULT '不知名小将',
    gender CHAR DEFAULT '男' NOT NULL,
    age INT DEFAULT 18 NOT NULL,
    description VARCHAR(50) DEFAULT '这个人很懒，还没有描述哦' NOT NULL,
    social_account VARCHAR(30) DEFAULT '还没有添加社交账号' NOT NULL,
    image_url VARCHAR(200) DEFAULT 'http://hsisssjz.png' NOT NULL,
    user_level VARCHAR(10) DEFAULT '初入江湖' NOT NULL,
    user_score INT DEFAULT 0 NOT NULL,
    game_total_counts INT DEFAULT 0 NOT NULL,
    game_person_counts INT DEFAULT 0 NOT NULL,
    game_ai_counts INT DEFAULT 0 NOT NULL,
    game_success_counts INT DEFAULT 0 NOT NULL,
    game_fail_counts INT DEFAULT 0 NOT NULL,
    game_dead_heat_counts INT DEFAULT 0 NOT NULL,
    online VARCHAR(3) DEFAULT '不在线' NOT NULL,
    last_online_time DATETIME,
    deleted TINYINT DEFAULT 0 NOT NULL,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL
);

# user_friends表

CREATE TABLE user_friends (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              user_id BIGINT NOT NULL,
                              friend_id BIGINT NOT NULL,
                              deleted TINYINT NOT NULL DEFAULT 0,
                              create_time DATETIME,
                              deleted_time DATETIME
);

# user_messages表
create table user_messages
(
    id           bigint auto_increment
        primary key,
    user_id      bigint                             not null,
    friend_id    bigint                             not null,
    message      varchar(500)                       not null,
    message_time datetime default CURRENT_TIMESTAMP not null,
    readed       tinyint                            null comment '标识信息是否已读（0已读 1未读）'
)
    engine = InnoDB;



#game_history表
CREATE TABLE game_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    black_id BIGINT NOT NULL,
    white_id BIGINT NOT NULL,
    begin_time DATETIME,
    end_time DATETIME,
    game_result TINYINT NOT NULL DEFAULT 2
);

#game_details表
CREATE TABLE game_details (
    id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    game_id BIGINT NOT NULL,
    move_x TINYINT NOT NULL,
    move_y TINYINT NOT NULL,
    color TINYINT NOT NULL,
    step_order INT NOT NULL
);

# admin表
CREATE TABLE admin (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(15) NOT NULL UNIQUE,
    nickname VARCHAR(15) NOT NULL DEFAULT '不知名管理员',
    password VARCHAR(15) NOT NULL,
    avatar VARCHAR(100) NOT NULL DEFAULT 'https://th.bing.com/th/id/OIP.o0eECfDkWwVfz3-3lwKd5AAAAA?rs=1&pid=ImgDetMain',
    type VARCHAR(15) NOT NULL,
    status BOOLEAN DEFAULT TRUE,
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP
);


# feedback表
CREATE TABLE feedback (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    feedback_content VARCHAR(500) NOT NULL,
    feedback_time DATETIME,
    fixed_time DATETIME
);

# website_day_info表
CREATE TABLE website_day_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    new_user_count INT DEFAULT 0,
    visitor_count INT NOT NULL DEFAULT 0,
    website_clicks INT NOT NULL DEFAULT 0,
    ai_game_counts INT NOT NULL DEFAULT 0,
    human_game_count INT NOT NULL DEFAULT 0,
    record_date DATE
);
# website_visitor_details_info
CREATE TABLE website_visitor_details_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) DEFAULT '未知用户',
    ip VARCHAR(40) DEFAULT '未知ip',
    address VARCHAR(30) DEFAULT '未知地址',
    access_time DATETIME DEFAULT CURRENT_TIMESTAMP
);



```

   

## 技术概述

### 五子棋AI算法

  本项目的算法是基于极大极小搜索+Alpha-beta剪枝+评估函数来实现的

**概述：**

​		2016年3月AlphaGo战胜围棋世界冠军李世石，计算机博弈算法又重新进入了人们的视野，AlphaGo的核心算法有走棋网络、快速走子、估值网络、蒙特卡罗树搜索。本项目五子棋AI算法虽不与AlphaGo不尽相同，但也有异曲同工之妙。本项目编写程序采用极大极小搜索对五子棋的每一步走法进行模拟，在每一轮中AI方使得分极大化、对手方使得分极小化。五子棋虽说是相对比较简单的棋种（已在1994年被Allis, Louis Victor两位计算机科学家证明黑棋有必胜的走法，但也有禁手的应对策略）。但是如果想要穷举出五子棋的所有走法显然是不现实的，指数级时间复杂度，在搜索深度为3的情况下，可能是225*224*223= 11239200。一千多万次搜索（粗略估计，可使用启发式搜索）。因此alpha-beta剪枝算法应运而生。它是通过每一层的节点维护alpha和beta值来减少搜索次数。在搜索到最底层时，我们需要对当前棋局状态打一个分。以便让AI决定是否选择。这就是评估函数。详细描述如下：

**极大极小搜索：**

 		我们设置搜索深度（代表的是模拟几次落子，比如depth=4就是AI先下一步，对手再下一步，AI再下一步，对手再下一步。模拟两轮4次），然后进行递归搜索在递归搜索到最底层的时候，会为当前的棋局状态打个分。在递归的值返回后，程序判断这一层模拟的是AI落子或对手落子，当AI落子的话，尝试让本层的得分极大化，筛选出得分极大化的坐标落子，返回给上一层。这时会轮到对手落子，假设对手是绝顶聪明的。这个时候他肯定会选择让得分极小化的坐标落子。

这样一来一回，通过极大极小搜索可以找到AI要落子的最佳坐标。

**注**：评估函数得出的得分是相对玩家的

### Alpha-beta剪枝

​		上面的问题很明显，问题是博弈树的节点太多，遍历所需时间太久或者不可能完成。Alpha-Beta剪枝算法让我们维护Alpha值和Beta值。Alpha值代表在目前探索的路径中，最大化玩家（我们）所能确保的最低得分。Beta值代表目前探索的路径中，最小化玩家（对手）所能确保的最高得分。对于每个节点（游戏的一个状态），都负责维护一个Alpha值和一个beta值。在我们的回合中，我们更新Alpha值。在对手的回合中，他负责更新beta值。一旦说，这个节点的Alpha值大于或等于Beta，那么就可以把这个节点和他的子树都剪掉，因为对手不会允许我们走到那个地方。这就是剪枝的逻辑

### 评估函数

​		在递归到最深层的时候，我们需要一个定量化的打分函数来对这个棋局打个分。这个打分函数是AI是否足够“聪明”的重点。他需要考虑这个局面对我有利的形式和对对手不利的形式。需要兼顾进攻和防守。在本项目中，我采用的是对每一个空位和落子都进行评估，对空位，我评估如果对手落在这里对我的影响。对对手的棋子，我评估当前棋局对手的得分，对我的棋子，我评估我自己的得分。这样得出一个综合的得分。

### AI算法改进

​		在本项目中，仅仅实现了一个基本版本的五子棋AI。由于本人能力与时间有限，没有再去探索那些优化的算法。如

**启发式搜索：**

先对当前节点的得分估计一下排个序，这样剪枝函数就可以更好的工作，剪掉更多的枝。

对每次下棋的时候，我们往往是在有棋子的附近进行下棋，不会去抢占那些没有战略意义的位置。因此在搜索时可以把附近没有棋子的点给剪枝掉。（这个附近的定义一般是2）

**启发式评估函数：**

本项目中为了实现简便，对于很多空位都进行了评估，实际上是没有必要的。具体参考启发式搜索的部分2

**蒙特卡罗搜索：**

以大数定律为理论依据的蒙特卡罗思想衍生出来的蒙特卡罗搜索树算法(MCTS)。通过不断地模拟找到一个近似的最优解。即为背景所述中AlphaGo所使用的算法。这就是另一类实现算法了，并非本项目中的逻辑。

### websocket的使用

​		在聊天和联机都使用到了WebSocket。WebSocket是基于TCP的，全双工，常用于一些实时性要求较高的场合。需要指出的是，在聊天时使用的websocket连接是一登录网站就需要创建的，用于实现表示用户在线状态的功能。

​		WebSocket数据传输的格式不限，但一般均使用json数据传输，在接口设计时。用户聊天数据的传输用户携带自己id，目标用户id，消息。发送给服务端，服务端再把这个消息转发给要聊天的人。

​		在联机对战中，由于功能较多，因此在消息传输设计时，我们使用一个type字段来标识当前是哪类消息有0-7 8种数据类型的消息，如聊天消息，下棋消息，房间人数变化消息（详细接口信息见接口文档）。服务端需要维护一个concurrentHashMap里面存储的键是房间号，值为另一个map，另一个map的键是用户id，值为用户角色等信息。最后还需要一个map存储每个房间的棋盘信息。

连接建立时要做的工作如下方的流程图.除此之外，服务端还需要新增棋局数据、初始化棋盘。还需要向所有session广播人数变化

![img](https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/clip_image002.jpg)

在连接发送消息的过程中。不同type类型的消息对应不同的逻辑，其中最为核心的是type=4的数据。下棋数据。服务端要做三件事

1：判断棋局状态并广播消息。

2：维护game_details表。

3：判断棋局是否结束。棋局结束后要维护game_history和user表，并移除房间中所有session。

###    Alpha-beta剪枝理论

Alpha-Beta 剪枝是一种用于减少极大极小搜索树节点数量的优化技术。它的基本思想是在搜索过程中忽略那些不可能改善已知结果的分支，从而减少搜索空间，提高效率。在这种方法中，两个重要的参数是α（Alpha）和β（Beta）值，它们分别代表了在搜索过程中已发现的最佳移动的下界（Alpha）和上界（Beta）。

Alpha值（α）

- **意义**：Alpha是在极大节点（AI）的搜索过程中，我们已经找到的最好的选择的最低分数。
- **用途**：在搜索树的极小节点（对手）中，如果发现一个移动的评分低于或等于Alpha，那么就不需要考虑这个移动，因为AI已经有更好的选择了。

 Beta值（β）

- **意义**：Beta是在极小节点（对手）的搜索过程中，对手已经找到的最好的选择的最高分数。
- **用途**：在搜索树的极大节点（AI）中，如果发现一个移动的评分高于或等于Beta，那么就不需要考虑这个移动，因为对手不会允许这种情况发生。

Alpha-Beta剪枝的依据

剪枝发生在以下两种情况：

1. **在极小节点**：如果一个移动的评分**低于或等于Alpha**，那么这个节点的其他分支就不需要再考虑，因为AI已经有一个更好的选择了（即Alpha值）。
2. **在极大节点**：如果一个移动的评分**高于或等于Beta**，那么这个节点的其他分支就不需要再考虑，因为对手（极小节点）会选择一个更低的分数来避免AI得到这种高分（即Beta值）。

参考文章：[【算法】极大极小α-β剪枝算法 - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/65219446)

   这里由于时间有限和本人能力问题，搞了一天也才仅仅实现了一个最基础版本的博弈树，极大极小算法+alpha-beta剪枝算法的五子棋AI版本，现在depth=1-3是可以应用的（搜索时间不到1秒），但是depth为4时在一些情况下可能最差时间会搜索几分钟（搜索亿量级的节点），最好时间在5秒内可以结束。后续可以进行剪枝优化和启发式搜索。如

- 在进行下棋时，每次下棋的落点肯定是在下棋点的附近。因此我们在进行计算时可以把那些附近没有（一般设置为2）棋子的节点进行剪枝。以提高效率。
- alpha-beta剪枝时可以对节点的值进行一个排序（走法，历史）。这样可以更早的剪枝。
- 存储已经计算过的状态（DP思想）
- 判断结束状态，尽早结束

还可以使用不同的算法，如以大数定律为理论依据的蒙特卡罗思想衍生出来的**蒙特卡罗搜索树算法(MCTS)**。通过不断地模拟找到一个近似的最优解。







## 踩坑日记

- websocket不能访问到spring管理的bean
- websocket没有通过拦截器
- el-dialog点击后遮罩层没了，或显示不出来
- keys不能
- npm install rimraf -g        rimraf node_modules
- datav的安装使用和使用边框时会出现的问题。padding加错位置了
- 使用全屏容器时需要封装成组件
- 在 Vue 和 Pinia 中，直接将一个对象赋值给另一个响应式对象时，你会完全替换原有对象，而不是合并它们的属性。这就是为什么你的原始 `userInfo` 对象中的字段被 `formModel` 中的字段完全替换的原因。
- 持久化插件不能持久化的原因
- 部署问题，插入数据不要随便插。CORS可能是没有放开端口
- windows mysql不区分大小写，linux区分
- 每次打包后是会变的
- 引用静态资源的问题
- 需要删除镜像重新up
- 关于audio音频播放的问题，如果切换当前角色，有的音频还未

出现这种情况，即使你更改了下拉框同时更新了音频的`src`属性，但仍然出现错误，可能是由于音频播放的请求被新的加载请求打断。这种情况通常发生在浏览器尚未完全加载新的音频文件时，就尝试播放该音频。

当你更改下拉框的选项，触发了音频`src`属性的更新，浏览器会开始加载新指定的音频文件。如果在这个加载过程中，代码试图播放音频（例如，自动播放或由于某些逻辑触发了播放），浏览器可能还没有足够的数据来开始播放，或者正在处理前一个音频的加载请求，这就会导致播放请求被打断，从而出现错误。

以下是几个可能的原因和解决方案：

1. **异步加载问题**：浏览器加载音频是异步的，而播放音频可能在加载完成之前就被触发了。解决这个问题的一个方法是使用`loadeddata`或`canplay`事件来确保音频加载完成后再进行播放。

2. **自动播放限制**：现代浏览器对自动播放媒体有严格的限制，特别是如果没有用户交互。确保音频播放是由用户的明确动作触发的，例如点击事件。

3. **播放和加载的同步**：在更改音频`src`并尝试播放之前，确保先暂停当前的播放，这可以帮助避免播放请求被新的加载请求打断。

4. **检查代码逻辑**：确保在更改音频源时，不会立即调用播放方法。可能需要重新审视你的代码逻辑，确保在更新源后有足够的时间让浏览器加载音频。

通过这些方法，你应该能够更有效地管理音频的加载和播放，避免因为加载和播放请求的冲突而导致的错误。

runtime-dom.esm-bundler.js:536  Uncaught (in promise) DOMException: The play() request was interrupted by a new load request.

- isWait为true是下的时候就为true



- git push的时候push不进去，是因为github默认分支是main，gitee默认分支是master。因此就会产生冲突，解决方法是

  1. 这个命令设置您的本地 `main` 分支跟踪远程的 `gitee` 仓库的 `master` 分支。

  ​    git branch -u gitee/master main

  ```
  2. 推上去git push gitee main:master
  
  git pull gitee master:main
  拉下来，从master——>main
  ```



Vue在同一路由变化时默认不渲染相同路径的组件，这是Vue的一个默认机制，目的的为了防止不必要的性能开销，默认会重用组件实例。

加一个这个

```vue
<router-view :key="$route.fullPath"></router-view>
```

监听器，手动实现刷新



清除github缓存

curl -X PURGE  https://camo.githubusercontent.com/03e93e7ecfa8a63131bdc1ac1eab57c6c995bd6daa61eab56bdd20dc4a71abb3/68747470733a2f2f6d792d706963747572652d626564312d313332313130303230312e636f732e61702d6265696a696e672e6d7971636c6f75642e636f6d2f6d7970696374757265732f7765636861742e6a7067

