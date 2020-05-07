package com.opensource.minio.autoconfigure.vo;

import io.minio.ObjectStat;
import lombok.Data;

import java.util.Date;

@Data
public class MinioObject {

    private String bucketName;
    private String name;
    private Date createdTime;
    private long length;
    private String etag;
    private String contentType;
    private String matDesc;

    public MinioObject(String bucketName, String name, Date createdTime, long length, String etag, String contentType, String matDesc) {
        this.bucketName = bucketName;
        this.name = name;
        this.createdTime = createdTime;
        this.length = length;
        this.etag = etag;
        this.contentType = contentType;
        this.matDesc = matDesc;
    }

    public MinioObject(ObjectStat os) {
        this.bucketName = os.bucketName();
        this.name = os.name();
        this.createdTime = os.createdTime();
        this.length = os.length();
        this.etag = os.etag();
        this.contentType = os.contentType();
        this.matDesc = os.matDesc();
    }

}
