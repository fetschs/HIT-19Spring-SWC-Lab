package circularorbit.iostrategy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ReaderAndWriterIo implements IoStrategy {

  private BufferedReader bufferedReader;
  private BufferedWriter bufferedWriter;
  //just strategy class ,fileR/W used to get the input.

  /**
   * get the instance for io.
   *
   * @param file the File will read or write.
   * @throws IOException if io error.
   */
  public ReaderAndWriterIo(File file) throws IOException {

    try {
      bufferedReader = new BufferedReader(new FileReader(file));
    } catch (IOException e) {
      e.printStackTrace();
      throw e;
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
    bufferedWriter = new BufferedWriter(new FileWriter(filename));
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
