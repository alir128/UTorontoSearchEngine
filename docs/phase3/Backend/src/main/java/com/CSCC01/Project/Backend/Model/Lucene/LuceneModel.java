package com.CSCC01.Project.Backend.Model.Lucene;

import com.CSCC01.Project.Backend.Controllers.InputBeans.SearchBean;
import com.CSCC01.Project.Backend.Controllers.InputBeans.UploadBean;
import com.CSCC01.Project.Backend.Controllers.OutputBeans.DocumentBean;
import com.CSCC01.Project.Backend.Model.SchemaBeans.User;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class LuceneModel {

    public static void addDocument(String path, String title, String ext, UploadBean uploadBean, User user) throws Exception {

        IndexWriter indexWriter = LuceneInit.getIndexWriter();

        // Get the file
        File file = new File(path);

        // Create the document
        Document document = new Document();

        // Add to the document
        // title
        document.add(new TextField("title", title, Field.Store.YES));

        // ext
        document.add(new StringField("ext", ext, Field.Store.YES));

        // tags
        ArrayList<String> tags = uploadBean.getTags();
        if (tags != null) {
            for (String tag: tags) {
                document.add(new StringField("tags", tag, Field.Store.YES));
            }
        }

        // path
        document.add(new StringField("path", path, Field.Store.YES));

        // file
        document.add(new TextField("body", new FileReader(file)));

        // file content
        String content = new Scanner(new File(path)).useDelimiter("\\Z").next();
        document.add(new StringField("content", content, Field.Store.YES));

        // access groups
        ArrayList<String> accessGroups = uploadBean.getAccessGroups();
        if (accessGroups != null) {
            for (String group: accessGroups) {
                document.add(new StringField("accessGroups", group, Field.Store.YES));
            }
        }

        // add document
        indexWriter.addDocument(document);
        indexWriter.commit();
    }

    public static ArrayList<Document> search(SearchBean searchBean) throws Exception {

        ArrayList<String> title = searchBean.getTitle();
        String ext = searchBean.getExt();
        ArrayList<String> tags = searchBean.getTags();
        ArrayList<String> body = searchBean.getBody();
        ArrayList<String> accessGroups = searchBean.getAccessGroups();

        // index, index reader, and index searcher
        FSDirectory index = LuceneInit.getIndex();
        IndexReader indexReader = DirectoryReader.open(index);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        String[] fields = {"title", "ext", "tags", "body", "accessGroups"};

        // query q
        String q = "";

        // adding body contents to the query
        if (!body.isEmpty()) {
            q += "+(";
            for (String word: body) {
                q += "+(body:" + word + ") ";
            }
            q += ") ";
        }

        // adding ext to the query
        if (!ext.equals("")) {
            q += "+(ext:" + ext + ") ";
        }

        // adding tags to the query
        if (!tags.isEmpty()) {
            q += "+(";
            for (String word: tags) {
                q += "+(tags:" + word + ") ";
            }
            q += ") ";
        }

        // adding title to the query
        if (!title.isEmpty()) {
            q += "+(";
            for (String word: title) {
                q += "+(title:" + word + ") ";
            }
            q += ") ";
        }

        // adding accessGroups
        if (accessGroups.isEmpty()) {
            q += "+(accessGroups:public) ";
        }
        else {
            q += "+(";
            for (String word: accessGroups) {
                q += "+(accessGroups:" + word + ") ";
            }
            q += ") ";
        }

        // execute query
        Query query = new MultiFieldQueryParser(fields, new StandardAnalyzer()).parse(q);

        TopDocs topDocs = indexSearcher.search(query, 10);
        ArrayList<Document> documents = new ArrayList<>();

        for (ScoreDoc scoreDoc: topDocs.scoreDocs) {
            documents.add(indexSearcher.doc(scoreDoc.doc));
        }

        return documents;
    }

    public static ArrayList<DocumentBean> getDocBeans(ArrayList<Document> documents) {

        ArrayList<DocumentBean> documentBeans = new ArrayList<>();

        for (Document document: documents) {
            documentBeans.add(new DocumentBean(document.get("title"), document.get("content")));
        }

        return documentBeans;
    }
}
