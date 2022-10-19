package com.github.davidegattini;

import com.github.davidegattini.FileUtility;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.EnglishPossessiveFilterFactory;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterGraphFilterFactory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.apache.lucene.codecs.Codec;
import org.apache.lucene.codecs.simpletext.SimpleTextCodec;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.util.*;

public class Indexer {
    private static String FILENAME = "filename";
    private static String CONTENT = "content";
    private static String LUCENE_IDX = "target/idx0";
    private static String LYRICS_PATH = "../lyrics/";

    public static void main(String[] args) {
        runIndexing();
    }

    public static void runIndexing() {
        // Where to store Lucene index
        Path path = Paths.get(LUCENE_IDX);
        // Create Lucene directory for r/w access on Lucene index
        try (Directory directory = FSDirectory.open(path)){
            long i = System.currentTimeMillis();
            try {
                indexDocs(directory, new SimpleTextCodec());
            } catch (Exception e) {
                System.out.println("Error occured during documents' indexing " + e.getMessage());
            }
            long j = System.currentTimeMillis() - i;
            System.out.println("Indexing time: " + j + " ms");
        } catch (IOException ioe) {
            System.out.println("Failed to open directory " + path + "\n" + ioe.getMessage());
        }
    }


    private static void indexDocs(Directory luceneDir, Codec codec) throws Exception{
        // Title's analyzer
        Analyzer standardAnalyzer = new StandardAnalyzer();
        // Content's analyzer
        CharArraySet stopWords = new CharArraySet(Arrays.asList("of", "an", "a", "the", "for"), true);
        Analyzer contentAnalyzer= CustomAnalyzer.builder()
                .withTokenizer(StandardTokenizerFactory.class)
                .addTokenFilter(LowerCaseFilterFactory.class)
                .addTokenFilter(StopFilterFactory.class, "ignoreCase", "false", "words", "stopwords.txt", "format", "wordset")
                .addTokenFilter(EnglishPossessiveFilterFactory.class).build();

        Map<String, Analyzer> perFieldAnalyzer = new HashMap<>();
        perFieldAnalyzer.put(FILENAME, standardAnalyzer);
        perFieldAnalyzer.put(CONTENT, contentAnalyzer);
        Analyzer analyzer = new PerFieldAnalyzerWrapper(standardAnalyzer, perFieldAnalyzer);

        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        if(codec != null){
            config.setCodec(codec);
        }

        IndexWriter writer = new IndexWriter(luceneDir, config);
        writer.deleteAll();

        // ciclo per tutti i documenti trasformando:
        // il nome del file      -> TextField = filename
        // il contenuto del file -> TextField = content

        FileUtility futils = new FileUtility();
        List<File> lyricsTxts = new ArrayList<>();
        lyricsTxts = futils.listAllFileOfDirectory(LYRICS_PATH);
        System.out.println(lyricsTxts.toString());

        if(lyricsTxts != null) {
            for (File lyTxt : lyricsTxts) {
                String filename = lyTxt.getName();
                String content = futils.readFromTxt(lyTxt.getPath());
                Document doc = new Document();
                doc.add(new TextField(FILENAME, filename, Field.Store.YES));
                doc.add(new TextField(CONTENT, content, Field.Store.YES));

                writer.addDocument(doc);
            }
            writer.commit();
        }
        writer.close();
    }
}
