package com.github.pablormier.freezer;

import java.lang.reflect.Field;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public final class Freezable<T> implements F<T> {
    private boolean frozen = false;
    private final T instance;


    public Freezable(T instance) {
        this.instance = instance;
    }

    @Override
    public boolean isFrozen() {
        return this.frozen;
    }

    @Override
    public void freeze() {
        this.frozen = true;
        // Call freeze in all freezable attributes of the instance
        for(Field f : instance.getClass().getDeclaredFields()){
            try {
                f.setAccessible(true);
                Object obj = f.get(instance);
                if (obj instanceof F) {
                    ((F) obj).freeze();
                }
            } catch (IllegalAccessException e){
                throw new RuntimeException("Cannot access field " + f);
            }
        }
    }

    @Override
    public void unfreeze() {
        this.frozen = false;
    }

    @Override
    public T instance() {
        return instance;
    }

    public static <T> F<T> of(T instance){
        return new Freezable<>(instance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Freezable freezable = (Freezable) o;

        if (frozen != freezable.frozen) return false;
        if (!instance.equals(freezable.instance)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (frozen ? 1 : 0);
        result = 31 * result + instance.hashCode();
        return result;
    }

    @Override
    public String toString() {
        if (frozen) return "*" + instance.toString() + "*";
        return instance.toString();
    }
}
