package com.github.pablormier.freezer;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@gmail.com">pablo.rodriguez.mier@gmail.com</a>>
 */
@FunctionalInterface
public interface Cloner<T> {
    T clone(T object);
}
