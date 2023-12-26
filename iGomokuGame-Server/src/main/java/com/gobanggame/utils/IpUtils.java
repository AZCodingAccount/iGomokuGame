package com.gobanggame.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.lionsoul.ip2region.xdb.Searcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;


/*
*
* 这个类是调用IPRegion这个开源库和自己编写的方法来获取访客的真实ip和这个ip对应的地址
* */
public class IpUtils {


    // 获取ip地址，从不同的请求头获取
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP，多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        // 处理异常情况，ipv6
        if (Objects.equals(ipAddress, "0:0:0:0:0:0:0:1")) {
            ipAddress = "127.0.0.1";
        }
        return ipAddress;
    }

    /**
     * 获取mac地址
     */
    public static String getMacAddress() throws Exception {
        // 取mac地址
        byte[] macAddressBytes = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
        // 下面代码是把mac地址拼装成String
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < macAddressBytes.length; i++) {
            if (i != 0) {
                sb.append("-");
            }
            // mac[i] & 0xFF 是为了把byte转化为正整数
            String s = Integer.toHexString(macAddressBytes[i] & 0xFF);
            sb.append(s.length() == 1 ? 0 + s : s);
        }
        return sb.toString().trim().toUpperCase();
    }

    private static final Logger log = LoggerFactory.getLogger(IpUtils.class);

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
        IpUtils myClass = new IpUtils();
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

}
