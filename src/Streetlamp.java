import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.Sphere;
import org.lwjgl.util.vector.Vector3f;


public class Streetlamp {
	
	Cylinder c;
	Sphere s;
	
	private Vector3f location;
	
	public Streetlamp(float x, float y, float z){
		this.location = new Vector3f(x, y, z);
		c = new Cylinder();
		s = new Sphere();
	}
	
	public void setLocationZ(float z){
		this.location.z = z;
	}
	
	public void setLocationX(float x){
		this.location.x = x;
	}
	
	public float getLocationZ(){
		return this.location.z;
	}
	
	public float getLocationX(){
		return this.location.x;
	}
	
	
	public void draw(){
		glPushMatrix();
		glTranslatef(this.location.x, this.location.y,this.location.z);
		
		glPushMatrix();
		/*
		glTranslatef(0, 4, 0);
		glColor3f(0.2f, 0.2f, 0.2f); 
		c.draw(0.2f, 0.2f, 2.5f, 16, 16);
		s.draw(0.25f,16,16);
		*/
		
		glTranslatef(0, 4f, 0);
		glColor3f(1.0f, 0.95f, 0.5f);
		s.draw(0.5f,16,16);
		
		glTranslatef(0, 0.25f, 0);
		glRotatef(-90,1,0,0);
		glColor3f(0.2f, 0.2f, 0.2f);
		c.draw(0.5f, 0.3f, 0.5f, 16, 16);
		
		glPopMatrix();
		
		//glPushMatrix();
		glRotatef(-90,1,0,0);
		//base, top, height, slices, stacks
		glColor3f(0.2f, 0.2f, 0.2f); 
		c.draw(0.2f, 0.2f, 4, 16, 16);
		//glPopMatrix();
		
		glPopMatrix();
	}
	
	/*
	public void rotate(){
		glPushMatrix();
		glTranslatef(-this.location.x, -this.location.y,-this.location.z);
		glRotatef(this.getTheta(), 1, 0, 0);
		glTranslatef(-this.location.x, -this.location.y,-this.location.z);
		glPopMatrix();
	}
	*/

}
