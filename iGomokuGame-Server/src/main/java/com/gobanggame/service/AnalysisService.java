package com.gobanggame.service;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-19 23:35
 * @description:
 **/
public interface AnalysisService {


    /**
     * @author AlbertZhang
     * @description 添加网站访客信息
     * @date 2023-12-19 23:39
     * @param ipAddr
     * @param cityInfoByFile
     * @return void
     **/
    void WebsiteClick(String ipAddr, String cityInfoByFile);



}
