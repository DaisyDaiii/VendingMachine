package vendingMachine.database;

import jdk.dynalink.linker.support.CompositeGuardingDynamicLinker;
import vendingMachine.model.Transaction;

import java.io.*;
import java.util.*;

/** Database Toolkit!<br>
 * This class will be the toolkit for the interaction with local database.
 * */
public class DatabaseToolkit {
	String accountDBPath;
	String categoriesDBPath;
	String historyDBPath;
	String itemsDBPath;
	String lastfiveDBPath;
	String cancelledTransactionDBPath;
	String soldItemsDBPath;

	// We only connect to one DB so information in the DBLoader will be the same
	public DatabaseToolkit() {
		this.accountDBPath = "src/main/resources/database/Accounts";
		this.categoriesDBPath = "src/main/resources/database/Categories";
		this.historyDBPath = "src/main/resources/database/History";
		this.itemsDBPath = "src/main/resources/database/Items";
		this.lastfiveDBPath = "src/main/resources/database/LastFive";
		this.cancelledTransactionDBPath = "src/main/resources/database/CancelledTransaction";
		this.soldItemsDBPath = "src/main/resources/database/SoldItems";
	}

	public boolean createConn() {
		return true;
	}

	/**
	 * Initialise the Vending Machine Database
	 *<br>
	 * quantity is by default set to be 7, the limit is 15
	 *<br>
	 * Drinks: Mineral Water, Sprite, Coca cola, Pepsi, Juice. <br>
	 * Chocolates: Mars, M&M, Bounty, Sneakers. <br>
	 * Chips: Smiths, Pringles, Kettle, Thins. <br>
	 * Candies: Mentos, Sour Patch, Skittles.
	 * */
	public void initDatabase() {
		try {
			PrintWriter accountWriter = new PrintWriter(accountDBPath);
			PrintWriter categoriesWriter = new PrintWriter(categoriesDBPath);
			PrintWriter historyWriter = new PrintWriter(historyDBPath);
			PrintWriter itemsWriter = new PrintWriter(itemsDBPath);
			PrintWriter lastfiveWriter = new PrintWriter(lastfiveDBPath);
			PrintWriter cancelledWriter = new PrintWriter(cancelledTransactionDBPath);
			PrintWriter soldItemWriter = new PrintWriter(soldItemsDBPath);

			// Write to Accounts DB
			accountWriter.println("group1 group1 admin");
			accountWriter.println("anonymous anonymous customer");
			// Write to Categories DB
			categoriesWriter.println("Drinks");
			categoriesWriter.println("Chocolates");
			categoriesWriter.println("Chips");
			categoriesWriter.println("Candies");
			// Write to Items DB
			// code name category quantity price
			itemsWriter.println("1,Mineral Water,Drinks,7,3");
			itemsWriter.println("2,Coca Cola,Drinks,7,4.5");
			itemsWriter.println("3,Pepsi,Drinks,7,4.5");
			itemsWriter.println("4,Juice,Drinks,7,4.5");
			itemsWriter.println("5,Sprite,Drinks,7,4.5");
			itemsWriter.println("6,Mars,Chocolates,7,2");
			itemsWriter.println("7,M&M,Chocolates,7,6");
			itemsWriter.println("8,Bounty,Chocolates,7,2");
			itemsWriter.println("9,Sneakers,Chocolates,7,2");
			itemsWriter.println("10,Smiths,Chips,7,2.5");
			itemsWriter.println("11,Pringles,Chips,7,4");
			itemsWriter.println("12,Kettle,Chips,7,4.5");
			itemsWriter.println("13,Thins,Chips,7,3.5");
			itemsWriter.println("14,Mentos,Candies,7,1.4");
			itemsWriter.println("15,Sour Patch,Candies,7,4");
			itemsWriter.println("16,Skittles,Candies,7,4.5");
			accountWriter.close();
			categoriesWriter.close();
			historyWriter.close();
			itemsWriter.close();
			lastfiveWriter.close();
			cancelledWriter.close();
			soldItemWriter.close();
		} catch (FileNotFoundException ffe) {
			ffe.printStackTrace();
		}

	}

