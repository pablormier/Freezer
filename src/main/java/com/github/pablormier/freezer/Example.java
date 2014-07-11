package com.github.pablormier.freezer;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class Example {

    public interface Mutable {
        @Mutator
        public void setValue(Integer value);
        public Integer getValue();
    }

    public static class MyObject implements Cloneable<MyObject>, Mutable {
        private Integer value = 0;

        public MyObject(Integer value) {
            this.value = value;
        }

        @Override
        public Integer getValue() {
            return value;
        }

        @Override
        @Mutator
        public void setValue(Integer value) {
            this.value = value;
        }

        @Override
        public MyObject clone() {
            return new MyObject(value);
        }

        @Override
        public String toString() {
            return "O(" + value + ")";
        }
    }


    public static void main(String[] args) {
        // Shared freezable object
        final F<Mutable> freezableObj = Freezable.of(new MyObject(0));
        // Create refs to freezableObj
        P<Mutable> r1 = new P<>(freezableObj);
        P<Mutable> r2 = new P<>(freezableObj);

        System.out.println("Initial configuration");
        System.out.println("R1 " + r1);
        System.out.println("R2 " + r2);
        System.out.println("R1 " + r1.get().instance().getValue());
        System.out.println("R2 " + r2.get().instance().getValue());

        // Now, we freeze ref2. Ref2 will keep the original reference.
        r1.get().freeze();

        System.out.println("R1 calls freeze()");
        System.out.println("R1 " + r1);
        System.out.println("R2 " + r2);

        // Ref1 changes the content. Since Ref2 has frozen the instance, Ref1 obtains a copy.
        // r1.get().instance() returns a proxied interface of the object. Method setValue
        // is intercepted. Since freezableObj is frozen, a new copy is generated and assigned
        // to r1.
        System.out.println("R2 tries to modify the value");
        r2.get().instance().setValue(1);

        System.out.println("R1 " + r1);
        System.out.println("R2 " + r2);
        System.out.println("R1 " + r1.get().instance().getValue());
        System.out.println("R2 " + r2.get().instance().getValue());

        // We change
    }
}
