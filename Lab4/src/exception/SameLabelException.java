package exception;

public class SameLabelException extends Exception {
  //if file has object with same label, this will be use.

  /**
   * print the exception message.
   */
  public SameLabelException() {
    super("File has input lines with same label.");
  }
}
