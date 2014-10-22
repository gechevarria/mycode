package com.tickloud.reader;


public class DataBaseReader {
  public static void main(String[] args) throws Exception {
	  Thread rt = new Thread(new ReadThread(), "rt");      
	  rt.start();
  }  
} 