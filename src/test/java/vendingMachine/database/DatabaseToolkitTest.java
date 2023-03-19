package vendingMachine.database;

import org.junit.Before;
import org.junit.Test;
import vendingMachine.model.Transaction;

import java.io.*;
import java.util.*;

import static org.junit.Assert.*;

public class DatabaseToolkitTest {
    String accountsPath = "src/main/resources/database/Accounts";
    String categoriesPath = "src/main/resources/database/Categories";
    String historyPath = "src/main/resources/database/History";
    String itemsPath = "src/main/resources/database/Items";
    String lastfivePath = "src/main/resources/database/LastFive";
    String cancelledTransactionDBPath = "src/main/resources/database/CancelledTransaction";
    String soldItemsDBPath = "src/main/resources/database/SoldItems";

    public int countLines(File f) {
        try {
            FileReader fr = new FileReader(f);
            BufferedReader bf = new BufferedReader(fr);
            int count = 0;
            while(bf.readLine() != null) {
                count++;
            }
            return count;
        } catch (IOException ffe) {
            ffe.printStackTrace();
        }
        return 0;
    }

    @Test
    public void createConn() {
        DatabaseToolkit dbt = new DatabaseToolkit();
        assertTrue(dbt.createConn());
    }

    @Before
    public void initDatabase() {
        DatabaseToolkit dbt = new DatabaseToolkit();
        dbt.initDatabase();
        File f = new File(accountsPath);
        assertEquals(countLines(f), 2);
        f = new File(categoriesPath);
        assertEquals(countLines(f), 4);
        f = new File(historyPath);
        assertEquals(countLines(f), 0);
        f = new File(itemsPath);
        assertEquals(countLines(f), 16);
        f = new File(lastfivePath);
        assertEquals(f.length(), 0);
    }

