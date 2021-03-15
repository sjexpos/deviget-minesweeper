package com.deviget.challenge.minesweeper.core.model;

public interface CellBoard {
	public enum State {
		COVERED((byte)0),
		UNCOVERED((byte)-1),
		FLAGGED((byte)1),
		MINE((byte)5);
		
		private byte value;
		
		State(byte value) {
			this.value = value;
		}
		
		protected byte toValue() {
			return this.value;
		}
		
		static protected State fromValue(byte value) {
			for (State s : values()) {
				if (s.value == value) {
					return s;
				}
			}
			return null;
		}
	};

	int getX();
	
	int getY();
	
	State getState();
	
	int surroundMines();
	
}
