package com.github.pablormier.freezer;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
@FunctionalInterface
public interface Cloneable<T> {
    T clone();
}
