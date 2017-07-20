package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import data.Client;
import data.Order;
import data.OrdersStorage;

public class ExchangeMain {

	/**
	 * TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		File clientsFile;
		File ordersFile;

		if (args.length == 0) {
			clientsFile = new File("clients.txt");
			ordersFile = new File("orders.txt");
		} else if (args.length == 2) {
			clientsFile = new File(args[0]);
			ordersFile = new File(args[1]);
		} else {
			// TODO: help
			showUsage();
			return;
		}
		if (!checkFiles(clientsFile, ordersFile)) {
			showUsage();
			System.exit(1);
		}

		List<Client> clients = readClientsData(clientsFile);
		processOrders(clients, ordersFile);

		for (Client client : clients) {
			System.out.println(client.toString());
		}
		// TODO: results
	}

	private static void processOrders(List<Client> clients, File ordersFile) {
		try {
			Scanner sc = new Scanner(ordersFile);
			int number = 0;
			OrdersStorage storage = new OrdersStorage();
			while (sc.hasNext()) {
				number++;
				String name = sc.next();
				String type = sc.next();
				String lot = sc.next();
				int price = sc.nextInt();
				int amount = sc.nextInt();
				Order order = new Order(number, name, type, lot, price, amount);
				processOrder(clients, order, storage);
			}
			System.out.println("Not executed orders: " + storage.getNumberOfOrders());
			sc.close();
		} catch (FileNotFoundException e) {
			fileNotFound(ordersFile);
			assert false : "checkFiles failed";
			System.exit(1);
		} catch (InputMismatchException e) {
			wrongFileFormat(ordersFile);
			System.exit(1);
		}

	}

	private static void processOrder(List<Client> clients, Order order, OrdersStorage storage) {
		boolean isDone = false;
		Order match = null;
		Iterator<Order> iter = storage.getOppositeOrders(order).iterator();
		while (iter.hasNext()) {
			Order current = iter.next();
			if (isOrdersMatch(order, current)) {
				if (isDone) {

					// TODO
					System.out.println("Prev: " + current + " new: " + order + " match: " + match);
					continue;
				}
				isDone = true;
				match = current;
				iter.remove();
			}
		}

		if (isDone) {
			calculateClients(clients, match, order);
		} else {
			storage.addOrder(order);
		}
	}

	private static void calculateClients(List<Client> clients, Order match, Order order) {
		Client buyer = null;
		Client seller = null;
		for (Client client : clients) {
			if (match.getName().equals(client.getName())) {
				if (match.isSale()) {
					assert seller == null;
					seller = client;
				} else {
					assert buyer == null;
					buyer = client;
				}
			}
			if (order.getName().equals(client.getName())) {
				if (order.isSale()) {
					assert seller == null;
					seller = client;
				} else {
					assert buyer == null;
					buyer = client;
				}
			}
		}
		assert seller != null && buyer != null;

		int amount = order.getAmount();
		int sum = amount * order.getPrice();
		seller.addToCash(sum);
		buyer.addToCash(-sum);
		switch (order.getLot()) {
		case "A":
			seller.addToA(amount);
			buyer.addToA(-amount);
			break;
		case "B":
			seller.addToB(amount);
			buyer.addToB(-amount);

			break;
		case "C":
			seller.addToC(amount);
			buyer.addToC(-amount);

			break;
		case "D":
			seller.addToD(amount);
			buyer.addToD(-amount);
			break;

		default:
			System.err.println("Error: unknown lot: " + order.getLot());
			assert false;
			break;
		}

	}

	private static boolean isOrdersMatch(Order order, Order current) {
		return current.isSale() ^ order.isSale() && current.getLot().equals(order.getLot())
				&& current.getAmount() == order.getAmount() && current.getPrice() == order.getPrice()
				&& !current.getName().equals(order.getName());
	}

	private static List<Client> readClientsData(File clientsFile) {
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
			fileNotFound(clientsFile);
			assert false : "checkFiles failed";
			System.exit(1);
		} catch (InputMismatchException e) {
			wrongFileFormat(clientsFile);
			System.exit(1);
		}
		return clients;
	}

	private static void wrongFileFormat(File clientsFile) {
		System.err.println("Error: wrong format of file: " + clientsFile.getName());
	}

	private static void showUsage() {
		System.out.println("Usage: ExchangeMain [path-to-clients.txt path-to-orders.txt]");
		System.out.println("\t by default input reads from files clients.txt and orders.txt in current directory");

	}

	private static boolean checkFiles(File clientsFile, File ordersFile) {
		if (!clientsFile.exists()) {
			fileNotFound(clientsFile);
			return false;
		}
		if (clientsFile.isDirectory()) {
			fileIsDirectory(clientsFile);
			return false;
		}
		if (!ordersFile.exists()) {
			fileNotFound(ordersFile);
			return false;
		}
		if (ordersFile.isDirectory()) {
			fileIsDirectory(ordersFile);
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

}
