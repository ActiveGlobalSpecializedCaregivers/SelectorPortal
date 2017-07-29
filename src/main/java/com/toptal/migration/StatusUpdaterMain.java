package com.toptal.migration;

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
public class StatusUpdaterMain
{

    @Autowired
    private MigrationController migrationController;

    public static void main(String[] args) {
        ApplicationContext ctx =
                new AnnotationConfigApplicationContext(MigrationAppConfig.class);

        StatusUpdaterMain main = ctx.getBean(StatusUpdaterMain.class);
        main.migrationController.updateStatusToHold();
    }
}
