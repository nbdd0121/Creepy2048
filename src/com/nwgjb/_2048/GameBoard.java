package com.nwgjb._2048;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.nwgjb.commons.util.MoreArrays;

public class GameBoard {
	int[][] board;
	int score=0;
	
	static final int UP=0;
	static final int DOWN=1;
	static final int LEFT=2;
	static final int RIGHT=3;
	static final String[] NAMES={"up", "down", "left", "right"};
	
	ArrayList<Integer> xl=new ArrayList<>();
	ArrayList<Integer> yl=new ArrayList<>();
	ArrayList<Integer> possibleMove=new ArrayList<>();
	
	static final boolean[] PATTERN={
		false,	//0000
		true,	//0001
		true,	//0010
		true,	//0011
		true,	//0100
		true,	//0101
		true,	//0110
		true,	//0111
		false,	//1000
		true,
		true,
		true,
		false,
		true,
		false,
		false
	};
	
	Random rand=new Random();
	
	public GameBoard(){
		board=new int[4][4];
		generateRandomly();
		generateRandomly();
	}
	
	public GameBoard(GameBoard b){
		board=MoreArrays.dup(b.board);
	}
	
	public GameBoard(int[][] b){
		board=b;
	}
	
	public void copyFrom(GameBoard b){
		MoreArrays.destoryDup(b.board, board);
	}
	
	public void getSpareSpace(){
		xl.clear();
		yl.clear();
		for(int x=0;x<4;x++){
			for(int y=0;y<4;y++){
				if(getTile(x, y)==0){
					xl.add(x);
					yl.add(y);
				}
			}
		}
	}
	
	public void generateRandomly() {
		getSpareSpace();
		if(xl.isEmpty()){
			//throw new RuntimeException("LOSE");
			return;
		}
		int index=(int)(rand.nextInt(xl.size()));
		int x=xl.get(index);
		int y=yl.get(index);
		putTile(x, y, rand.nextInt(10)==0?2:1);
	}
	
	public void left(){
		for(int y=0;y<4;y++){
			int free=0;
			int prev=0;
			for(int x=0;x<4;x++){
				int t=getTile(x, y);
				if(t!=0){
					putTile(x, y, 0);
					if(prev==t){
						score+=1<<(t+1);
						putTile(free-1, y, t+1);
						prev=0;
					}else{
						putTile(free++, y, t);
						prev=t;
					}
				}
			}
		}
	}
	
	public void up(){
		for(int x=0;x<4;x++){
			int free=0;
			int prev=0;
			for(int y=0;y<4;y++){
				int t=getTile(x, y);
				if(t!=0){
					putTile(x, y, 0);
					if(prev==t){
						score+=1<<(t+1);
						putTile(x, free-1, t+1);
						prev=0;
					}else{
						putTile(x, free++, t);
						prev=t;
					}
				}
			}
		}
	}
	
	public void right(){
		for(int y=0;y<4;y++){
			int free=3;
			int prev=0;
			for(int x=3;x>=0;x--){
				int t=getTile(x, y);
				if(t!=0){
					putTile(x, y, 0);
					if(prev==t){
						score+=1<<(t+1);
						putTile(free+1, y, t+1);
						prev=0;
					}else{
						putTile(free--, y, t);
						prev=t;
					}
				}
			}
		}
	}
	
	public void down(){
		for(int x=0;x<4;x++){
			int free=3;
			int prev=0;
			for(int y=3;y>=0;y--){
				int t=getTile(x, y);
				if(t!=0){
					putTile(x, y, 0);
					if(prev==t){
						score+=1<<(t+1);
						putTile(x, free+1, t+1);
						prev=0;
					}else{
						putTile(x, free--, t);
						prev=t;
					}
				}
			}
		}
	}
	
	public boolean canLeft(){
		for(int y=0;y<4;y++){
			int prev=getTile(0, y);
			for(int x=1;x<4;x++){
				int t=getTile(x, y);
				if(t!=0){
					if(t==prev){
						return true;
					}else{
						prev=t;
					}
				}
			}
			int c=getTile(0, y)==0?0:0b1000;
			c+=getTile(1, y)==0?0:0b100;
			c+=getTile(2, y)==0?0:0b10;
			c+=getTile(3, y)==0?0:0b1;
			if(PATTERN[c]){
				return true;
			}
		}
		
		return false;
	}
	
	public boolean canRight(){
		for(int y=0;y<4;y++){
			int prev=getTile(0, y);
			for(int x=1;x<4;x++){
				int t=getTile(x, y);
				if(t!=0){
					if(t==prev){
						return true;
					}else{
						prev=t;
					}
				}
			}
			int c=getTile(3, y)==0?0:0b1000;
			c+=getTile(2, y)==0?0:0b100;
			c+=getTile(1, y)==0?0:0b10;
			c+=getTile(0, y)==0?0:0b1;
			if(PATTERN[c]){
				return true;
			}
		}
		return false;
	}
	
	public boolean canUp(){
		for(int x=0;x<4;x++){
			int prev=getTile(x, 0);
			for(int y=1;y<4;y++){
				int t=getTile(x, y);
				if(t!=0){
					if(t==prev){
						return true;
					}else{
						prev=t;
					}
				}
			}
			int c=getTile(x, 0)==0?0:0b1000;
			c+=getTile(x, 1)==0?0:0b100;
			c+=getTile(x, 2)==0?0:0b10;
			c+=getTile(x, 3)==0?0:0b1;
			if(PATTERN[c]){
				return true;
			}
		}
		return false;
	}
	
	public boolean canDown(){
		for(int x=0;x<4;x++){
			int prev=getTile(x, 0);
			for(int y=1;y<4;y++){
				int t=getTile(x, y);
				if(t!=0){
					if(t==prev){
						return true;
					}else{
						prev=t;
					}
				}
			}
			int c=getTile(x, 3)==0?0:0b1000;
			c+=getTile(x, 2)==0?0:0b100;
			c+=getTile(x, 1)==0?0:0b10;
			c+=getTile(x, 0)==0?0:0b1;
			if(PATTERN[c]){
				return true;
			}
		}
		return false;
	}

	public boolean canDo(int index){
		switch(index){
		case UP:return canUp();
		case DOWN:return canDown();
		case LEFT:return canLeft();
		case RIGHT:return canRight();
		default:throw new UnsupportedOperationException();
		}
	}
	
	public void makeMove(int index){
		switch(index){
		case UP:up();break;
		case DOWN:down();break;
		case LEFT:left();break;
		case RIGHT:right();break;
		default:throw new UnsupportedOperationException();
		}
	}
	
	public List<Integer> getPossibleMoves(){
		possibleMove.clear();
		if(canUp())
			possibleMove.add(UP);
		if(canDown())
			possibleMove.add(DOWN);
		if(canLeft())
			possibleMove.add(LEFT);
		if(canRight())
			possibleMove.add(RIGHT);
		return possibleMove;
	}
	

	public void putTile(int x, int y, int i) {
		board[y][x]=i;
	}

	public int getTile(int x, int y){
		return board[y][x];
	}
	
	
}
