package com.cscc01.demo.Models.Lucene;

import com.cscc01.demo.Models.Lucene.CustomAnalyzers.MyAnalyzer;
import com.cscc01.demo.Models.Lucene.LuceneBeans.DocumentWithHighlights;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.FSDirectory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class LuceneSearch {

    public static ArrayList<DocumentWithHighlights> searchOther(String q, String field, int maxHits) throws Exception {

        Query query = singleFieldQuery(q, field, maxHits);

        // return object
        ArrayList<DocumentWithHighlights> returnObjects = new ArrayList<>();

        for (Document document : search(query, maxHits)) {
            returnObjects.add(new DocumentWithHighlights(document, new String[0]));
        }

        return returnObjects;
    }

    public static ArrayList<DocumentWithHighlights> searchBody(String q, int maxHits) throws Exception {

        Query query = singleFieldQuery(q, "body", maxHits);
        Highlighter highlighter = bodyHighlighter(query);

        // return object
        ArrayList<DocumentWithHighlights> returnObjects = new ArrayList<>();

        for (Document document : search(query, maxHits)) {
            // make a new return object
            returnObjects.add(new DocumentWithHighlights(document,
                    getFragments(document, highlighter)));
        }

        return returnObjects;
    }

    private static Highlighter bodyHighlighter(Query query) throws Exception {

        QueryScorer queryScorer = new QueryScorer(query, "body");
        Formatter formatter = new SimpleHTMLFormatter("<mark style='background:pink'>", "</mark>");
        Highlighter highlighter = new Highlighter(formatter, queryScorer); // Set the best scorer fragments

//        Fragmenter fragmenter = new SimpleSpanFragmenter(queryScorer);
        Fragmenter fragmenter = new SimpleFragmenter(100);
        highlighter.setTextFragmenter(fragmenter); // Set fragment to highlight
        highlighter.setMaxDocCharsToAnalyze(Integer.MAX_VALUE);

        return highlighter;
    }

    private static Query singleFieldQuery(String q, String field, int maxHits) throws Exception {

        QueryParser parser = new QueryParser(field, MyAnalyzer.wrapperAnalyzer()); // using stemming
        parser.setAllowLeadingWildcard(true);

        // Escape the fileType string
        q = StringEscapeUtils.escapeJava(q);

        // Remove eof
        q = q.replaceAll("(\\r|\\n|\\t)", "");

        Query query = parser.parse(QueryParser.escape(q));

        return query;
    }

    private static ArrayList<Document> search(Query query, int maxHits) throws Exception {

        // index, index reader, and index searcher
        FSDirectory index = LuceneInit.getIndex();
        IndexReader indexReader = DirectoryReader.open(index);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        // get the top docs
        TopDocs topDocs = indexSearcher.search(query, maxHits);
        // make the return object
        ArrayList<Document> documents = new ArrayList<>();

        // iterate through each document
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            // get the document
            Document document = indexSearcher.doc(scoreDoc.doc);
            documents.add(document);
        }

        // return the documents with highlight data
        return documents;

    }

    // Multifield
    public static ArrayList<DocumentWithHighlights> multiSearch(
            String body,
            String tags,
            String groupCodes,
            String owner,
            String fromDate,
            String toDate,
            String fileType,
            String viewType,
            int maxHits,
            Occur occurLogic
    ) throws Exception {

        // Create the boolean query builder
    	BooleanQuery.Builder builder = new BooleanQuery.Builder();
    	
    	// check if body is empty

        if (!body.equals("")) {
    		
    		// make the query 
    		Query query = singleFieldQuery(body, "body", maxHits);
    		
    		// add the query to the builder
    		builder.add(new BooleanClause(query, Occur.MUST));
    	}
    	
    	// check if tags is empty
    	if (!tags.equals("")) {
    		
    		// make the query
    		Query query = singleFieldQuery(tags, "tags", maxHits);
    		
    		// add the query to the builder
    		builder.add(new BooleanClause(query, occurLogic));
    	}

    	// check if groupCodes is empty
        if (!groupCodes.equals("")) {

            // make the query
            Query query = singleFieldQuery(groupCodes, "groupCodes", maxHits);

            // add the query to the builder
            builder.add(new BooleanClause(query, occurLogic));
        }
    	
    	// check if owner is empty
    	if (!owner.equals("")) {
    		
    		// make the query
    		Query query = singleFieldQuery(owner, "owner", maxHits);
    		
    		// add the query to the builder
    		builder.add(new BooleanClause(query, occurLogic));
    	}
    	
    	// check if fromDate and toDate are empty
    	if (!fromDate.equals("") && !toDate.equals("")) {
			System.out.println(fromDate);
    		fromDate = new SimpleDateFormat("yyMMdd").format(
                new SimpleDateFormat("yy-MM-dd").parse(fromDate));
    		
    		toDate = new SimpleDateFormat("yyMMdd").format(
                new SimpleDateFormat("yy-MM-dd").parse(toDate));
    		System.out.println(fromDate);
    		// make the query
    		Query query = singleFieldQuery(
                "[" + fromDate + " TO " + toDate + "]",
                "date", maxHits);
    		
    		// add the query to the builder
    		builder.add(new BooleanClause(query, occurLogic));
    	}
    	
    	// check if owner is empty
    	if (!fileType.equals("")) {




            Query query = singleFieldQuery(fileType, "fileType", maxHits);
            builder.add(new BooleanClause(query, occurLogic));
    	}

        // check if viewType is empty
        if (!viewType.equals("")) {

            // make the query
            Query query = singleFieldQuery(viewType, "viewType", maxHits);

            // add the query to the builder
            builder.add(new BooleanClause(query, occurLogic));
        }
        
        builder.setMinimumNumberShouldMatch(1);
        BooleanQuery query = builder.build();

        ArrayList<Document> documents = search(query, maxHits);
        Highlighter highlighter = bodyHighlighter(query);

        // return objects
        ArrayList<DocumentWithHighlights> returnObjects = new ArrayList<>();

        for(Document document: documents) {

            // make a new return object
            returnObjects.add(new DocumentWithHighlights(document,
                    getFragments(document, highlighter)));
        }

        return returnObjects;
    }

    private static String[] getFragments(Document document, Highlighter highlighter) throws Exception {

        // get the fragments
        TokenStream stream = (MyAnalyzer.wrapperAnalyzer()).tokenStream("body", document.get("body"));
        String[] highlightData = highlighter.getBestFragments(stream, document.get("body"), 10);

        return  highlightData;
    }
}
