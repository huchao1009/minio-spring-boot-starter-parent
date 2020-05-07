package com.opensource.minio.autoconfigure.service;

import com.opensource.minio.autoconfigure.vo.MinioItem;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.Result;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Minio操作模板对象
 */
public class MinioTemplate {

    private String endpoint;
    private String accessKey;
    private String secretKey;

    public MinioTemplate() {
    }

    /**
     * @param endpoint  minio URL
     * @param accessKey accessKey.
     * @param secretKey secretKey.
     */
    public MinioTemplate(String endpoint, String accessKey, String secretKey) {
        this.endpoint = endpoint;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    /**
     * 创建具有默认区域的新存储桶。
     *
     * @param bucketName 存储桶名称
     * @throws XmlPullParserException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws IOException
     * @throws InvalidPortException
     * @throws InvalidEndpointException
     * @throws RegionConflictException
     * @throws NoResponseException
     * @throws InternalException
     * @throws ErrorResponseException
     * @throws InsufficientDataException
     * @throws InvalidBucketNameException
     */
    public void createBucket(String bucketName) throws XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, IOException, InvalidPortException, InvalidEndpointException, RegionConflictException, NoResponseException, InternalException, ErrorResponseException, InsufficientDataException, InvalidBucketNameException {
        MinioClient client = getMinioClient();
        if (!client.bucketExists(bucketName)) {
            client.makeBucket(bucketName);
        }
    }

    /**
     * 列出所有的存储桶
     *
     * @return
     * @throws InvalidPortException
     * @throws InvalidEndpointException
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws InsufficientDataException
     * @throws InternalException
     * @throws NoResponseException
     * @throws InvalidBucketNameException
     * @throws XmlPullParserException
     * @throws ErrorResponseException
     */
    public List<Bucket> getAllBuckets() throws InvalidPortException, InvalidEndpointException, IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        return getMinioClient().listBuckets();
    }

    public Optional<Bucket> getBucket(String bucketName) throws InvalidPortException, InvalidEndpointException, IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        return getMinioClient().listBuckets().stream().filter(b -> b.name().equals(bucketName)).findFirst();
    }

    /**
     * 删除存储桶，removeBucket不会删除bucket中的对象。需要使用removeObject API删除这些对象。
     *
     * @param bucketName Name of the bucket
     * @throws InvalidPortException
     * @throws InvalidEndpointException
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws InsufficientDataException
     * @throws InternalException
     * @throws NoResponseException
     * @throws InvalidBucketNameException
     * @throws XmlPullParserException
     * @throws ErrorResponseException
     */
    public void removeBucket(String bucketName) throws InvalidPortException, InvalidEndpointException, IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        getMinioClient().removeBucket(bucketName);
    }

    /**
     * 在给定的bucket、前缀和递归标志中获取对象列表
     * @param bucketName
     * @param prefix
     * @param recursive
     * @return
     * @throws InvalidPortException
     * @throws InvalidEndpointException
     */
    public List<MinioItem> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) throws InvalidPortException, InvalidEndpointException {
        List objectList = new ArrayList();
        Iterable<Result<Item>> objectsIterator = getMinioClient().listObjects(bucketName, prefix, recursive);
        objectsIterator.forEach(i -> {
            try {
                objectList.add(new MinioItem(i.get()));
            } catch (Exception e) {
                new Exception(e);
            }
        });
        return objectList;
    }


    /**
     * 为HTTP GET操作生成预签名的URL。
     * 浏览器/移动客户端可以指向这个URL直接下载对象，即使bucket是私有的。
     * 此预签名的URL可以有一个相关的过期时间（秒），在该时间之后，它将不再运行。默认到期日设置为7天
     *
     * @param bucketName 桶名称
     * @param objectName 对象名称
     * @param expires    过期时间（单位：秒，默认7天）
     * @return
     * @throws InvalidPortException
     * @throws InvalidEndpointException
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws InsufficientDataException
     * @throws InvalidExpiresRangeException
     * @throws InternalException
     * @throws NoResponseException
     * @throws InvalidBucketNameException
     * @throws XmlPullParserException
     * @throws ErrorResponseException
     */
    public String getObjectURL(String bucketName, String objectName, Integer expires) throws InvalidPortException, InvalidEndpointException, IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidExpiresRangeException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        return getMinioClient().presignedGetObject(bucketName, objectName, expires);
    }

    /**
     * 获取对象的完整URL
     *
     * @param bucketName 桶名称
     * @param objectName bucket中的对象名
     * @return
     * @throws InvalidPortException
     * @throws InvalidEndpointException
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws InsufficientDataException
     * @throws InternalException
     * @throws NoResponseException
     * @throws InvalidBucketNameException
     * @throws XmlPullParserException
     * @throws ErrorResponseException
     */
    public String getObjectUrl(String bucketName, String objectName) throws InvalidPortException, InvalidEndpointException, IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        return getMinioClient().getObjectUrl(bucketName, objectName);
    }

    /**
     * 将给定文件作为给定bucket中的对象上载。
     * 如果对象大于5MB，客户端将自动使用多部分会话。
     * 如果多部分会话失败，则上载的部分将自动中止
     *
     * @param bucketName  桶名称
     * @param objectName  bucket中的对象名
     * @param stream      文件流
     * @param size        文件大小
     * @param contentType 文件类型
     * @throws InvalidPortException
     * @throws InvalidEndpointException
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws InsufficientDataException
     * @throws InvalidArgumentException
     * @throws InternalException
     * @throws NoResponseException
     * @throws InvalidBucketNameException
     * @throws XmlPullParserException
     * @throws ErrorResponseException
     */
    public void putObject(String bucketName, String objectName, InputStream stream, long size, String contentType) throws InvalidPortException, InvalidEndpointException, IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        getMinioClient().putObject(bucketName, objectName, stream, size, contentType);
    }

    /**
     * 获取对象的元数据
     *
     * @param bucketName 桶名称
     * @param objectName bucket中的对象名
     * @return
     * @throws InvalidPortException
     * @throws InvalidEndpointException
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws InsufficientDataException
     * @throws InternalException
     * @throws NoResponseException
     * @throws InvalidBucketNameException
     * @throws XmlPullParserException
     * @throws ErrorResponseException
     */
    public ObjectStat getObjectInfo(String bucketName, String objectName) throws InvalidPortException, InvalidEndpointException, IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        return getMinioClient().statObject(bucketName, objectName);
    }

    /**
     * 删除存储桶中的对象
     *
     * @param bucketName 桶名称
     * @param objectName bucket中的对象名
     * @throws InvalidPortException
     * @throws InvalidEndpointException
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws InsufficientDataException
     * @throws InternalException
     * @throws NoResponseException
     * @throws InvalidBucketNameException
     * @throws XmlPullParserException
     * @throws ErrorResponseException
     * @throws InvalidArgumentException
     */
    public void removeObject(String bucketName, String objectName) throws InvalidPortException, InvalidEndpointException, IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, InvalidArgumentException {
        getMinioClient().removeObject(bucketName, objectName);
    }


    /**
     * 创建MinioClient对象
     *
     * @return
     * @throws InvalidPortException
     * @throws InvalidEndpointException
     */
    public MinioClient getMinioClient() throws InvalidPortException, InvalidEndpointException {
        return new MinioClient(endpoint, accessKey, secretKey);
    }
}
