package com.cloudaxis.agsc.portal.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.cloudaxis.agsc.portal.constants.ImageThumbnailConstants;
import com.cloudaxis.agsc.portal.helpers.StringUtil;
import com.cloudaxis.agsc.portal.model.Caregiver;
import com.cloudaxis.agsc.portal.service.FileService;
import com.cloudaxis.agsc.portal.service.SelectedCaregiverService;

/**
 * 
 * @author Damien
 *
 */

@Controller
@RequestMapping("/file")
public class FileController extends AbstractController{

	protected Logger logger	= Logger.getLogger(FileController.class);

	@Autowired
	private FileService fileService;
	
	@Autowired
	Environment					env;
	
	@Autowired
	private SelectedCaregiverService selectedCaregiverService;
	
	private static final String charPath = File.separator;
	
	public FileController() {
		super(FileController.class);
	}

	/**
	 * put the attachment in the server temporarily
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/uploadAttachment")
	public void uploadAttachment(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception{
		List<MultipartFile> fileList = request.getFiles("fileList");

		String userId = request.getParameter("userId");
		String id = request.getParameter("id");
		if(!StringUtil.isBlank(userId)){
			String path = request.getSession().getServletContext().getRealPath(charPath) + "resources"+charPath+"attachment"+charPath;
			
			//put the attachment in the server temporarily
			for(MultipartFile file : fileList){
				String key = userId + charPath +file.getOriginalFilename();
				InputStream is = file.getInputStream();
				fileService.uploadFormFile(is, path, key);		//upload to the server
			}
		}else if(!StringUtil.isBlank(id)){		//templates
			String path = request.getSession().getServletContext().getRealPath(charPath) + "resources"+charPath+"templates"+charPath;
			
			//put the template's attachment in the server temporarily
			for(MultipartFile file : fileList){
				String key = id + charPath +file.getOriginalFilename();
				InputStream is = file.getInputStream();
				fileService.uploadFormFile(is, path, key);		//upload to the server
			}
		}
	}
	
	/**
	 * delete temporary attachment in the by server
	 * @param request
	 * @param response
	 * @param documentName
	 */
	@RequestMapping("/deleteAttachment")
	public void deleteAttachement(HttpServletRequest request, HttpServletResponse response, String documentName){
		
		if(!StringUtil.isBlank(documentName)){
			String userId = request.getParameter("userId");
			String path = request.getSession().getServletContext().getRealPath(charPath) + "resources" +charPath+ "attachment" +charPath;
			String filePath = path + userId + charPath +documentName;
			
			File file = new File(filePath);
			if(file.exists()){
				file.delete();
			}
		}
	}
	
