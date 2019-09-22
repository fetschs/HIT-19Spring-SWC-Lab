package exception;

import lombok.Getter;

@Getter
public class InvalidInputException extends Exception {

  //if method get invalid input.
  //AF(method) = the name of the method which throw this.
  //no RI or safety question.
  private final String method;

  /**
   * print the exception message.
   */
  public InvalidInputException(String methodName, String message) {
    super(message);
    method = methodName;
  }
}
