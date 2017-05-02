package com.toptal.migration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <code>PropertiesFileResolver</code> resolves files using properties file.
 *
 * @author Eduard Napolov
 * @version 1.0
 */
public class PropertiesDataFileProcessorFactory implements DataFileProcessorFactory {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesDataFileProcessorFactory.class);
    private final Properties properties;

    public PropertiesDataFileProcessorFactory(String propertiesFileName) throws IOException {
        logger.info("Creating factory using properties from file:"+propertiesFileName);
        properties = new Properties();
        properties.load(new FileInputStream(propertiesFileName));

        logger.info("Loaded properties:"+properties);
    }

    @Override
    public DataFileProcessor getDataFileProcessor(String fileKey) throws IOException {
        String fileName = properties.getProperty(fileKey);
        if(fileName == null){
            throw new RuntimeException("Missing key:"+fileKey);
        }
        File file = new File(fileName);
        if(!file.exists()){
            throw new RuntimeException("Invalid key:"+fileKey);
        }
        logger.info("getDataFileProcessor, key:"+fileKey+" fileName:"+fileName);
        return new DefaultDataFileProcessor(file);
    }
}
