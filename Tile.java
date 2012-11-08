package pacman;

public class Tile {

	enum TileType  { WALL, POINT, BIGPOINT, PATH, TELEPORT }; // points -> path when eaten
	private TileType tileType;
	
	public Tile(TileType tileType){
		this.tileType = tileType;
	}
	
	public void setTile(TileType tileType){
		this.tileType = tileType;		
	}
	
	public TileType getTile(){
		return this.tileType;
	}
}