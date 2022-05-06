package com.jimphieffer.utils;



import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

public class FileUtilities {
     public static String loadFile(String path) {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(FileUtilities.class.getResourceAsStream(path)))) {
            return br.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static byte[] loadFileAsBytes(String path) {
        try(InputStream input = FileUtilities.class.getResourceAsStream(path)) {
            return input.readAllBytes();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
