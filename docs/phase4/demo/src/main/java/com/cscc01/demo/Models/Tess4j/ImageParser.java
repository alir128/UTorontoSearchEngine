package com.cscc01.demo.Models.Tess4j;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

import java.io.File;

public class ImageParser {

    public static String parse(File file) {

        // boiler plate
        ITesseract instance = new Tesseract();
        instance.setDatapath("tessdata/");
        
        // extract string
        try {
            String result = instance.doOCR(file);

            System.out.println("===============");
            System.out.println(result);
            System.out.println("===============");

            return result;
        }
        catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }
}
