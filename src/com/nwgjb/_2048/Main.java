package com.nwgjb._2048;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Main extends JFrame{
	
	GameBoard board=new GameBoard();
	boolean stop=true;
	
	Computer comp=new EasyComputer();
	
	public Main() {
		final Agent agent=new MonteCarloAI();
		
		setFocusable(true);
		
		JPanel topPanel=new JPanel();
		final JButton hint=new JButton("Hint");
		hint.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int action=agent.makeMove(board);
				hint.setText("Hint: "+GameBoard.NAMES[action]);
				requestFocus();
			}
		});
		topPanel.add(hint);
		
		final JButton ai=new JButton("Auto");
		ai.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(stop){
					stop=false;
					new Thread(new Runnable(){
						@Override
						public void run() {
							while(!stop){
								/*try {
									Thread.sleep(500);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}*/
								int r=agent.makeMove(board);
								board.makeMove(r);
								comp.makeMove(board);
								//board.generateRandomly();
								repaint();
							}
						}
					}).start();
					hint.setEnabled(false);
				}else{
					stop=true;
					requestFocus();
					hint.setEnabled(true);
				}
			}
		});
		topPanel.add(hint);
		topPanel.add(ai);
		
		
		addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e) {
				int action=-1;
				switch(e.getKeyCode()){
					case KeyEvent.VK_LEFT:
						action=GameBoard.LEFT;
						break;
					case KeyEvent.VK_RIGHT:
						action=GameBoard.RIGHT;
						break;
					case KeyEvent.VK_UP:
						action=GameBoard.UP;
						break;
					case KeyEvent.VK_DOWN:
						action=GameBoard.DOWN;
						break;
					default:
						return;
				}
				if(board.canDo(action)){
					board.makeMove(action);
					//board.generateRandomly();
					comp.makeMove(board);
					hint.setText("Hint");
				}
				repaint();
			}
		});
		
		add(topPanel, BorderLayout.NORTH);
		add(new GameBoardUI(board));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		pack();

	}
	
	public void paint(Graphics g){
		setTitle("2048 - Score "+board.score);
		super.paint(g);
	}
	
	public static void main(String[] args){
		new Main();
	}
}
