package ru.sbertech.main;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import ru.sbertech.data.Client;
import ru.sbertech.data.Order;
import ru.sbertech.data.OrdersStorage;
import ru.sbertech.main.ExchangeMain;

public class ExchangeMainTest {
	// TODO: use generic instead of making all methods public
	@Test
	public void getOppositeOrderNoOppositeTest() {
		Order order = new Order(1, "C1", "b", "A", 10, 5);
		OrdersStorage storage = new OrdersStorage();
		storage.addOrder(new Order(2, "C1", "s", "A", 10, 5));
		storage.addOrder(new Order(3, "C2", "b", "A", 10, 5));
		storage.addOrder(new Order(4, "C2", "s", "B", 10, 5));
		storage.addOrder(new Order(5, "C2", "s", "A", 11, 5));
		storage.addOrder(new Order(6, "C2", "s", "A", 10, 6));
		Order opposite = ExchangeMain.getOppositeOrder(order, storage);
		Assert.assertNull(opposite);
	}

	@Test
	public void getOppositeOrderPositiveTest() {
		Order order = new Order(1, "C1", "b", "A", 10, 5);
		OrdersStorage storage = new OrdersStorage();
		Order order2 = new Order(2, "C2", "s", "A", 10, 5);
		storage.addOrder(order2);
		storage.addOrder(new Order(3, "C2", "b", "A", 10, 5));
		storage.addOrder(new Order(4, "C2", "s", "B", 10, 5));
		storage.addOrder(new Order(5, "C2", "s", "A", 11, 5));
		storage.addOrder(new Order(6, "C2", "s", "A", 10, 6));
		Order opposite = ExchangeMain.getOppositeOrder(order, storage);
		Assert.assertEquals(opposite, order2);
	}

	@Test
	public void getOppositeOrderMultiOppositeTest() {
		Order order = new Order(1, "C1", "b", "A", 10, 5);
		OrdersStorage storage = new OrdersStorage();
		Order order2 = new Order(2, "C2", "s", "A", 10, 5);
		storage.addOrder(order2);
		storage.addOrder(new Order(3, "C2", "b", "A", 10, 5));
		storage.addOrder(new Order(4, "C2", "s", "B", 10, 5));
		storage.addOrder(new Order(5, "C2", "s", "A", 11, 5));
		storage.addOrder(new Order(6, "C2", "s", "A", 10, 6));
		storage.addOrder(new Order(7, "C3", "s", "A", 10, 5));
		storage.addOrder(new Order(8, "C4", "s", "A", 10, 5));
		Order opposite = ExchangeMain.getOppositeOrder(order, storage);
		Assert.assertEquals(opposite, order2);
	}

	@Test(expected = IllegalStateException.class)
	public void calculateClientsCrushNoSuchLotTest() {
		List<Client> clients = new LinkedList<>();
		Client client1 = new Client("C1", 100, 10, 20, 30, 40);
		clients.add(client1);
		Client client2 = new Client("C2", 200, 15, 25, 35, 45);
		clients.add(client2);
		Order order1 = new Order(1, "C1", "b", "F", 11, 7);
		Order order2 = new Order(1, "C2", "s", "F", 11, 7);
		ExchangeMain.calculateClients(clients, order1, order2);
	}

	@Test(expected = AssertionError.class)
	public void calculateClientsCrush2BuyersTest() {
		List<Client> clients = new LinkedList<>();
		Client client1 = new Client("C1", 100, 10, 20, 30, 40);
		clients.add(client1);
		Client client2 = new Client("C1", 200, 15, 25, 35, 45);
		clients.add(client2);
		Order order1 = new Order(1, "C1", "b", "A", 11, 7);
		Order order2 = new Order(1, "C2", "s", "A", 11, 7);
		ExchangeMain.calculateClients(clients, order1, order2);
	}

	@Test(expected = AssertionError.class)
	public void calculateClientsCrush2SellersTest() {
		List<Client> clients = new LinkedList<>();
		Client client1 = new Client("C2", 100, 10, 20, 30, 40);
		clients.add(client1);
		Client client2 = new Client("C2", 200, 15, 25, 35, 45);
		clients.add(client2);
		Order order1 = new Order(1, "C1", "b", "A", 11, 7);
		Order order2 = new Order(1, "C2", "s", "A", 11, 7);
		ExchangeMain.calculateClients(clients, order1, order2);
	}

