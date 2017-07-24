package ru.sbertech.data;

public class Order {
	final int number;
	final String name;
	final boolean isSale;
	final String lot;
	final int price;
	final int amount;

	public Order(int number, String name, String type, String lot, int price, int amount) {
		super();
		this.number = number;
		this.name = name;
		if (type.equals("s"))
			this.isSale = true;
		else
			this.isSale = false;
		this.lot = lot;
		this.price = price;
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "Order [number=" + number + ", name=" + name + ", isSale=" + isSale + ", lot=" + lot + ", price=" + price
				+ ", amount=" + amount + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + amount;
		result = prime * result + (isSale ? 1231 : 1237);
		result = prime * result + ((lot == null) ? 0 : lot.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + number;
		result = prime * result + price;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (amount != other.amount)
			return false;
		if (isSale != other.isSale)
			return false;
		if (lot == null) {
			if (other.lot != null)
				return false;
		} else if (!lot.equals(other.lot))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (number != other.number)
			return false;
		if (price != other.price)
			return false;
		return true;
	}

	public int getNumber() {
		return number;
	}

	public String getName() {
		return name;
	}

	public boolean isSale() {
		return isSale;
	}

	public String getLot() {
		return lot;
	}

	public int getPrice() {
		return price;
	}

	public int getAmount() {
		return amount;
	}
}
