package com.cloudaxis.agsc.portal.service;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.cloudaxis.agsc.portal.constants.ImageThumbnailConstants;
import com.cloudaxis.agsc.portal.helpers.ImageUtils;
import com.cloudaxis.agsc.portal.helpers.StringUtil;
import com.cloudaxis.agsc.portal.model.Caregiver;
import com.cloudaxis.agsc.portal.model.DocumentFile;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

@Service
public class FileService {
	
	@Autowired
	Environment env;
	
	protected Logger		logger	= Logger.getLogger(FileService.class);

	private static final String charPath = File.separator;
	
	private static final int MAX_CHAR = 85;
	
	private static final float LINE_SPACE = 6.5f;
	
	/**
	 * Download Caregiver documents from S3 server
	 * 
	 * @author Vineela Sharma
	 * @param path
	 * @param caregiver
	 * @param dfList 
	 * @throws Exception
	 */
	public List<DocumentFile> downloadCaregiverDocuments(String path, Caregiver caregiver, List<DocumentFile> dfList) throws Exception {
		AmazonS3 s3client = new AmazonS3Client();
		String region = env.getProperty("aws.region");
		String[] regionSuffix = region.split("-");
		String bucketName = env.getProperty("aws.bucket.name") + "-" + regionSuffix[0];
		String key = caregiver.getUserId() + "/";

		Region reg = Region.getRegion(Regions.fromName(region));
		s3client.setRegion(reg);

		ListObjectsRequest documentsList = new ListObjectsRequest()
				.withBucketName(bucketName)
				.withPrefix(key);

		try {
			ObjectListing objects = s3client.listObjects(documentsList);

			for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
				String objetkey = objectSummary.getKey();
				//S3Object object = s3client.getObject(new GetObjectRequest(bucketName, objetkey));
				
				//InputStream objectData = object.getObjectContent();
				//uploadFormFile(objectData, path, objetkey);			//download to server
				
				String[] ok = objetkey.split("/");
				if(ok.length == 3){
					//get the file url from s3
					GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(
			        		bucketName, objetkey, HttpMethod.GET);
			        URL s3Url = s3client.generatePresignedUrl(urlRequest);  
			        
					DocumentFile df = new DocumentFile(); 
					df.setName(ok[2]);
					if(!ok[2].contains("thumbnail.jpg")){
						String[] extension = ok[2].split("\\.");
						
						df.setPath(s3Url.toString());
						df.setExtension(extension[extension.length-1].toUpperCase());
						df.setType(ok[1]);
						df.setLastModified(objectSummary.getLastModified());
						df.setS3Key(objetkey);
						
						dfList.add(df);
					}else if(ok[2].contains("thumbnail.jpg")){
						df.setThumbnail(s3Url.toString());
						df.setS3Key(objetkey);
						dfList.add(df);
					}
				}else if(ok.length == 4){		//attachment
					GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(
			        		bucketName, objetkey, HttpMethod.GET);
			        URL s3Url = s3client.generatePresignedUrl(urlRequest);  
			        
					DocumentFile df = new DocumentFile(); 
					df.setName(ok[3]);
					if(!ok[3].contains("thumbnail.jpg")){
						String[] extension = ok[3].split("\\.");
						
						df.setPath(s3Url.toString());
						df.setExtension(extension[extension.length-1].toUpperCase());
						df.setType(ok[1]);
						df.setLastModified(objectSummary.getLastModified());
						df.setS3Key(objetkey);
						
						dfList.add(df);
					}else if(ok[3].contains("thumbnail.jpg")){
						df.setThumbnail(s3Url.toString());
						df.setS3Key(objetkey);
						dfList.add(df);
					}
				}
			}
		}
		catch (AmazonServiceException ase) {
			logger.error("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon S3, but was rejected with an error response for some reason.");
			logger.error("Error Message:    " + ase.getMessage());
			logger.error("HTTP Status Code: " + ase.getStatusCode());
			logger.error("AWS Error Code:   " + ase.getErrorCode());
			logger.error("Error Type:       " + ase.getErrorType());
			logger.error("Request ID:       " + ase.getRequestId());
		}
		catch (AmazonClientException ace) {
			logger.error("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with S3, "
					+ "such as not being able to access the network.");
			logger.error("Error Message: " + ace.getMessage());
		}
		
