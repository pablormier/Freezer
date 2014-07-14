package com.github.pablormier.freezer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Dynamic Proxy Ref to Freezable objects.
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public final class R<T> implements F<T> {
    private F<T> freezableProxiedInstance;
    private T originalInstance;

    @SuppressWarnings("unchecked")
    public R(F<T> freezableInstance) {
        // Automatically find a valid cloner in the classpath. The cloner is needed for
        // cloning the instance when the object is frozen.
        this(freezableInstance, null);
    }

    private static <T> Cloner<T> findCloner(Class<T> classToClone){
        return null;
    }

    @SuppressWarnings("unchecked")
    public R(final F<T> freezableInstance, Cloner<T> cloner) {

        this.freezableProxiedInstance = freezableInstance;
        this.originalInstance = freezableInstance.instance();

        // Auto-generate a proxy for the instance wrapped in freezableInstance.
        // The proxy intercepts the calls to the original instance and checks if the
        // instance is frozen or not. If the instance is frozen, a copy of the instance
        // is generated and the reference is changed to the new instance. Then the call
        // is redirected to the original instance (or the new instance).


        // Each invocation to any method of the instance is proxied through the invocation handler.
        // If the method is annotated as a potential mutable method and the instance is frozen,
        // then a new copy of the object is generated and the reference is reassigned.
        final InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // By default, the target instance for the method invocation is the original instance
                T internalInstance = originalInstance;

                if (method.getAnnotation(Mutator.class) != null && freezableProxiedInstance.isFrozen()) {
                    // Protect access by copy-on-write strategy. Change also
                    // the reference of the instance to use the cloned object instead
                    // of the original ref.
                    T newInstance;
                    if (internalInstance instanceof Cloneable && cloner == null){
                        newInstance = ((Cloneable<T>)internalInstance).clone();
                    } else {
                        // Use an external cloner to clone the instance
                        newInstance = cloner.clone(internalInstance);
                    }

                    // Change the original ref and create a new Freezable of the new instance.
                    T proxied = (T)Proxy.newProxyInstance(newInstance.getClass().getClassLoader(), newInstance.getClass().getInterfaces(), this);

                    // Create a new freezable but using the proxied instance (unlocked by default). This may be referenced by other objects!
                    freezableProxiedInstance = Freezable.of(proxied);
                    originalInstance = newInstance;
                    return method.invoke(newInstance, args);
                }
                // Redirect the call to the instance (the original or the cloned)
                return method.invoke(originalInstance, args);
            }
        };

        // Generate a proxy for the object to intercept and wrap annotated methods with the appropriated check
        T proxiedInstance = (T) Proxy.newProxyInstance(originalInstance.getClass().getClassLoader(),
                originalInstance.getClass().getInterfaces(), handler);

        // Simple Freezable Proxy
        this.freezableProxiedInstance = new ProxyF<T>(freezableInstance) {
            @Override
            public T instance() {
                return proxiedInstance;
            }
        };
    }

    public synchronized F<T> get() {
        return freezableProxiedInstance;
    }

    public static <T> R<T> createRef(F<T> freezable){
        return new R<>(freezable);
    }

    @Override
    public String toString() {
        return "Ref[" + this.freezableProxiedInstance +  "]";
    }

    @Override
    public boolean isFrozen() {
        return get().isFrozen();
    }

    @Override
    public void freeze() {
        get().freeze();
    }

    @Override
    public void unfreeze() {
        get().unfreeze();
    }

    @Override
    public T instance() {
        return get().instance();
    }
}
