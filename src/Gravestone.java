import static org.lwjgl.opengl.GL11.glCallList;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;


public class Gravestone {
	
	public int listnum;
	
	private float x;
	private float z;
	
	public Gravestone(int listnum, float x, float z){
		this.listnum = listnum;
		this.x = x;
		this.z = z;
	}
	
	public void draw(){
		glPushMatrix();
		glScalef(0.75f, 0.75f, 0.75f);
		glTranslatef(this.x, -2, this.z);
		glCallList(this.listnum);
		glPopMatrix();
	}

	public boolean isNear(float px, float pz){
		double diff1 = Math.pow(this.x - px,2);
		double diff2 = Math.pow(this.z - pz,2);
		
		double dist = Math.sqrt(diff1 + diff2);
		
		if(dist < 8){
			return true;
		}
		else{
			return false;
		}
	}
}
