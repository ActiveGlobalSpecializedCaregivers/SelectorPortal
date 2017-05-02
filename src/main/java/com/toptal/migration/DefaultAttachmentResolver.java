package com.toptal.migration;

import java.io.File;
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

    public DefaultAttachmentResolver() {
        root = System.getProperty("attachments.root");
        logger.info("Root of attachments: "+root);
    }

    @Override
    public File resolveProspectFile(String prospectId, String fileName) {
        final File file = new File(root + File.separatorChar +
                                   "files" + File.separatorChar +
                                   "prospects" + File.separatorChar +
                                   prospectId + File.separatorChar +
                                   fileName);
        logger.info("resolveProspectFile prospectId:"+prospectId+" fileName:"+fileName+" resolved to file:"+file);
        return file;
    }

    @Override
    public File resolveProspectResume(String resumeName) {
        final File file = new File(root +
                                   File.separatorChar +
                                   "resumes" +
                                   File.separatorChar +
                                   resumeName);
        logger.info("resolveProspectResume resumeName:"+resumeName+" resolved to file:"+file);
        return file;
    }
}