	public void replaceLineHelper(File f, String oldLine, String newLine) {
		try {
			// input the (modified) file content to the StringBuffer "input"
			BufferedReader file = new BufferedReader(new FileReader(f));
			StringBuilder buffer = new StringBuilder();
			String line;

			while ((line = file.readLine()) != null) {
				if (line.equals(oldLine)) {
					line = newLine;
				}
				buffer.append(line);
				if (!line.equals("")) {
					buffer.append('\n');
				}
			}
			file.close();
			FileOutputStream fileOut = new FileOutputStream(f);
			fileOut.write(buffer.toString().getBytes());
			fileOut.close();
		} catch (Exception ignored) {}
	}

	/**Get all Items <br>
	 * @return ArrayList of ArrayList <br>
	 *			[ArrayList, ArrayList, ...] <br>
	 *			[id, name, category, quantity, price]
	 * */
	public ArrayList<ArrayList<String>> getItemInfo() {
		ArrayList<ArrayList<String>> items = new ArrayList<>();
		try {
			File f = new File(itemsDBPath);
			Scanner s = new Scanner(f);
			while (s.hasNextLine()) {
				items.add(new ArrayList<>(Arrays.asList(s.nextLine().split(","))));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return items;
	}

	/**Get all user information
	 * @return ArrayList of ArrayList <br>
	 *			[ArrayList, ArrayList, ...] <br>
	 *			[username, password, role]
	 * */
	public ArrayList<ArrayList<String>> getUserInfo() {
		ArrayList<ArrayList<String>> user = new ArrayList<>();
		try {
			File f = new File(accountDBPath);
			Scanner s = new Scanner(f);
			while (s.hasNextLine()) {
				user.add(new ArrayList<>(Arrays.asList(s.nextLine().split(" "))));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return user;
	}

	/**Insert purchase History<br>
	 * This function should be called after purchasing CANCELLED
	 * (will also be called automatically if purchasing succeed)
	 * @param ts Transaction generate after purchasing */
	public void insertHistory(Transaction ts) {
		StringBuilder transaction = new StringBuilder();
		transaction.append(ts.getTimeStampString());
		transaction.append(",");
		transaction.append(ts.getUsername());
		transaction.append(",");
		for (String item : ts.getItems().keySet()) {
			transaction.append(item);
			transaction.append("*");
			transaction.append(ts.getItems().get(item));
			transaction.append(" ");
		}
		transaction.setCharAt(transaction.length()-1, ',');
		transaction.append(ts.getPaid());
		transaction.append(",");
		transaction.append(ts.getChange());
		transaction.append(",");
		transaction.append(ts.getPayMethod());
		transaction.append(",");
		transaction.append(ts.getStatus());
		transaction.append('\n');

		// Check status to decide which file to write
		File f;
		if (ts.getStatus().equals("done")) {
			f = new File(historyDBPath);
		} else {
			f = new File(cancelledTransactionDBPath);
		}

		try {
			FileWriter fw = new FileWriter(f, true);
			BufferedWriter bw = new BufferedWriter(fw);
			String ss = transaction.toString();
			bw.write(ss);
			bw.close();
			fw.close();
		} catch (IOException ignored) {
		}
	}

	/**Update LastFive
	 * @param ts Transaction
	 * */
	public void updateLastFive(Transaction ts) {
		String username = ts.getUsername();
		String[] curLine;
		File f = new File(lastfiveDBPath);
		if (getLastFive(username).size() == 0) {
			StringBuilder sb = new StringBuilder();
			sb.append(username).append(",");
			ArrayList<String> newItems = new ArrayList<>(ts.getItems().keySet());
			if (newItems.size() > 5) {
				newItems = new ArrayList<>(newItems.subList(0,5));
			}
			for (String item : newItems) {
				sb.append(item).append(",");
			}
			sb.deleteCharAt(sb.length()-1).append('\n');
			try {
				FileWriter fw = new FileWriter(f, true);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(sb.toString());
				bw.close();
				fw.close();
			} catch (IOException ignored){}
		} else {
			StringBuilder sb = new StringBuilder();
			ArrayList<String> newItems = new ArrayList<>(ts.getItems().keySet());
			ArrayList<String> oldItems = new ArrayList<>();
			try {
				Scanner s = new Scanner(f);
				while (s.hasNextLine()) {
					curLine = s.nextLine().split(",");
					if (curLine[0].equals(username)) {
						oldItems.addAll(Arrays.asList(curLine).subList(1, curLine.length));
						Collections.reverse(oldItems);
						for (String item : oldItems) {
							if (!newItems.contains(item)) {
								newItems.add(item);
							}
						}
						if (newItems.size() > 5) {
							newItems = new ArrayList<>(newItems.subList(0,5));
						}
						sb.append(curLine[0]).append(",");
						for (String item : newItems) {
							sb.append(item).append(",");
						}
					} else {
						for (String item : curLine) {
							sb.append(item).append(",");
						}
					}
					sb.deleteCharAt(sb.length()-1).append('\n');
				}
				PrintWriter pw = new PrintWriter(f);
				pw.print(sb.toString());
				pw.close();
				s.close();
			} catch (IOException ignored) {}
		}
	}

	/**Update purchase item number
	 * */
	public void updatePurchasedItem(Transaction ts) {
		File f = new File(soldItemsDBPath);
		try {
			BufferedReader file = new BufferedReader(new FileReader(f));
			StringBuilder buffer = new StringBuilder();
			String line;
			String[] attr;
			HashMap<String, Integer> soldI = ts.getItems();
			while ((line = file.readLine()) != null) {
				attr = line.split(",");
				if (soldI.containsKey(attr[0])) {
					attr[1] = String.valueOf(Integer.parseInt(attr[1]) + soldI.get(attr[0]));
					for (String i : attr) {
						buffer.append(i);
						buffer.append(',');
					}
					buffer.deleteCharAt(buffer.length()-1);
					soldI.remove(attr[0]);
				}
				buffer.append('\n');
			}
			Iterator it = soldI.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry)it.next();
				buffer.append(pair.getKey()).append(",").append(pair.getValue()).append('\n');
				it.remove();
			}
			file.close();
			FileOutputStream fileOut = new FileOutputStream(f);
			fileOut.write(buffer.toString().getBytes());
			fileOut.close();
		} catch (Exception ignored) {
		}

	}

	/**Done purchase<br>
	 * Every time done a purchase, this function should be run to update<br>
	 *     the record in database
	 * @param ts Transaction
	 * */
	public void donePurchase(Transaction ts) {
		ts.done();
		for (String item : ts.getItems().keySet()) {
			updateItemQuantity(item, getItemQuantity(item)-ts.getItems().get(item));
		}
		insertHistory(ts);
		updateLastFive(ts);
		updatePurchasedItem(ts);
	}

	/**Get last five records<br>
	 * Not always five records, need to check the length of return value
	 * @param username Record of which user you want to get
	 * @return ArrayList item names<br>
	 *     [] for not found (Arraylist with length 0)
	 *     length > 0 for succeed
	 * */
	public ArrayList<String> getLastFive(String username) {
		ArrayList<String> lastFiveItems = new ArrayList<>();
		String[] buffer;
		File f = new File(lastfiveDBPath);
		try {
			Scanner s = new Scanner(f);
			while (s.hasNextLine()) {
				buffer = s.nextLine().split(",");
				if (buffer[0].equals(username)) {
					lastFiveItems.addAll(Arrays.asList(buffer).subList(1, buffer.length));
					s.close();
					break;
				}
			}
		} catch (IOException ignored) {}
		return lastFiveItems;
	}


	/*
	 * ############################################
	 * # Following are methods for table Accounts #
	 * ############################################
	 * */

	/**
	 * Create new user account
	 * @param username Username of the user
	 * @param password Password of the user
	 * @param role Role of the user
	 * @return int	<br>
	 * 		0 for Insert failed <br>
	 * 		-1 for Account already exist <br>
	 * 		>0 for success
	 * */
	public int createAccount(String username, String password, String role) {
		int flag = 0;
		try {
			File f = new File(accountDBPath);
			Scanner s = new Scanner(f);
			while (s.hasNextLine()) {
				if (s.nextLine().split(" ")[0].equals(username)) {
					flag = -1;
					break;
				}
			}
			if (flag != -1) {
				FileWriter fw = new FileWriter(f, true);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(username + " " + password + " " + role);
				bw.newLine();
				bw.close();
				fw.close();
				flag = 1;
			}
		} catch (IOException e) {
			flag = 0;
		}
		return flag;
	}

	/**
	 * Delete account
	 * @param username Username of the user
	 * @return int	<br>
	 *      1 for success <br>
	 * 		0 for Cannot find account <br>
	 * 		-1 for error occured <br>
	 * 		-2 for Root account cannot be deleted
	 * */
	public int deleteAccount(String username) {
		int flag = 0;
		if (username.equals("group1")) {
			return -2;
		}
		File f = new File(accountDBPath);
		try {
			// input the (modified) file content to the StringBuffer "input"
			BufferedReader file = new BufferedReader(new FileReader(f));
			StringBuilder buffer = new StringBuilder();
			String line;

			while ((line = file.readLine()) != null) {
				if (line.split(" ")[0].equals(username)) {
					line = "";
					flag = 1;
				}
				if (!line.equals("")) {
					buffer.append(line);
					buffer.append('\n');
				}
			}
			file.close();
			FileOutputStream fileOut = new FileOutputStream(f);
			fileOut.write(buffer.toString().getBytes());
			fileOut.close();
		} catch (Exception e) {
			flag = -1;
		}
		return flag;
	}

	/**
	 * Check account
	 * @param username Username of the user
	 * @param password Password of the user
	 * @return boolean	<br>
	 * 		true for identity valid <br>
	 * 		false for identity invalid
	 * */
	public boolean checkAccount(String username, String password) {
		boolean flag = false;
		File f = new File(accountDBPath);
		String[] buffer;
		try	{
			Scanner s = new Scanner(f);
			while (s.hasNextLine())	{
				buffer = s.nextLine().split(" ");
				if (buffer[0].equals(username)) {
					flag = buffer[0].equals(password);
					s.close();
					break;
				}
			}
			s.close();
		} catch (IOException ignored) {
		}
		return flag;
	}

	/**
	 * Get role of user
	 * @param username Username of the user
	 * @return String <br>
	 * 		role of user if exists <br>
	 * 		null if not exists
	 * */
	public String getUserRole(String username) {
		String[] buffer;
		File f = new File(accountDBPath);
		try	{
			Scanner s = new Scanner(f);
			while(s.hasNextLine()) {
				buffer = s.nextLine().split(" ");
				if (buffer[0].equals(username)) {
					return buffer[2];
				}
			}
			s.close();
		} catch (IOException ignored) {}
		return null;
	}

	/*
	* #########################################
	* # Following are methods for table Items #
	* #########################################
	* */

	/** Get Item quantity
	 * @param name Name of the item
	 * @return int <br>
	 *     -1  Item not found<br>
	 *     >=0 quantity of the item
	 * */
	public int getItemQuantity(String name) {
		File f = new File(itemsDBPath);
		String[] buffer;
		try	{
			Scanner s = new Scanner(f);
			while(s.hasNextLine()){
				buffer = s.nextLine().split(",");
				if (buffer[1].equals(name)) {
					return Integer.parseInt(buffer[3]);
				}
			}
		} catch (IOException ignored) {}
		return -1;
	}

	/** Check code not exists
	 * @param code Integer type item code
	 * @return boolean <br>
	 * 		true Not exists<br>
	 * 		false means exists
	 * */
	public boolean checkItemCodeNotExist(int code) {
		boolean result = true;
		File f = new File(itemsDBPath);
		String co = Integer.toString(code);
		try {
			Scanner s = new Scanner(f);
			while (s.hasNextLine()) {
				if (s.nextLine().split(",")[0].equals(co)) {
					result = false;
					break;
				}
			}
		} catch (IOException ignored) {
		}
		return result;
	}

	/**
	 * Update the Item quantity
	 * @param name Name of the item
	 * @param quantity Amount of the item
	 * @return int	<br>
	 * 		0 for Item not found <br>
	 * 		-1 for quantity over limit 15 <br>
	 * 		>0 for success
	 * */
	public int updateItemQuantity(String name, int quantity) {
		int flag = 0;
		if (quantity > 15) {
			return -1;
		}
		try {
			File f = new File(itemsDBPath);
			BufferedReader file = new BufferedReader(new FileReader(f));
			StringBuilder buffer = new StringBuilder();
			String line;
			String[] attr;
			while ((line = file.readLine()) != null) {
				attr = line.split(",");
				if (attr[1].equals(name)) {
					flag = 1;
					attr[3] = Integer.toString(quantity);
					for (String i : attr) {
						buffer.append(i);
						buffer.append(',');
					}
					buffer.deleteCharAt(buffer.length()-1);
				} else {
					buffer.append(line);
				}
				buffer.append('\n');
			}
			file.close();
			FileOutputStream fileOut = new FileOutputStream(f);
			fileOut.write(buffer.toString().getBytes());
			fileOut.close();
		} catch (Exception ignored) {
		}
		return flag;
	}

	/**
	 * Update the Item price
	 * @param name Name of the item
	 * @param price Price of the item
	 * @return int	<br>
	 * 		0 for Item not found <br>
	 * 		-1 Price not valid <br>
	 * 		>0 for success
	 * */
	public int updateItemPrice(String name, double price) {
		int flag = 0;
		if (price < 0) {
			return -1;
		}
		try {
			File f = new File(itemsDBPath);
			BufferedReader file = new BufferedReader(new FileReader(f));
			StringBuilder buffer = new StringBuilder();
			String line;
			String[] attr;
			while ((line = file.readLine()) != null) {
				attr = line.split(",");
				if (attr[1].equals(name)) {
					flag = 1;
					attr[4] = Double.toString(price);
					for (String i : attr) {
						buffer.append(i);
						buffer.append(',');
					}
					buffer.deleteCharAt(buffer.length()-1);
				} else {
					buffer.append(line);
				}
				buffer.append('\n');
			}
			file.close();
			FileOutputStream fileOut = new FileOutputStream(f);
			fileOut.write(buffer.toString().getBytes());
			fileOut.close();
		} catch (Exception ignored) {
		}
		return flag;
	}

	/**
	 * Update the Item code
	 * @param name Name of the item
	 * @param code Code of the item
	 * @return int	<br>
	 * 		0 for Item not found <br>
	 * 		-1 code already exist <br>
	 * 		>0 for success
	 * */
	public int updateItemCode(String name, int code) {
		int flag = 0;
		if (!checkItemCodeNotExist(code)) {
			flag = -1;
			return flag;
		}
		try {
			File f = new File(itemsDBPath);
			BufferedReader file = new BufferedReader(new FileReader(f));
			StringBuilder buffer = new StringBuilder();
			String line;
			String[] attr;
			while ((line = file.readLine()) != null) {
				attr = line.split(",");
				if (attr[1].equals(name)) {
					flag = 1;
					attr[0] = Integer.toString(code);
					for (String i : attr) {
						buffer.append(i);
						buffer.append(',');
					}
					buffer.deleteCharAt(buffer.length()-1);
				} else {
					buffer.append(line);
				}
				buffer.append('\n');
			}
			file.close();
			FileOutputStream fileOut = new FileOutputStream(f);
			fileOut.write(buffer.toString().getBytes());
			fileOut.close();
		} catch (Exception ignored) {}
		return flag;
	}

	/** Check item name not exists
	 * @param name Name of the item
	 * @return boolean <br>
	 *   	true Not exist<br>
	 *   	false exists
	 * */
	public boolean checkItemNameNotExist(String name) {
		boolean result = true;
		File f = new File(itemsDBPath);
		try {
			Scanner s = new Scanner(f);
			while (s.hasNextLine()) {
				if (s.nextLine().split(",")[1].equals(name)) {
					result = false;
					break;
				}
			}
		} catch (IOException ignored) {
		}
		return result;
	}

	/**
	 * Update the Item name
	 * @param name Name of the item
	 * @param newName New name of the item
	 * @return int	<br>
	 * 		0 for Item not found <br>
	 * 		-1 Item name already exists <br>
	 * 		>0 for success
	 * */
	public int updateItemName(String name, String newName) {
		int flag = 0;
		if (!checkItemNameNotExist(newName)) {
			flag = -1;
			return flag;
		}
		try {
			File f = new File(itemsDBPath);
			BufferedReader file = new BufferedReader(new FileReader(f));
			StringBuilder buffer = new StringBuilder();
			String line;
			String[] attr;
			while ((line = file.readLine()) != null) {
				attr = line.split(",");
				if (attr[1].equals(name)) {
					flag = 1;
					attr[1] = newName;
					for (String i : attr) {
						buffer.append(i);
						buffer.append(',');
					}
					buffer.deleteCharAt(buffer.length()-1);
				} else {
					buffer.append(line);
				}
				buffer.append('\n');
			}
			file.close();
			FileOutputStream fileOut = new FileOutputStream(f);
			fileOut.write(buffer.toString().getBytes());
			fileOut.close();
		} catch (Exception ignored) {}
		return flag;
	}

	/** Check Category Not Exist
	 * @param name Name of category
	 * @return boolean <br>
	 * 		true Category not exists<br>
	 * 		false Category exists
	 * */
	public boolean checkCategoryNotExist(String name) {
		File f = new File(categoriesDBPath);
		try {
			Scanner s = new Scanner(f);
			while (s.hasNextLine())	{
				if (s.nextLine().equals(name)) {
					return false;
				}
			}
		} catch (IOException ignored){
		}
		return true;
	}

	/**
	 * Insert new Item
	 * @param code Code of the item
	 * @param name Name of the item
	 * @param category Category of the item
	 * @param quantity Amount of the item
	 * @param price Price of the item
	 * @return int	<br>
	 * 		0 for Item not found <br>
	 * 		>0 for success <br>
	 * 		-1 for code already exist <br>
	 * 		-2 for name already exist <br>
	 * 		-3 for category not exist <br>
	 * 		-4 for quantity not valid <br>
	 * 		-5 for price not valid
	 * */
	public int insertItem(int code, String name, String category, int quantity, double price) {
		if (!checkItemCodeNotExist(code)) {
			return -1;
		} else if (!checkItemNameNotExist(name)) {
			return -2;
		} else if (checkCategoryNotExist(category)) {
			return -3;
		} else if (quantity > 15) {
			return -4;
		} else if (price < 0) {
			return -5;
		}
		File f = new File(itemsDBPath);
		try {
			FileWriter fw = new FileWriter(f, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(code + "," + name + "," + category + "," + quantity + "," + price);
			bw.newLine();
			bw.close();
			fw.close();
		} catch (IOException ignored) {
		}
		return 1;
	}

	/**
	 * Delete Item
	 * @param name Name of the item
	 * @return int	<br>
	 * 		0 for Item not found <br>
	 * 		-1 for remove error <br>
	 * 		>0 for success
	 * */
	public int deleteItem(String name) {
		int flag = 0;
		File f = new File(itemsDBPath);
		try {
			// input the (modified) file content to the StringBuffer "input"
			BufferedReader file = new BufferedReader(new FileReader(f));
			StringBuilder buffer = new StringBuilder();
			String line;

			while ((line = file.readLine()) != null) {
				if (line.split(",")[1].equals(name)) {
					line = "";
					flag = 1;
				}
				if (!line.equals("")) {
					buffer.append(line);
					buffer.append('\n');
				}
			}
			file.close();
			FileOutputStream fileOut = new FileOutputStream(f);
			fileOut.write(buffer.toString().getBytes());
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
			flag = -1;
		}
		return flag;
	}

	/**
	 * */
}

