package com.toptal.migration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * <code>FileResolver</code> resolves key to the file.
 *
 * @author Eduard Napolov
 * @version 1.0
 */
public interface DataFileProcessorFactory {

    DataFileProcessor getDataFileProcessor(String dataKey) throws FileNotFoundException,
                                                                  IOException;
}
