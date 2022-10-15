package com.github.davidegattini;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.codecs.Codec;
import org.apache.lucene.codecs.simpletext.SimpleTextCodec;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.FieldInfos;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.LMJelinekMercerSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.tests.analysis.TokenStreamToDot;

public class Main {
    public static void main(String[] args) throws Exception{

        // prendi la query da riga di comando o altro (sito web?)

        Query query = new MatchAllDocsQuery();

        // Where to store Lucene index
        Path path = Paths.get("target/idx0");
        // Create Lucene directory 4 r/w access on Lucene index
        try (Directory directory = FSDirectory.open(path)){
            indexDocs(directory, new SimpleTextCodec());
            try (IndexReader reader = DirectoryReader.open((directory))){
                IndexSearcher searcher = new IndexSearcher(reader);
                runQuery(searcher, query);
            } finally {
                directory.close();
            }
        }
    }

    private static void runQuery(IndexSearcher searcher, Query query) throws Exception {
        runQuery(searcher, query, false);
    }

    private static void runQuery(IndexSearcher searcher, Query query, boolean explain) throws Exception{
        TopDocs hits = searcher.search(query, 10);
        for(int i=0; i<hits.scoreDocs.length; i++){
            ScoreDoc scoreDoc = hits.scoreDocs[i];
            Document doc = searcher.doc(scoreDoc.doc);
            System.out.println("doc"+scoreDoc.doc + ":"+ doc.get("filename") + " (" + scoreDoc.score +")");
            if (explain) {
                Explanation explanation = searcher.explain(query, scoreDoc.doc);
                System.out.println(explanation);
            }
        }
    }

    /* indicizzo i documenti nella cartella (./lyrics/*)
     * considerando due campi (quindi due indici):
     * - uno per il nome del file
     * - contenuto del file
     * e utilizza due analyzer
     */

    private static void indexDocs(Directory dir, Codec codec) throws Exception{
        // Title's analyzer
        // minuscole e divisioni parole per spazion bianco
        CharArraySet stopWords = new CharArraySet(Arrays.asList("of", "an", "a", "the", "for"), true);
        Analyzer defaultAnalyzer = new StandardAnalyzer(stopWords);
        // Content's analyzer
        // minuscole, divisioni parole con spazio bianco
        // no stopwords e stemming
        Analyzer enAnalyzer = new EnglishAnalyzer();

        Map<String, Analyzer> perFieldAnalyzer = new HashMap<>();
        perFieldAnalyzer.put("filename", defaultAnalyzer);
        perFieldAnalyzer.put("content", enAnalyzer);
        Analyzer analyzer = new PerFieldAnalyzerWrapper(defaultAnalyzer, perFieldAnalyzer);

        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        if(codec != null){
            config.setCodec(codec);
        }
        IndexWriter writer = new IndexWriter(dir, config);
        writer.deleteAll();

        // ciclo per tutti i documenti trasformando:
        // il nome del file      -> TextField = filename
        // il contenuto del file -> TextField = content
        // todo: cambia il path
        String path = "../lyrics/Coldplay";
        File lyricsDirectory = new File(path);
        File[] lyricsTxts = lyricsDirectory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return !file.isHidden();
            }
        });
        if(lyricsTxts != null) {
            int i = 0;
            for (File lyTxt : lyricsTxts) {
                String filename = lyTxt.getName();
                String content = readStringFromTxt(lyTxt.getPath());
                Document doc = new Document();
                doc.add(new TextField("filename", filename, Field.Store.YES));
                doc.add(new TextField("content", content, Field.Store.YES));

                writer.addDocument(doc);
            }
            writer.commit();
        }
        writer.close();
    }

    private static String readStringFromTxt(String pathFile) throws Exception{
        System.out.println(pathFile);
        Path filePath = Path.of(pathFile);
        String content = Files.readString(filePath);
        content = content.replace("\n", " ");
        return content;
    }
}