	/**
	 * rename file type
	 * 
	 * @param request
	 * @param caregiver
	 * @param active
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/renameFileType")
	public String renameFileType(HttpServletRequest request, Caregiver caregiver, String active)
			throws IOException {
		
		String type = request.getParameter("type");
		String originalType = request.getParameter("originalType");
		String originalPath = request.getParameter("bucketName");		//file path
		
		String[] fileNames = originalPath.split("/");
		String fileName = fileNames[fileNames.length-1];
		String newPath = fileNames[0].concat("/"+type+"/"+fileNames[fileNames.length-1]);
		caregiver = selectedCaregiverService.getCaregiver(caregiver.getUserId());		//get caregiver
		fileService.renameFileType(originalPath,newPath,fileName,type);		//rename file type
		fileType(caregiver,originalType,fileName,type);			//edit file tyle
		selectedCaregiverService.update(caregiver);				//save data to database
		
		return "redirect:/dashboard/getCandidate?userId=" + caregiver.getUserId() + "&active=" + active;
	}
	
	/**
	 * Rename a caregiver document
	 * 
	 * @param request
	 * @param caregiver
	 * @param active
	 * @return
	 * @throws IOException
	 * @author Vineela Sharma
	 */
	@RequestMapping("rename")
	public String renameCaregiverDocument(HttpServletRequest request, Caregiver caregiver, String active)
			throws IOException {

		String newName = request.getParameter("newName");
		String originalPath = request.getParameter("bucketName");

		AmazonS3 s3client = new AmazonS3Client();
		String region = env.getProperty("aws.region");
		String[] regionSuffix = region.split("-");
		String bucketName = env.getProperty("aws.bucket.name") + "-" + regionSuffix[0];

		String originalFileName = originalPath.substring(originalPath.lastIndexOf("/") + 1);
		String destinationPath = originalPath.substring(0, originalPath.lastIndexOf("/") + 1);

		caregiver = selectedCaregiverService.getCaregiver(caregiver.getUserId());

		Region reg = Region.getRegion(Regions.fromName(region));
		s3client.setRegion(reg);

		CopyObjectRequest copyObjRequest = new CopyObjectRequest(bucketName,
				originalPath, bucketName, destinationPath + newName);

		s3client.copyObject(copyObjRequest);
		s3client.deleteObject(new DeleteObjectRequest(bucketName, originalPath));
		
		// rename thumbnail image
		try {
			String suffix = originalPath.substring(originalPath.lastIndexOf(".") + 1, originalPath.length())
					.toUpperCase();
			if (ImageThumbnailConstants.IMAGE_FORMAT_SUFFIXS.contains(suffix)) {
				String newThumbnailName = newName + ImageThumbnailConstants.FILE_SUFFIX;
				String originThumbnailPath = originalPath + ImageThumbnailConstants.FILE_SUFFIX;

				CopyObjectRequest copyObjRequestForThumbnail = new CopyObjectRequest(bucketName,
						originalPath + ImageThumbnailConstants.FILE_SUFFIX, bucketName,
						destinationPath + newThumbnailName);

				s3client.copyObject(copyObjRequestForThumbnail);
				s3client.deleteObject(new DeleteObjectRequest(bucketName, originThumbnailPath));

			}
		} catch (AmazonS3Exception exception) {
			logger.error("Error to rename image thumbnail: " + exception.getMessage());
		}

		String documentType = originalPath.substring(originalPath.indexOf("/") + 1, originalPath.lastIndexOf("/"));

		caregiver = updateRenamedDocumentReferences(caregiver, originalFileName, newName, documentType);
		selectedCaregiverService.update(caregiver);

		return "redirect:/dashboard/getCandidate?userId=" + caregiver.getUserId() + "&active=" + active;
	}
	
	/**
	 * Delete a caregiver document
	 * 
	 * @param request
	 * @param caregiver
	 * @param active
	 * @return
	 * @throws IOException
	 * @author Vineela Sharma
	 */
	@RequestMapping("delete")
	public String delelteCaregiverDocument(HttpServletRequest request, Caregiver caregiver, String active)
			throws IOException {

		String originalPath = request.getParameter("key");

		AmazonS3 s3client = new AmazonS3Client();
		String region = env.getProperty("aws.region");
		String[] regionSuffix = region.split("-");
		String bucketName = env.getProperty("aws.bucket.name") + "-" + regionSuffix[0];

		caregiver = selectedCaregiverService.getCaregiver(caregiver.getUserId());

		Region reg = Region.getRegion(Regions.fromName(region));
		s3client.setRegion(reg);

		// delete thumbnail image
		try {
			s3client.deleteObject(new DeleteObjectRequest(bucketName, originalPath));
			
			String suffix = originalPath.substring(originalPath.lastIndexOf(".") + 1, originalPath.length()).toUpperCase();
			if (ImageThumbnailConstants.IMAGE_FORMAT_SUFFIXS.contains(suffix)) {
				s3client.deleteObject(new DeleteObjectRequest(bucketName, originalPath + ImageThumbnailConstants.FILE_SUFFIX));
			}
		} catch (AmazonS3Exception exception) {
			logger.error("Error to delete image thumbnail: " + exception.getMessage());
		}
		String documentType = originalPath.substring(originalPath.indexOf("/") + 1, originalPath.lastIndexOf("/"));
		String originalFileName = originalPath.substring(originalPath.lastIndexOf("/") + 1);

		caregiver = updateDeletedDocumentReferences(caregiver, originalFileName, documentType);

		selectedCaregiverService.update(caregiver);

		return "redirect:/dashboard/getCandidate?userId=" + caregiver.getUserId() + "&active=" + active;
	}
	
