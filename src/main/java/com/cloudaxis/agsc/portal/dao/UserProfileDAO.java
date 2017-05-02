package com.cloudaxis.agsc.portal.dao;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.cloudaxis.agsc.portal.model.Profile;
import com.cloudaxis.agsc.portal.model.User;

public interface UserProfileDAO {

	public void updateProfile(Profile profile);

	public int addProfile(Profile profile);

	public void saveMailHistory(Profile profile, MimeMessage message, User user) throws IOException, MessagingException;
}
