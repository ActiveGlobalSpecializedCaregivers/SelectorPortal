package com.cloudaxis.agsc.portal.security;

import com.cloudaxis.agsc.portal.helpers.StringUtil;
import com.cloudaxis.agsc.portal.model.User;
import de.taimos.totp.TOTP;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

/**
 * Provides 2 factor authentication capabilities to the login process.
 */
public class TwoFADaoAuthenticationProvider extends DaoAuthenticationProvider {

	private final static Logger logger	= Logger.getLogger(TwoFADaoAuthenticationProvider.class);

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		logger.info("additionAuthentication, user="+userDetails+" authentication.credentials="+authentication.getCredentials()+" principal="+authentication.getPrincipal());
		super.additionalAuthenticationChecks(userDetails, authentication);

		User user = (User) userDetails;
		String secretKey = user.getSecretKey();
		logger.info("user="+user);
		if(StringUtil.isBlank(secretKey)){
			logger.debug("Secret key is missing");
			throw new BadCredentialsException("Google Authenticator configuration is missing.");
		}

		String verificationCode = ((CustomWebAuthenticationDetails) authentication.getDetails()).getVerificationCode();
		logger.debug("verificationCode="+verificationCode);
		if (!validVerificationCode(verificationCode, secretKey)) {
			throw new BadCredentialsException("Invalid verification code");
		}
	}

	private boolean validVerificationCode(String verificationCode, String secretKey) {
		Base32 base32 = new Base32();
		byte[] bytes = base32.decode(secretKey);
		String hexKey = Hex.encodeHexString(bytes);
		String calculatedOtp = TOTP.getOTP(hexKey);
		boolean validCode = calculatedOtp.equals(verificationCode);
		logger.info("checking verification code, received:"+verificationCode+" expected="+calculatedOtp);
		if(!validCode){
			logger.info("invalid verification code, received:"+verificationCode+" expected="+calculatedOtp);
			logger.info("Current server time now:"+new Date());
		}
		return validCode;
	}
}
