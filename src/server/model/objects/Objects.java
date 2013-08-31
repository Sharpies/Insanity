package server.model.objects;


public class Objects {

	public long delay, oDelay;
	public int xp, item, owner, target, times;
	public boolean bait;
	
	public static int objectId;
	public static int objectX;
	public static int objectY;
	public int objectHeight;
	public int objectFace;
	public int objectType;
	public int objectTicks;
	
	public static int getObjectId() {
		return objectId;
	}
	
	public static int getObjectX() {
		return objectX;
	}
	
	public static int getObjectY() {
		return objectY;
	}
	
	
	public Objects(int id, int x, int y, int height, int face, int type, int ticks) {
		Objects.objectId = id;
		Objects.objectX = x;
		Objects.objectY = y;
		this.objectHeight = height;
		this.objectFace = face;
		this.objectType = type;
		this.objectTicks = ticks;
	}
	

	
	
	public int getObjectHeight() {
		return this.objectHeight;
	}
	
	public int getObjectFace() {
		return this.objectFace;
	}
	
	public int getObjectType() {
		return this.objectType;
	}
	
	
}