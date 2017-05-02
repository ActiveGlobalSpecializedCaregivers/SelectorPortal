package com.cloudaxis.agsc.portal.helpers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

public class RandomCodeUtils {

	protected Logger logger = Logger.getLogger(RandomCodeUtils.class);

	private static final String RANDOM_CODE_KEY = "random"; // put
															// the
															// code
															// in
															// the
															// session
															// of
															// the
															// key

	private static final int CODE_NUM = 4;

	// set the picture font and size
	private static Font myFont = new Font("Arial", Font.BOLD, 16);

	private static char[] charSequence = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

	private static Random random = new Random();

	/**
	 * produce the random code
	 * 
	 * @param request
	 * @param response
	 */
	public void createRandomCode(HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");

		// point to the code picture size
		int width = 80, height = 25;
		// the new picture
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		g.setColor(getRandomColor(200, 250));
		g.fillRect(1, 1, width - 1, height - 1);
		g.setColor(new Color(102, 102, 102));
		g.drawRect(0, 0, width - 1, height - 1);
		g.setFont(myFont);

		g.setColor(getRandomColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width - 1);
			int y = random.nextInt(height - 1);
			int x1 = random.nextInt(6) + 1;
			int y1 = random.nextInt(12) + 1;
			g.drawLine(x, y, x + x1, y + y1);
		}

		for (int i = 0; i < 70; i++) {
			int x = random.nextInt(width - 1);
			int y = random.nextInt(height - 1);
			int x1 = random.nextInt(12) + 1;
			int y1 = random.nextInt(6) + 1;
			g.drawLine(x, y, x - x1, y - y1);
		}

		StringBuilder sRand = new StringBuilder(CODE_NUM);
		for (int i = 0; i < CODE_NUM; i++) {
			String tmp = getRandomChar();
			sRand.append(tmp);

			g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(tmp, 15 * i + 10, 20);
		}

		HttpSession session = request.getSession(true);
		session.setAttribute(RANDOM_CODE_KEY, sRand.toString());
		g.dispose();
		try {
			ImageIO.write(image, "JPEG", response.getOutputStream());
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * check the code
	 * 
	 * @param request
	 * @param inputCode
	 * @return true or false
	 */
	public boolean checkRandomCode(HttpServletRequest request, String inputCode) {
		HttpSession session = request.getSession(false);
		if (session != null && StringUtils.hasLength(inputCode)) {
			String code = (String) session.getAttribute(RANDOM_CODE_KEY);
			// logger.info("inputCode:"+inputCode.trim()+",code:"+code);
			return inputCode.trim().equalsIgnoreCase(code);
		}
		return false;
	}

	/**
	 * remove the code
	 * 
	 * @param request
	 * @param inputCode
	 */
	public static void removeRandomCode(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(RANDOM_CODE_KEY);
		}
	}

	private static Color getRandomColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	private static String getRandomChar() {
		int index = random.nextInt(charSequence.length);
		return String.valueOf(charSequence[index]);
	}
}
