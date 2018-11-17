package com.cscc01.demo.Models.Lucene;

import com.cscc01.demo.Models.Lucene.CustomAnalyzers.MyAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;

import static com.cscc01.demo.Constants.PATH;

public class LuceneInit {

    // Lucene variables
    private static FSDirectory index = null;
    private static Analyzer analyzer = null;
    private static IndexWriterConfig indexWriterConfig = null;
    private static IndexWriter indexWriter = null;
    private static IndexReader indexReader = null;
    private static IndexSearcher indexSearcher = null;

    static {
        try {
            index = FSDirectory.open(Paths.get(PATH + "lucene.index"));

            // todo analyzer for stemming and synonyms
//            analyzer = MyAnalyzer.testAnalyzer();
//            analyzer = MyAnalyzer.stemAnalyzer();
//            analyzer = new StandardAnalyzer();
            analyzer = MyAnalyzer.wrapperAnalyzer();
//            analyzer = new WhitespaceAnalyzer();

            indexWriterConfig = new IndexWriterConfig(analyzer);
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

    public static Analyzer getAnalyzer() {
        return analyzer;
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