	@Test(expected = AssertionError.class)
	public void calculateClientsCrushNoBuyersTest() {
		List<Client> clients = new LinkedList<>();
		Client client1 = new Client("C3", 100, 10, 20, 30, 40);
		clients.add(client1);
		Client client2 = new Client("C1", 200, 15, 25, 35, 45);
		clients.add(client2);
		Order order1 = new Order(1, "C1", "b", "A", 11, 7);
		Order order2 = new Order(1, "C2", "s", "A", 11, 7);
		ExchangeMain.calculateClients(clients, order1, order2);
	}

	@Test(expected = AssertionError.class)
	public void calculateClientsCrushNoSellersTest() {
		List<Client> clients = new LinkedList<>();
		Client client1 = new Client("C1", 100, 10, 20, 30, 40);
		clients.add(client1);
		Client client2 = new Client("C3", 200, 15, 25, 35, 45);
		clients.add(client2);
		Order order1 = new Order(1, "C1", "b", "A", 11, 7);
		Order order2 = new Order(1, "C2", "s", "A", 11, 7);
		ExchangeMain.calculateClients(clients, order1, order2);
	}

	@Test
	public void calculateClientsPositiveTest() {
		List<Client> clients = new LinkedList<>();
		Client client1 = new Client("C1", 100, 10, 20, 30, 40);
		clients.add(client1);
		Client client2 = new Client("C2", 200, 15, 25, 35, 45);
		clients.add(client2);
		Order order1 = new Order(1, "C1", "b", "A", 11, 7);
		Order order2 = new Order(1, "C2", "s", "A", 11, 7);
		ExchangeMain.calculateClients(clients, order1, order2);
		Assert.assertEquals(client1.getCash(), 23);
		Assert.assertEquals(client1.getA(), 17);
		Assert.assertEquals(client1.getB(), 20);
		Assert.assertEquals(client1.getC(), 30);
		Assert.assertEquals(client1.getD(), 40);
		Assert.assertEquals(client2.getCash(), 277);
		Assert.assertEquals(client2.getA(), 8);
		Assert.assertEquals(client2.getB(), 25);
		Assert.assertEquals(client2.getC(), 35);
		Assert.assertEquals(client2.getD(), 45);
	}

	@Test
	public void isOrdersMatchPositiveTest() {
		Order order = new Order(1, "C1", "b", "A", 10, 5);
		Order current = new Order(2, "C2", "s", "A", 10, 5);
		Assert.assertTrue(ExchangeMain.isOrdersMatch(order, current));
	}

	@Test
	public void isOrdersMatchNegativeNameTest() {
		Order order = new Order(1, "C1", "b", "A", 10, 5);
		Order current = new Order(2, "C1", "s", "A", 10, 5);
		Assert.assertFalse(ExchangeMain.isOrdersMatch(order, current));
	}

	@Test
	public void isOrdersMatchNegativeTypeTest() {
		Order order = new Order(1, "C1", "b", "A", 10, 5);
		Order current = new Order(2, "C2", "b", "A", 10, 5);
		Assert.assertFalse(ExchangeMain.isOrdersMatch(order, current));
	}

	@Test
	public void isOrdersMatchNegativeLotTest() {
		Order order = new Order(1, "C1", "b", "A", 10, 5);
		Order current = new Order(2, "C2", "s", "B", 10, 5);
		Assert.assertFalse(ExchangeMain.isOrdersMatch(order, current));
	}

	@Test
	public void isOrdersMatchNegativePriceTest() {
		Order order = new Order(1, "C1", "b", "A", 10, 5);
		Order current = new Order(2, "C2", "s", "A", 11, 5);
		Assert.assertFalse(ExchangeMain.isOrdersMatch(order, current));
	}

	@Test
	public void isOrdersMatchNegativeAmountTest() {
		Order order = new Order(1, "C1", "b", "A", 10, 5);
		Order current = new Order(2, "C2", "s", "A", 10, 6);
		Assert.assertFalse(ExchangeMain.isOrdersMatch(order, current));
	}

}
