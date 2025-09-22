package org.gayan.dls.validation;

/** Author: Gayan Sanjeewa User: gayan Date: 9/20/25 Time: 11:22 PM */
public interface Validator<T> {
  T validateAndGet(T entity);
}
