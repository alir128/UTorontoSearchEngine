package com.cscc01.demo.Controllers;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cscc01.demo.Controllers.PublicOutputBeans.ResultObject;
import com.cscc01.demo.Models.Lucene.LuceneProcess;
import com.cscc01.demo.Models.Lucene.LuceneSearch;
import com.cscc01.demo.Models.Lucene.LuceneBeans.DocumentWithHighlights;

@Controller
public class TextEditorController {
	
    @PostMapping("/textSearch")
    @ResponseBody
    public ArrayList<ResultObject> ajaxStuff(
            @RequestParam("searchQuery") String searchQuery
    ) throws Exception {

        // lucene -> documents
        ArrayList<DocumentWithHighlights> documents = LuceneSearch.searchBody(searchQuery, 10);

        // documents -> resultObjects
        ArrayList<ResultObject> resultObjects = LuceneProcess.fromDocuments(documents);

        // return resultObjects
        return resultObjects;
    }
    
    // Get request for the text editor
    @GetMapping("/textSearch")
    public String getTextSearch() throws Exception {
    	
    	return "public/textEditor";
    }
}
