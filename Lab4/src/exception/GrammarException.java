package exception;

import lombok.Getter;

@Getter
public class GrammarException extends Exception {
  //if file has invalid grammar input line.

  /**
   * print the exception message.
   */
  public GrammarException(String tempReason) {
    super("File has input line with invalid grammar. Reason:\"" + tempReason);
  }
}
