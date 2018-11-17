package com.cscc01.demo.Models.Lucene.LuceneBeans;

import org.apache.lucene.document.Document;

import java.util.ArrayList;
import java.util.Arrays;

public class DocumentWithHighlights {

    private Document document;
    private String[] fragments;

    public DocumentWithHighlights(Document document, String[] fragments) {
        this.document = document;
        this.fragments = fragments;
    }

    @Override
    public String toString() {
        return "DocumentWithHighlights{" +
                "document=" + document +
                ", fragments=" + Arrays.toString(fragments) +
                '}';
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public String[] getFragments() {
        return fragments;
    }

    public void setFragments(String[] fragments) {
        this.fragments = fragments;
    }
}
