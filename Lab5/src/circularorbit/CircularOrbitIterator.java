package circularorbit;

public interface CircularOrbitIterator<E> {

  /**
   * check if has next object.
   * @return an boolean
   */
  boolean hasNext();

  /**
   * get next object.
   * @return an E object.
   */
  E next();

}
