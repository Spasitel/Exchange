package main;

import java.io.File;
import java.util.List;

import data.Client;
import data.Order;
import data.OrdersStorage;

/**
 * 
 * @author никита
 *
 */
public class ExchangeMain {

	public static void main(String[] args) {
		IOUtils.init();
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
			IOUtils.showUsage();
			return;
		}
		if (!IOUtils.checkFiles(clientsFile, ordersFile)) {
			IOUtils.showUsage();
			System.exit(1);
		}

		try {
			List<Client> clients = IOUtils.readClients(clientsFile);
			List<Order> orders = IOUtils.readOrders(ordersFile);
			OrdersStorage storage = new OrdersStorage();
			for (Order order : orders) {
				processOrder(clients, order, storage);
			}

			IOUtils.end(clients, orders, storage);
		} catch (IllegalStateException e) {
			System.exit(1);
		}
	}

	public static void processOrder(List<Client> clients, Order order, OrdersStorage storage) {
		Order match = getOppositeOrder(order, storage);

		if (match != null) {
			boolean remove = storage.remove(match);
			assert remove;
			calculateClients(clients, match, order);
		} else {
			storage.addOrder(order);
		}
	}

	public static Order getOppositeOrder(Order order, OrdersStorage storage) {
		Order match = null;
		for (Order current : storage.getOppositeOrders(order)) {
			if (isOrdersMatch(order, current)) {
				if (match != null) {
					IOUtils.debug("choice", "Order: " + order + " old choice: " + match + " alternative: " + current);
					continue;
				}
				match = current;
			}
		}
		return match;
	}

	public static void calculateClients(List<Client> clients, Order match, Order order) {
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
		IOUtils.debug("full", match.toString());
		IOUtils.debug("full", order.toString());

		assert seller != null && buyer != null;
		IOUtils.debug("full", "seller: " + seller);
		IOUtils.debug("full", "buyer: " + buyer);

		int amount = order.getAmount();
		int price = order.getPrice();
		int sum = amount * price;
		seller.addToCash(sum);
		buyer.addToCash(-sum);
		switch (order.getLot()) {
		case "A":
			seller.addToA(-amount);
			buyer.addToA(amount);
			break;
		case "B":
			seller.addToB(-amount);
			buyer.addToB(amount);

			break;
		case "C":
			seller.addToC(-amount);
			buyer.addToC(amount);

			break;
		case "D":
			seller.addToD(-amount);
			buyer.addToD(amount);
			break;

		default:
			IOUtils.printErr("Error: unknown lot: " + order);
			throw new IllegalStateException();
		}
		IOUtils.debug("full", "seller after: " + seller);
		IOUtils.debug("full", "buyer after: " + buyer);
		IOUtils.debug("full", "***************");

	}

	public static boolean isOrdersMatch(Order order, Order current) {
		boolean sale = current.isSale() ^ order.isSale();
		boolean lot = current.getLot().equals(order.getLot());
		boolean amount = current.getAmount() == order.getAmount();
		boolean price = current.getPrice() == order.getPrice();
		boolean name = !current.getName().equals(order.getName());
		return sale && lot && amount && price && name;
	}

}
