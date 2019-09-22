package circularorbit;

public interface CircularOrbitIterator<E> {

  /**
   * check if has next object.
   * @return an boolean
   */
  public boolean hasNext();

  /**
   * get next object.
   * @return an E object.
   */
  public E next();

}
