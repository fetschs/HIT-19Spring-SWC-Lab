package circularorbit.iostrategy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class StreamIo implements IoStrategy {

  private BufferedReader bufferedReader;
  private BufferedWriter bufferedWriter;
  //just strategy class ,Stream used to get the input.

  /**
   * construction method .
   *
   * @param file the file will io.
   * @throws FileNotFoundException io error.
   */
  public StreamIo(File file) throws FileNotFoundException {
    try {
      bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file),
          StandardCharsets.UTF_8.name()));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      throw e;
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String nextLine() {
    String ret;
    try {
      ret = bufferedReader.readLine();
    } catch (IOException e) {
      e.printStackTrace();
      ret = null;
    }
    return ret;
  }

  @Override
  public void writes(String filename, List<String> contents) throws IOException {
    bufferedWriter = new BufferedWriter(
        new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8.name()));
    for (String string : contents) {
      bufferedWriter.write(string);
      bufferedWriter.newLine();
    }
  }

  @Override
  public void closeIo() {
    try {
      if (bufferedReader != null) {
        bufferedReader.close();
      }
      if (bufferedWriter != null) {
        bufferedWriter.flush();
        bufferedWriter.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
