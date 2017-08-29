package com.toptal.migration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import com.cloudaxis.agsc.portal.dao.SelectedCaregiverDAO;
import com.cloudaxis.agsc.portal.service.FileService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 * <code>MigrationController</code> controls data migration process. Reads from data file
 * data and, depending on data, either creates or updates candidate data.
 *
 * @author Eduard Napolov
 * @version 1.0
 */
public class FileValidationController
{
    private static final Logger logger = Logger.getLogger(FileValidationController.class);

    @Autowired
    private FileService fileService;
    @Autowired
    private SelectedCaregiverDAO selectedCaregiverDAO;

    public void validateFiles() throws Exception
    {
        BufferedWriter writer = new BufferedWriter(new FileWriter("invalid_files.txt"));
        PrintWriter printWriter = new PrintWriter(writer);

        logger.info("Starting file validation....");
        // validate
        // 1. Get the list of all keys
        List<String> allFiles = fileService.findAllFiles();
        Collections.sort(allFiles);
        logger.info("Loaded files: "+allFiles.size());
        HashSet<String> processed = new HashSet<>();
        HashSet<String> notFound = new HashSet<>();
        HashSet<String> found = new HashSet<>();
        // 2. For each key:
        int count = 0;
        for(String key : allFiles){
            //    - extract user id
            String[] tokens = key.split("/");
            String candidateId = tokens[0];
            if(notFound.contains(candidateId)){
                logger.info("No caregiver found for key:"+key);
                count++;
                continue;
            }
            if(processed.contains(candidateId)){
                continue;
            }
            processed.add(candidateId);

            try
            {
                if(Integer.parseInt(candidateId) <= 7581){
                    continue;
                }
                //    - check if user is in the DB
                if(!candidateId.equals(selectedCaregiverDAO.getCaregiver(candidateId).getUserId()))
                {
                    //    - if not log, update statistics and delete files in S3
                    logger.info("No caregiver found for key:"+key+" id:"+candidateId);
                    printWriter.println(key);
                    count++;
                    notFound.add(candidateId);

                }
                else{
                    logger.info("Caregiver found with id:"+candidateId);
                    found.add(candidateId);
                }
            }
            catch (EmptyResultDataAccessException e)
            {
                //    - if not log, update statistics and delete files in S3
                logger.info("No caregiver found for key:"+key);
                count++;
                notFound.add(candidateId);
            }
            catch(NumberFormatException nfe)
            {
                // ignore, candidate id is not a key
            }
        }

        printWriter.flush();
        printWriter.close();

        logger.info("Total S3 entries without caregiver:"+count);
        logger.info("Total entries:"+allFiles.size());
        logger.info("Not found caregivers:"+notFound.size());
        logger.info("Found caregivers:"+found.size());
    }

    public void deleteInvalidFiles() throws IOException
    {
        BufferedReader reader = new BufferedReader(new FileReader("invalid_files.txt"));
        int count = 0;
        String key;
        while((key = reader.readLine()) != null){
            count++;
            System.out.println("deleting key = " + key);

            fileService.deleteFileFromS3(key);
        }
        System.out.println("total count = " + count);
    }
}
