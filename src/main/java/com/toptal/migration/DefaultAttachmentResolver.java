package com.toptal.migration;

import java.io.File;
import java.util.Map;

import com.toptal.migration.model.CandidateDocumentMapping;
import org.apache.log4j.Logger;

/**
 * <code>DefaultAttachmentResolver</code>
 *
 * @author Eduard Napolov
 * @version 1.0
 */
public class DefaultAttachmentResolver implements AttachmentResolver {

    private static final Logger logger = Logger.getLogger(MigrationController.class);

    private final String root;
    private final CandidateStorage candidateStorage;

    public DefaultAttachmentResolver(CandidateStorage candidateStorage) {
        this.candidateStorage = candidateStorage;
        root = System.getProperty("attachments.root");
        logger.info("Root of attachments: "+root);
    }

    @Override
    public File resolveProspectFile(String prospectId, String fileName) {
        Map<String, CandidateDocumentMapping> candidateMappings = candidateStorage.loadCandidateDocumentMappings(prospectId);
        logger.info("For prospect "+prospectId+" found mappings:" +candidateMappings);

        File file;
        if(candidateMappings != null && candidateMappings.containsKey(fileName)){
            CandidateDocumentMapping candidateDocumentMapping = candidateMappings.get(fileName);

            file = resolveUpdated(candidateDocumentMapping);
        }
        else{
            file = resolveOriginal(prospectId, fileName);
        }

        logger.info("resolveProspectFile prospectId:"+prospectId+" fileName:"+fileName+" resolved to file:"+file);
        return file;
    }

    private File resolveUpdated(CandidateDocumentMapping candidateDocumentMapping)
    {
        return new File(root + File.separatorChar +
                        candidateDocumentMapping.getDirectory()+File.separatorChar
                        +candidateDocumentMapping.getNewFileName());
    }

    private File resolveOriginal(String prospectId, String fileName)
    {
        return new File(root + File.separatorChar +
                                       "files" + File.separatorChar +
                                       "prospects" + File.separatorChar +
                                       prospectId + File.separatorChar +
                                       fileName);
    }

    @Override
    public File resolveProspectResume(String prospectId, String resumeName) {
        Map<String, CandidateDocumentMapping> candidateMappings = candidateStorage.loadCandidateDocumentMappings(prospectId);

        File file;
        if(candidateMappings != null && candidateMappings.containsKey(resumeName)){
            CandidateDocumentMapping candidateDocumentMapping = candidateMappings.get(resumeName);
            file = resolveUpdated(candidateDocumentMapping);
        }
        else{
            file = resolveResumeOriginal(resumeName);
        }

        logger.info("resolveProspectResume resumeName:"+resumeName+" resolved to file:"+file);
        return file;
    }

    private File resolveResumeOriginal(String resumeName)
    {
        return new File(root +
                        File.separatorChar +
                        "resumes" +
                        File.separatorChar +
                        resumeName);
    }
}
