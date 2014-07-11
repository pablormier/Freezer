package com.github.pablormier.freezer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public final class Freezable<T> implements F<T> {
    private boolean frozen = false;
    private T proxiedInstance;


    @SuppressWarnings("unchecked")
    public Freezable(final T instance) {
        // Automatically find a valid cloner in the classpath. The cloner is needed for
        // cloning the instance when the object is frozen.
        this(instance, findCloner((Class<T>)instance.getClass()));
    }

    private static <T> Cloner<T> findCloner(Class<T> cloneableClass){
        return null;
    }

    @SuppressWarnings("unchecked")
    public Freezable(T instance, Cloner<T> cloner) {

        final InvocationHandler handler = (proxy, method, args) -> {
            if (method.getAnnotation(Setter.class) != null && isFrozen()) {
                // Protect access by copy-on-write strategy. Change also
                // the reference of the instance to use the cloned object instead
                // of the original ref.
                if (instance instanceof Cloneable){
                    proxiedInstance = ((Cloneable<T>)instance).clone();
                } else {
                    // Use an external cloner to clone the instance
                    proxiedInstance = cloner.clone(instance);
                }
            }
            // Redirect the call to the proxified instance
            return method.invoke(proxiedInstance, args);
        };

        // Generate a proxy for the object to intercept and wrap annotated methods with the appropriated checks

        this.proxiedInstance = (T) Proxy.newProxyInstance(instance.getClass().getClassLoader(),
                instance.getClass().getInterfaces(), handler);
    }

    public boolean isFrozen() {
        return this.frozen;
    }


    public void freeze() {
        this.frozen = true;
    }


    public T instance() {
        return proxiedInstance;
    }

    public static <T> F<T> of(T instance){
        return new Freezable<T>(instance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Freezable freezable = (Freezable) o;

        if (frozen != freezable.frozen) return false;
        if (!proxiedInstance.equals(freezable.proxiedInstance)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (frozen ? 1 : 0);
        result = 31 * result + proxiedInstance.hashCode();
        return result;
    }

    @Override
    public String toString() {
        if (frozen) {
            return "![" + proxiedInstance.toString() + "]";
        }
        return proxiedInstance.toString();
    }
}
