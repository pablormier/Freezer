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

    class Default<T> implements F<T> {
        private F<T> freezable;

        public Default(F<T> freezable) {
            this.freezable = freezable;
        }

        @Override
        public boolean isFrozen() {
            return freezable.isFrozen();
        }

        @Override
        public void freeze() {
            freezable.freeze();
        }

        @Override
        public void unfreeze() {
            freezable.unfreeze();
        }

        @Override
        public T instance() {
            return freezable.instance();
        }

        @Override
        public String toString(){
            return freezable.toString();
        }
    }
}
