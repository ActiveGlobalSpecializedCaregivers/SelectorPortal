package com.toptal.migration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * <code>TestDataFileProcessor</code>
 *
 * @author Eduard Napolov
 * @version 1.0
 */
public class TestDataFileProcessor  implements DataFileProcessor{

    private final ArrayList<String> lines;
    private Iterator<String> iterator;

    public TestDataFileProcessor() {
        this.lines = new ArrayList<>();
    }

    public void addLine(String line){
        lines.add(line);
    }

    @Override
    public boolean hasNextLine() throws IOException {
        ensureIterator();
        return iterator.hasNext();
    }

    private void ensureIterator() {
        if(iterator == null){
            iterator = lines.iterator();
        }
    }

    @Override
    public String nextLine() throws IOException {
        ensureIterator();
        return iterator.next();
    }

    @Override
    public void close() throws IOException {
        iterator = null;
    }
}
