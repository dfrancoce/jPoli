package poli;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
	private List<Box> board = new ArrayList<Box>();
	private List<Player> players = new ArrayList<Player>();
	private int nextTurn;
	private int currentBoardPosition;
	private int round;
	private static String SYSTEM_COLOR = Log.WHITE;

	public Game() {
		this.nextTurn = 1;
		this.currentBoardPosition = 0;
		this.round = 1;

		generateBoardBoxes();
		generatePlayers();
	}

	public void play() {
		Log.write(SYSTEM_COLOR, "Starting game...");

		while (players.size() > 1) {
			Log.write(SYSTEM_COLOR, "Starting round " + round);

			for (int i = 0; i < players.size(); i++) {
				Player p = findNextPlayer(nextTurn);
				assertNotNull(p);

				int dice = generateRandomNumber(1, 6);
				Log.write(p.getConsoleColor(), "Player " + p.getName() + " is tossing the dice....... and it's a " + dice);
				movePlayer(p, dice);
			}

			resumeRound();
			round++;
			nextTurn = 1;
		}

		Log.write(SYSTEM_COLOR, "GAME OVER!");
		Log.write(SYSTEM_COLOR, "THE WINNER IS " + players.get(0).getName());
	}

	private void resumeRoundResults() {
		for (Player p: players) {
			Log.write(p.getConsoleColor(), "Player name: " + p.getName());
			Log.write(p.getConsoleColor(), "Player position: " + p.getPosition());
			Log.write(p.getConsoleColor(), "Player cash: " + p.getCash());
			Log.write(p.getConsoleColor(), "");
		}
	}

	private void resumeRound() {
		Log.write(SYSTEM_COLOR, "");
		Log.write(SYSTEM_COLOR, "Round " + round + " results:");
		resumeRoundResults();
		Log.write(SYSTEM_COLOR, "Finishing round " + round);
		Log.write(SYSTEM_COLOR, "");
	}

	private void buyHouse(Player p, Box b) {
		if (p.getCash() >= Box.HOUSE_VALUE) {
			p.setCash(p.getCash() - Box.HOUSE_VALUE);
			b.addHouse();
			Log.write(p.getConsoleColor(), p.getName() + " is buying a house");
		}
	}

	private void buyHotel(Player p, Box b) {
		if (p.getCash() >= Box.HOTEL_VALUE) {
			p.setCash(p.getCash() - Box.HOTEL_VALUE);
			b.addHotel();
			Log.write(p.getConsoleColor(), p.getName() + " is buying a hotel");
		}
	}

	private void buyBuilding(Player p, Box b) {
		// 1 --> Don't buy it
		// 2 --> Buy house if i can
		// 3 --> Buy hotel if i can
		int buyIt = generateRandomNumber(1, 3);

		if (buyIt == 2) {
			buyHouse(p, b);
		} else if (buyIt == 3) {
			buyHotel(p, b);
		}
	}

	private void buyProperty(Player p, Box b) {
		Double propertyValue = (b.getPropertyValue() * b.getBoxValue().getValue());
		int buyIt = generateRandomNumber(1, 2);

		if (buyIt > 1) {
			if (p.getCash() >= propertyValue) {
				p.setCash(p.getCash() - propertyValue);
				b.setPlayer(p);
				p.addProperty(b);
				Log.write(p.getConsoleColor(), p.getName() + " is buying the box " + b.getPosition());
			}
		}
	}

	private void eliminateBoxProperties(Player p) {
		for (Box b: board) {
			if (b.getPlayer() != null) {
				if (b.getPlayer().equals(p)) {
					b.setPlayer(null);
				}
			}
		}
	}

	private void reArrangeTurns() {
		for (int i = 0; i < players.size(); i ++) {
			players.get(i).setTurn(i+1);
		}
	}

	private void eliminatePlayer(Player p) {
		Log.write(p.getConsoleColor(), "GAME OVER!! Player " + p.getName());
		eliminateBoxProperties(p);
		nextTurn = p.getTurn();
		players.remove(p);
		reArrangeTurns();
	}

	private void pay(Player p, Box b) {
		Player playerToPay = b.getPlayer();
		Double amountToPay = b.amountToPay();

		if (p.getCash() < amountToPay) {
			playerToPay.setCash(playerToPay.getCash() + p.getCash());
			Log.write(p.getConsoleColor(), p.getName() + " pays " + p.getCash() + " to " + playerToPay.getName());
			eliminatePlayer(p);
		} else {
			playerToPay.setCash(playerToPay.getCash() + amountToPay);
			Log.write(p.getConsoleColor(), p.getName() + " pays " + amountToPay + " to " + playerToPay.getName());
			p.setCash(p.getCash() - amountToPay);
		}
	}

	private void checkIsMyProperty(Player p, Box b) {
		if (b.getPlayer().equals(p)) {
			buyBuilding(p, b);
		} else {
			Log.write(SYSTEM_COLOR, "Box " + b.getPosition() + " is player " + b.getPlayer().getName() + " property");
			pay(p, b);
		}
	}

	private void checkProperty(Player p) {
		Box box = getBoxByPosition(currentBoardPosition);

		// Someone's property
		if (box.getPlayer() != null) {
			checkIsMyProperty(p, box);
		} else {
			buyProperty(p, box);
		}
	}

	private void movePlayer(Player p, int dice) {
		currentBoardPosition = (p.getPosition() + dice);

		if (currentBoardPosition > 80) {
			currentBoardPosition = currentBoardPosition - 80;
		}

		p.setPosition(currentBoardPosition);
		Log.write(SYSTEM_COLOR, "Player " + p.getName() + " is on " + currentBoardPosition + " position now");
		checkProperty(p);
	}

	private Box getBoxByPosition(int position) {
		for (Box b: board) {
			if (b.getPosition() == position) {
				return b;
			}
		}

		return null;
	}

	private Player findNextPlayer(int turn) {
		Player player = null;

		for (Player p: players) {
			if (p.getTurn() == turn) {
				player = p;
			}
		}

		nextTurn = nextTurn + 1;
		return player;
	}


	// Generation
	private BoxValue generateRandomBoxValue() {
		int randomNumber = generateRandomNumber(1, 4);

		BoxValue boxValue;
		switch (randomNumber) {
			case 1:
				boxValue = BoxValue.BLUE;
				break;
			case 2:
				boxValue = BoxValue.GREEN;
				break;
			case 3:
				boxValue = BoxValue.RED;
				break;
			case 4:
				boxValue = BoxValue.BLACK;
				break;
			default:
				boxValue = BoxValue.BLUE;
		}

		return boxValue;
	}

	private void generateBoardBoxes() {
		for (int i = 0; i < 80; i++) {
			board.add(new Box((i+1), generateRandomBoxValue()));
		}
	}

	private void generatePlayers() {
		players.add(new Player("Player1", 10000.00, 1, Log.BLUE));
		players.add(new Player("Player2", 10000.00, 2, Log.GREEN));
		players.add(new Player("Player3", 10000.00, 3, Log.RED));
		players.add(new Player("Player4", 10000.00, 4, Log.YELLOW));
	}

	// Util
	private int generateRandomNumber(int minValue, int maxValue) {
		Random rand = new Random();
		return (rand.nextInt((maxValue - minValue) + 1) + minValue);
	}

	private void assertNotNull(Object o) {
		if (o == null) {
			throw new RuntimeException();
		}
	}
}
