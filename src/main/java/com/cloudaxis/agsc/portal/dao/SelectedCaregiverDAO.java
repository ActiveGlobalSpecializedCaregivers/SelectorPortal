package com.cloudaxis.agsc.portal.dao;

import java.util.List;

import com.cloudaxis.agsc.portal.model.Bio;
import com.cloudaxis.agsc.portal.model.Caregiver;
import com.cloudaxis.agsc.portal.model.Comment;
import com.cloudaxis.agsc.portal.model.SendCV;
import com.cloudaxis.agsc.portal.model.User;

public interface SelectedCaregiverDAO {

	public List<Caregiver> listCaregivers();

	public List<Caregiver> list();

	public Caregiver getCaregiver(String userId);

	public void save(Caregiver caregiver);

	public void editStatus(Caregiver caregiver, User user, String changeStatus);
	
	public void addHistory(String applicantId, String userId, String workflow, String statusFrom, String statusTo);
	
	public void postNewComment(Comment newComment);
	
	public List<Comment> getCommentsByCaregiverId(Integer caregiverId);

	public List<Comment> updateCommentsWithUserNameAndInitials(List<Comment> comments);

	public List<Caregiver> searchByProfileFilter(List<String> criteria);

	public void saveSearch(String searchTitle, User user, String shareWithOtherUsers, String searchList);

	public void update(Caregiver caregiver);

	public void changeTheStatisOfRegisteredConcorde(String userId, String caregiverId, String status);

	public void changeTheStatusOfAdvancePlacementScheme(String userId, String caregiverId, String status);

	public void changeTheStatusOfMedicalCertVerified(String validatorId, String caregiverId, String status);

	public void saveBio(Bio bio);
	
	public Bio getBioByCaregiverId(int caregiverId);

	public void editBioInfoByCaregiverId(Bio bio);

	public Integer getTotalAmount(String querySql);

	public Integer getAvailableAmount(String querySql);

	public Integer getContractedAmount(String querySql);

	public Integer getNewAmount(String querySql);

	public Integer getTaggedAmount(String querySql);

	public Integer getOnHoldAmount(String querySql);
	
	public void saveSendCV(SendCV cv, String sData);

	public List<SendCV> getSendCv();

	public List<Caregiver> getCaregiverListIds(String ids);

	public void updateCaregiver(Caregiver caregiver);

	public void updatePhoto(Caregiver caregiver);

	public Caregiver getCaregiverBio(String userId);

	public List<Caregiver> getCandidatesByParams(String queryInfo);
	
	public int saveCaregiverOfDashboard(Caregiver candidate);

	public void editCaregiverOfDashboard(Caregiver candidate);

	public void editCaregiverCv(Caregiver candidate);

	public void deleteCandidatesByIds(String ids);

	public void editCaregiverInfo(Bio bio);

	public void deleteComment(String id);

	public void editCvBio(Bio bio, Integer userId);

	public int checkExistCandidate(String email);

	public int checkExistCandidate(String email, String userEmail);

	public String getEmailByCandidateId(String candidateId);
	
}
