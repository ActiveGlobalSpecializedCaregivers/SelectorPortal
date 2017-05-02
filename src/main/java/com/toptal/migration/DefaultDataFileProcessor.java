package com.toptal.migration;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * <code>DefaultDataFileProcessor</code>
 *
 * @author Eduard Napolov
 * @version 1.0
 */
public class DefaultDataFileProcessor implements DataFileProcessor {


    private final BufferedInputStream inputStream;
    private final Scanner scanner;

    public DefaultDataFileProcessor(File file) throws FileNotFoundException {
        inputStream = new BufferedInputStream(new FileInputStream(file), 1024*1024);
        scanner = new Scanner(inputStream);
    }

    @Override
    public boolean hasNextLine() throws IOException {
        return scanner.hasNextLine();
    }

    @Override
    public String nextLine() throws IOException {
        return scanner.nextLine();
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }
}
