import static org.lwjgl.opengl.GL11.glCallList;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotated;
import static org.lwjgl.opengl.GL11.glTranslated;

public class Blackhawk {
	
	public int listnum;
	
	public double z;
	public double x;
	public double y;
	public double theta;
	
	public Blackhawk(int listnum){
		this.listnum = listnum;
		this.x = 0;
		this.y = 25;
		this.z = 0;
		this.theta = 0;
	}
	
	public void fly(){
		this.x = Math.sin(theta) * 150;
		this.z = Math.cos(theta) * 150;
		theta = theta + 0.005;
		if(theta > 360){
			theta = 0;
		}
		
		this.draw();		
	}
	
	
	public void draw(){
		glPushMatrix();
		//glRotated(-90, 0, 1, 0);
		glTranslated(this.x, this.y, this.z);
		glCallList(this.listnum);
		glPopMatrix();
	}

}
