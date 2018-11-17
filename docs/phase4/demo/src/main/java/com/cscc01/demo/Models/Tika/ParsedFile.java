package com.cscc01.demo.Models.Tika;

import com.cscc01.demo.Models.CloudSpeech.SpeechParser;
import com.cscc01.demo.Models.Tess4j.ImageParser;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static com.cscc01.demo.Constants.WRITELIMIT;


public class ParsedFile {

    private String fileType;
    private String body;

    public ParsedFile(File file) throws Exception {
        TikaConfig config = new TikaConfig("tika.xml");

        // setup boilerplate
        Metadata metadata = new Metadata();
        ContentHandler contentHandler = new BodyContentHandler(WRITELIMIT);
        ParseContext parseContext = new ParseContext();

        Parser parser = new AutoDetectParser(config);
        InputStream stream = new FileInputStream(file);

        // parse
        parser.parse(stream, contentHandler, metadata, parseContext);
        stream.close();

        // get the file type
        this.fileType = metadata.get("Content-Type");
        System.out.println(fileType);
        // extract body
        if (fileType.startsWith("image")) {
            this.body = ImageParser.parse(file);
        }
        else if (fileType.startsWith("audio")) {
        	this.body = SpeechParser.parse(file.getName());
        }
        else {
            this.body = contentHandler.toString();
        }
    }

    public String getFileType() {
        return fileType;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "ParsedFile{" +
                "fileType='" + fileType + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
