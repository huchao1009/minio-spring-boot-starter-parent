## minio-spring-boot-starter

### 一、简介

- 说明：文件存储服务minio操作组件


### 二、引入依赖

###### gradle：
```groovy
    compile 'com.opensource.component:minio-spring-boot-starter:1.0.0-SNAPSHOT'
```

###### maven:
```xml
    <dependency>
          <groupId>com.opensource.component</groupId>
          <artifactId>minio-spring-boot-starter</artifactId>
          <version>1.0.0-SNAPSHOT</version>
    </dependency>
```
### 三、引入配置

```yml
minio:
  url: http://files.domain.con:81
  access-key: you_key
  secret-key: you_secret
```

### 四、构建发布

```groovy
gradle -x test clean build publish
```

### 五、使用例子

```java
public class MinioEndpoint {

    @Autowired
    private MinioTemplate template;


    /**
     * 创建具有默认区域的新存储桶。
     *
     * @param bucketName
     * @return
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws XmlPullParserException
     * @throws InvalidPortException
     * @throws ErrorResponseException
     * @throws InternalException
     * @throws NoResponseException
     * @throws InvalidBucketNameException
     * @throws InsufficientDataException
     * @throws InvalidEndpointException
     * @throws RegionConflictException
     */
    @PostMapping("/bucket/{bucketName}")
    public Bucket createBucker(@PathVariable String bucketName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidPortException, ErrorResponseException, InternalException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, RegionConflictException {
        template.createBucket(bucketName);
        return template.getBucket(bucketName).get();
    }

    /**
     * 列出所有的存储桶
     *
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws InvalidPortException
     * @throws ErrorResponseException
     * @throws NoResponseException
     * @throws InvalidBucketNameException
     * @throws InsufficientDataException
     * @throws InvalidEndpointException
     * @throws InternalException
     */
    @GetMapping("/bucket")
    public List<Bucket> getBuckets() throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidPortException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, InternalException {
        return template.getAllBuckets();
    }

    /**
     * 获取存储桶信息
     *
     * @param bucketName
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws InvalidPortException
     * @throws ErrorResponseException
     * @throws NoResponseException
     * @throws InvalidBucketNameException
     * @throws InsufficientDataException
     * @throws InvalidEndpointException
     * @throws InternalException
     */
    @GetMapping("/bucket/{bucketName}")
    public Bucket getBucket(@PathVariable String bucketName) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidPortException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, InternalException {
        return template.getBucket(bucketName).orElseThrow(() -> new IllegalArgumentException("Bucket Name not found!"));
    }

    /**
     * 删除存储桶，removeBucket不会删除bucket中的对象。需要使用removeObject API删除这些对象。
     *
     * @param bucketName
     * @throws IOException
     * @throws XmlPullParserException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws InvalidPortException
     * @throws ErrorResponseException
     * @throws NoResponseException
     * @throws InvalidBucketNameException
     * @throws InsufficientDataException
     * @throws InvalidEndpointException
     * @throws InternalException
     */
    @DeleteMapping("/bucket/{bucketName}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void removeBucket(@PathVariable String bucketName) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidPortException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, InternalException {
        template.removeBucket(bucketName);
    }

    /**
     * 将给定文件作为给定bucket中的对象上载。
     * 如果对象大于5MB，客户端将自动使用多部分会话。
     * 如果多部分会话失败，则上载的部分将自动中止
     *
     * @param bucketName 桶名称
     * @return
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws XmlPullParserException
     * @throws InvalidPortException
     * @throws ErrorResponseException
     * @throws InternalException
     * @throws NoResponseException
     * @throws InvalidBucketNameException
     * @throws InsufficientDataException
     * @throws InvalidEndpointException
     * @throws RegionConflictException
     * @throws InvalidArgumentException
     */
    @PostMapping("/object/{bucketName}")
    public MinioObject putObject(@RequestBody MultipartFile object, @PathVariable String bucketName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidPortException, ErrorResponseException, InternalException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, RegionConflictException, InvalidArgumentException {
        String name = object.getOriginalFilename();
        template.putObject(bucketName, name, object.getInputStream(), object.getSize(), object.getContentType());
        return new MinioObject(template.getObjectInfo(bucketName, name));
    }


    /**
     * 将给定文件作为给定bucket中的对象上载。
     * 如果对象大于5MB，客户端将自动使用多部分会话。
     * 如果多部分会话失败，则上载的部分将自动中止
     *
     * @param bucketName 桶名称
     * @param objectName bucket中的对象名
     * @return
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws XmlPullParserException
     * @throws InvalidPortException
     * @throws ErrorResponseException
     * @throws InternalException
     * @throws NoResponseException
     * @throws InvalidBucketNameException
     * @throws InsufficientDataException
     * @throws InvalidEndpointException
     * @throws RegionConflictException
     * @throws InvalidArgumentException
     */
    @PostMapping("/putObject")
    public MinioObject putObject(@RequestBody MultipartFile object, String bucketName, String objectName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidPortException, ErrorResponseException, InternalException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, RegionConflictException, InvalidArgumentException {
        template.putObject(bucketName, objectName, object.getInputStream(), object.getSize(), object.getContentType());
        return new MinioObject(template.getObjectInfo(bucketName, objectName));
    }

    /**
     * 在给定的bucket、前缀和递归标志中获取对象列表
     *
     * @param bucketName 桶名称
     * @param objectName bucket中的对象名
     * @return
     * @throws InvalidPortException
     * @throws InvalidEndpointException
     */
    @GetMapping("/object/{bucketName}/{objectName}")
    public List<MinioItem> filterObject(@PathVariable String bucketName, @PathVariable String objectName) throws InvalidPortException, InvalidEndpointException {
        return template.getAllObjectsByPrefix(bucketName, objectName, true);
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
     * @throws IOException
     * @throws XmlPullParserException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws InvalidPortException
     * @throws ErrorResponseException
     * @throws NoResponseException
     * @throws InvalidBucketNameException
     * @throws InsufficientDataException
     * @throws InvalidEndpointException
     * @throws InternalException
     * @throws InvalidExpiresRangeException
     */
    @GetMapping("/object/{bucketName}/{objectName}/{expires}")
    public Map<String, Object> getObject(@PathVariable String bucketName, @PathVariable String objectName, @PathVariable Integer expires) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidPortException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, InternalException, InvalidExpiresRangeException {
        Map<String, Object> responseBody = new HashMap<>();
        // Put Object info
        responseBody.put("bucket", bucketName);
        responseBody.put("object", objectName);
        responseBody.put("url", template.getObjectURL(bucketName, objectName, expires));
        responseBody.put("expires", expires);
        return responseBody;
    }

    /**
     * 删除存储桶中的对象
     *
     * @param bucketName 桶名称
     * @param objectName bucket中的对象名
     * @throws IOException
     * @throws XmlPullParserException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws InvalidPortException
     * @throws ErrorResponseException
     * @throws NoResponseException
     * @throws InvalidBucketNameException
     * @throws InsufficientDataException
     * @throws InvalidEndpointException
     * @throws InternalException
     * @throws InvalidArgumentException
     */
    @DeleteMapping("/object/{bucketName}/{objectName}/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteObject(@PathVariable String bucketName, @PathVariable String objectName) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidPortException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, InternalException, InvalidArgumentException {
        template.removeObject(bucketName, objectName);
    }

}
```