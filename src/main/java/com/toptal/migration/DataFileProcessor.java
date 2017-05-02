package com.toptal.migration;

import java.io.IOException;

/**
 * <code>DataFileProcessor</code>
 *
 * @author Eduard Napolov
 * @version 1.0
 */
public interface DataFileProcessor extends AutoCloseable {

    boolean hasNextLine() throws IOException;
    String nextLine() throws IOException;
    void close() throws IOException;
}
