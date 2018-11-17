package com.CSCC01.Project.Backend.Model.Lucene;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;

import static com.CSCC01.Project.Backend.Constants.PATH;

public class LuceneInit {

    // Lucene variables
    private static FSDirectory index = null;
    private static StandardAnalyzer standardAnalyzer = null;
    private static IndexWriterConfig indexWriterConfig = null;
    private static IndexWriter indexWriter = null;
    private static IndexReader indexReader = null;
    private static IndexSearcher indexSearcher = null;

    static {
        try {
            index = FSDirectory.open(Paths.get(PATH + "lucene.index"));
            standardAnalyzer = new StandardAnalyzer();
            indexWriterConfig = new IndexWriterConfig(standardAnalyzer);
            indexWriter = new IndexWriter(index, indexWriterConfig);

            indexWriter.commit();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public static FSDirectory getIndex() {
        return index;
    }

    public static StandardAnalyzer getStandardAnalyzer() {
        return standardAnalyzer;
    }

    public static IndexWriterConfig getIndexWriterConfig() {
        return indexWriterConfig;
    }

    public static IndexWriter getIndexWriter() {
        return indexWriter;
    }

    public static IndexReader getIndexReader() {
        return indexReader;
    }

    public static IndexSearcher getIndexSearcher() {
        return indexSearcher;
    }
}
