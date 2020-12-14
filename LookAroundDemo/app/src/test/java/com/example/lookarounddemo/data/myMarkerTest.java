package com.example.lookarounddemo.data;

import org.junit.Test;

import static org.junit.Assert.*;

public class myMarkerTest {

    private double laRandom = Math.random();
    private double loRandom = Math.random();
    private myMarker Maker = new myMarker(laRandom,loRandom,"title",true);

    @Test
    public void getLatitude() {
        System.out.println("-----Testing getLatitude()-----");
        long l = new Double(Maker.getLatitude()).longValue();
        long la = new Double(laRandom).longValue();
        assertEquals(la,l);
        System.out.println("-----Testing getLatitude()-----");
    }

    @Test
    public void getLongitude() {
        System.out.println("-----Testing getLongitude()-----");
        long l = new Double(Maker.getLongitude()).longValue();
        long lo = new Double(loRandom).longValue();
        assertEquals(lo,l);
        System.out.println("-----Tested getLongitude()-----");
    }

    @Test
    public void getTitle() {
        System.out.println("-----Testing getTitle()-----");
        assertEquals("title",Maker.getTitle());
        System.out.println("-----Tested getTitle()-----");
    }

    @Test
    public void getIfnew() {
        System.out.println("-----Testing getIfnew()-----");
        assertEquals(true,Maker.getIfnew());
        System.out.println("-----Tested getIfnew()-----");
    }
}