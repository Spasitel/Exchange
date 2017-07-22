package main;

import java.io.File;
import java.util.Iterator;
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

		List<Client> clients = IOUtils.readClientsData(clientsFile);
		List<Order> orders = IOUtils.readOrders(clients, ordersFile);
		OrdersStorage storage = new OrdersStorage();
		for (Order order : orders) {
			processOrder(clients, order, storage);
		}

		IOUtils.end(clients, orders, storage);
	}

	private static void processOrder(List<Client> clients, Order order, OrdersStorage storage) {
		boolean isDone = false;
		Order match = null;
		Iterator<Order> iter = storage.getOppositeOrders(order).iterator();
		while (iter.hasNext()) {
			Order current = iter.next();
			if (isOrdersMatch(order, current)) {
				if (isDone) {
					IOUtils.debug("choice", "Order: " + order + " old choice: " + match + " alternative: " + current);
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
		IOUtils.debug("full", match.toString());
		IOUtils.debug("full", order.toString());

		assert seller != null && buyer != null;
		IOUtils.debug("full", "seller: " + seller);
		IOUtils.debug("full", "buyer: " + buyer);

		int amount = order.getAmount();
		int sum = amount * order.getPrice();
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
			System.err.println("Error: unknown lot: " + order);
			System.exit(1);
			break;
		}
		IOUtils.debug("full", "seller after: " + seller);
		IOUtils.debug("full", "buyer after: " + buyer);
		IOUtils.debug("full", "***************");

	}

	private static boolean isOrdersMatch(Order order, Order current) {
		return current.isSale() ^ order.isSale() && current.getLot().equals(order.getLot())
				&& current.getAmount() == order.getAmount() && current.getPrice() == order.getPrice()
				&& !current.getName().equals(order.getName());
	}

}
