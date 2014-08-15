package com.nwgjb._2048;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EasyComputer implements Computer {

	Random rand = new Random();

	static ExecutorService pool = Executors.newFixedThreadPool(4);

	Future<Integer> createTask(final GameBoard b) {
		return pool.submit(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				return evalBoard(b);
			}
		});
	}

	@Override
	public void makeMove(final GameBoard b) {
		b.getSpareSpace();
		ArrayList<Future<Integer>> list2 = new ArrayList<>();
		ArrayList<Future<Integer>> list4 = new ArrayList<>();
		for (int i = 0; i < b.xl.size(); i++) {
			int x = b.xl.get(i);
			int y = b.yl.get(i);
			GameBoard brd = new GameBoard(b);
			brd.putTile(x, y, 1);
			list2.add(createTask(brd));
			brd = new GameBoard(b);
			brd.putTile(x, y, 2);
			list4.add(createTask(brd));
		}
		int min = Integer.MAX_VALUE;
		int x = -1;
		int y = -1;
		int v = 0;
		try {
			for (int i = 0; i < list2.size(); i++) {
				int ret = list2.get(i).get();
				if (ret < min) {
					min = ret;
					x = b.xl.get(i);
					y = b.yl.get(i);
					v = 1;
				}
			}
			for (int i = 0; i < list4.size(); i++) {
				int ret = list4.get(i).get();
				if (ret < min) {
					min = ret;
					x = b.xl.get(i);
					y = b.yl.get(i);
					v = 2;
				}
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		b.putTile(x, y, v);
	}

	private int evalBoard(GameBoard movedBoard) {
		GameBoard board = new GameBoard();
		int c = 0;
		for (int i = 0; i < 512; i++) {
			int sepC = 0;
			board.copyFrom(movedBoard);
			List<Integer> possibleMoves;
			while (sepC < 30
					&& !(possibleMoves = board.getPossibleMoves()).isEmpty()) {
				board.makeMove(possibleMoves.get(rand.nextInt(possibleMoves
						.size())));
				board.generateRandomly();
				sepC++;
			}
			c += sepC == 30 ? 30 * 1.5 : sepC;
		}
		return -c;
	}

}
