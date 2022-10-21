package com.github.davidegattini;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.PatternSyntaxException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterGraphFilterFactory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Searcher {
    private static String FILENAME = "filename";
    private static String CONTENT = "content";
    private static String LUCENE_IDX = "target/idx0";
    public static void main(String[] args) throws Exception{
        newsearch();
    }

    public static void newsearch() throws Exception{
        String[] splittedInput = {};
        String field = "";
        String query = "";
        QueryParser qp = null;
        Query q = null;
        Path path = null;
        boolean isValidField = true;
        boolean isValidQuery = true;
        boolean quitTyped = false;

        Scanner in = new Scanner(System.in);
        Analyzer queryAnalyzer = new StandardAnalyzer();

        while(!quitTyped){
            System.out.println("---------------------------------------------------");
            System.out.println("Available fields are \"filename\" and \"content\"");
            System.out.println("What are you searching for?");
            System.out.println("Sintax: <field>: <query> (e.g. filename: \"sky full of stars\")");
            System.out.println("Type quit to stop searching");
            System.out.print("> ");

            String userInput = in.nextLine().trim();
            if("quit".equals(userInput.toLowerCase())){
                quitTyped = true;
                continue;
            }

            splittedInput = userInput.split(":");
            isValidQuery = (splittedInput.length == 2);
            field = splittedInput[0].trim();
            isValidField = (field.equals(FILENAME) || field.equals(CONTENT));

            if(!isValidField) {
                System.out.println("First word must be \"filename\" or \"content\" followed by \":\"");
                continue;
            }

            if(!isValidQuery){
                System.out.println("Typed an incorrect query");
                continue;
            }

            query = splittedInput[1].trim();
            qp = new QueryParser(field, queryAnalyzer);
            q = parseQuery(query, qp, q);
            path = Paths.get(LUCENE_IDX);

            try (Directory directory = FSDirectory.open(path)) {
                try (IndexReader reader = DirectoryReader.open((directory))) {
                    IndexSearcher searcher = new IndexSearcher(reader);
                    runQuery(searcher, q);
                } finally {
                    directory.close();
                }
            }
        }
        in.close();
    }

    private static Query parseQuery(String query, QueryParser qp, Query q) {
        try {
            q = qp.parse(query);
        } catch (ParseException pe) {
            q = new MatchNoDocsQuery();
        }
        return q;
    }

    private static void runQuery(IndexSearcher searcher, org.apache.lucene.search.Query query) throws Exception {
        runQuery(searcher, query, false);
    }

    private static void runQuery(IndexSearcher searcher, org.apache.lucene.search.Query query, boolean explain) throws Exception{
        TopDocs hits = searcher.search(query, 10);
        int totHit = hits.scoreDocs.length;
        if(totHit == 0)
            System.out.println("No docs found");
        for(int i=0; i<totHit; i++){
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