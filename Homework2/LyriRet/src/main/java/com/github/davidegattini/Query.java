package com.github.davidegattini;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterGraphFilterFactory;
import org.apache.lucene.codecs.simpletext.SimpleTextCodec;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Query {
    private static String FILENAME = "filename";
    private static String CONTENT = "content";
    private static String LUCENE_IDX = "target/idx0";
    public static void main(String[] args) throws Exception{

        org.apache.lucene.search.Query query = makeQuery();
        Path path = Paths.get(LUCENE_IDX);
        try (Directory directory = FSDirectory.open(path)){
            try (IndexReader reader = DirectoryReader.open((directory))){
                IndexSearcher searcher = new IndexSearcher(reader);
                runQuery(searcher, query);
            } finally {
                directory.close();
            }
        }
    }

    public static org.apache.lucene.search.Query makeQuery() throws Exception{
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
                        .addTokenFilter(WordDelimiterGraphFilterFactory.class)
                        .addTokenFilter(LowerCaseFilterFactory.class)
                        .build();
        QueryParser queryParser = new QueryParser(field, queryAnalyzer);
        return queryParser.parse(query);
    }

    private static void runQuery(IndexSearcher searcher, org.apache.lucene.search.Query query) throws Exception {
        runQuery(searcher, query, false);
    }

    private static void runQuery(IndexSearcher searcher, org.apache.lucene.search.Query query, boolean explain) throws Exception{
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




}