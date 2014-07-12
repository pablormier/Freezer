package com.github.pablormier.freezer;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class Example {

    public interface MutableMethods {

        @Mutator
        public void setValue(Integer value);

        public Integer getValue();
    }

    public static class MyObject implements Cloneable<MyObject>, MutableMethods {
        private Integer value = 0;

        public MyObject(Integer value) {
            this.value = value;
        }

        @Override
        public Integer getValue() {
            return value;
        }

        @Override
        public void setValue(Integer value) {
            this.value = value;
        }

        @Override
        public MyObject clone() {
            return new MyObject(value);
        }

        @Override
        public String toString() {
            return "Obj(v=" + value + ")";
        }
    }


    public static void main(String[] args) {

        // Original instance
        MyObject obj = new MyObject(0);
        // Shared freezable object
        final F<MutableMethods> freezableObj = Freezable.of(obj);
        // Create refs to freezableObj
        P<MutableMethods> r1 = new P<>(freezableObj);
        P<MutableMethods> r2 = new P<>(freezableObj);

        System.out.println("Initial configuration");
        System.out.println("R1 " + r1);
        System.out.println("R2 " + r2);

        // One of them changes the value
        System.out.println("R2 changes the value of the instance to 5");
        r2.get().instance().setValue(5);

        // Now, we freeze ref2. Ref2 will keep the original reference.
        r1.get().freeze();

        System.out.println("R1 calls freeze()");
        System.out.println("R1 " + r1);
        System.out.println("R2 " + r2);

        // Ref1 changes the content. Since Ref2 has frozen the instance, Ref1 obtains a copy.
        // r1.get().instance() returns a proxied interface of the object. Method setValue
        // is intercepted. Since freezableObj is frozen, a new copy is generated and assigned
        // to r1.
        System.out.println("R2 modify the value to 1 and obtains a new copy");
        r2.get().instance().setValue(1);
        // Now r2 has a different copy
        System.out.println("R1 " + r1);
        System.out.println("R2 " + r2);

        System.out.println("We create R3 with the first instance (still locked)");
        P<MutableMethods> r3 = new P<>(freezableObj);
        System.out.println("R3 " + r3);

        System.out.println("R2 has freedom to change its own copy");
        r2.get().instance().setValue(100);
        System.out.println("R1 " + r1);
        System.out.println("R2 " + r2);
        System.out.println("R3 " + r3);

        System.out.println("R3 changes its copy");
        r3.get().instance().setValue(200);
        System.out.println("R1 " + r1);
        System.out.println("R2 " + r2);
        System.out.println("R3 " + r3);
    }
}
