package com.github.pablormier.freezer;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@gmail.com">pablo.rodriguez.mier@gmail.com</a>>
 */
public abstract class ProxyF<T> implements F<T> {
    private F<T> freezable;

    public ProxyF(F<T> freezable) {
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
        if (isFrozen()) return "*" + instance().toString() + "*";
        return instance().toString();
    }
}
