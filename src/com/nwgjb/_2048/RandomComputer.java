package com.nwgjb._2048;

public class RandomComputer implements Computer{
	public void makeMove(GameBoard b){
		b.generateRandomly();
	}
}
