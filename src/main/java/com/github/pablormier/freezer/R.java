package com.github.pablormier.freezer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public final class R<T> {
    private F<T> proxiedInstance;

    @SuppressWarnings("unchecked")
    public R(F<T> instance) {
        // Automatically find a valid cloner in the classpath. The cloner is needed for
        // cloning the instance when the object is frozen.
        this(instance, findCloner((Class<T>)instance.getClass()));
    }

    private static <T> Cloner<T> findCloner(Class<T> cloneableClass){
        return null;
    }

    @SuppressWarnings("unchecked")
    public R(F<T> instance, Cloner<T> cloner) {
        F<T> freezableInstance = Freezable.of(instance);

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


}