		return dfList;
	}
	
	
	public void uploadFiles(List<MultipartFile> files, String id, HttpServletRequest request) throws IOException {
		AmazonS3 s3client = new AmazonS3Client();
		String region = env.getProperty("aws.region");
		String[] regionSuffix = region.split("-");
		String bucketName = env.getProperty("aws.bucket.name") + "-" + regionSuffix[0];

		Region reg = Region.getRegion(Regions.fromName(region));
		s3client.setRegion(reg);
		
		ImageInputStream imageInputStream=null;
		
		for (MultipartFile file : files) {
			try {
				String key = "";
				if ("resume1".equals(file.getName())) {		//resume
					if(file.getSize() > 0){
						String filename = file.getOriginalFilename();
						String suffixName = filename.substring(filename.lastIndexOf(".")+1, filename.length());
						if("pdf".equals(suffixName) || "PDF".equals(suffixName)){
							//key = id.concat("/resume/resume.pdf");
							key = id.concat("/resume/"+filename);
							ObjectMetadata meta = new ObjectMetadata();
							meta.setContentLength(file.getSize());
							meta.setContentType(file.getContentType());
				
							PutObjectRequest req = new PutObjectRequest(bucketName, key, file.getInputStream(), meta);
							s3client.putObject(req);
						}else{
							key = id +"/"+ "resume" +"/"+ file.getOriginalFilename();
							ObjectMetadata meta = new ObjectMetadata();
							meta.setContentLength(file.getSize());
							meta.setContentType(file.getContentType());
				
							PutObjectRequest req = new PutObjectRequest(bucketName, key, file.getInputStream(), meta);
							s3client.putObject(req);
							
							//resume template
							String path = request.getSession().getServletContext().getRealPath(charPath) 
									+ "resources" +charPath+ "resume" +charPath+ "resume.pdf";
							File f = new File(path);
							if(f.exists()){
								key = id +"/"+ "resume" +"/"+ f.getName();
								//ObjectMetadata meta1 = new ObjectMetadata();
								//meta1.setContentLength(f.length());
								
								PutObjectRequest req1 = new PutObjectRequest(bucketName, key, f);
								s3client.putObject(req1);
							}
						}
					}else{
						String path = request.getSession().getServletContext().getRealPath(charPath) 
								+ "resources" +charPath+ "resume" +charPath+ "resume.pdf";
	
						File f = new File(path);
						if(f.exists()){
							key = id +"/"+ "resume" +"/"+ f.getName();
						}
						//ObjectMetadata meta = new ObjectMetadata();
						//meta.setContentLength(f.length());
			
						//PutObjectRequest req = new PutObjectRequest(bucketName, key, new FileInputStream(f), meta);
						PutObjectRequest req = new PutObjectRequest(bucketName, key, f);
						
						s3client.putObject(req);
					}
				}else if("my_photo".equals(file.getName())){
					String type = file.getContentType();
					if(type.contains("jpg") || type.contains("png") || type.contains("jpeg")){
						key = id + "/" + file.getName() + "/" + file.getOriginalFilename();
						ObjectMetadata meta = new ObjectMetadata();
						meta.setContentLength(file.getSize());
						meta.setContentType(file.getContentType());
						PutObjectRequest req = new PutObjectRequest(bucketName, key, file.getInputStream(), meta);
						s3client.putObject(req);
			
						// thumbnail image
			            boolean isImage = false;
			            imageInputStream = ImageIO.createImageInputStream(file.getInputStream());
			            Iterator<ImageReader> iter = ImageIO.getImageReaders(imageInputStream);
			            if (iter.hasNext()) {
			                isImage = true;
			            }
			            if(isImage){
			                BufferedImage originalBufferedImage = ImageIO.read(imageInputStream);
			                BufferedImage resizedImage = new ImageUtils().createThumbnailImage(originalBufferedImage);

			                File newFile = new File(file.getOriginalFilename() + ImageThumbnailConstants.FILE_SUFFIX);
			                ImageIO.write(resizedImage, "jpg", newFile);
			                String image_thumbnail_name = key + ImageThumbnailConstants.FILE_SUFFIX;
			                PutObjectRequest req_img_thumbnail = new PutObjectRequest(bucketName, image_thumbnail_name, newFile);
			                s3client.putObject(req_img_thumbnail);
			            }
					}else{
						key = id + "/other_file/" + file.getOriginalFilename();
						ObjectMetadata meta = new ObjectMetadata();
						meta.setContentLength(file.getSize());
						meta.setContentType(file.getContentType());
						PutObjectRequest req = new PutObjectRequest(bucketName, key, file.getInputStream(), meta);
						s3client.putObject(req);
			
						// thumbnail image
			            boolean isImage = false;
			            imageInputStream = ImageIO.createImageInputStream(file.getInputStream());
			            Iterator<ImageReader> iter = ImageIO.getImageReaders(imageInputStream);
			            if (iter.hasNext()) {
			                isImage = true;
			            }
			            if(isImage){
			                BufferedImage originalBufferedImage = ImageIO.read(imageInputStream);
			                BufferedImage resizedImage = new ImageUtils().createThumbnailImage(originalBufferedImage);

			                File newFile = new File(file.getOriginalFilename() + ImageThumbnailConstants.FILE_SUFFIX);
			                ImageIO.write(resizedImage, "jpg", newFile);
			                String image_thumbnail_name = key + ImageThumbnailConstants.FILE_SUFFIX;
			                PutObjectRequest req_img_thumbnail = new PutObjectRequest(bucketName, image_thumbnail_name, newFile);
			                s3client.putObject(req_img_thumbnail);
			            }
					}
				}else {		//other file
					key = id + "/" + file.getName() + "/" + file.getOriginalFilename();
					ObjectMetadata meta = new ObjectMetadata();
					meta.setContentLength(file.getSize());
					meta.setContentType(file.getContentType());
					PutObjectRequest req = new PutObjectRequest(bucketName, key, file.getInputStream(), meta);
					s3client.putObject(req);
		
					// thumbnail image
		            boolean isImage = false;
		            imageInputStream = ImageIO.createImageInputStream(file.getInputStream());
		            Iterator<ImageReader> iter = ImageIO.getImageReaders(imageInputStream);
		            if (iter.hasNext()) {
		                isImage = true;
		            }
		            if(isImage){
		                BufferedImage originalBufferedImage = ImageIO.read(imageInputStream);
		                BufferedImage resizedImage = new ImageUtils().createThumbnailImage(originalBufferedImage);

		                File newFile = new File(file.getOriginalFilename() + ImageThumbnailConstants.FILE_SUFFIX);
		                ImageIO.write(resizedImage, "jpg", newFile);
		                String image_thumbnail_name = key + ImageThumbnailConstants.FILE_SUFFIX;
		                PutObjectRequest req_img_thumbnail = new PutObjectRequest(bucketName, image_thumbnail_name, newFile);
		                s3client.putObject(req_img_thumbnail);
		            }
					
				}
			}
			catch (AmazonServiceException ase) {
				logger.error("Caught an AmazonServiceException, which means your request made it "
						+ "to Amazon S3, but was rejected with an error response for some reason.");
				logger.error("Error Message:    " + ase.getMessage());
				logger.error("HTTP Status Code: " + ase.getStatusCode());
				logger.error("AWS Error Code:   " + ase.getErrorCode());
				logger.error("Error Type:       " + ase.getErrorType());
				logger.error("Request ID:       " + ase.getRequestId());
			}
			catch (AmazonClientException ace) {
				logger.error("Caught an AmazonClientException, which means the client encountered "
						+ "a serious internal problem while trying to communicate with S3, "
						+ "such as not being able to access the network.");
				logger.error("Error Message: " + ace.getMessage());
			}
		}
			
	}
	
	/**
	 * upload on the server
	 * 
	 * @author Damien
	 * @param is
	 * @param path
	 * @param key
	 * @throws Exception
	 */
	public void uploadFormFile(InputStream is, String path, String key) throws Exception {
		OutputStream os = null;
		String newpath = path + key;
		if("\\".equals(charPath)){   
			newpath = newpath.replace("/", "\\");
		} 
		//linux
		if("/".equals(charPath)){   
			newpath = newpath.replace("\\", "/");
		}
		File file = new File(newpath);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		
		try {
			os = new FileOutputStream(file);
			int bytes = 0;
			byte[] buffer = new byte[8192];
			while ((bytes = is.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytes);
			}
			os.close();
			is.close();
		}
		catch (Exception e) {
			throw e;
		}
		finally {
			if (os != null) {
				try {
					os.close();
					os = null;
				}
				catch (Exception e1) {
					;
				}
			}
			if (is != null) {
				try {
					is.close();
					is = null;
				}
				catch (Exception e1) {
					;
				}
			}
		}
	}

	/**
	 * upload file to s3
	 * 
	 * @param file				attachment
	 * @param key				attachment folder (like:attachment/12/a.jpg)
	 * @throws IOException
	 */
	public void uploadLocalDocument(File file, String key) throws IOException {
		AmazonS3 s3client = new AmazonS3Client();
		String region = env.getProperty("aws.region");
		String[] regionSuffix = region.split("-");
		String bucketName = env.getProperty("aws.bucket.name") + "-" + regionSuffix[0];
		
		Region reg = Region.getRegion(Regions.fromName(region));
		s3client.setRegion(reg);
		
		ImageInputStream imageInputStream=null;
		try {
			s3client.putObject(new PutObjectRequest(bucketName, key, file)); 
			
			// thumbnail image
            boolean isImage = false;
            imageInputStream = ImageIO.createImageInputStream(file);
            Iterator<ImageReader> iter = ImageIO.getImageReaders(imageInputStream);
            if (iter.hasNext()) {
                isImage = true;
            }
            if(isImage){
                BufferedImage originalBufferedImage = ImageIO.read(imageInputStream);
                BufferedImage resizedImage = new ImageUtils().createThumbnailImage(originalBufferedImage);

                File newFile = new File(file.getName() + ImageThumbnailConstants.FILE_SUFFIX);
                ImageIO.write(resizedImage, "jpg", newFile);
                String image_thumbnail_name = key + ImageThumbnailConstants.FILE_SUFFIX;
                PutObjectRequest req_img_thumbnail = new PutObjectRequest(bucketName, image_thumbnail_name, newFile);
                s3client.putObject(req_img_thumbnail);
            }
		}
		catch (AmazonServiceException ase) {
			logger.error("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon S3, but was rejected with an error response for some reason.");
			logger.error("Error Message:    " + ase.getMessage());
			logger.error("HTTP Status Code: " + ase.getStatusCode());
			logger.error("AWS Error Code:   " + ase.getErrorCode());
			logger.error("Error Type:       " + ase.getErrorType());
			logger.error("Request ID:       " + ase.getRequestId());
		}
		catch (AmazonClientException ace) {
			logger.error("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with S3, "
					+ "such as not being able to access the network.");
			logger.error("Error Message: " + ace.getMessage());
		}
	}

	public Object downLoadAttachment(List<DocumentFile> dfList, String path, String key, String userId) throws Exception {
		AmazonS3 s3client = new AmazonS3Client();
		String region = env.getProperty("aws.region");
		String[] regionSuffix = region.split("-");
		String bucketName = env.getProperty("aws.bucket.name") + "-" + regionSuffix[0];
		
		Region reg = Region.getRegion(Regions.fromName(region));
		s3client.setRegion(reg);

		ListObjectsRequest documentsList = new ListObjectsRequest()
				.withBucketName(bucketName)
				.withPrefix(key);

		try {
			ObjectListing objects = s3client.listObjects(documentsList);
			
			for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
				DocumentFile df = new DocumentFile();
				String objetkey = objectSummary.getKey();
				
				//get file s3 url 
		        GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName, objetkey);  
		        URL url = s3client.generatePresignedUrl(urlRequest);  
		        
		        //get file 
				/*
				S3Object object = s3client.getObject(new GetObjectRequest(bucketName, objetkey));
				InputStream objectData = object.getObjectContent();
				String[] attachment = objetkey.split("/");
				String attachmentName = "attachment" + charPath +userId+ charPath +attachment[attachment.length-1];
				uploadFormFile(objectData, path, attachmentName);		//download to the server
				*/		
		        
				df.setName(objetkey.replace(key, ""));
				df.setPath(url.toString());
				df.setSize(objectSummary.getSize());
				
				dfList.add(df);
				//objectData.close();
			}
		}
		catch (AmazonServiceException ase) {
			logger.error("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon S3, but was rejected with an error response for some reason.");
			logger.error("Error Message:    " + ase.getMessage());
			logger.error("HTTP Status Code: " + ase.getStatusCode());
			logger.error("AWS Error Code:   " + ase.getErrorCode());
			logger.error("Error Type:       " + ase.getErrorType());
			logger.error("Request ID:       " + ase.getRequestId());
		}
		catch (AmazonClientException ace) {
			logger.error("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with S3, "
					+ "such as not being able to access the network.");
			logger.error("Error Message: " + ace.getMessage());
		}
		
		return dfList;
		
	}
	
	/**
	 * delete email
	 * 
	 * @param ids
	 */
	public void deleteAttachment(String ids){
		AmazonS3 s3client = new AmazonS3Client();
		//String region = env.getProperty("aws.region");
		String region = "cn-north-1";
		String[] regionSuffix = region.split("-");
		//String bucketName = env.getProperty("aws.bucket.name") + "-" + regionSuffix[0];
		String bucketName = "agsc-profiles" + "-" + regionSuffix[0];
		
		Region reg = Region.getRegion(Regions.fromName(region));
		s3client.setRegion(reg);

		try {
			for(String id : ids.split(",")){
				String path = "attachment/" + id;
				ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName).withPrefix(path);
			    ObjectListing objects = s3client.listObjects(listObjectsRequest);
			    do {
			        for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
			        	s3client.deleteObject(bucketName, objectSummary.getKey());
			        }
			        objects = s3client.listNextBatchOfObjects(objects);
			    } while (objects.isTruncated());
				
			}
		}
		catch (AmazonServiceException ase) {
			logger.error("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon S3, but was rejected with an error response for some reason.");
			logger.error("Error Message:    " + ase.getMessage());
			logger.error("HTTP Status Code: " + ase.getStatusCode());
			logger.error("AWS Error Code:   " + ase.getErrorCode());
			logger.error("Error Type:       " + ase.getErrorType());
			logger.error("Request ID:       " + ase.getRequestId());
		}
		catch (AmazonClientException ace) {
			logger.error("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with S3, "
					+ "such as not being able to access the network.");
			logger.error("Error Message: " + ace.getMessage());
		}
	}

	/**
	 * download one file
	 * @param path  path of the local server
	 * @param key  file path in the S3   like(templates/id/fileName:templates/1/resume.pdf)
	 * @throws Exception 
	 */
	public void getFile(String key, String path) throws Exception {
		AmazonS3 s3client = new AmazonS3Client();
		String region = env.getProperty("aws.region");
		String[] regionSuffix = region.split("-");
		String bucketName = env.getProperty("aws.bucket.name") + "-" + regionSuffix[0];
		Region reg = Region.getRegion(Regions.fromName(region));
		s3client.setRegion(reg);
		
		try {
			S3Object object = s3client.getObject(new GetObjectRequest(bucketName, key));
			InputStream objectData = object.getObjectContent();
			uploadFormFile(objectData, path, key);		//download to the server
		}
		catch (AmazonServiceException ase) {
			logger.error("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon S3, but was rejected with an error response for some reason.");
			logger.error("Error Message:    " + ase.getMessage());
			logger.error("HTTP Status Code: " + ase.getStatusCode());
			logger.error("AWS Error Code:   " + ase.getErrorCode());
			logger.error("Error Type:       " + ase.getErrorType());
			logger.error("Request ID:       " + ase.getRequestId());
		}
		catch (AmazonClientException ace) {
			logger.error("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with S3, "
					+ "such as not being able to access the network.");
			logger.error("Error Message: " + ace.getMessage());
		}
	}


	/**
	 * upload the templates attachment to s3
	 * @param fileList
	 * @param id 
	 * @throws IOException 
	 */
	public void uploadTemplatesAttachment(List<MultipartFile> fileList, long id) throws IOException {
		AmazonS3 s3client = new AmazonS3Client();
		String region = env.getProperty("aws.region");
		String[] regionSuffix = region.split("-");
		String bucketName = env.getProperty("aws.bucket.name") + "-" + regionSuffix[0];
		Region reg = Region.getRegion(Regions.fromName(region));
		s3client.setRegion(reg);
		
		try {
			for(MultipartFile mf : fileList){
				String key = "templates/" +id +"/" + mf.getOriginalFilename();
				
				ObjectMetadata meta = new ObjectMetadata();
				meta.setContentLength(mf.getSize());
				
				PutObjectRequest req = new PutObjectRequest(bucketName, key, mf.getInputStream(), meta);
				s3client.putObject(req);
			}
		}
		catch (AmazonServiceException ase) {
			logger.error("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon S3, but was rejected with an error response for some reason.");
			logger.error("Error Message:    " + ase.getMessage());
			logger.error("HTTP Status Code: " + ase.getStatusCode());
			logger.error("AWS Error Code:   " + ase.getErrorCode());
			logger.error("Error Type:       " + ase.getErrorType());
			logger.error("Request ID:       " + ase.getRequestId());
		}
		catch (AmazonClientException ace) {
			logger.error("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with S3, "
					+ "such as not being able to access the network.");
			logger.error("Error Message: " + ace.getMessage());
		}
	}

	/**
	 * get the attachment data from s3
	 * 
	 * @param dfList			return list attachment data
	 * @param key				attachment folder(like:attachment/12/)
	 * @param attachment		list attachment name
	 * @return
	 */
	public List<DocumentFile> getEmailAttachment(List<DocumentFile> dfList, String key, String attachment) {
		AmazonS3 s3client = new AmazonS3Client();
		String region = env.getProperty("aws.region");
		String[] regionSuffix = region.split("-");
		String bucketName = env.getProperty("aws.bucket.name") + "-" + regionSuffix[0];
		
		Region reg = Region.getRegion(Regions.fromName(region));
		s3client.setRegion(reg);

		ListObjectsRequest documentsList = new ListObjectsRequest()
				.withBucketName(bucketName)
				.withPrefix(key);

		try {
			ObjectListing objects = s3client.listObjects(documentsList);
			
			for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
				String objetkey = objectSummary.getKey();
				for(String name : attachment.split(",")){
					if(objetkey.contains(name)){
						DocumentFile df = new DocumentFile();
						GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(
								bucketName, objetkey, HttpMethod.GET);
						URL s3Url = s3client.generatePresignedUrl(urlRequest);
						
						df.setPath(s3Url.toString());
						df.setName(name);
						df.setSize(objectSummary.getSize());
						
						dfList.add(df);
					}
				}
			}
		}
		catch (AmazonServiceException ase) {
			logger.error("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon S3, but was rejected with an error response for some reason.");
			logger.error("Error Message:    " + ase.getMessage());
			logger.error("HTTP Status Code: " + ase.getStatusCode());
			logger.error("AWS Error Code:   " + ase.getErrorCode());
			logger.error("Error Type:       " + ase.getErrorType());
			logger.error("Request ID:       " + ase.getRequestId());
		}
		catch (AmazonClientException ace) {
			logger.error("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with S3, "
					+ "such as not being able to access the network.");
			logger.error("Error Message: " + ace.getMessage());
		}
		
		return dfList;
	}


	public void renameFileType(String originalPath, String newPath, String fileName, String type) {
		AmazonS3 s3client = new AmazonS3Client();
		String region = env.getProperty("aws.region");
		String[] regionSuffix = region.split("-");
		String bucketName = env.getProperty("aws.bucket.name") + "-" + regionSuffix[0];

		Region reg = Region.getRegion(Regions.fromName(region));
		s3client.setRegion(reg);
		
		try {
			CopyObjectRequest copyObjRequest = new CopyObjectRequest(bucketName,originalPath,bucketName,newPath);
			if("resume.pdf".equals(fileName) && "resume".equals(type)){
				s3client.deleteObject(new DeleteObjectRequest(bucketName, newPath));
			}
			s3client.copyObject(copyObjRequest);
			s3client.deleteObject(new DeleteObjectRequest(bucketName, originalPath));
			
			// renameFileType thumbnail image
			String suffix = originalPath.substring(originalPath.lastIndexOf(".") + 1, originalPath.length()).toUpperCase();
			if (ImageThumbnailConstants.IMAGE_FORMAT_SUFFIXS.contains(suffix)) {
				String originThumbnailPath = originalPath + ImageThumbnailConstants.FILE_SUFFIX;
				CopyObjectRequest copyObjRequestForThumbnail = new CopyObjectRequest(bucketName,
						originThumbnailPath, bucketName,
						newPath + ImageThumbnailConstants.FILE_SUFFIX);

				s3client.copyObject(copyObjRequestForThumbnail);
				s3client.deleteObject(new DeleteObjectRequest(bucketName, originThumbnailPath));
			}
		}
		catch (AmazonServiceException ase) {
			logger.error("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon S3, but was rejected with an error response for some reason.");
			logger.error("Error Message:    " + ase.getMessage());
			logger.error("HTTP Status Code: " + ase.getStatusCode());
			logger.error("AWS Error Code:   " + ase.getErrorCode());
			logger.error("Error Type:       " + ase.getErrorType());
			logger.error("Request ID:       " + ase.getRequestId());
		}
		catch (AmazonClientException ace) {
			logger.error("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with S3, "
					+ "such as not being able to access the network.");
			logger.error("Error Message: " + ace.getMessage());
		}
	}

	public void uploadPhoto(MultipartFile file, String userId) throws IOException {
		AmazonS3 s3client = new AmazonS3Client();
		String region = env.getProperty("aws.region");
		String[] regionSuffix = region.split("-");
		String bucketName = env.getProperty("aws.bucket.name") + "-" + regionSuffix[0];

		Region reg = Region.getRegion(Regions.fromName(region));
		s3client.setRegion(reg);
		
		String key = userId + "/my_photo/" + file.getOriginalFilename();
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(file.getSize());
		metadata.setContentType(file.getContentType());
		
		try {
			PutObjectRequest req = new PutObjectRequest(bucketName, key, file.getInputStream(), metadata);
			s3client.putObject(req);
		}
		catch (AmazonServiceException ase) {
			logger.error("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon S3, but was rejected with an error response for some reason.");
			logger.error("Error Message:    " + ase.getMessage());
			logger.error("HTTP Status Code: " + ase.getStatusCode());
			logger.error("AWS Error Code:   " + ase.getErrorCode());
			logger.error("Error Type:       " + ase.getErrorType());
			logger.error("Request ID:       " + ase.getRequestId());
		}
		catch (AmazonClientException ace) {
			logger.error("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with S3, "
					+ "such as not being able to access the network.");
			logger.error("Error Message: " + ace.getMessage());
		}
	}

	public String  downloadPdf(Caregiver caregiver, String photoPath, String path) throws Exception{	
		//get the model pdf
		PdfReader reader = new PdfReader(path + "pdf_template" +charPath+ "biodata_template.pdf");		
		
		//output pdf
		String myPdfPath = path + caregiver.getUserId() +charPath+ "pdf_template" +charPath+ "biodata_template.pdf";
		
		File file = new File(myPdfPath);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(file));
		for(int i=1; i<=stamper.getReader().getNumberOfPages(); i++){
			PdfContentByte overContent = stamper.getOverContent(i);
			
			if(i==1){
				BaseFont font = BaseFont.createFont();
				overContent.beginText();
				overContent.setFontAndSize(font, 10);
				overContent.setTextMatrix(200, 200);
				
				//full_name
				overContent.showTextAligned(Element.ALIGN_LEFT,caregiver.getFullName(),97,658,0);	
				
				/*dob  eg:02/12/16*/
				Date dob = caregiver.getDateOfBirth();
				SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd");
				String date = format.format(dob);
				
				String[] d = date.split("-");
				String day = d[2];
				String mon = d[1];
				String years = d[0];
				//day
				overContent.showTextAligned(Element.ALIGN_LEFT,day.substring(0,1),110,628,0);			// eg:0 
				overContent.showTextAligned(Element.ALIGN_LEFT,day.substring(1,2),130,628,0);			// eg:2 
				//mon  eg:02
				overContent.showTextAligned(Element.ALIGN_LEFT,mon.substring(0,1),153,628,0);			// eg:1 
				overContent.showTextAligned(Element.ALIGN_LEFT,mon.substring(1,2),172,628,0);			// eg:2 
				//year  eg:16
				overContent.showTextAligned(Element.ALIGN_LEFT,years.substring(0,1),197,628,0);			// eg:1 
				overContent.showTextAligned(Element.ALIGN_LEFT,years.substring(1,2),216,628,0);			// eg:6
				
				//age
				String age = caregiver.getAge();
				if(!StringUtil.isBlank(age)){
					overContent.showTextAligned(Element.ALIGN_LEFT,age.substring(0,1),282,628,0);			// eg:6
					overContent.showTextAligned(Element.ALIGN_LEFT,age.substring(1,age.length()),301,628,0);			// eg:6
				}
				
				//place of birth
				if(!StringUtil.isBlank(caregiver.getCountryOfBirth())){
					overContent.showTextAligned(Element.ALIGN_LEFT,caregiver.getCountryOfBirth(),112,606,0);		
				}
				
				//height
				String height = caregiver.getHeight();
				if(!StringUtil.isBlank(height)){
					if(height.length() > 0){
						overContent.showTextAligned(Element.ALIGN_LEFT,height.substring(0,1),121,580,0);			
					}
					if(height.length() > 1){
						overContent.showTextAligned(Element.ALIGN_LEFT,height.substring(1,2),141,580,0);			
					}
					if(height.length() > 2){
						overContent.showTextAligned(Element.ALIGN_LEFT,height.substring(2,3),160,580,0);			
					}
				}
				
				//weight
				String weight = caregiver.getWeight();
				if(!StringUtil.isBlank(weight)){
					if(weight.length() > 0){
						overContent.showTextAligned(Element.ALIGN_LEFT,weight.substring(0,1),234,580,0);			
					}
					if(weight.length() > 1){
						overContent.showTextAligned(Element.ALIGN_LEFT,weight.substring(1,weight.length()),254,580,0);			
					}
				}
				
				//nationality
				overContent.showTextAligned(Element.ALIGN_LEFT,caregiver.getNationality(),120,558,0);		
				
				//current_address
				if(!StringUtil.isBlank(caregiver.getCurrentAddress())){
					overContent.showTextAligned(Element.ALIGN_LEFT,caregiver.getCurrentAddress(),90,510,0);		
				}
				
				//nearest airport
				if(!StringUtil.isBlank(caregiver.getNearestAirport())){
					overContent.showTextAligned(Element.ALIGN_LEFT,caregiver.getNearestAirport(),242,486,0);		
				}
				
				//mobile
				if(!StringUtil.isBlank(caregiver.getMobile())){
					overContent.showTextAligned(Element.ALIGN_LEFT,caregiver.getMobile(),210,463,0);		
				}
				
				//religion
				overContent.showTextAligned(Element.ALIGN_LEFT,caregiver.getReligion(),100,438,0);		
				
				//educational_level
				if(!StringUtil.isBlank(caregiver.getEducationalLevel())){
					overContent.showTextAligned(Element.ALIGN_LEFT,caregiver.getEducationalLevel(),155,414,0);		
				}
				
				//siblings
				if(!StringUtil.isBlank(caregiver.getSiblings())){
					overContent.showTextAligned(Element.ALIGN_LEFT,caregiver.getSiblings(),150,390,0);		
				}
				
				//marital_status
				overContent.showTextAligned(Element.ALIGN_LEFT,caregiver.getMaritalStatus(),143,366,0);		
				
				//has_children
				overContent.showTextAligned(Element.ALIGN_LEFT,caregiver.getHasChildren(),165,338,0);		
				
				//children_names
				if(!StringUtil.isBlank(caregiver.getChildrenNames())){
					overContent.showTextAligned(Element.ALIGN_LEFT,caregiver.getChildrenNames(),184,316,0);		
				}
				
				//allergies
				if(!StringUtil.isBlank(caregiver.getAllergies())){
					overContent.showTextAligned(Element.ALIGN_LEFT,caregiver.getAllergies(),135,260,0);		
				}
				
				//diagnosed_conditions
				String diagnosed_conditions = caregiver.getDiagnosedConditions();
				if(diagnosed_conditions.contains("Mental illness")){	//i
					overContent.showTextAligned(Element.ALIGN_LEFT,"x",194,214,0);	//YES
				}else{
					overContent.showTextAligned(Element.ALIGN_LEFT,"x",266,214,0);	//NO
				}
				
				if(diagnosed_conditions.contains("Epilepsy")){			//ii
					overContent.showTextAligned(Element.ALIGN_LEFT,"x",194,202,0);	
				}else{
					overContent.showTextAligned(Element.ALIGN_LEFT,"x",266,202,0);	
				}
				
				if(diagnosed_conditions.contains("Asthma")){			//iii
					overContent.showTextAligned(Element.ALIGN_LEFT,"x",194,190,0);	
				}else{
					overContent.showTextAligned(Element.ALIGN_LEFT,"x",266,190,0);	
				}
				
				if(diagnosed_conditions.contains("Diabetes")){			//iv
					overContent.showTextAligned(Element.ALIGN_LEFT,"x",194,178,0);	
				}else{
					overContent.showTextAligned(Element.ALIGN_LEFT,"x",266,178,0);	
				}
				
				if(diagnosed_conditions.contains("Hypertension")){		//v
					overContent.showTextAligned(Element.ALIGN_LEFT,"x",194,166,0);	
				}else{
					overContent.showTextAligned(Element.ALIGN_LEFT,"x",266,166,0);	
				}
				
				if(diagnosed_conditions.contains("Tuberculosis")){		//vi
					overContent.showTextAligned(Element.ALIGN_LEFT,"x",447,214,0);	
				}else{
					overContent.showTextAligned(Element.ALIGN_LEFT,"x",519,214,0);	
				}
				
				if(diagnosed_conditions.contains("Heart disease")){		//vii
					overContent.showTextAligned(Element.ALIGN_LEFT,"x",447,202,0);	
				}else{
					overContent.showTextAligned(Element.ALIGN_LEFT,"x",519,202,0);	
				}
				
				if(diagnosed_conditions.contains("Malaria")){		//viii
					overContent.showTextAligned(Element.ALIGN_LEFT,"x",447,190,0);	
				}else{
					overContent.showTextAligned(Element.ALIGN_LEFT,"x",519,190,0);	
				}
				
				if(diagnosed_conditions.contains("Operations")){		//ix
					overContent.showTextAligned(Element.ALIGN_LEFT,"x",447,178,0);	
				}else{
					overContent.showTextAligned(Element.ALIGN_LEFT,"x",519,178,0);
				}

				if(diagnosed_conditions.contains("Other")){		//x
					overContent.showTextAligned(Element.ALIGN_LEFT,"other",370,165,0);	//other
				}
				
				//Physical disabilities
				if(diagnosed_conditions.contains("Physical disabilities")){		
					overContent.showTextAligned(Element.ALIGN_LEFT,"Yes",141,140,0);	
				}else{
					overContent.showTextAligned(Element.ALIGN_LEFT,"No",141,140,0);	
				}
				
				//Dietary restrictions
				overContent.showTextAligned(Element.ALIGN_LEFT,"No restrictions",141,114,0);
				
				overContent.endText();
				
				//add photo
				Image image = Image.getInstance(photoPath);
				image.setAbsolutePosition(380,360);		//path  w*h
				image.scaleAbsolute(200,300);			//size x,y			(x:left to right; y:down to up)
				overContent.addImage(image);
			}else if(i==2){
				BaseFont font = BaseFont.createFont();
				overContent.beginText();
				overContent.setFontAndSize(font, 10);
				overContent.setTextMatrix(200, 200);
				
				//interview via 
				overContent.showTextAligned(Element.ALIGN_LEFT,"x",59,550,0);
				
				//1.care of infants
				overContent.setFontAndSize(font, 14);
				overContent.showTextAligned(Element.ALIGN_LEFT,"No",231,402,0);
				
				//2.care of elderly
				overContent.showTextAligned(Element.ALIGN_LEFT,"Yes",231,353,0);
				if(!StringUtil.isBlank(caregiver.getExp())){
					overContent.showTextAligned(Element.ALIGN_LEFT,caregiver.getExp(),290,353,0);
				}
				
				//3.care of disabled
				
				//4.general housework
				overContent.showTextAligned(Element.ALIGN_LEFT,"No",231,307,0);
				
				//5.cooking
				
				//6.languages
				overContent.setFontAndSize(font, 6);
				if(!StringUtil.isBlank(caregiver.getLanguages())){
					showMessage(overContent, caregiver.getLanguages(), 328, 180);
				}
				
				//7.other skills
				if(!StringUtil.isBlank(caregiver.getSpecialities())){
					showMessage(overContent, caregiver.getSpecialities(), 328, 130);
				}
				
				overContent.endText();
			}else if(i==3){
				BaseFont font = BaseFont.createFont();
				overContent.beginText();
				overContent.setFontAndSize(font, 10);
				overContent.setTextMatrix(200, 200);
				
				//interview via 
				overContent.showTextAligned(Element.ALIGN_LEFT,"x",60,723,0);
				
				//1.care of infants
				overContent.setFontAndSize(font, 14);
				overContent.showTextAligned(Element.ALIGN_LEFT,"No",230,580,0);
				
				//2.care of elderly
				overContent.showTextAligned(Element.ALIGN_LEFT,"Yes",230,530,0);
				if(!StringUtil.isBlank(caregiver.getExp())){
					overContent.showTextAligned(Element.ALIGN_LEFT,caregiver.getExp(),289,530,0);
				}
				
				//care of disabled
				
				//4.general housework
				overContent.showTextAligned(Element.ALIGN_LEFT,"No",229,431,0);
				
				//5.cooking
				
				//6.languages
				overContent.setFontAndSize(font, 6);
				if(!StringUtil.isBlank(caregiver.getLanguages())){
					showMessage(overContent, caregiver.getLanguages(), 328, 360);
				}
				
				//7.other skills
				if(!StringUtil.isBlank(caregiver.getSpecialities())){
					showMessage(overContent, caregiver.getSpecialities(), 328, 310);
				}
				
				overContent.endText();
			}else if(i==4){
			}
		}
		stamper.close();
		
		return myPdfPath;
	}

	private void showMessage(PdfContentByte overContent, String message, int width, int height) {
		String[] messagesArr = message.split(" ");
		int total = 0;
		int line = 0;
		String messageInfo = null;
		for(int i = 0 ; i  < messagesArr.length ; i++){
			if(total == 0){
				messageInfo = messagesArr[i];
				total += messagesArr[i].length();
			}else if(total + messagesArr[i].length() <= MAX_CHAR){
					messageInfo += " " + messagesArr[i];
					total += messagesArr[i].length();
			}else{
				float y = height - line * LINE_SPACE ;
				overContent.showTextAligned(Element.ALIGN_LEFT,messageInfo,width,y,0);
				line ++ ;
				messageInfo = messagesArr[i];
				total = messagesArr[i].length();
			}
			if(messageInfo != null && i == messagesArr.length -1 ){
				float y = height - line * LINE_SPACE ;
				overContent.showTextAligned(Element.ALIGN_LEFT,messageInfo,width,y,0);
			}
		}
	}
	
	public String getFileUrl(String key) {
		AmazonS3 s3client = new AmazonS3Client();
		String region = env.getProperty("aws.region");
		String[] regionSuffix = region.split("-");
		String bucketName = env.getProperty("aws.bucket.name") + "-" + regionSuffix[0];
		Region reg = Region.getRegion(Regions.fromName(region));
		s3client.setRegion(reg);
		
		String url = null;
		InputStream netFileInputStream = null;
		try {
			 //get file s3 url 
			GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(
	        		bucketName, key, HttpMethod.GET);
	        URL s3Url = s3client.generatePresignedUrl(urlRequest);
	        
	        URLConnection urlConn = s3Url.openConnection();
	        netFileInputStream = urlConn.getInputStream();
			if (null != netFileInputStream) {
				url = s3Url.toString();
			}
			netFileInputStream.close();
	        
		}
		catch (AmazonServiceException ase) {
			logger.error("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon S3, but was rejected with an error response for some reason.");
			logger.error("Error Message:    " + ase.getMessage());
			logger.error("HTTP Status Code: " + ase.getStatusCode());
			logger.error("AWS Error Code:   " + ase.getErrorCode());
			logger.error("Error Type:       " + ase.getErrorType());
			logger.error("Request ID:       " + ase.getRequestId());
		}
		catch (AmazonClientException ace) {
			logger.error("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with S3, "
					+ "such as not being able to access the network.");
			logger.error("Error Message: " + ace.getMessage());
		}catch (IOException e) {
			logger.error("S3 file does not exists: " + bucketName + "/" + key);
		} 
		
		return url;
	}
	
	/**
	 * Delete the files under the directory
	 * 
	 * @param dirPath			file path
	 * @return
	 */
	public void deleteFile(File file) {
	   if (!file.isDirectory()) {  
		   file.delete();  
	   }
	}

	/**
	 * Delete file Directory;
	 * 
	 * @param path		file path
	 */
	public void deleteDirectory(File path){
		if(!path.exists()){
			return;
		}
		
		File[] listFile = path.listFiles();
		if(listFile.length <= 0){
			path.delete();
		}
	}
	
	/**
	 * 
	 * @param key		delete the folder of all files
	 */
	public void deleteS3File(String key) {
		AmazonS3 s3client = new AmazonS3Client();
		String region = env.getProperty("aws.region");
		String[] regionSuffix = region.split("-");
		String bucketName = env.getProperty("aws.bucket.name") + "-" + regionSuffix[0];
		Region reg = Region.getRegion(Regions.fromName(region));
		s3client.setRegion(reg);
		
		try {
			ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName).withPrefix(key);
		    ObjectListing objects = s3client.listObjects(listObjectsRequest);
		    do {
		        for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
		        	s3client.deleteObject(bucketName, objectSummary.getKey());
		        }
		        objects = s3client.listNextBatchOfObjects(objects);
		    } while (objects.isTruncated());
		}
		catch (AmazonServiceException ase) {
			logger.error("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon S3, but was rejected with an error response for some reason.");
			logger.error("Error Message:    " + ase.getMessage());
			logger.error("HTTP Status Code: " + ase.getStatusCode());
			logger.error("AWS Error Code:   " + ase.getErrorCode());
			logger.error("Error Type:       " + ase.getErrorType());
			logger.error("Request ID:       " + ase.getRequestId());
		}
		catch (AmazonClientException ace) {
			logger.error("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with S3, "
					+ "such as not being able to access the network.");
			logger.error("Error Message: " + ace.getMessage());
		}
	}

	/**
	 * download file from s3
	 * 
	 * @param attachment			the file name
	 * @param dfList				return list file
	 * @param key					file folder
	 */
	public List<DocumentFile> downLoadAttachments(String attachment, List<DocumentFile> dfList, String key) {
		AmazonS3 s3client = new AmazonS3Client();
		String region = env.getProperty("aws.region");
		String[] regionSuffix = region.split("-");
		String bucketName = env.getProperty("aws.bucket.name") + "-" + regionSuffix[0];
		
		Region reg = Region.getRegion(Regions.fromName(region));
		s3client.setRegion(reg);
		
		ListObjectsRequest documentsList = new ListObjectsRequest()
				.withBucketName(bucketName)
				.withPrefix(key);
		
		try {
			ObjectListing objects = s3client.listObjects(documentsList);
			
			for(String fileName : attachment.split(",")){
				for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
					String objetkey = objectSummary.getKey();
					String s3FileName = objetkey.replace(key, "");
					if(fileName.equals(s3FileName)){
						DocumentFile df = new DocumentFile();
						
						//get file s3 url 
				        GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName, objetkey);  
				        URL url = s3client.generatePresignedUrl(urlRequest);  
						
						df.setName(s3FileName);
						df.setPath(url.toString());
						df.setSize(objectSummary.getSize());
						
						dfList.add(df);
					}
				}
			}
		}
		catch (AmazonServiceException ase) {
			logger.error("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon S3, but was rejected with an error response for some reason.");
			logger.error("Error Message:    " + ase.getMessage());
			logger.error("HTTP Status Code: " + ase.getStatusCode());
			logger.error("AWS Error Code:   " + ase.getErrorCode());
			logger.error("Error Type:       " + ase.getErrorType());
			logger.error("Request ID:       " + ase.getRequestId());
		}
		catch (AmazonClientException ace) {
			logger.error("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with S3, "
					+ "such as not being able to access the network.");
			logger.error("Error Message: " + ace.getMessage());
		}
		
		return dfList;
		
	}

	/**
	 * delete file from s3
	 * 
	 * @param key		the file path
	 */
	public void deleteFileFromS3(String key) {
		AmazonS3 s3client = new AmazonS3Client();
		String region = env.getProperty("aws.region");
		String[] regionSuffix = region.split("-");
		String bucketName = env.getProperty("aws.bucket.name") + "-" + regionSuffix[0];
		
		Region reg = Region.getRegion(Regions.fromName(region));
		s3client.setRegion(reg);
		try{
			s3client.deleteObject(new DeleteObjectRequest(bucketName, key));
		}catch (AmazonServiceException ase) {
			logger.error("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon S3, but was rejected with an error response for some reason.");
			logger.error("Error Message:    " + ase.getMessage());
			logger.error("HTTP Status Code: " + ase.getStatusCode());
			logger.error("AWS Error Code:   " + ase.getErrorCode());
			logger.error("Error Type:       " + ase.getErrorType());
			logger.error("Request ID:       " + ase.getRequestId());
		}
		catch (AmazonClientException ace) {
			logger.error("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with S3, "
					+ "such as not being able to access the network.");
			logger.error("Error Message: " + ace.getMessage());
		}
	}

	/**
	 * get the file inputStream
	 * 
	 * @param key		the file path(like: 12/12/a.jpg)
	 * @return
	 */
	public InputStream downloadFile(String key) {
		AmazonS3 s3client = new AmazonS3Client();
		String region = env.getProperty("aws.region");
		String[] regionSuffix = region.split("-");
		String bucketName = env.getProperty("aws.bucket.name") + "-" + regionSuffix[0];
		
		Region reg = Region.getRegion(Regions.fromName(region));
		s3client.setRegion(reg);
		
		S3Object s3Object = null;
		InputStream in = null;
		try{
			s3Object = s3client.getObject(new GetObjectRequest(bucketName, key));
			in = s3Object.getObjectContent();
		}catch (AmazonServiceException ase) {
			logger.error("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon S3, but was rejected with an error response for some reason.");
			logger.error("Error Message:    " + ase.getMessage());
			logger.error("HTTP Status Code: " + ase.getStatusCode());
			logger.error("AWS Error Code:   " + ase.getErrorCode());
			logger.error("Error Type:       " + ase.getErrorType());
			logger.error("Request ID:       " + ase.getRequestId());
		}
		catch (AmazonClientException ace) {
			logger.error("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with S3, "
					+ "such as not being able to access the network.");
			logger.error("Error Message: " + ace.getMessage());
		}
		return in;
	}

	/**
	 * get the file data
	 * 
	 * @param key			file folder	(like:12/21/)
	 * @param fileName		file name
	 * @return				return the file data(file name,the file S3 path,file size)
	 */
	public DocumentFile getDocument(String key, String fileName) {
		AmazonS3 s3client = new AmazonS3Client();
		String region = env.getProperty("aws.region");
		String[] regionSuffix = region.split("-");
		String bucketName = env.getProperty("aws.bucket.name") + "-" + regionSuffix[0];
		
		Region reg = Region.getRegion(Regions.fromName(region));
		s3client.setRegion(reg);
		
		DocumentFile df = new DocumentFile();
		ListObjectsRequest documentsList = new ListObjectsRequest()
				.withBucketName(bucketName)
				.withPrefix(key);
		
		try {
			ObjectListing objects = s3client.listObjects(documentsList);
			
			for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
				String objetkey = objectSummary.getKey();
				if(objetkey.equals(key+fileName)){
					//get file s3 url 
			        GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName, objetkey, HttpMethod.GET);  
			        URL url = s3client.generatePresignedUrl(urlRequest);  
					
					df.setName(fileName);
					df.setPath(url.toString());
					df.setSize(objectSummary.getSize());
				}
			}
		}catch (AmazonServiceException ase) {
			logger.error("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon S3, but was rejected with an error response for some reason.");
			logger.error("Error Message:    " + ase.getMessage());
			logger.error("HTTP Status Code: " + ase.getStatusCode());
			logger.error("AWS Error Code:   " + ase.getErrorCode());
			logger.error("Error Type:       " + ase.getErrorType());
			logger.error("Request ID:       " + ase.getRequestId());
		}
		catch (AmazonClientException ace) {
			logger.error("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with S3, "
					+ "such as not being able to access the network.");
			logger.error("Error Message: " + ace.getMessage());
		}
		return df;
	}


	 /** 
     * cut image
     * 
     * @param file.  original image 
     * @param x  target cut image X coordinate origin
     * @param y  target cut image Y coordinate origin
     * @param w  target cut image width
     * @param h  target cut image height
     */  
    public File cutImage(String path, MultipartFile file, int x ,int y ,int w,int h){  
    	File f = null;
        try {  
            java.awt.Image img;  
            ImageFilter cropFilter;  
            //get the original image
            BufferedImage bi = ImageIO.read(file.getInputStream());  
            int srcWidth = bi.getWidth();      // original width 
            int srcHeight = bi.getHeight();    // original height
              
            if (srcWidth >= w && srcHeight >= h) {  
            	java.awt.Image image = bi.getScaledInstance(srcWidth, srcHeight,java.awt.Image.SCALE_DEFAULT);  
                  
                /*int x1 = x*srcWidth/400;  
                int y1 = y*srcHeight/270;  
                int w1 = w*srcWidth/400;  
                int h1 = h*srcHeight/270;*/  
                  
                cropFilter = new CropImageFilter(x, y, w, h);  
                img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));  
                BufferedImage tag = new BufferedImage(w, h,BufferedImage.TYPE_INT_RGB);  
                Graphics g = tag.getGraphics();  
                g.drawImage(img, 0, 0, null);
                g.dispose();  
                f = new File(path.concat(file.getOriginalFilename()));
                
                if (!f.getParentFile().exists()) {
        			f.getParentFile().mkdirs();
        		}
                
                ImageIO.write(tag, "JPEG",f);
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        
        return f;
    }

    /**
     * move file to other folder
     * 
     * @param fileName		file name
     * @param key			original folder 
     * @param newKey		target folder
     */
	public void moveFileFromS3(String fileName, String key, String newKey) {
		
		AmazonS3 s3client = new AmazonS3Client();
		String region = env.getProperty("aws.region");
		String[] regionSuffix = region.split("-");
		String bucketName = env.getProperty("aws.bucket.name") + "-" + regionSuffix[0];
		
		Region reg = Region.getRegion(Regions.fromName(region));
		s3client.setRegion(reg);
		ListObjectsRequest documentsList = new ListObjectsRequest()
				.withBucketName(bucketName)
				.withPrefix(key);
		
		try {
			ObjectListing objects = s3client.listObjects(documentsList);
			do {
			    for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
			    	String objetkey = objectSummary.getKey();
			    	String[] s3FileNames = objetkey.split("/");
			    	String s3FileName = s3FileNames[s3FileNames.length-1];
			    	if(fileName.equals(s3FileName) ||  fileName.concat(".thumbnail.jpg").equals(s3FileName)){
			    		String targetKey = newKey.concat(s3FileName);
						CopyObjectRequest copyObjRequestForThumbnail = new CopyObjectRequest(bucketName, objetkey, bucketName, targetKey);

						s3client.copyObject(copyObjRequestForThumbnail);
						s3client.deleteObject(new DeleteObjectRequest(bucketName, objetkey));
			    	}
			    }
			    objects = s3client.listNextBatchOfObjects(objects);
			} while (objects.isTruncated());
		}catch (AmazonServiceException ase) {
			logger.error("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon S3, but was rejected with an error response for some reason.");
			logger.error("Error Message:    " + ase.getMessage());
			logger.error("HTTP Status Code: " + ase.getStatusCode());
			logger.error("AWS Error Code:   " + ase.getErrorCode());
			logger.error("Error Type:       " + ase.getErrorType());
			logger.error("Request ID:       " + ase.getRequestId());
		}
		catch (AmazonClientException ace) {
			logger.error("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with S3, "
					+ "such as not being able to access the network.");
			logger.error("Error Message: " + ace.getMessage());
		}
	}

	/**
	 * upload file to s3
	 * 
	 * @param file
	 * @param key		the s3 folder(like:12/12/)
	 * @throws IOException
	 */
	public void uploadPhotoToS3(File file, String key) throws IOException {
		AmazonS3 s3client = new AmazonS3Client();
		String region = env.getProperty("aws.region");
		String[] regionSuffix = region.split("-");
		String bucketName = env.getProperty("aws.bucket.name") + "-" + regionSuffix[0];
		
		Region reg = Region.getRegion(Regions.fromName(region));
		s3client.setRegion(reg);
		ImageInputStream imageInputStream=null;
		
		try {
			InputStream in = new FileInputStream(file);
			
			//upload the file
			s3client.putObject(new PutObjectRequest(bucketName, key+file.getName(),file)); 
			
			// thumbnail image
	        boolean isImage = false;
	        imageInputStream = ImageIO.createImageInputStream(in);
	        Iterator<ImageReader> iter = ImageIO.getImageReaders(imageInputStream);
	        if (iter.hasNext()) {
	            isImage = true;
	        }
	        if(isImage){
	            BufferedImage originalBufferedImage = ImageIO.read(imageInputStream);
	            BufferedImage resizedImage = new ImageUtils().createThumbnailImage(originalBufferedImage);

	            File newFile = new File(file.getName() + ImageThumbnailConstants.FILE_SUFFIX);
	            ImageIO.write(resizedImage, "jpg", newFile);
	            String image_thumbnail_name = key + file.getName() + ImageThumbnailConstants.FILE_SUFFIX;
	            PutObjectRequest req_img_thumbnail = new PutObjectRequest(bucketName, image_thumbnail_name, newFile);
	            s3client.putObject(req_img_thumbnail);
	        }
		}
		catch (AmazonServiceException ase) {
			logger.error("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon S3, but was rejected with an error response for some reason.");
			logger.error("Error Message:    " + ase.getMessage());
			logger.error("HTTP Status Code: " + ase.getStatusCode());
			logger.error("AWS Error Code:   " + ase.getErrorCode());
			logger.error("Error Type:       " + ase.getErrorType());
			logger.error("Request ID:       " + ase.getRequestId());
		}
		catch (AmazonClientException ace) {
			logger.error("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with S3, "
					+ "such as not being able to access the network.");
			logger.error("Error Message: " + ace.getMessage());
		}
	}

	/**
	 * check whether the file exists s3 
	 * 
	 * @param key		the s3 folder(like:12/12/)
	 * @param name 		file name
	 * @param flag		if true means file existed in S3,false means not
	 */
	public Boolean checkFile(String key,String name,boolean flag) {
		AmazonS3 s3client = new AmazonS3Client();
		String region = env.getProperty("aws.region");
		String[] regionSuffix = region.split("-");
		String bucketName = env.getProperty("aws.bucket.name") + "-" + regionSuffix[0];
		
		Region reg = Region.getRegion(Regions.fromName(region));
		s3client.setRegion(reg);
		ListObjectsRequest documentsList = new ListObjectsRequest()
				.withBucketName(bucketName)
				.withPrefix(key);
		
		try {
			ObjectListing objects = s3client.listObjects(documentsList);
			do {
			    for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
			    	String objetkey = objectSummary.getKey();
			    	String[] s3FileNames = objetkey.split("/");
			    	String s3FileName = s3FileNames[s3FileNames.length-1];
			    	if(name.equals(s3FileName)){
			    		flag = true;
			    		break;
			    	}
			    }
			    objects = s3client.listNextBatchOfObjects(objects);
			} while (objects.isTruncated());
		}catch (AmazonServiceException ase) {
			logger.error("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon S3, but was rejected with an error response for some reason.");
			logger.error("Error Message:    " + ase.getMessage());
			logger.error("HTTP Status Code: " + ase.getStatusCode());
			logger.error("AWS Error Code:   " + ase.getErrorCode());
			logger.error("Error Type:       " + ase.getErrorType());
			logger.error("Request ID:       " + ase.getRequestId());
		}
		catch (AmazonClientException ace) {
			logger.error("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with S3, "
					+ "such as not being able to access the network.");
			logger.error("Error Message: " + ace.getMessage());
		}
		return flag;
	}  
	
	
}
