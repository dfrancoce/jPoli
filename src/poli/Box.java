package poli;

public class Box {
	private int position;

	private int nHouses;
	public static Double HOUSE_VALUE = 500.00;
	private int nHotels;
	public static Double HOTEL_VALUE = 1000.00;

	private BoxValue boxValue;
	private Player player;
	private Double propertyValue = 100.00;

	public Box(int position, BoxValue boxValue) {
		this.position = position;
		this.boxValue = boxValue;
		nHouses = nHotels = 0;
	}

	public void addHouse() {
		nHouses++;
	}

	public void addHotel() {
		nHotels++;
	}

	public int getPosition() {
		return position;
	}

	public BoxValue getBoxValue() {
		return boxValue;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Double getPropertyValue() {
		return propertyValue;
	}


	public Double amountToPay() {
		Double amountToPay = propertyValue + (propertyValue * this.boxValue.getValue());

		amountToPay = amountToPay + (nHouses * HOUSE_VALUE);
		amountToPay = amountToPay + (nHotels * HOTEL_VALUE);

		return amountToPay;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("boxValue = " + boxValue + ", ")
				.append("houses = " + nHouses + ", ")
				.append("hotels = " + nHotels);

		return sb.toString();
	}
}

enum BoxValue {
	BLUE(1), GREEN(2), RED(3), BLACK(4);

	private int value;
	private BoxValue(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
