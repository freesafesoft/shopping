package fss.shopping.web.service;

import junit.framework.TestCase;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Alex on 14.02.2018.
 */

public class SignInOutTest extends TestCase {
    private static String TEST_USER = "TestUser@mail.com";
    private static String TEST_PASSWORD = "password";

    @Test
    public void addition_isCorrect() throws Exception {
        new SignInRequest.Builder().addEmail("TestUser@mail.com").addPassword("password")
                .addSignInListener(new SignInRequest.Listener() {

                    @Override
                    public void onSignInSuccess() {

                    }

                    @Override
                    public void onSignInFailure(String errorMessage) {
                        fail();
                    }
                }).build().process();
        assertEquals(4, 2 + 2);
    }
}
