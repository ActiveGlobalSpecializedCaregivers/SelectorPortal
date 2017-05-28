package com.toptal.migration;

import java.io.File;

/**
 * <code>AttachmentResolver</code> resolves prospects attachments
 *
 * @author Eduard Napolov
 * @version 1.0
 */
public interface AttachmentResolver {

    File resolveProspectFile(String prospectId, String fileName);
    File resolveProspectResume(String prospectId, String resumeName);
}
