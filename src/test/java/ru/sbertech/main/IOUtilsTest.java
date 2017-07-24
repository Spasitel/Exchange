package ru.sbertech.main;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import ru.sbertech.data.Client;
import ru.sbertech.data.Order;

public class IOUtilsTest {

	@Test
	public void readClientsTest() {
		// C1 100 130 240 760 320
		// C2 430 370 120 950 560
		String file = getClass().getClassLoader().getResource("ru/sbertech/main/IOUtilsRes/clients.txt").getFile();
		File clientsFile = new File(file);
		List<Client> clients = IOUtils.readClients(clientsFile);
		Assert.assertEquals(clients.size(), 2);
		Client firts = clients.get(0);
		Assert.assertEquals(firts.getName(), "C1");
		Assert.assertEquals(firts.getCash(), 100);
		Assert.assertEquals(firts.getA(), 130);
		Assert.assertEquals(firts.getB(), 240);
		Assert.assertEquals(firts.getC(), 760);
		Assert.assertEquals(firts.getD(), 320);
		Client second = clients.get(1);
		Assert.assertEquals(second.getName(), "C2");
		Assert.assertEquals(second.getCash(), 430);
		Assert.assertEquals(second.getA(), 370);
		Assert.assertEquals(second.getB(), 120);
		Assert.assertEquals(second.getC(), 950);
		Assert.assertEquals(second.getD(), 560);

	}

	@Test
	public void readOrdersTest() {
		// C8 b C 15 4
		// C2 s C 14 5
		File ordersFile = new File(
				getClass().getClassLoader().getResource("ru/sbertech/main/IOUtilsRes/orders.txt").getFile());
		List<Order> orders = IOUtils.readOrders(ordersFile);
		Assert.assertEquals(orders.size(), 2);
		Order firts = orders.get(0);
		Assert.assertEquals(firts.getName(), "C8");
		Assert.assertFalse(firts.isSale());
		Assert.assertEquals(firts.getLot(), "C");
		Assert.assertEquals(firts.getPrice(), 15);
		Assert.assertEquals(firts.getAmount(), 4);
		Order second = orders.get(1);
		Assert.assertEquals(second.getName(), "C2");
		Assert.assertTrue(second.isSale());
		Assert.assertEquals(second.getLot(), "C");
		Assert.assertEquals(second.getPrice(), 14);
		Assert.assertEquals(second.getAmount(), 5);

	}
}
