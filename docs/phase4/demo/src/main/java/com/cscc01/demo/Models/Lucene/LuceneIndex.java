package com.cscc01.demo.Models.Lucene;

import com.cscc01.demo.Models.Tika.ParsedFile;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

import static com.cscc01.demo.Constants.PATH;

public class LuceneIndex {

    public static void indexDocument(
            MultipartFile file,
            String id,
            String owner,
            String time,
            String date,
            ArrayList<String> tags,
            ArrayList<String> groupCodes,
            String fileType,
            String viewType,
            String body
    ) throws Exception {
        IndexWriter indexWriter = LuceneInit.getIndexWriter();

        // new Document
        Document document = new Document();

        // add id
        document.add(new StringField("id", id, Field.Store.YES));

        // add path
        document.add(new StringField("path", PATH + file.getOriginalFilename(), Field.Store.YES));

        // add file
        document.add(new StringField("file", file.getOriginalFilename(), Field.Store.YES));

        // add owner
        document.add(new StringField("owner", owner, Field.Store.YES));
        
        // add timestamp
        document.add(new StringField("time", time, Field.Store.YES));
        
        // add date
        document.add(new StringField("date", date, Field.Store.YES));

        // add tags
        for (String tag: tags) {
            document.add(new StringField("tags", tag, Field.Store.YES));
        }

        // add groupCodes
        for (String groupCode: groupCodes) {
            document.add(new StringField("groupCodes", groupCode, Field.Store.YES));
        }

        // add body
        document.add(new TextField("body", body, Field.Store.YES));

        // add fileType
        document.add(new StringField("fileType", fileType.split(";")[0], Field.Store.YES));

        // add viewType
        document.add(new StringField("viewType", viewType, Field.Store.YES));

        // add the document and commit
        indexWriter.addDocument(document);
        indexWriter.commit();
    }
}
