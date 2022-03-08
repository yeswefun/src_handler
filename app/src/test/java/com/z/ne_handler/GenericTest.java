package com.z.ne_handler;

import org.junit.Test;

/*
    @SuppressWarnings("unchecked")
    T result = (T)e.value;
 */
public class GenericTest {
    @Test
    public void test00() {

    }
}

class Data<T> {

    private T d;

    public T getD() {
        return d;
    }

    public void setD(T d) {
        this.d = d;
    }
}
