package com.github.davidegattini;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterGraphFilterFactory;
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
    private static String FILENAME = "filename";
    private static String CONTENT = "content";
    private static String LUCENE_IDX = "target/idx0";
    private static String LYRICS_PATH = "../lyrics/";
    public static void main(String[] args) throws Exception{

        Query query = makeQuery();

        // Where to store Lucene index
        Path path = Paths.get(LUCENE_IDX);
        // Create Lucene directory for r/w access on Lucene index
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

    private static Query makeQuery() throws Exception{
        String field = "";
        String[] splittedInput = {};
        String userInput = "";
        boolean isValidField = false;

        Scanner in = new Scanner(System.in);
        do {
            System.out.println("What are you searching for?");
            userInput = in.nextLine();
            splittedInput = userInput.split(":");
            field = splittedInput[0].trim();
            isValidField = field.equals(FILENAME) || field.equals(CONTENT);
            if(!isValidField) {
                System.out.println("First word must be \"filename\" or \"content\"");
            }
        } while (!isValidField);
        in.close();
        String query = splittedInput[1].trim();
        System.out.println("INPUT: " + userInput);
        System.out.println("SEARCHED FIELD: " + field);
        System.out.println("SEARCHED QUERY: " + query);
        Analyzer queryAnalyzer = CustomAnalyzer.builder()
                        .withTokenizer(WhitespaceTokenizerFactory.class)
                        .addTokenFilter(LowerCaseFilterFactory.class)
                        .addTokenFilter(WordDelimiterGraphFilterFactory.class)
                        .build();
        QueryParser queryParser = new QueryParser(field, queryAnalyzer);
        return queryParser.parse(query);
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

    private static void indexDocs(Directory luceneDir, Codec codec) throws Exception{
        // Title's analyzer = StandardAnalyzer
        // {Tokenizer: Whitespace; TokenFilter: delimiter, lowercase, stopwords}
        CharArraySet stopWords = new CharArraySet(Arrays.asList("of", "an", "a", "the", "for"), true);
        Analyzer defaultAnalyzer = new StandardAnalyzer(stopWords);
        // Content's analyzer = EnglishAnalyzer
        // {Tokenizer: Whitespace; TokenFilter: stemming, lowercase, stopwords, english possessive, set keyword marker}
        Analyzer enAnalyzer = new EnglishAnalyzer();

        Map<String, Analyzer> perFieldAnalyzer = new HashMap<>();
        perFieldAnalyzer.put(FILENAME, defaultAnalyzer);
        perFieldAnalyzer.put(CONTENT, enAnalyzer);
        Analyzer analyzer = new PerFieldAnalyzerWrapper(defaultAnalyzer, perFieldAnalyzer);

        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        if(codec != null){
            config.setCodec(codec);
        }
        IndexWriter writer = new IndexWriter(luceneDir, config);
        writer.deleteAll();

        // ciclo per tutti i documenti trasformando:
        // il nome del file      -> TextField = filename
        // il contenuto del file -> TextField = content

        List<File> lyricsTxts = new ArrayList<>();
        listAllFileOfDirectory(LYRICS_PATH, lyricsTxts);

        if(lyricsTxts != null) {
            for (File lyTxt : lyricsTxts) {
                String filename = lyTxt.getName();
                String content = readStringFromTxt(lyTxt.getPath());
                Document doc = new Document();
                doc.add(new TextField(FILENAME, filename, Field.Store.YES));
                doc.add(new TextField(CONTENT, content, Field.Store.YES));

                writer.addDocument(doc);
            }
            writer.commit();
        }
        writer.close();
    }

    public static void listAllFileOfDirectory(String directoryName, List<File> files) {
        File directory = new File(directoryName);

        // Get all files from a directory.
        File[] fList = directory.listFiles();
        if(fList != null)
            for (File file : fList) {
                if(!file.isHidden()){
                    if (file.isFile()) {
                        files.add(file);
                    } else if (file.isDirectory()) {
                        listAllFileOfDirectory(file.getAbsolutePath(), files);
                    }
                }
            }
    }

    private static String readStringFromTxt(String pathFile) throws Exception{
        //System.out.println(pathFile);
        Path filePath = Path.of(pathFile);
        String content = Files.readString(filePath);
        content = content.replace("\n", " ");
        return content;
    }
}