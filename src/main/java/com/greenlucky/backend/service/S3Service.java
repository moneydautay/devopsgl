package com.greenlucky.backend.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Mr Lam on 11/11/2016.
 */
@Service
public class S3Service {

    /** The application logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(S3Service.class);


    private static final String PROFILE_IMAGE_FILE_NAME = "profileImage";

    @Value("${aws.s3.root.bucket.name}")
    private String bucketName;

    @Value("${aws.s3.profile}")
    private String awsProfileName;

    @Value("${image.store.tmp.folder}")
    private String tempImageStore;

    @Autowired
    private AmazonS3Client s3Client;

    /**
     * It stores the given file name in S3 and returns the key under which the file has been stored
     * @param uploadFile The multipart file uploaded by the user
     * @param username The username for which to upload this file
     * @return The URL of the upload image
     * @throws IOException
     */
    public String storeProfileImage(MultipartFile uploadFile, String username) throws IOException{

        String profileImageUrl = null;

        if(uploadFile != null && !uploadFile.isEmpty()){
            byte[] bytes = uploadFile.getBytes();

            //The root of our temporary assets. Will create if it doesn't exist
            File tmpImageStoredFolder = new File(tempImageStore + File.separatorChar + username);
            if(!tmpImageStoredFolder.exists()){
                LOGGER.info("Creating the temporary root for the S3 assets");
                tmpImageStoredFolder.mkdirs();
            }

            // The temporary file where the profile image will be stored
            File tmpProfileImageFile = new File(tmpImageStoredFolder.getAbsolutePath()
                    + File.separatorChar
                    + PROFILE_IMAGE_FILE_NAME
                    + "."
                    + FilenameUtils.getExtension(uploadFile.getOriginalFilename()));
            LOGGER.info("Temporary file will be saved to {}", tmpProfileImageFile.getAbsolutePath());

            try(BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(tmpProfileImageFile.getAbsolutePath())))){
                stream.write(bytes);
            }

            profileImageUrl = this.storeProfileImageToS3(tmpProfileImageFile, username);

            //Clean up the temporary folder
            tmpProfileImageFile.delete();
        }
        return profileImageUrl;
    }

    /**
     * It stores the given file name in S3 and returns the key under which the file has been stored
     * @param resource The file resource to upload to S3
     * @param username The username of user
     * @return The URL of the uploaded resource or null if a problem occurred
     */
    private String storeProfileImageToS3(File resource, String username) {
        String profileImageUrl = null;

        if(!resource.exists()){
            LOGGER.error("The file {} does not exist. Throwing an exception", resource.getAbsolutePath());
            throw new IllegalArgumentException("The file "+ resource.getAbsolutePath()+" doesn't exist");
        }

        String rootBucketUrl = this.ensureBucketExists(bucketName);

        if(null == rootBucketUrl){

            LOGGER.error("The bucket {} does not exist and the application"
                    + " was not able to create it. The image won't be stored with the profile", rootBucketUrl );
        }else {
            AccessControlList acl = new AccessControlList();
            acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);

            String key = username + "/" + PROFILE_IMAGE_FILE_NAME + "." + FilenameUtils.getExtension(resource.getName());

            try {
                s3Client.putObject(new PutObjectRequest(bucketName, key, resource).withAccessControlList(acl));
                profileImageUrl = s3Client.getResourceUrl(bucketName, key);
            }catch (AmazonS3Exception ace){
                LOGGER.error("A client exception occurred trying to store the profile"
                + " image {} on S3. The profile image won't be stored", resource.getAbsolutePath(), ace);
            }
        }

        return profileImageUrl;
    }


    /**
     * Returns the root URL where the bucket name is located
     * <p>Please note that the URL does not contain the bucket name</p>
     * @param bucketName The bucket name
     * @return the root URL where the bucket name is located
     */
    private String ensureBucketExists(String bucketName) {
        String bucketUrl = null;

        try{
            if(!s3Client.doesBucketExist(bucketName)){
                LOGGER.info("Bucket {} doesn't exists...Creating one");
                s3Client.createBucket(bucketName);
                LOGGER.info("Created bucket: {}", bucketName);
            }
            bucketUrl = s3Client.getResourceUrl(bucketName, null) + bucketName;
        }catch (AmazonClientException ace){
            LOGGER.error("An error occurred while connecting to S3. Will not execute action"
            + " for bucket: {}", bucketName, ace);
        }
        return bucketUrl;
    }

}
