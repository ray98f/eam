package com.wzmtr.eam.utils;

import io.minio.*;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * MINIO工具类
 *
 * @author zhangxin
 * @version 1.0
 * @date 2021/7/5 9:24
 */
@Component
public class MinioUtils {

    /**
     * 桶占位符
     */
    private static final String BUCKET_PARAM = "${bucket}";

    /**
     * bucket权限-只读
     */
    private static final String READ_ONLY = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetBucketLocation\",\"s3:ListBucket\"],\"Resource\":[\"arn:aws:s3:::" + BUCKET_PARAM + "\"]},{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetObject\"],\"Resource\":[\"arn:aws:s3:::" + BUCKET_PARAM + "/*\"]}]}";


    /**
     * bucket权限-只写
     */
    private static final String WRITE_ONLY = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetBucketLocation\",\"s3:ListBucketMultipartUploads\"],\"Resource\":[\"arn:aws:s3:::" + BUCKET_PARAM + "\"]},{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:AbortMultipartUpload\",\"s3:DeleteObject\",\"s3:ListMultipartUploadParts\",\"s3:PutObject\"],\"Resource\":[\"arn:aws:s3:::" + BUCKET_PARAM + "/*\"]}]}";

    /**
     * bucket权限-读写
     */
    private static final String READ_WRITE = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetBucketLocation\",\"s3:ListBucket\",\"s3:ListBucketMultipartUploads\"],\"Resource\":[\"arn:aws:s3:::" + BUCKET_PARAM + "\"]},{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:DeleteObject\",\"s3:GetObject\",\"s3:ListMultipartUploadParts\",\"s3:PutObject\",\"s3:AbortMultipartUpload\"],\"Resource\":[\"arn:aws:s3:::" + BUCKET_PARAM + "/*\"]}]}";


    @Autowired
    public MinioClient instance;

    /**
     * 判断 bucket是否存在
     *
     * @param bucketName
     * @return
     */
    public boolean bucketExists(String bucketName) {
        try {
            return instance.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 创建 bucket
     *
     * @param bucketName
     */
    public void makeBucket(String bucketName) {
        try {
            instance.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            setPolicy(bucketName, "default");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeObject(String bucketName, String objectName) {
        try {
            instance.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPolicy(String bucketName, String policy) {
        try {
            switch (policy) {
                case "WRITE_ONLY":
                    instance.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(WRITE_ONLY.replace(BUCKET_PARAM, bucketName)).build());
                    break;
                case "READ_WRITE":
                    instance.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(READ_WRITE.replace(BUCKET_PARAM, bucketName)).build());
                    break;
                default:
                    instance.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(READ_ONLY.replace(BUCKET_PARAM, bucketName)).build());
                    break;
            }
        } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
                 | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
                 | IllegalArgumentException | IOException e) {
            e.printStackTrace();
        }
    }


}
