package ru.sbertech.data;

public class Client {
	private final String name;
	private int cash;
	private int a;
	private int b;
	private int c;
	private int d;

	public Client(String name, int cash, int a, int b, int c, int d) {
		super();
		this.name = name;
		this.cash = cash;
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}

	public int getCash() {
		return cash;
	}

	public void addToCash(int cash) {
		this.cash += cash;
	}

	public int getA() {
		return a;
	}

	public void addToA(int a) {
		this.a += a;
	}

	public int getB() {
		return b;
	}

	public void addToB(int b) {
		this.b += b;
	}

	public int getC() {
		return c;
	}

	public void addToC(int c) {
		this.c += c;
	}

	public int getD() {
		return d;
	}

	public void addToD(int d) {
		this.d += d;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name + "\t" + cash + "\t" + a + "\t" + b + "\t" + c + "\t" + d;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + a;
		result = prime * result + b;
		result = prime * result + c;
		result = prime * result + cash;
		result = prime * result + d;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Client other = (Client) obj;
		if (a != other.a)
			return false;
		if (b != other.b)
			return false;
		if (c != other.c)
			return false;
		if (cash != other.cash)
			return false;
		if (d != other.d)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
