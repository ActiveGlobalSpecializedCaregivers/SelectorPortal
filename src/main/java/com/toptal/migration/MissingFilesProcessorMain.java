package com.toptal.migration;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

/**
 * <code>MigrationMain</code>
 *
 * @author Eduard Napolov
 * @version 1.0
 */
@Component
public class MissingFilesProcessorMain {

    @Autowired
    private MigrationController migrationController;

    public static void main(String[] args) throws IOException {
        ApplicationContext ctx =
                new AnnotationConfigApplicationContext(MigrationAppConfig.class);

        MissingFilesProcessorMain main = ctx.getBean(MissingFilesProcessorMain.class);
        main.migrationController.processMissingProspectFile(args[0]);
    }
}
