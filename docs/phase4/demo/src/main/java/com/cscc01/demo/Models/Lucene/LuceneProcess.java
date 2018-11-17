package com.cscc01.demo.Models.Lucene;

import com.cscc01.demo.Controllers.PublicOutputBeans.ResultObject;
import com.cscc01.demo.Models.Lucene.LuceneBeans.DocumentWithHighlights;
import org.apache.lucene.document.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class LuceneProcess {

    public static ArrayList<ResultObject> fromDocuments(ArrayList<DocumentWithHighlights> highlightDocuments) throws Exception {
        ArrayList<ResultObject> results = new ArrayList<>();

        for (DocumentWithHighlights documentWithHighlights: highlightDocuments ) {
//            String content = new Scanner(new File(document.get("path"))).useDelimiter("\\Z").next();

            // get the document
            Document document = documentWithHighlights.getDocument();

            // get the highlight content
            String[] fragments = documentWithHighlights.getFragments();

            String content = "";
            for (String fragment: fragments) {
                content += fragment + " ... ";
            }

            results.add(new ResultObject(document.get("id"),
                    content,
                    document.get("file"),
                    document.get("time"),
                    document.get("owner"),
                    new SimpleDateFormat("yy-MM-dd")
                            .format(
                                    new SimpleDateFormat("yyMMdd")
                                            .parse(document.get("date"))
                    ),
                    document.get("fileType")
            ));
        }

        return results;
    }

    public static ResultObject fullContent(DocumentWithHighlights document) throws Exception {
    	Document doc = document.getDocument();
//    	String content = new Scanner(new File(doc.get("path"))).useDelimiter("\\Z").next();
        String content = doc.get("body");

        return new ResultObject(doc.get("id"),
                content,
                doc.get("file"),
                doc.get("time"),
                doc.get("owner"),
                new SimpleDateFormat("yy-MM-dd")
                        .format(
                                new SimpleDateFormat("yyMMdd")
                                        .parse(doc.get("date"))
                        ),
                doc.get("fileType")
        );
    }

}
