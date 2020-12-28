package com.example.lookarounddemo.data;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MarkersArrayTest {

    private double laRandom = Math.random();
    private double loRandom = Math.random();
    private myMarker Maker = new myMarker(laRandom,loRandom,"title",true);
    private MarkersArray MA = new MarkersArray();


    @Test
    public void getFirstMarker() {
        System.out.println("-----Testing getFirstMaker()-----");
        //empty array test
        assertNull(MA.getFirstMarker());
        //add one marker and test
        MA.add(Maker);
        long l = new Double(MA.getFirstMarker().getLatitude()).longValue();
        long la = new Double(laRandom).longValue();
        assertEquals(la,l);
        System.out.println("-----Tested getFirstMaker()-----");
    }

    @Test
    public void addNewpost() {
        System.out.println("-----Testing addNewPost()-----");
        MA.addNewpost(laRandom,loRandom);
        assertEquals("刚发的post",MA.getFirstMarker().getTitle());
        System.out.println("-----Tested addNewPost()-----");
    }
}