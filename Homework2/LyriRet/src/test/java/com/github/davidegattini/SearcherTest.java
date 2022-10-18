package com.github.davidegattini;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

public class SearcherTest {

    /* PQ = Phrase query
    *  ...*/

    private Indexer idx;
    private Searcher searcher;

    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    @Before
    public void init() {
        Indexer.runIndexing();
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    private String getOutput() {
        return testOut.toString();
    }

    @After
    public void restoreSystemInputOutput() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }

    //===FILENAME TEST===
    @Test
    public void testExactFilenamePQ(){

    }

    @Test
    public void testExactFilenameLowerCasePQ(){

    }

    @Test
    public void testWrongFilenamePQ(){

    }


    //===CONTENT TEST===
}
