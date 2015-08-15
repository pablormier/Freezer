package com.github.pablormier.freezer;

/**
 * Freezable interface. Wraps an object of type T and decorates it adding methods to freeze
 * and unfreeze.
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@gmail.com">pablo.rodriguez.mier@gmail.com</a>>
 */
public interface F<T> {

    boolean isFrozen();

    void freeze();

    void unfreeze();

    T instance();
}
