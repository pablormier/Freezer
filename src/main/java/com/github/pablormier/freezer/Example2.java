package com.github.pablormier.freezer;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@gmail.com">pablo.rodriguez.mier@gmail.com</a>>
 */
public class Example2 {

    interface User extends Cloneable<User> {

        @Mutator void setUserName(String name);

        String getUserName();
    }

    interface Account extends Cloneable<Account> {

        @Mutator void setQuantity(Double quantity);

        Double getQuantity();
    }

    public static class UserImpl implements User {
        private R<Account> account;
        private String name;

        public UserImpl(String name, R<Account> account) {
            this.name = name;
            this.account = account;
        }

        @Override
        public void setUserName(String name) {
            this.name = name;
        }

        @Override
        public String getUserName() {
            return name;
        }

        @Override
        public UserImpl clone() {
            return new UserImpl(name, account.clone());
        }
    }

    public static class AccountImpl implements Account {

        @Override
        public void setQuantity(Double quantity) {

        }

        @Override
        public Double getQuantity() {
            return null;
        }

        @Override
        public AccountImpl clone() {
            return null;
        }
    }

    public static void main(String[] args){

    }
}
