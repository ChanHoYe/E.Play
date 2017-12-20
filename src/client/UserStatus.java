package client;

public class UserStatus {
	public enum Location {
		WAIT(0), ROOM1(1), ROOM2(2), ROOM3(3), ROOM4(4), OTHER(5);
		private int value;
		Location(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return this.value;
		}
		
		@Override
		public String toString() {
			String str = null;
			switch(value) {
			case 0:
				str = "WAIT";
			case 1:
				str = "ROOM1";
			case 2:
				str = "ROOM2";
			case 3:
				str = "ROOM3";
			case 4:
				str = "ROOM4";
			}
			
			return str;
		}
	}
	
	public enum Status {
		DEAD(0), IDLE(1), GAME(2);
		private int value;
		Status(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return this.value;
		}
		
		@Override
		public String toString() {
			String str = null;
			switch(value) {
			case 0:
				str = "GAME";
			case 1:
				str = "IDLE";
			case 2:
				str = "DEAD";
			}
			
			return str;
		}
	}
}