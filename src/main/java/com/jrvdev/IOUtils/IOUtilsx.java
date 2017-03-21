package com.jrvdev.IOUtils;

import java.io.ObjectInput;
import java.io.FileOutputStream;
import java.io.InputStream;

import java.io.IOException;

import java.util.zip.ZipFile;

public class IOUtilsx {
  public static void closeQuietly(ObjectInput o) {
    if (o == null) return;

    try {
      o.close();
    }
    catch (IOException e) {
      // ignore
    }
  }

  public static void closeQuietly(FileOutputStream o ) {
    if ( o == null ) return;
    try {
      o.close();
    }
    catch ( IOException e ) {
      // ignore
    }
  }
  public static void closeQuietly(InputStream i ) {
    if ( i == null ) return;
    try {
      i.close();
    }
    catch ( IOException e ) {
      // ignore
    }
  }
  public static void closeQuietly( ZipFile z ) {
    if ( z == null ) return;
    try {
      z.close();
    }
    catch ( IOException e ) {
      // ignore
    }
  }
}