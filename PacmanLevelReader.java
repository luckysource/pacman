package pacman;

import java.awt.Color;

import pacman.Tile.TileType;
import tools.LevelReader;

public class PacmanLevelReader extends LevelReader{
	
	static Color levelImg [][] = levelRead("D:/workspace/WebTutorials/src/tools/level1.png");
	static Tile level [][];
	
	/**
	 * Converts RGB values of a map image to a type of Tile
	 * @return
	 */
	public static Tile [][] pacLevelRead(){	
		
		if (levelImg !=null){
			for (int i=0; i<levelImg.length; i++){
				for (int j=0; j<levelImg[0].length; j++){					
					
					  if(levelImg[i][j].getRed() == 0
						      && levelImg[i][j].getGreen() == 0
						       && levelImg[i][j].getBlue() == 0) {
						  level[i][j] = new Tile(TileType.WALL);
					  }
					  else if(levelImg[i][j].getRed() == 255
						      && levelImg[i][j].getGreen() == 255
						       && levelImg[i][j].getBlue() == 0) {
						  level[i][j] = new Tile(TileType.POINT);
					  }
					  else if(levelImg[i][j].getRed() == 255
						      && levelImg[i][j].getGreen() == 0
						       && levelImg[i][j].getBlue() == 0) {
						  level[i][j] = new Tile(TileType.BIGPOINT);
					  }
					  else if(levelImg[i][j].getRed() == 255
						      && levelImg[i][j].getGreen() == 255
						       && levelImg[i][j].getBlue() == 255) {
						  level[i][j] = new Tile(TileType.PATH);
					  }
					  else if(levelImg[i][j].getRed() == 0
						      && levelImg[i][j].getGreen() == 0
						       && levelImg[i][j].getBlue() == 255) {
						  level[i][j] = new Tile(TileType.TELEPORT);
					  }					
				}
			}
		}
		
		return level;
	}
}
