package vendingMachine.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class TransactionTest {

    @Test
    public void getUsername() {
        HashMap<String, Integer> a = new HashMap<>();
        a.put("item1", 1);
        a.put("item2", 2);
        Transaction ts = new Transaction("group1", a, 12, 10, "card");
        assertEquals("group1", ts.getUsername());
    }

    @Test
    public void getItems() {
        HashMap<String, Integer> a = new HashMap<>();
        a.put("item1", 1);
        a.put("item2", 2);
        Transaction ts = new Transaction("group1", a, 12, 10, "card");
        assertEquals(a.keySet().toString(), ts.getItems().keySet().toString());
    }

    @Test
    public void getPaid() {
        HashMap<String, Integer> a = new HashMap<>();
        a.put("item1", 1);
        a.put("item2", 2);
        Transaction ts = new Transaction("group1", a, 12, 10, "card");
        assertEquals(12, ts.getPaid(), 0);
    }

    @Test
    public void getChange() {
        HashMap<String, Integer> a = new HashMap<>();
        a.put("item1", 1);
        a.put("item2", 2);
        Transaction ts = new Transaction("group1", a, 12, 10, "card");
        assertEquals("group1", ts.getUsername());
    }

    @Test
    public void getPayMethod() {
        HashMap<String, Integer> a = new HashMap<>();
        a.put("item1", 1);
        a.put("item2", 2);
        Transaction ts = new Transaction("group1", a, 12, 10, "card");
        assertEquals(10, ts.getChange(), 0);
    }

    @Test
    public void getTimeStampString() {
        HashMap<String, Integer> a = new HashMap<>();
        a.put("item1", 1);
        a.put("item2", 2);
        Transaction ts = new Transaction("group1", a, 12, 10, "card");
        assertEquals(ts.getTimeStamp().toString(), ts.getTimeStampString());
    }

    @Test
    public void getTimeStamp() {
        HashMap<String, Integer> a = new HashMap<>();
        a.put("item1", 1);
        a.put("item2", 2);
        Transaction ts = new Transaction("group1", a, 12, 10, "card");
        assertEquals(ts.getTimeStampString(), ts.getTimeStamp().toString());
    }

    @Test
    public void done() {
    }

    @Test
    public void getStatus() {
    }
}