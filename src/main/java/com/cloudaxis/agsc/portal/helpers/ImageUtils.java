package com.cloudaxis.agsc.portal.helpers;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import com.cloudaxis.agsc.portal.constants.ImageThumbnailConstants;

public class ImageUtils {

	public BufferedImage createThumbnailImage(BufferedImage originImage) {

		BufferedImage bimage = null;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			// Determine the type of transparency of the new buffered image
			int transparency = Transparency.OPAQUE;

			// Create the buffered image
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			bimage = gc.createCompatibleImage(ImageThumbnailConstants.SHUMBNAIL_WIDTH, ImageThumbnailConstants.SHUMBNAIL_HEIGTH, transparency);
		} catch (HeadlessException e) {
			// The system does not have a screen
		}
		
		if (bimage == null) {
			// Create a buffered image using the default color model
			int type = originImage.getType();
			bimage = new BufferedImage(ImageThumbnailConstants.SHUMBNAIL_WIDTH, ImageThumbnailConstants.SHUMBNAIL_HEIGTH, type);
		}

		// Copy image to buffered image
		Graphics g = bimage.createGraphics();

		// Paint the image onto the buffered image
		g.drawImage(originImage, 0, 0, ImageThumbnailConstants.SHUMBNAIL_WIDTH,ImageThumbnailConstants.SHUMBNAIL_HEIGTH, null);
		g.dispose();

		return bimage;

	}
}
