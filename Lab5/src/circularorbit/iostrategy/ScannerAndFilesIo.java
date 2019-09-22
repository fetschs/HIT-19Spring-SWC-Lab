package circularorbit.iostrategy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class ScannerAndFilesIo implements IoStrategy {

  private final Scanner sc;

  //just strategy class ,Scanner used to get the input.

  /**
   * get an instance for input.
   *
   * @param inputFile the inputFile object.
   * @throws IOException if can't get the file.
   */
  public ScannerAndFilesIo(File inputFile) throws IOException {
    try {
      sc = new Scanner(inputFile,"utf-8");
    } catch (IOException e) {
      e.printStackTrace();
      throw e;
    }
  }

  /**
   * get an instance for output.
   */
  public ScannerAndFilesIo() {
    sc = null;
  }


  @Override
  public String nextLine() {
    if (!sc.hasNext()) {
      return null;
    }
    return sc.nextLine();
  }

  @Override
  public void writes(String filename, List<String> contents) throws IOException {
    Path path = Paths.get(filename);
    try {
      Files.write(path, contents);
    } catch (IOException e) {
      e.printStackTrace();
      throw e;
    }
  }

  @Override
  public void closeIo() {
    sc.close();
  }
}