    @Test
    public void replaceLineHelper() {
        String oldLine = "anonymous anonymous customer";
        String newLine = "a a c";
        File f = new File(accountsPath);
        DatabaseToolkit dbt = new DatabaseToolkit();
        // Test replace
        dbt.replaceLineHelper(f, oldLine, newLine);
        boolean foundFlag = false;
        boolean notFoundFlag = true;
        try {
            Scanner s = new Scanner(f);
            String buffer;
            while (s.hasNextLine()) {
                buffer = s.nextLine();
                if (buffer.equals(oldLine)) {
                    notFoundFlag = false;
                }
                if (buffer.equals(newLine)) {
                    foundFlag = true;
                }
            }
            assertTrue(notFoundFlag);
            assertTrue(foundFlag);
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Test roll back
        dbt.replaceLineHelper(f, newLine, oldLine);
        foundFlag = false;
        notFoundFlag = true;
        try {
            Scanner s = new Scanner(f);
            String buffer;
            while (s.hasNextLine()) {
                buffer = s.nextLine();
                if (buffer.equals(newLine)) {
                    notFoundFlag = false;
                }
                if (buffer.equals(oldLine)) {
                    foundFlag = true;
                }
            }
            assertTrue(notFoundFlag);
            assertTrue(foundFlag);
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getItemInfo() {
        DatabaseToolkit dbt = new DatabaseToolkit();
        ArrayList<ArrayList<String>> t = dbt.getItemInfo();
        assertEquals(t.size(), 16);
        assertEquals(t.toString(), "[[1, Mineral Water, Drinks, 7, 3], " +
                "[2, Coca Cola, Drinks, 7, 4.5], [3, Pepsi, Drinks, 7, 4.5], " +
                "[4, Juice, Drinks, 7, 4.5], [5, Sprite, Drinks, 7, 4.5], " +
                "[6, Mars, Chocolates, 7, 2], [7, M&M, Chocolates, 7, 6], " +
                "[8, Bounty, Chocolates, 7, 2], [9, Sneakers, Chocolates, 7, 2], " +
                "[10, Smiths, Chips, 7, 2.5], [11, Pringles, Chips, 7, 4], " +
                "[12, Kettle, Chips, 7, 4.5], [13, Thins, Chips, 7, 3.5], " +
                "[14, Mentos, Candies, 7, 1.4], [15, Sour Patch, Candies, 7, 4], " +
                "[16, Skittles, Candies, 7, 4.5]]");
    }

    @Test
    public void getUserInfo() {
        DatabaseToolkit dbt = new DatabaseToolkit();
        ArrayList<ArrayList<String>> t = dbt.getUserInfo();
        assertEquals(t.size(), 2);
        assertEquals(t.toString(), "[[group1, group1, admin], [anonymous, anonymous, customer]]");
    }

    @Test
    public void insertHistory() {
        DatabaseToolkit dbt = new DatabaseToolkit();
        HashMap<String, Integer> a = new HashMap<>();
        a.put("haha", 1);
        a.put("haha2", 2);
        Transaction ts1 = new Transaction("group1", a, 12.2, 10, "cash");
        ts1.done();
        dbt.insertHistory(ts1);
        File f = new File(historyPath);
        assertEquals(1, countLines(f));
        try {
            Scanner s = new Scanner(f);
            String[] buffer = s.nextLine().split(",");
            assertEquals("group1", buffer[1]);
            String[] items = buffer[2].split(" ");
            assertEquals(items.length, 2);
            assertEquals(items[0], "haha*1");
            assertEquals(items[1], "haha2*2");
            assertEquals(buffer[3], "12.2");
            assertEquals(buffer[4], "10.0");
            assertEquals(buffer[5], "cash");
            PrintWriter pw = new PrintWriter(f);
            pw.close();
        } catch (IOException ignored) {}
        // Test cancelled transaction
        Transaction ts2 = new Transaction("group1", a, 12.2, 10, "cash");
        dbt.insertHistory(ts2);
        File fc = new File(cancelledTransactionDBPath);
        assertEquals(1, countLines(fc));
        try {
            Scanner s = new Scanner(fc);
            String[] buffer = s.nextLine().split(",");
            assertEquals("group1", buffer[1]);
            String[] items = buffer[2].split(" ");
            assertEquals(items.length, 2);
            assertEquals(items[0], "haha*1");
            assertEquals(items[1], "haha2*2");
            assertEquals(buffer[3], "12.2");
            assertEquals(buffer[4], "10.0");
            assertEquals(buffer[5], "cash");
            PrintWriter pw = new PrintWriter(fc);
            pw.close();
        } catch (IOException ignored) {}

    }

    @Test
    public void updateLastFive() {
        DatabaseToolkit dbt = new DatabaseToolkit();
        String[] buffer;
        HashMap<String, Integer> items = new HashMap<>();
        items.put("item1", 2);
        items.put("item2", 3);
        Transaction ts1 = new Transaction("group1", items, 12, 10, "card");
        dbt.updateLastFive(ts1);
        File f = new File(lastfivePath);
        assertEquals(1, countLines(f));
        // Insert new items with repeated item
        items.put("item3", 4);
        items.put("item4", 4);
        items.put("item5", 5);
        Transaction ts2 = new Transaction("group1", items, 12, 12, "card");
        dbt.updateLastFive(ts2);
        assertEquals(1, countLines(f));
        try {
            Scanner s = new Scanner(f);
            buffer = s.nextLine().split(",");
            HashSet<String> FromFile = new HashSet<>(Arrays.asList(buffer).subList(1, buffer.length));
            assertEquals(FromFile, items.keySet());
        } catch (IOException ignored) {}
        // Insert more than 5 items
        items.put("item6", 6);
        Transaction ts3 = new Transaction("group1", items, 12, 12, "card");
        dbt.updateLastFive(ts3);
        assertEquals(1, countLines(f));
        try {
            Scanner s = new Scanner(f);
            buffer = s.nextLine().split(",");
            HashSet<String> FromFile = new HashSet<>(Arrays.asList(buffer).subList(1, buffer.length));
            assertTrue(FromFile.contains("item6"));
            assertEquals(5, FromFile.size());
            s.close();
        } catch (IOException ignored) {}
        // Brand new transaction
        items.clear();
        items.put("i1", 2);
        items.put("i2", 2);
        items.put("i3", 4);
        items.put("i4", 3);
        items.put("i5", 7);
        Transaction ts4 = new Transaction("group1", items, 12, 12, "card");
        dbt.updateLastFive(ts4);
        assertEquals(1, countLines(f));
        try {
            Scanner s = new Scanner(f);
            buffer = s.nextLine().split(",");
            HashSet<String> FromFile = new HashSet<>(Arrays.asList(buffer).subList(1, buffer.length));
            assertTrue(FromFile.contains("i1"));
            assertTrue(FromFile.contains("i2"));
            assertTrue(FromFile.contains("i3"));
            assertTrue(FromFile.contains("i4"));
            assertTrue(FromFile.contains("i5"));
            assertEquals(5, FromFile.size());
            s.close();
        } catch (IOException ignored) {}
    }

    @Test
    public void donePurchase() {
    }

    @Test
    public void updatePurchasedItem() {
        DatabaseToolkit dbt = new DatabaseToolkit();
        HashMap<String, Integer> a = new HashMap<>();
        a.put("haha", 1);
        a.put("haha2", 2);
        Transaction ts1 = new Transaction("group1", a, 12.2, 10, "cash");
        dbt.updatePurchasedItem(ts1);
        File f = new File(soldItemsDBPath);
        assertEquals(2, countLines(f));
        try {
            Scanner s = new Scanner(f);
            String[] buffer = s.nextLine().split(",");
            System.out.println(Arrays.toString(buffer));
            assertEquals("haha", buffer[0]);
            assertEquals("1", buffer[1]);
            PrintWriter pw = new PrintWriter(f);
            pw.print("");
            pw.close();
        } catch (IOException ignored) {}
    }

    @Test
    public void getLastFive() {
        DatabaseToolkit dbt = new DatabaseToolkit();
        assertEquals(0, dbt.getLastFive("Not Exist User").size());
    }

    @Test
    public void createAccount() {
        /* Test already existing error case */
        DatabaseToolkit dbt = new DatabaseToolkit();
        String username = "group1";
        String password = "group1";
        String role = "customer";
        // Test return correct value
        assertEquals(dbt.createAccount(username, password, role), -1);
        File f = new File(accountsPath);
        // Test no new text in file
        assertEquals(countLines(f), 2);

        /* Test successfully created */
        username = "hahaha";
        password = "hahaha";
        role = "customer";
        // Test return correct value
        assertEquals(dbt.createAccount(username, password, role), 1);
        // Test file content
        String t;
        try {
            boolean exists = false;
            Scanner s = new Scanner(f);
            while (s.hasNextLine()) {
                t = s.nextLine().split(" ")[0];
                if (t.equals(username)) {
                    exists = true;
                }
            }
            assertTrue(exists);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dbt.deleteAccount(username);
    }

    @Test
    public void deleteAccount() {
        DatabaseToolkit dbt = new DatabaseToolkit();
        String username = "hahaha";
        String password = "hahaha";
        String role = "customer";
        assertEquals(dbt.createAccount(username, password, role), 1);
        assertEquals(dbt.deleteAccount(username), 1);
        assertEquals(dbt.deleteAccount("group1"), -2);
    }

    @Test
    public void checkAccount() {
        DatabaseToolkit dbt = new DatabaseToolkit();
        String username = "group1";
        String password = "group1";
        assertTrue(dbt.checkAccount(username, password));
        assertFalse(dbt.checkAccount(username, "hahaha"));
        assertFalse(dbt.checkAccount("hahaha", password));
        assertFalse(dbt.checkAccount("somehting", "something"));
    }

    @Test
    public void getUserRole() {
        DatabaseToolkit dbt = new DatabaseToolkit();
        assertNull(dbt.getUserRole("something"));
        assertEquals(dbt.getUserRole("group1"), "admin");
        assertEquals(dbt.getUserRole("anonymous"), "customer");
    }

    @Test
    public void getItemQuantity() {
        DatabaseToolkit dbt = new DatabaseToolkit();
        assertEquals(7, dbt.getItemQuantity("Coca Cola"));
        assertEquals(-1, dbt.getItemQuantity("Something"));
    }

    @Test
    public void checkItemCodeNotExist() {
        DatabaseToolkit dbt = new DatabaseToolkit();
        assertTrue(dbt.checkItemCodeNotExist(123456));
        assertFalse(dbt.checkItemCodeNotExist(1));
    }

    @Test
    public void updateItemQuantity() {
        DatabaseToolkit dbt = new DatabaseToolkit();
        assertEquals(7, dbt.getItemQuantity("Coca Cola"));
        assertEquals(1, dbt.updateItemQuantity("Coca Cola", 15));
        assertEquals(15, dbt.getItemQuantity("Coca Cola"));
        assertEquals(-1, dbt.updateItemQuantity("Coca Cola", 16));
        assertEquals(1, dbt.updateItemQuantity("Coca Cola", 7));
        assertEquals(0, dbt.updateItemQuantity("something", 0));
    }

    @Test
    public void updateItemPrice() {
        DatabaseToolkit dbt = new DatabaseToolkit();
        assertEquals(1, dbt.updateItemPrice("Coca Cola", 12.2));
        assertEquals(1, dbt.updateItemPrice("Coca Cola", 4.5));
        assertEquals(0, dbt.updateItemPrice("Coca", 12.2));
        assertEquals(-1, dbt.updateItemPrice("Coca Cola", -123));
    }

    @Test
    public void updateItemCode() {
        DatabaseToolkit dbt = new DatabaseToolkit();
        assertFalse(dbt.checkItemCodeNotExist(1));
        assertEquals(dbt.updateItemCode("haha", 123), 0);
        assertEquals(dbt.updateItemCode("Mineral Water", 1), -1);
        assertEquals(dbt.updateItemCode("Mineral Water", 111), 1);
        assertFalse(dbt.checkItemCodeNotExist(111));
        assertTrue(dbt.checkItemCodeNotExist(1));
        assertEquals(dbt.updateItemCode("Mineral Water", 1), 1);
    }

    @Test
    public void checkItemNameNotExist() {
        DatabaseToolkit dbt = new DatabaseToolkit();
        assertTrue(dbt.checkItemNameNotExist("Something"));
        assertFalse(dbt.checkItemNameNotExist("Coca Cola"));
    }

    @Test
    public void updateItemName() {
        DatabaseToolkit dbt = new DatabaseToolkit();
        assertEquals(dbt.updateItemName("Coca Cola", "Coca Cola"), -1);
        assertEquals(dbt.updateItemName("Coca", "hohoho"), 0);
        assertEquals(dbt.updateItemName("Coca Cola", "Sprite"), -1);
        assertEquals(dbt.updateItemName("Coca Cola", "hohoho"), 1);
        assertFalse(dbt.checkItemNameNotExist("hohoho"));
        assertTrue(dbt.checkItemNameNotExist("Coca Cola"));
        assertEquals(dbt.updateItemName("hohoho", "Coca Cola"), 1);
    }

    @Test
    public void insertItem() {
        DatabaseToolkit dbt = new DatabaseToolkit();
        // Check code exists case
        assertEquals(dbt.insertItem(1, "Something", "Drinks", 2, 2.3), -1);
        // Check name exists case
        assertEquals(dbt.insertItem(1000, "Coca Cola", "Drinks", 2, 2.3), -2);
        // Check category not exists case
        assertEquals(dbt.insertItem(1000, "Something", "D", 2, 2.3), -3);
        // Check quantity > 15 case
        assertEquals(dbt.insertItem(1000, "Something", "Drinks", 16, 2.3), -4);
        // Check price < 0 case
        assertEquals(dbt.insertItem(1000, "Something", "Drinks", 12, -12), -5);
        // Check valid case
        assertEquals(dbt.insertItem(1000, "Something", "Drinks", 2, 2.3), 1);
        assertEquals(dbt.deleteItem("Something"), 1);
    }

    @Test
    public void deleteItem() {
        DatabaseToolkit dbt = new DatabaseToolkit();
        assertEquals(dbt.insertItem(1000, "Something", "Drinks", 2, 2.3), 1);
        assertEquals(dbt.deleteItem("Something"), 1);
        // Check not exist case
        assertEquals(dbt.deleteItem("abcdefg"), 0);
    }

    @Test
    public void checkCategoryNotExist() {
        DatabaseToolkit dbt = new DatabaseToolkit();
        assertTrue(dbt.checkCategoryNotExist("something"));
        assertFalse(dbt.checkCategoryNotExist("Drinks"));
    }
}