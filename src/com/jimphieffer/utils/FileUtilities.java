package com.jimphieffer.utils;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

public class FileUtilities {
    @NotNull public static String loadFile(String path) {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(FileUtilities.class.getResourceAsStream(path)))) {
            return br.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
