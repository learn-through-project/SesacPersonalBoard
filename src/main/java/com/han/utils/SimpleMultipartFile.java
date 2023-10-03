package com.han.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class SimpleMultipartFile implements MultipartFile {

  private final String filename;
  private final byte[] fileContent;
  private final String contentType;

  public SimpleMultipartFile(String filename, byte[] fileContent, String contentType) {
    this.filename = filename;
    this.fileContent = fileContent;
    this.contentType = contentType;
  }

  @Override
  public String getName() {
    return this.filename;
  }
  @Override
  public String getOriginalFilename() {
    return this.filename;
  }

  @Override
  public String getContentType() {
    return this.contentType;
  }

  @Override
  public boolean isEmpty() {
    return this.fileContent.length == 0;
  }

  @Override
  public byte[] getBytes() throws IOException {
    return this.fileContent;
  }

  @Override
  public long getSize() {
    throw new RuntimeException("not implemented");
//    return 0;
  }

  @Override
  public InputStream getInputStream() throws IOException {
    throw new RuntimeException("not implemented");
//    return null;
  }

  @Override
  public void transferTo(File dest) throws IOException, IllegalStateException {
    throw new RuntimeException("not implemented");
  }
}
