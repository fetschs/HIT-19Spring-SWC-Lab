package exception;

public class DependencyException extends Exception {
  //if file has invalid dependency input line.

  /**
   * print the exception message.
   */
  public DependencyException() {
    super("File has input lines with invalid Dependency");
  }
}
