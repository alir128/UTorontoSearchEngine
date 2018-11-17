package com.cscc01.demo.Models.Lucene.CustomAnalyzers;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.synonym.SynonymGraphFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.analysis.synonym.WordnetSynonymParser;

import java.io.FileReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class MyAnalyzer {

    public static PerFieldAnalyzerWrapper wrapperAnalyzer() {

        Map<String, Analyzer> analyzerMap = new HashMap<>();
        analyzerMap.put("body", MyAnalyzer.stemAnalyzer());
//        analyzerMap.put("tags",
//                new Analyzer() {
//                    @Override
//                    protected TokenStreamComponents createComponents(String fieldName) {
//
//                        Tokenizer source = new WhitespaceTokenizer();
//                        TokenStream result = new LowerCaseFilter(source);
//
//                        return new TokenStreamComponents(source, result);
//                    }
//                });

//        analyzerMap.put("groupCodes",
//                new Analyzer() {
//                    @Override
//                    protected TokenStreamComponents createComponents(String fieldName) {
//
//                        Tokenizer source = new WhitespaceTokenizer();
//                        TokenStream result = new LowerCaseFilter(source);
//
//                        return new TokenStreamComponents(source, result);
//                    }
//                });

        PerFieldAnalyzerWrapper wrapper = new PerFieldAnalyzerWrapper(
                new WhitespaceAnalyzer(),
                analyzerMap
        );

        return wrapper;
    }

    public static Analyzer testAnalyzer() {

        Analyzer analyzer = new Analyzer() {
            @Override
            protected TokenStreamComponents createComponents(String fieldName) {

                Tokenizer source = new StandardTokenizer();
                TokenStream result = new StandardFilter(source);

                result = new LowerCaseFilter(result);

                try {
                    WordnetSynonymParser parser = new WordnetSynonymParser(true
                            , false
                            , new StandardAnalyzer(CharArraySet.EMPTY_SET));
                    Reader reader = new FileReader("wn/wn_s.pl");
                    parser.parse(reader);
                    SynonymMap synonymMap = parser.build();

                    result = new SynonymGraphFilter(source, synonymMap, true);
                }
                catch (Exception e) {
                    System.out.println(e);
                }

                result = new PorterStemFilter(result);
                result = new StopFilter(result, StopAnalyzer.ENGLISH_STOP_WORDS_SET);

                return new TokenStreamComponents(source, result);
            }
        };

        return analyzer;
    }

    public static Analyzer stemAnalyzer() {
        Analyzer analyzer = new Analyzer() {
            @Override
            protected TokenStreamComponents createComponents(String fieldName) {

                Tokenizer source = new StandardTokenizer();
                TokenStream result = new StandardFilter(source);

                result = new LowerCaseFilter(result);

                result = new PorterStemFilter(result);
                result = new StopFilter(result, StopAnalyzer.ENGLISH_STOP_WORDS_SET);

                return new TokenStreamComponents(source, result);
            }
        };

        return analyzer;
    }
}
