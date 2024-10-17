# iGomokuGame

<hr>

## 介绍📘

​		iGomokuGame是一个基于**Vue3**、**SpringBoot3**，**Element-Plus**、**WebSocket**、**SpringTask**、**DataV**、**五子棋AI算法**等实现的一款在线五子棋游戏。支持**排行榜**、**好友**、**人机对战**、**人机互动**、**联机对战**、**数据可视化分析**等功能。项目使用最新技术开发，开发过程规范，逻辑严谨。

B站项目介绍地址： https://b23.tv/ph4Oz5l

## 在线预览👀

- 前台：http://game.bugdesigner.cn		账号`demo1`:`demo1`。`demo2`:`demo2`。`demo3`:`demo3` ，您也可以自行注册账号
- 后台：http://game.bugdesigner.cn/admin    账号：`admin` 密码：`admin`

ℹ️ 对于用户端，您可能需要**Ctrl+滚轮**将屏幕缩放到一定尺寸（一般是80%）

ℹ️ 第一次访问请耐心等待浏览器从服务器拉取资源

## 注意🔒

由于本项目主要为演示项目，如需注册账号，请避免使用个人常用用户名与密码，以免因数据泄露被黑客获取进行**撞库攻击**

## 代码仓库🌟

- Gitee：https://gitee.com/Albert_han/i-gomoku-game
- GitHub：https://github.com/AZCodingAccount/iGomokuGame

## 项目亮点💡

1. 技术新。采用最新版企业主流单体应用开发技术。
2. 应用新。探索AI语音、互动交友与棋类游戏结合的新可能。
3. 算法新。实现经典算法并加入原创部分。
4. 有广度。完整CRUD、AI算法、数据实时通信、数据可视化应用等

## 项目技术应用🛠️

1. 本项目采用`Vue3`+`SpringBoot3`为主要开发技术。

2. 使用`Spring Task`技术实现定时任务,自动关闭房间。

3. 使用`WebSocket`技术实现用户聊天与联机对战的实现。

4. 使用`博弈树`+`极大极小搜索`+`Alpha-Beta剪枝`+`评估函数`实现人机对战

5. 使用`Echarts`和`DataView`实现数据可视化

6. `HTTP短轮询`实现数据可视化中数据的更新

7. 使用前端的`Excel`包实现数据的导入导出

8. 使用`animate.css`实现游戏结束时动画的播放

9. 使用`TTS`技术实现文本转语音完成AI与用户互动的功能

10. 基于`knife4j`遵循`OpenAPI3`注解规范自动生成接口文档

11. 使用`IP2Region`实现根据IP获取用户地址

## 快速开始 🚀

- **拉取项目** (您需要先安装Git)

```bash
# Gitee
git pull https://gitee.com/Albert_han/i-gomoku-game.git
# GitHub
git pull https://github.com/AZCodingAccount/iGomokuGame
```

- **运行前端项目**

```bash
cd 前端项目目录
pnpm i	   # 安装依赖
pnpm dev   # 运行程序	
```

- **运行数据库脚本**

init.sql文件在  `/iGomokuGame-Server/src/main/resources/sql`目录下。

1：右键使用idea运行

2：找到一款数据库管理工具，如Navicat ，DataGrip等，导入sql文件，运行。

3：使用命令行运行

```bash
mysql -u username -p < ./init.sql	# sql文件的相对或绝对路径
```

- **运行后端项目**

1：右键使用idea打开。点击run即可运行

2：使用命令行运行

```bash
 cd 前端项目目录
 mvn clean package	# 打包项目
 cd target 
 java -jar jar包名  # 运行项目
```

**项目部署**

项目部署请移步博客文章：[Docker部署Java项目的步骤 ](https://blog.bugdesigner.cn/docker部署java项目的步骤/)

## 项目部分界面预览👁️

- 主页

![image-20231226160043863](https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/image-20231226160043863.png)

- 人机对战页

![image-20231226191604276](https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/image-20231226191604276.png)



- 后台首页

![image-20231226160300415](https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/image-20231226160300415.png)



- 可视化大屏

![image-20231226160435752](https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/image-20231226160435752.png)



- 流量分析页

![image-20231226160507134](https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/image-20231226160507134.png)

## 鸣谢🌹

1. TTS文本转演讲技术 

​		AI嘉然：模型作者：`Xz乔希`

​		AI丁真：魔塔社区项目地址：https://modelscope.cn/studios/MiDd1Eye/DZ-Bert-VITS2/summary

​	二者基于Bert-VITS2模型，Github项目地址：  https://github.com/fishaudio/Bert-VITS2

2. DataV数据可视化技术

   官网：http://datav.jiaminghi.com/guide/

3. Knife4j

​		Github项目地址：https://github.com/xiaoymin/knife4j

2. IP2Region

   Github项目地址：https://github.com/lionsoul2014/ip2region

3. 五子棋AI算法参考博客

   [【算法】极大极小α-β剪枝算法 - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/65219446)

## 捐赠🍵

如果您认为这个项目对您有帮助，可以通过下面方式支持我

- Star、Fork、Watch 一键三连 🌟
- 通过微信，支付宝请我喝杯奶茶 ❤

|                             微信                             |                            支付宝                            |
| :----------------------------------------------------------: | :----------------------------------------------------------: |
| <img src="https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/wechat.jpg" alt="wechat" style="zoom: 15%;margin:0 auto" /> | <img src="https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/alipay.jpg" alt="alipay" style="zoom:15%;margin:0 auto" /> |