	private Caregiver updateDeletedDocumentReferences(Caregiver caregiver, String originalFileName, String documentType) {
		if ("resume".equals(documentType)) {
			String resumes = caregiver.getResume();
			String[] fileNames = resumes.split(",");
		
			if(fileNames.length == 1){												//only one
				caregiver.setResume("");
			}else if((originalFileName).equals(fileNames[0]) || !(originalFileName).equals(fileNames[fileNames.length-1])){	 //first or middle
				caregiver.setResume(resumes.replace(originalFileName+",", ""));
			}else{		//last
				caregiver.setResume(resumes.replace(","+originalFileName, ""));
			}
		}
		else if ("my_photo".equals(documentType)) {
			String photos = caregiver.getPhoto();
			String[] fileNames = photos.split(",");
		
			if(fileNames.length == 1){												//only one
				caregiver.setPhoto("");
			}else if((originalFileName).equals(fileNames[0]) || !(originalFileName).equals(fileNames[fileNames.length-1])){	 //first or middle
				caregiver.setPhoto(photos.replace(originalFileName+",", ""));
			}else{		//last
				caregiver.setPhoto(photos.replace(","+originalFileName, ""));
			}
		}
		else if ("passport".equals(documentType)) {
			String passports = caregiver.getPhotoPassport();
			String[] fileNames = passports.split(",");
		
			if(fileNames.length == 1){												//only one
				caregiver.setPhotoPassport("");
			}else if((originalFileName).equals(fileNames[0]) || !(originalFileName).equals(fileNames[fileNames.length-1])){	 //first or middle
				caregiver.setPhotoPassport(passports.replace(originalFileName+",", ""));
			}else{		//last
				caregiver.setPhotoPassport(passports.replace(","+originalFileName, ""));
			}
		}
		else if ("degrees".equals(documentType)) {
			String degrees = caregiver.getPhotoDegrees();
			String[] fileNames = degrees.split(",");
		
			if(fileNames.length == 1){												//only one
				caregiver.setPhotoDegrees("");
			}else if((originalFileName).equals(fileNames[0]) || !(originalFileName).equals(fileNames[fileNames.length-1])){	 //first or middle
				caregiver.setPhotoDegrees(degrees.replace(originalFileName+",", ""));
			}else{		//last
				caregiver.setPhotoDegrees(degrees.replace(","+originalFileName, ""));
			}
		}
		else if ("other_file".equals(documentType)) {
			String otherFiles = caregiver.getOtherFiles();
			String[] fileNames = otherFiles.split(",");
		
			if(fileNames.length == 1){												//only one
				caregiver.setOtherFiles("");
			}else if((originalFileName).equals(fileNames[0]) || !(originalFileName).equals(fileNames[fileNames.length-1])){	 //first or middle
				caregiver.setOtherFiles(otherFiles.replace(originalFileName+",", ""));
			}else{		//last
				caregiver.setOtherFiles(otherFiles.replace(","+originalFileName, ""));
			}
		}
		return caregiver;

	}
	
	private Caregiver updateRenamedDocumentReferences(Caregiver caregiver, String originalFileName, String newName,
			String documentType) {
		if ("resume".equals(documentType)) {
			caregiver.setResume(caregiver.getResume().replace(originalFileName, newName));
		}
		else if ("my_photo".equals(documentType)) {
			caregiver.setPhoto(caregiver.getPhoto().replace(originalFileName, newName));
		}
		else if ("passport".equals(documentType)) {
			caregiver.setPhotoPassport(caregiver.getPhotoPassport().replace(originalFileName, newName));
		}
		else if ("degrees".equals(documentType)) {
			caregiver.setPhotoDegrees(caregiver.getPhotoDegrees().replace(originalFileName, newName));
		}
		else if ("other_file".equals(documentType)) {
			caregiver.setOtherFiles(caregiver.getOtherFiles().replace(originalFileName, newName));
		}
		return caregiver;
	}
	
