package com.gobanggame.utils;

import com.aliyun.oss.*;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.ObjectMetadata;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

@Data
@AllArgsConstructor
@Slf4j
public class AliOssUtil {

    private String endpoint;    // 端点（在哪个地域）
    private String accessKeyId; // 访问的key
    private String accessKeySecret; // 访问的秘钥
    private String bucketName;  // 桶名称

    /**
     * @author AlbertZhang
     * @description 上传文件对象，都是固定写法
     * @date 2023-12-25 15:46
     * @param bytes
     * @param objectName
     * @param fileName
     * @return java.lang.String
     **/
    public String upload(byte[] bytes, String objectName, String fileName) {

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentDisposition("inline");

            // 创建PutObject请求。
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(bytes));

            // 验证URL访问是否直接下载。
            Date expiration = new Date(new Date().getTime() + 3600 * 1000);
            GeneratePresignedUrlRequest signRequest = new GeneratePresignedUrlRequest(bucketName, objectName, HttpMethod.GET);
            signRequest.setExpiration(expiration);
            URL signedUrl = ossClient.generatePresignedUrl(signRequest);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        // 文件访问路径规则 https://BucketName.Endpoint/ObjectName
        StringBuilder stringBuilder = new StringBuilder("https://");
        stringBuilder
                .append(bucketName)
                .append(".")
                .append(endpoint)
                .append("/")
                .append(objectName);

        log.info("文件上传到:{}", stringBuilder.toString());

        return stringBuilder.toString();
    }
}
