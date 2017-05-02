package com.toptal.migration;

import java.io.IOException;

/**
 * <code>CandidateMigrationStrategy</code>
 *
 * @author Eduard Napolov
 * @version 1.0
 */
public interface CandidateMigrationStrategy {
    void migrateCandidate() throws IOException;
}
