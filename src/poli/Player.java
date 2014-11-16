package poli;

import java.util.ArrayList;
import java.util.List;

public class Player {
	private String name;
	private Double cash;
	private int turn;
	private int position;

	private List<Box> properties;
	private String consoleColor;

	public Player(String name, Double cash, int turn, String consoleColor) {
		this.name = name;
		this.cash = cash;
		this.turn = turn;
		this.position = 0;
		this.consoleColor = consoleColor;

		properties = new ArrayList<Box>();
	}

	public String getName() {
		return name;
	}

	public int getTurn() {
		return turn;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getPosition() {
		return position;
	}

	public void addProperty(Box box) {
		this.properties.add(box);
	}

	public Double getCash() {
		return cash;
	}

	public void setCash(Double cash) {
		this.cash = cash;
	}

	public String getConsoleColor() {
		return consoleColor;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("name: " + name + ", ")
				.append("cash: " + cash.toString());

		return sb.toString();
	}
}
