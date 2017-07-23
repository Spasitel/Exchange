package data;

import java.util.LinkedList;
import java.util.List;

public class OrdersStorage {
	// TODO: split by lot, if it's too slow
	private final List<Order> ordersToSell = new LinkedList<Order>();
	private final List<Order> ordersToBuy = new LinkedList<Order>();

	public List<Order> getOppositeOrders(Order order) {
		if (order.isSale)
			return ordersToBuy;
		else
			return ordersToSell;
	}

	public void addOrder(Order order) {
		if (order.isSale)
			ordersToSell.add(order);
		else
			ordersToBuy.add(order);
	}

	public int getNumberOfOrders() {
		return ordersToBuy.size() + ordersToSell.size();
	}

	public boolean remove(Order order) {
		if (order.isSale)
			return ordersToSell.remove(order);
		else
			return ordersToBuy.remove(order);

	}

}
