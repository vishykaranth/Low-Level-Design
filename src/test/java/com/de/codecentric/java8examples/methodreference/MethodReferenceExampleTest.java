package com.de.codecentric.java8examples.methodreference;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Callable;

import static org.junit.Assert.assertEquals;

/**
 * Test case showing the use of method references.
 */
public class MethodReferenceExampleTest {

    private Foo myFoo;

    @Before
    public void setUp() throws Exception {
        myFoo = new Foo();
    }

    @Test
    public void callbackWithoutMethodReference() throws Exception {
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return myFoo.doSomething();
            }
        };

        assertEquals(myFoo.doSomething(), callable.call());
    }

    @Test
    public void callbackWithMethodReference() throws Exception {
        Callable<String> callable = myFoo::doSomething;

        assertEquals(myFoo.doSomething(), callable.call());
    }
}
