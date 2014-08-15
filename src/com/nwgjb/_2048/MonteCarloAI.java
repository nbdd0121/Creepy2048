package com.nwgjb._2048;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class MonteCarloAI implements Agent{

	Random rand=new Random();
	
	int roundPlayed=0;
	int searchDepth=100;
	int simulation=1024;
	
	static ExecutorService pool=Executors.newFixedThreadPool(4);
	
	Future<Integer> createTask(final GameBoard b, final int move){
		return pool.submit(new Callable<Integer>(){
			@Override
			public Integer call() throws Exception {
				return evalMove(b, move);
			}
		});
	}
	
	@Override
	public synchronized int makeMove(final GameBoard b) {
		List<Integer> moves=b.getPossibleMoves();
		int best=-1;
		int bestScore=-1;
		ArrayList<ArrayList<Future<Integer>>> futures=new ArrayList<>();
		for(int i:moves){
			ArrayList<Future<Integer>> list=new ArrayList<>();
			list.add(createTask(b, i));
			list.add(createTask(b, i));
			list.add(createTask(b, i));
			list.add(createTask(b, i));
			futures.add(list);
		}
		int id=0;
		for(int i:moves){
			ArrayList<Future<Integer>> arr=futures.get(id++);
			int score=0;
			for(Future<Integer> f:arr){
				try {
					score+=f.get();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
			if(score>bestScore){
				bestScore=score;
				best=i;
			}
		}
		/*for(final int i:moves){
			Future<Integer> r1=createTask(b, i);
			Future<Integer> r2=createTask(b, i);
			Future<Integer> r3=createTask(b, i);
			int s=evalMove(b, i);
			try {
				s+=r1.get()+r2.get()+r3.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			if(s>bestScore){
				bestScore=s;
				best=i;
			}
		}*/
		roundPlayed++;
		searchDepth=roundPlayed/10+100;
	    simulation=roundPlayed/16+1024;
		return best;
	}

	private int evalMove(GameBoard b, int move) {
		GameBoard movedBoard=new GameBoard(b);
		movedBoard.makeMove(move);
		movedBoard.generateRandomly();
		GameBoard board=new GameBoard();
		int c=0;
		for(int i=0;i<simulation;i++){
			int sepC=0;
			board.copyFrom(movedBoard);
			List<Integer> possibleMoves;
			while(sepC<searchDepth&&!(possibleMoves=board.getPossibleMoves()).isEmpty()){
				board.makeMove(possibleMoves.get(rand.nextInt(possibleMoves.size())));
				board.generateRandomly();
				sepC++;
			}
			c+=sepC==searchDepth?searchDepth*1.5:sepC;
		}
		return c;
	}

}
