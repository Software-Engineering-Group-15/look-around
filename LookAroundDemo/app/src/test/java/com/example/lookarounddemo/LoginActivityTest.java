package com.example.lookarounddemo;

import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

public class LoginActivityTest {

    private LoginActivity LoginActivity;
    private java.io.InputStream InputStream;
    @Test
    public void onCreate() {
    }

    @Test
    public void login_finish() {
    }

    @Test
    public void forget_password() {
    }

    @Test
    public void streamToString() {
        assertEquals("",LoginActivity.streamToString(InputStream));
    }
}