	public void fileType(Caregiver caregiver, String originalType, String fileName, String type){
		if ("resume".equals(originalType)) {
			if(caregiver.getResume().contains(fileName+",")){
				caregiver.setResume(caregiver.getResume().replace(fileName+",", ""));
			}else if(caregiver.getResume().contains(","+fileName)){
				caregiver.setResume(caregiver.getResume().replace(","+fileName, ""));
			}else{
				caregiver.setResume("");
			}
			
			if ("my_photo".equals(type)) {
				if(caregiver.getPhoto() != null){
					caregiver.setPhoto(caregiver.getPhoto() +","+ fileName);
				}else{
					caregiver.setPhoto(fileName);
				}
			}else if ("passport".equals(type)) {
				if(caregiver.getPhotoPassport() != null){
					caregiver.setPhotoPassport(caregiver.getPhotoPassport() +","+ fileName);
				}else{
					caregiver.setPhotoPassport(fileName);
				}
			}else if ("degrees".equals(type)) {
				if(caregiver.getPhotoDegrees() != null){
					caregiver.setPhotoDegrees(caregiver.getPhotoDegrees() +","+ fileName);
				}else{
					caregiver.setPhotoDegrees(fileName);
				}
			}else if ("other_file".equals(type)) {
				if(caregiver.getOtherFiles() != null){
					caregiver.setOtherFiles(caregiver.getOtherFiles() +","+ fileName);
				}else{
					caregiver.setOtherFiles(fileName);
				}
			}
		}else if ("my_photo".equals(originalType)) {
			if(caregiver.getPhoto().contains(fileName+",")){
				caregiver.setPhoto(caregiver.getPhoto().replace(fileName+",", ""));
			}else if(caregiver.getPhoto().contains(","+fileName)){
				caregiver.setPhoto(caregiver.getPhoto().replace(","+fileName, ""));
			}else{
				caregiver.setPhoto("");
			}
			
			if ("resume".equals(type)) {
				if(caregiver.getResume() != null){
					if(!"resume.pdf".equals(fileName)){
						caregiver.setResume(caregiver.getResume() +","+ fileName);
					}
				}else{
					caregiver.setResume(fileName);
				}
			}else if ("passport".equals(type)) {
				if(caregiver.getPhotoPassport() != null){
					caregiver.setPhotoPassport(caregiver.getPhotoPassport() +","+ fileName);
				}else{
					caregiver.setPhotoPassport(fileName);
				}
			}else if ("degrees".equals(type)) {
				if(caregiver.getPhotoDegrees() != null){
					caregiver.setPhotoDegrees(caregiver.getPhotoDegrees() +","+ fileName);
				}else{
					caregiver.setPhotoDegrees(fileName);
				}
			}else if ("other_file".equals(type)) {
				if(caregiver.getOtherFiles() != null){
					caregiver.setOtherFiles(caregiver.getOtherFiles() +","+ fileName);
				}else{
					caregiver.setOtherFiles(fileName);
				}
			}
		}else if ("passport".equals(originalType)) {
			if(caregiver.getPhotoPassport().contains(fileName+",")){
				caregiver.setPhotoPassport(caregiver.getPhotoPassport().replace(fileName+",", ""));
			}else if(caregiver.getPhoto().contains(","+fileName)){
				caregiver.setPhotoPassport(caregiver.getPhotoPassport().replace(","+fileName, ""));
			}else{
				caregiver.setPhotoPassport("");
			}
			
			if ("resume".equals(type)) {
				if(caregiver.getResume() != null){
					if(!"resume.pdf".equals(fileName)){
						caregiver.setResume(caregiver.getResume() +","+ fileName);
					}
				}else{
					caregiver.setResume(fileName);
				}
			}else if ("my_photo".equals(type)) {
				if(caregiver.getPhoto() != null){
					caregiver.setPhoto(caregiver.getPhoto() +","+ fileName);
				}else{
					caregiver.setPhoto(fileName);
				}
			}else if ("degrees".equals(type)) {
				if(caregiver.getPhotoDegrees() != null){
					caregiver.setPhotoDegrees(caregiver.getPhotoDegrees() +","+ fileName);
				}else{
					caregiver.setPhotoDegrees(fileName);
				}
			}else if ("other_file".equals(type)) {
				if(caregiver.getOtherFiles() != null){
					caregiver.setOtherFiles(caregiver.getOtherFiles() +","+ fileName);
				}else{
					caregiver.setOtherFiles(fileName);
				}
			}
		}else if ("degrees".equals(originalType)) {
			if(caregiver.getPhotoDegrees().contains(fileName+",")){
				caregiver.setPhotoDegrees(caregiver.getPhotoDegrees().replace(fileName+",", ""));
			}else if(caregiver.getPhoto().contains(","+fileName)){
				caregiver.setPhotoDegrees(caregiver.getPhotoDegrees().replace(","+fileName, ""));
			}else{
				caregiver.setPhotoDegrees("");
			}
			
			if ("resume".equals(type)) {
				if(caregiver.getResume() != null){
					if(!"resume.pdf".equals(fileName)){
						caregiver.setResume(caregiver.getResume() +","+ fileName);
					}
				}else{
					caregiver.setResume(fileName);
				}
			}else if ("my_photo".equals(type)) {
				if(caregiver.getPhoto() != null){
					caregiver.setPhoto(caregiver.getPhoto() +","+ fileName);
				}else{
					caregiver.setPhoto(fileName);
				}
			}else if ("passport".equals(type)) {
				if(caregiver.getPhotoPassport() != null){
					caregiver.setPhotoPassport(caregiver.getPhotoPassport() +","+ fileName);
				}else{
					caregiver.setPhotoPassport(fileName);
				}
			}else if ("other_file".equals(type)) {
				if(caregiver.getOtherFiles() != null){
					caregiver.setOtherFiles(caregiver.getOtherFiles() +","+ fileName);
				}else{
					caregiver.setOtherFiles(fileName);
				}
			}
		}else if ("other_file".equals(originalType)) {
			if(caregiver.getOtherFiles().contains(fileName+",")){
				caregiver.setOtherFiles(caregiver.getOtherFiles().replace(fileName+",", ""));
			}else if(caregiver.getPhoto().contains(","+fileName)){
				caregiver.setOtherFiles(caregiver.getOtherFiles().replace(","+fileName, ""));
			}else{
				caregiver.setOtherFiles("");
			}
			
			if ("resume".equals(type)) {
				if(caregiver.getResume() != null){
					if(!"resume.pdf".equals(fileName)){
						caregiver.setResume(caregiver.getResume() +","+ fileName);
					}
				}else{
					caregiver.setResume(fileName);
				}
			}else if ("my_photo".equals(type)) {
				if(caregiver.getPhoto() != null){
					caregiver.setPhoto(caregiver.getPhoto() +","+ fileName);
				}else{
					caregiver.setPhoto(fileName);
				}
			}else if ("degrees".equals(type)) {
				if(caregiver.getPhotoDegrees() != null){
					caregiver.setPhotoDegrees(caregiver.getPhotoDegrees() +","+ fileName);
				}else{
					caregiver.setPhotoDegrees(fileName);
				}
			}else if ("passport".equals(type)) {
				if(caregiver.getPhotoPassport() != null){
					caregiver.setPhotoPassport(caregiver.getPhotoPassport() +","+ fileName);
				}else{
					caregiver.setPhotoPassport(fileName);
				}
			}
		}
	}
	
