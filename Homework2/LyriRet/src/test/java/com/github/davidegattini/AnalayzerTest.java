package com.github.davidegattini;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterGraphFilterFactory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.tests.analysis.TokenStreamToDot;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;


public class AnalayzerTest {



    @Test
    public void testEnglishAnalyzer() throws Exception{
        Analyzer enAnalyzer = new EnglishAnalyzer();
        TokenStream ts = enAnalyzer.tokenStream(null, "Everything you want's a dream away");
        StringWriter w = new StringWriter();
        new TokenStreamToDot(null, ts, new PrintWriter(w)).toDot();
        System.out.println(w);
    }

    @Test
    public void testWhitespaceAnalyzer() throws Exception{
        Analyzer whitespaceAnalyzer = new WhitespaceAnalyzer();
        TokenStream ts = whitespaceAnalyzer.tokenStream(null, "How to be an Engineer of content, please?");
        StringWriter w = new StringWriter();
        new TokenStreamToDot(null, ts, new PrintWriter(w)).toDot();
        System.out.println(w);
    }

    @Test
    public void testStandardAnalyzer() throws Exception{
        Analyzer standardAnalyzer = new StandardAnalyzer();
        TokenStream ts = standardAnalyzer.tokenStream(null, "How to be an Engineer of data, please?");
        StringWriter w = new StringWriter();
        new TokenStreamToDot(null, ts, new PrintWriter(w)).toDot();
        System.out.println(w);
    }

    @Test
    public void testStandardAnalyzerWithDash() throws Exception{
        Analyzer standardAnalyzer = new StandardAnalyzer();
        TokenStream ts = standardAnalyzer.tokenStream(null, "How to access: wi-fi, please?");
        StringWriter w = new StringWriter();
        new TokenStreamToDot(null, ts, new PrintWriter(w)).toDot();
        System.out.println(w);
    }

    @Test
    public void testStandardAnalyzerUnited() throws Exception{
        Analyzer standardAnalyzer = new StandardAnalyzer();
        TokenStream ts = standardAnalyzer.tokenStream(null, "Thequickbrownfoxcan'tJump32.3feetright");
        StringWriter w = new StringWriter();
        new TokenStreamToDot(null, ts, new PrintWriter(w)).toDot();
        System.out.println(w);
    }

    // Quando si usano le stop words, si sostituiscono con "." (per le phrase query)
    @Test
    public void testStandardAnalyzerWithStopWords() throws Exception{
        CharArraySet stopWords = new CharArraySet(Arrays.asList("of", "an", "a", "the", "for"), true);
        Analyzer enAnalyzer = new StandardAnalyzer(stopWords);
        TokenStream ts = enAnalyzer.tokenStream(null, "How to be an Engineer of data, please?");
        StringWriter w = new StringWriter();
        new TokenStreamToDot(null, ts, new PrintWriter(w)).toDot();
        System.out.println(w);
    }

    @Test
    public void testQueryPasers() throws Exception{
        Analyzer queryAnalyzer = CustomAnalyzer.builder()
                .withTokenizer(WhitespaceTokenizerFactory.class)
                .addTokenFilter(LowerCaseFilterFactory.class)
                .addTokenFilter(WordDelimiterGraphFilterFactory.class)
                .build();
        TokenStream ts = queryAnalyzer.tokenStream(null, "How to be an Engineer of data, please?");
        StringWriter w = new StringWriter();
        new TokenStreamToDot(null, ts, new PrintWriter(w)).toDot();
        System.out.println(w);
    }
}
