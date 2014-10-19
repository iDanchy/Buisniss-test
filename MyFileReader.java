package com.example.testapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import android.util.Log;
public class MyFileReader {
	
 public String read(String fname){
     BufferedReader br = null;
     String response = null;
      try {
        StringBuffer output = new StringBuffer();
        String fpath = "/sdcard/"+fname+".txt";
        br = new BufferedReader(new FileReader(fpath));
        String line = "";
        while ((line = br.readLine()) != null) {
          output.append(line +"\n");
        }
        response = output.toString();
      } catch (IOException e) {
        e.printStackTrace();
        return null;
      }
      return response;
   }
}