	/**
	 * download file to local
	 * 
	 * @param request
	 * @param response
	 * @param path			the file path(local server path like:  attachment\312\a.jpg, S3 path like:attachment\312\a.jpg??X-Amz-Algorithm=AWS4-HMAC...)
	 * @throws IOException
	 */
	@RequestMapping("downFile")
	public void downFile(HttpServletRequest request, HttpServletResponse response, String path, String fileName, String type) throws IOException {  
		if("local".equals(type)){
			String serverPath = request.getSession().getServletContext().getRealPath(charPath) + "resources" +charPath;
			path = serverPath + path;
		}
		File file = new File(path,fileName);
	    InputStream in=null;
	    OutputStream out=null;  
	    
	    response.reset();
	    response.setContentType("multipart/form-data"); 
	    String fileName1 = new String(fileName.getBytes(), "ISO-8859-1");
		response.setHeader("Content-Disposition", "attachment;filename=\""+fileName1+"\""); 
		//response.setCharacterEncoding("UTF-8");
		
		try {
			
			out = response.getOutputStream();  
			
			int b = 0;  
            
			if(file.isFile()){
				
				fileName = file.getName();
				in = new FileInputStream(file);
				while((b=in.read())!= -1)  
				{  
					out.write(b);  
				}  
				
			}else{
				AmazonS3URI uri = new AmazonS3URI(path,true);
				String key = uri.getKey();
				key = key.substring(0,fileName.length()+key.indexOf(fileName));
		        in = fileService.downloadFile(key);
		        
				if (null != in) {
					while((b=in.read())!= -1)  
					{  
						out.write(b);  
					} 
				}
			}
			
			in.close();
			out.close();  
            out.flush(); 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}  
	}  
	
	/**
	 * check whether file existed in S3
	 * 
	 * @param userId		candidate id
	 * @param fileName		name of the receive mail's attachment 
	 * @param request
	 * @return msg
	 */
	@RequestMapping("/checkFile")
	public @ResponseBody String checkFile(@RequestParam(value = "userId", required = true) String userId,
			@RequestParam(value = "fileName", required = true)String fileName,HttpServletRequest request){
		
		boolean flag = false;
		
		String key = userId + "/attachment/";
		flag = fileService.checkFile(key,fileName,flag);
		
		String msg = null;
		if(flag){
			 msg = "{\"status\":\"error\", \"msg\":\"This file have existed in the database, do you want to overwrite it?\"}";
		}else{
			msg = "{\"status\":\"success\"}";
		}
		return msg;
	}
}
