package com.github.pablormier.freezer;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class Example {



    public static class MyObject implements Cloneable<MyObject> {
        private Integer value = 0;

        public MyObject(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }

        @Setter public void setValue(Integer value) {
            this.value = value;
        }

        @Override
        public MyObject clone() {
            return new MyObject(value);
        }
    }


    public static void main(String[] args) {
        // Shared freezable object
        F<MyObject> freezableObj = Freezable.of(new MyObject(0));
        // Create refs
        R<MyObject> r1 = new R<>(freezableObj);
        R<MyObject> r2 = new R<>(freezableObj);

        MyObject obj = new MyObject(0);

        F<MyObject> ref1 = Freezable.of(obj);
        F<MyObject> ref2 = Freezable.of(obj);

        System.out.println(ref1.instance());
        System.out.println(ref2.instance());

        // Now, we freeze ref2. Ref2 will keep the original reference.
        ref2.freeze();

        // Ref1 changes the content. Since Ref2 has frozen the instance, Ref1 obtains a copy
        ref1.instance().setValue(1);

        System.out.println(ref1.instance());
        System.out.println(ref2.instance());

        // We change
    }
}
