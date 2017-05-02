package com.toptal.migration;

import java.util.ArrayList;

/**
 * <code>TestDataFileProcessorFactory</code>
 *
 * @author Eduard Napolov
 * @version 1.0
 */
public class TestDataFileProcessorFactory implements DataFileProcessorFactory {
    private final ArrayList<String> lines;

    public TestDataFileProcessorFactory(ArrayList<String> lines) {
        this.lines = lines;
    }

    @Override
    public DataFileProcessor getDataFileProcessor(String dataKey) {
        TestDataFileProcessor processor = new TestDataFileProcessor();
        lines.forEach(processor::addLine);
        return processor;
    }
}
