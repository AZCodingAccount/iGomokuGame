package com.gobanggame;

import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;

import java.io.*;
import java.util.concurrent.TimeUnit;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-19 23:26
 * @description: 测试根据ip查询地域
 **/
public class IpTest {
    private static final Logger log = LoggerFactory.getLogger(IpTest.class);

    public Searcher createSearcher() {
        String resourcePath = "/ip/ip2region.xdb";
        File tempFile = null;
        Searcher searcher = null;

        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                log.error("资源未找到: {}", resourcePath);
                return null;
            }

            // 创建临时文件
            tempFile = File.createTempFile("ip2region", ".xdb");
            try (OutputStream outputStream = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            // 使用临时文件路径创建 Searcher 对象
            searcher = Searcher.newWithFileOnly(tempFile.getAbsolutePath());

        } catch (IOException e) {
            log.error("failed to create searcher: ", e);
        }

        // 返回 Searcher 对象，记得在不需要时删除临时文件
        return searcher;
    }


    /**
     * 获取ip属地(文件扫描)
     *
     * @param ip IP地址
     * @return 返回地址
     */
    public static String getCityInfoByFile(String ip) {


        // 1、创建 searcher 对象
        IpTest myClass = new IpTest();
        Searcher searcher = myClass.createSearcher();

        // 2、查询
        try {
            long sTime = System.nanoTime();
            String region = searcher.search(ip);
            long cost = TimeUnit.NANOSECONDS.toMicros(System.nanoTime() - sTime);
            log.info("{region: {}, ioCount: {}, took: {} μs}", region, searcher.getIOCount(), cost);
            return region;
        } catch (Exception e) {
            log.info("failed to search({}): ", ip, e);
        }
        return null;
        // 3、备注：并发使用，每个线程需要创建一个独立的 searcher 对象单独使用。
    }

    public static void main(String[] args) throws Exception {
        String cityInfoByFile = getCityInfoByFile("111.0.72.130");
        if (cityInfoByFile != null) {
            System.out.println(cityInfoByFile.split("\\|")[2]);
        }
    }

}
