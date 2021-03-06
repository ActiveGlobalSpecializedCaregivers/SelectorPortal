package com.toptal.migration;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

/**
 * <code>EmailValidationProcessorMain</code>
 *
 * @author Eduard Napolov
 * @version 1.0
 */
@Component
public class EmailValidationProcessorMain
{
    @Autowired
    private MigrationController migrationController;

    public static void main(String[] args) throws IOException {
        ApplicationContext ctx =
                new AnnotationConfigApplicationContext(MigrationAppConfig.class);

        EmailValidationProcessorMain main = ctx.getBean(EmailValidationProcessorMain.class);
        main.migrationController.validateEmailForUpdate(args[0]);
    }
}
