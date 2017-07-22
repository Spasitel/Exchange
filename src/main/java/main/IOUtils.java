package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import data.Client;
import data.Order;
import data.OrdersStorage;

/**
 * 
 * @author никита
 *
 */
public class IOUtils {

	// TODO: check English

	private static Map<String, PrintWriter> debugLogs = new LinkedHashMap<>();
	private static boolean debug = false;

	public static void init() {
		if (System.getProperty("debug").equalsIgnoreCase("true"))
			debug = true;
	}

	public static void debug(String logName, String message) {
		if (debug) {
			PrintWriter outputStream = debugLogs.get(logName);
			if (outputStream == null) {
				String fileName = "debug-" + logName + ".log";
				try {
					outputStream = new PrintWriter(fileName);
					debugLogs.put(logName, outputStream);
				} catch (FileNotFoundException e) {
					System.err.println("Error: could not open debug file: " + fileName);
					e.printStackTrace();
					System.exit(1);
				}
			}

			outputStream.println(message);
		}

	}

	public static List<Client> readClientsData(File clientsFile) {
		List<Client> clients = new LinkedList<Client>();
		try {
			Scanner sc = new Scanner(clientsFile);
			while (sc.hasNext()) {
				String name = sc.next();
				int cash = sc.nextInt();
				int a = sc.nextInt();
				int b = sc.nextInt();
				int c = sc.nextInt();
				int d = sc.nextInt();
				Client client = new Client(name, cash, a, b, c, d);
				clients.add(client);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			IOUtils.fileNotFound(clientsFile);
			e.printStackTrace();
			assert false : "checkFiles failed";
			System.exit(1);
		} catch (InputMismatchException e) {
			IOUtils.wrongFileFormat(clientsFile);
			e.printStackTrace();
			System.exit(1);
		}
		return clients;
	}

	private static void wrongFileFormat(File clientsFile) {
		System.err.println("Error: wrong format of file: " + clientsFile.getName());
	}

	public static void showUsage() {
		System.out.println("Usage: ExchangeMain [path-to-clients.txt path-to-orders.txt]");
		System.out.println("\t by default input reads from files clients.txt and orders.txt in current directory");

	}

	public static boolean checkFiles(File clientsFile, File ordersFile) {
		if (!clientsFile.exists()) {
			IOUtils.fileNotFound(clientsFile);
			return false;
		}
		if (clientsFile.isDirectory()) {
			IOUtils.fileIsDirectory(clientsFile);
			return false;
		}
		if (!ordersFile.exists()) {
			IOUtils.fileNotFound(ordersFile);
			return false;
		}
		if (ordersFile.isDirectory()) {
			IOUtils.fileIsDirectory(ordersFile);
			return false;
		}

		return true;
	}

	private static void fileIsDirectory(File file) {
		System.err.println("Error: file " + file.getName() + " is directory");
	}

	private static void fileNotFound(File file) {
		System.err.println("Error: file " + file.getName() + " doesn't exist");
	}

	public static List<Order> readOrders(List<Client> clients, File ordersFile) {
		List<Order> result = new LinkedList<>();
		try {
			Scanner sc = new Scanner(ordersFile);
			int number = 0;
			while (sc.hasNext()) {
				number++;
				String name = sc.next();
				String type = sc.next();
				String lot = sc.next();
				int price = sc.nextInt();
				int amount = sc.nextInt();
				Order order = new Order(number, name, type, lot, price, amount);
				result.add(order);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			fileNotFound(ordersFile);
			assert false : "checkFiles failed";
			e.printStackTrace();
			System.exit(1);
		} catch (InputMismatchException e) {
			wrongFileFormat(ordersFile);
			e.printStackTrace();
			System.exit(1);
		}
		return result;

	}

	public static void end(List<Client> clients, List<Order> orders, OrdersStorage storage) {
		System.out.println("Not executed orders: " + storage.getNumberOfOrders());
		String resultFileName = "result.txt";

		try {
			PrintWriter printWriter = new PrintWriter(resultFileName);
			for (Client client : clients) {
				System.out.println(client);
				printWriter.println(client);
			}
			printWriter.close();
		} catch (FileNotFoundException e) {
			System.err.println("Error: could not open file: " + resultFileName);
			e.printStackTrace();
		}
		for (PrintWriter writer : debugLogs.values()) {
			writer.close();
		}
	}

}
