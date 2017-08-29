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
public class FileValidatorMain
{

    @Autowired
    private FileValidationController validationController;

    public static void main(String[] args) throws Exception
    {
        ApplicationContext ctx =
                new AnnotationConfigApplicationContext(FileValidationAppConfig.class);

        FileValidatorMain main = ctx.getBean(FileValidatorMain.class);
        main.validationController.validateFiles();
//        main.validationController.deleteInvalidFiles();
    }
}
