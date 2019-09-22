package circularorbit.iostrategy;

import java.io.IOException;
import java.util.List;

public interface IoStrategy {


  /**
   * get the next line in input.
   *
   * @return a line in input as string.if not this line ,return null.
   */
  String nextLine();


  /**
   * write the content in the file.
   *
   * @param filename the name of file will write.
   * @param contents the contents will write.
   * @throws IOException if can't write.
   */
  void writes(String filename, List<String> contents) throws IOException;

  /**
   * close the io.
   */
  void closeIo();
}
