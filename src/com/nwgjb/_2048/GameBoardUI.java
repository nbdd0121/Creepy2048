package com.nwgjb._2048;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

public class GameBoardUI extends JComponent{
	private static final long serialVersionUID = 5872942232468416972L;

	TileUI[][] tiles=new TileUI[4][4];
	
	GameBoard board;
	
	Color[] color={
			new Color(204, 192, 179),
			new Color(0xEEE4DA),
			new Color(0xede0c8),
			new Color(0xf2b179),
			new Color(0xf59563)
	};
	
	static final Color FONT_COLOR=new Color(0x776E65);
	
	public GameBoardUI(GameBoard b){
		board=b;
		
		for(int x=0;x<4;x++){
			for(int y=0;y<4;y++){
				tiles[y][x]=new TileUI(new Tile(0));
				tiles[y][x].tilePos(x, y);
				add(tiles[y][x]);
			}
		}
		
	}
	
	public Dimension getPreferredSize(){
		return new Dimension(400, 400);
	}
	
	public void paintComponent(Graphics g){
		for(int x=0;x<4;x++){
			for(int y=0;y<4;y++){
				tiles[y][x].tile.pow=board.getTile(x, y);
			}
		}
		g.setColor(new Color(0xbbada0));
		g.fillRect(0, 0, 400, 400);
	}
}
