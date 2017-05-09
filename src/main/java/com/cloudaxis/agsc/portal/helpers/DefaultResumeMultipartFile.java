package com.cloudaxis.agsc.portal.helpers;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

/**
 * <code>DefaultResumeMultipartFile</code>
 *
 * @author Eduard Napolov
 * @version 1.0
 */
public class DefaultResumeMultipartFile implements MultipartFile
{
    private static final Logger logger = Logger.getLogger(DefaultResumeMultipartFile.class);

    public static DefaultResumeMultipartFile getInstance()
    {
        return instance;
    }

    private static final DefaultResumeMultipartFile instance = new DefaultResumeMultipartFile();

    private byte[] defaultResumeBytes;

    private DefaultResumeMultipartFile()
    {
        InputStream in = getClass().getResourceAsStream("default-resume.pdf");
        try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(in)){
            byte[] buffer = new byte[1024];
            int length;
            while ((length = bufferedInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
            defaultResumeBytes = byteArrayOutputStream.toByteArray();
        }
        catch (IOException e)
        {
            logger.error("Failed to load default resume:"+e, e);
        }
    }

    @Override
    public String getName()
    {
        return "resume.pdf";
    }

    @Override
    public String getOriginalFilename()
    {
        return "resume.pdf";
    }

    @Override
    public String getContentType()
    {
        return null;
    }

    @Override
    public boolean isEmpty()
    {
        return false;
    }

    @Override
    public long getSize()
    {
        return defaultResumeBytes.length;
    }

    @Override
    public byte[] getBytes() throws IOException
    {
        return defaultResumeBytes;
    }

    @Override
    public InputStream getInputStream() throws IOException
    {
        return new ByteArrayInputStream(defaultResumeBytes);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException
    {
        try(FileOutputStream fileOutputStream = new FileOutputStream(dest))
        {
            fileOutputStream.write(defaultResumeBytes);
            fileOutputStream.flush();
        }
    }
}
