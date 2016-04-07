import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;

import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.Disk;
import org.lwjgl.util.glu.Sphere;
import org.lwjgl.util.vector.Vector3f;


public class Person {
	float xa;
	float xb;
	float ya;
	float yb;
	float za;
	float zb;

	public float bodyAngle;
	public float movement;
	public float velocity;
	public float walkDist;
	public boolean turnAround = false;
	public boolean sideways = false;
	public Vector3f color;
	public Vector3f start;

	public float testAngle;
	public float testDelta;

	public boolean drawHat;


	public Person(float movement, float velocity, float angleLim, Vector3f color){
		bodyAngle = 0;
		this.movement = movement;
		this.velocity = velocity;
		this.walkDist = angleLim;
		this.color = color;
		this.testAngle = 0;
		this.testDelta = 1.0f;
		this.drawHat = false;
	}

	/**
	 * Use this method to toggle the turn around boolean
	 */

	public void setStart(Vector3f start){
		this.start = start;
	}

	public void hat(){
		this.drawHat = true;
	}

	public void setSideways(){
		this.sideways = true;
	}

	public void toggleTurn(){
		if(this.turnAround == false){
			this.turnAround = true;
		}
		else{
			this.turnAround = false;
		}
	}

	/**
	 * Get methods
	 */
	public float getMovement(){
		return this.movement;
	}

	public float getWalk(){
		return this.walkDist;
	}

	public void incWalk(){
		this.walkDist = this.walkDist + this.velocity;
	}

	public void incMove(){
		this.movement = this.movement + this.velocity;
	}

	public void decWalk(){
		this.walkDist = this.walkDist - this.velocity;
	}

	public void decMove(){
		this.movement = this.movement - this.velocity;
	}

	public boolean getTurn(){
		return this.turnAround;
	}

	/**
	 * High level walk method
	 */

	public void highWalk(){
		glPushMatrix();
		glTranslatef(this.start.x, this.start.y, this.start.z);

		if(this.sideways){
			glTranslatef(this.getMovement(), 0, 0);
			glRotatef(-90,0,1,0);
		}
		else{
			glTranslatef(0, 0, this.getMovement());
		}

		if(this.getWalk() > -25 && !this.getTurn()){
			this.incMove();
			this.incWalk();
		}
		else if(this.getWalk() < 30){
			this.turnAround = true;
			glRotatef(180, 0, 1, 0);
			this.decMove();
			this.decWalk();
		}
		else if(this.getWalk() > 30){
			this.turnAround = false;
		}
		this.draw();
		glPopMatrix();
	}

	/**
	 * Draw method
	 */
	public void draw(){
		float r = color.x;
		float g = color.y;
		float b = color.z;

		glBegin(GL_QUADS); // Start Drawing The Cube

		//TORSO
		//FOR Y AXIS, ALL -1S BECOME 0S

		glColor3f(r, g, b); // Set The Color

		glVertex3f(0.4f, 1.0f, -0.2f); // Top Right Of The Quad (Top)
		glVertex3f(-0.4f, 1.0f, -0.2f); // Top Left Of The Quad (Top)
		glVertex3f(-0.4f, 1.0f, 0.2f); // Bottom Left Of The Quad (Top)
		glVertex3f(0.4f, 1.0f, 0.2f); // Bottom Right Of The Quad (Top)

		//glColor3f(1.0f, 0.4f, 0.0f); // Set The Color To Orange
		glVertex3f(0.4f, -0.25f, 0.2f); // Top Right Of The Quad (Bottom)
		glVertex3f(-0.4f, -0.25f, 0.2f); // Top Left Of The Quad (Bottom)
		glVertex3f(-0.4f, -0.25f, -0.2f); // Bottom Left Of The Quad (Bottom)
		glVertex3f(0.4f, -0.25f, -0.2f); // Bottom Right Of The Quad (Bottom)

		//glColor3f(1.0f, 0.0f, 0.0f); // Set The Color To Red
		glVertex3f(0.4f, 1.0f, 0.2f); // Top Right Of The Quad (Front)
		glVertex3f(-0.4f, 1.0f, 0.2f); // Top Left Of The Quad (Front)
		glVertex3f(-0.4f, -0.25f, 0.2f); // Bottom Left Of The Quad (Front)
		glVertex3f(0.4f, -0.25f, 0.2f); // Bottom Right Of The Quad (Front)

		////glColor3f(1.0f, 1.0f, 0.0f); // Set The Color To Yellow
		glVertex3f(0.4f, -0.25f, -0.2f); // Bottom Left Of The Quad (Back)
		glVertex3f(-0.4f, -0.25f, -0.2f); // Bottom Right Of The Quad (Back)
		glVertex3f(-0.4f, 1.0f, -0.2f); // Top Right Of The Quad (Back)
		glVertex3f(0.4f, 1.0f, -0.2f); // Top Left Of The Quad (Back)

		//glColor3f(0.0f, 0.0f, 0.2f); // Set The Color To Blue
		glVertex3f(-0.4f, 1.0f, 0.2f); // Top Right Of The Quad (Left)
		glVertex3f(-0.4f, 1.0f, -0.2f); // Top Left Of The Quad (Left)
		glVertex3f(-0.4f, -0.25f, -0.2f); // Bottom Left Of The Quad (Left)
		glVertex3f(-0.4f, -0.25f, 0.2f); // Bottom Right Of The Quad (Left)

		//glColor3f(1.0f, 0.0f, 1.0f); // Set The Color To Violet
		glVertex3f(0.4f, 1.0f, -0.2f); // Top Right Of The Quad (Right)
		glVertex3f(0.4f, 1.0f, 0.2f); // Top Left Of The Quad (Right)
		glVertex3f(0.4f, -0.25f, 0.2f); // Bottom Left Of The Quad (Right)
		glVertex3f(0.4f, -0.25f, -0.2f); // Bottom Right Of The Quad (Right)

		//LEFT LEG
		//FOR Y, ALL 1'S BECOME 0S, ALL 0S BECOME -1S

		//glColor3f(0.0f, 1.0f, 0.0f); // Set The Color To Green
		glVertex3f(-0.1F, -0.25f, -0.2f); // Top Right Of The Quad (Top)
		glVertex3f(-0.3F, -0.25f, -0.2f); // Top Left Of The Quad (Top)
		glVertex3f(-0.3F, -0.25f, 0.2f); // Bottom Left Of The Quad (Top)
		glVertex3f(-0.1F, -0.25f, 0.2f); // Bottom Right Of The Quad (Top)

		//glColor3f(1.0f, 0.0f, 0.0f); // Set The Color To Orange
		glVertex3f(-0.1F, -1.0f, 0.2f); // Top Right Of The Quad (Bottom)
		glVertex3f(-0.3F, -1.0f, 0.2f); // Top Left Of The Quad (Bottom)
		glVertex3f(-0.3F, -1.0f, -0.2f); // Bottom Left Of The Quad (Bottom)
		glVertex3f(-0.1F, -1.0f, -0.2f); // Bottom Right Of The Quad (Bottom)

		//glColor3f(1.0f, 0.0f, 0.0f); // Set The Color To Red
		glVertex3f(-0.1F, -0.25f, 0.2f); // Top Right Of The Quad (Front)
		glVertex3f(-0.3F, -0.25f, 0.2f); // Top Left Of The Quad (Front)
		glVertex3f(-0.3F, -1.0f, 0.2f); // Bottom Left Of The Quad (Front)
		glVertex3f(-0.1F, -1.0f, 0.2f); // Bottom Right Of The Quad (Front)

		//glColor3f(1.0f, 1.0f, 0.0f); // Set The Color To Yellow
		glVertex3f(-0.1F, -1.0f, -0.2f); // Bottom Left Of The Quad (Back)
		glVertex3f(-0.3F, -1.0f, -0.2f); // Bottom Right Of The Quad (Back)
		glVertex3f(-0.3F, -0.25f, -0.2f); // Top Right Of The Quad (Back)
		glVertex3f(-0.1F, -0.25f, -0.2f); // Top Left Of The Quad (Back)

		//glColor3f(0.0f, 0.0f, 0.2f); // Set The Color To Blue
		glVertex3f(-0.3F, -0.25f, 0.2f); // Top Right Of The Quad (Left)
		glVertex3f(-0.3F, -0.25f, -0.2f); // Top Left Of The Quad (Left)
		glVertex3f(-0.3F, -1.0f, -0.2f); // Bottom Left Of The Quad (Left)
		glVertex3f(-0.3F, -1.0f, 0.2f); // Bottom Right Of The Quad (Left)

		//glColor3f(1.0f, 0.0f, 1.0f); // Set The Color To Violet
		glVertex3f(-0.1F, -0.25f, -0.2f); // Top Right Of The Quad (Right)
		glVertex3f(-0.1F, -0.25f, 0.2f); // Top Left Of The Quad (Right)
		glVertex3f(-0.1F, -1.0f, 0.2f); // Bottom Left Of The Quad (Right)
		glVertex3f(-0.1F, -1.0f, -0.2f); // Bottom Right Of The Quad (Right)


		//RIGHT LEG
		//glColor3f(0.0f, 1.0f, 0.0f); // Set The Color To Green
		glVertex3f(0.3f, -0.25f, -0.2f); // Top Right Of The Quad (Top)
		glVertex3f(0.1f, -0.25f, -0.2f); // Top Left Of The Quad (Top)
		glVertex3f(0.1f, -0.25f, 0.2f); // Bottom Left Of The Quad (Top)
		glVertex3f(0.3f, -0.25f, 0.2f); // Bottom Right Of The Quad (Top)

		//glColor3f(1.0f, 0.0f, 0.0f); // Set The Color To Orange
		glVertex3f(0.3f, -1.0f, 0.2f); // Top Right Of The Quad (Bottom)
		glVertex3f(0.1f, -1.0f, 0.2f); // Top Left Of The Quad (Bottom)
		glVertex3f(0.1f, -1.0f, -0.2f); // Bottom Left Of The Quad (Bottom)
		glVertex3f(0.3f, -1.0f, -0.2f); // Bottom Right Of The Quad (Bottom)

		//glColor3f(1.0f, 0.0f, 0.0f); // Set The Color To Red
		glVertex3f(0.3f, -0.25f, 0.2f); // Top Right Of The Quad (Front)
		glVertex3f(0.1f, -0.25f, 0.2f); // Top Left Of The Quad (Front)
		glVertex3f(0.1f, -1.0f, 0.2f); // Bottom Left Of The Quad (Front)
		glVertex3f(0.3f, -1.0f, 0.2f); // Bottom Right Of The Quad (Front)

		//glColor3f(1.0f, 1.0f, 0.0f); // Set The Color To Yellow
		glVertex3f(0.3f, -1.0f, -0.2f); // Bottom Left Of The Quad (Back)
		glVertex3f(0.1f, -1.0f, -0.2f); // Bottom Right Of The Quad (Back)
		glVertex3f(0.1f, -0.25f, -0.2f); // Top Right Of The Quad (Back)
		glVertex3f(0.3f, -0.25f, -0.2f); // Top Left Of The Quad (Back)

		//glColor3f(0.0f, 0.0f, 0.2f); // Set The Color To Blue
		glVertex3f(0.1f, -0.25f, 0.2f); // Top Right Of The Quad (Left)
		glVertex3f(0.1f, -0.25f, -0.2f); // Top Left Of The Quad (Left)
		glVertex3f(0.1f, -1.0f, -0.2f); // Bottom Left Of The Quad (Left)
		glVertex3f(0.1f, -1.0f, 0.2f); // Bottom Right Of The Quad (Left)

		//glColor3f(1.0f, 0.0f, 1.0f); // Set The Color To Violet
		glVertex3f(0.3f, -0.25f, -0.2f); // Top Right Of The Quad (Right)
		glVertex3f(0.3f, -0.25f, 0.2f); // Top Left Of The Quad (Right)
		glVertex3f(0.3f, -1.0f, 0.2f); // Bottom Left Of The Quad (Right)
		glVertex3f(0.3f, -1.0f, -0.2f); // Bottom Right Of The Quad (Right)


		//LEFT ARM
		xa = -0.4f;
		xb = -0.6f;
		ya = 1.0f;
		yb = 0.1f;
		za = -0.15f;
		zb = 0.15f;
		//glColor3f(0.0f, 1.0f, 0.0f); // Set The Color To Green
		glVertex3f(xa, ya, za); // Top Right Of The Quad (Top)
		glVertex3f(xb, ya, za); // Top Left Of The Quad (Top)
		glVertex3f(xb, ya, zb); // Bottom Left Of The Quad (Top)
		glVertex3f(xa, ya, zb); // Bottom Right Of The Quad (Top)

		//glColor3f(1.0f, 0.0f, 0.0f); // Set The Color To Orange
		glVertex3f(xa, yb, zb); // Top Right Of The Quad (Bottom)
		glVertex3f(xb, yb, zb); // Top Left Of The Quad (Bottom)
		glVertex3f(xb, yb, za); // Bottom Left Of The Quad (Bottom)
		glVertex3f(xa, yb, za); // Bottom Right Of The Quad (Bottom)

		//glColor3f(1.0f, 0.0f, 0.0f); // Set The Color To Red
		glVertex3f(xa, ya, zb); // Top Right Of The Quad (Front)
		glVertex3f(xb, ya, zb); // Top Left Of The Quad (Front)
		glVertex3f(xb, yb, zb); // Bottom Left Of The Quad (Front)
		glVertex3f(xa, yb, zb); // Bottom Right Of The Quad (Front)

		//glColor3f(1.0f, 1.0f, 0.0f); // Set The Color To Yellow
		glVertex3f(xa, yb, za); // Bottom Left Of The Quad (Back)
		glVertex3f(xb, yb, za); // Bottom Right Of The Quad (Back)
		glVertex3f(xb, ya, za); // Top Right Of The Quad (Back)
		glVertex3f(xa, ya, za); // Top Left Of The Quad (Back)

		//glColor3f(0.0f, 0.0f, 0.2f); // Set The Color To Blue
		glVertex3f(xb, ya, zb); // Top Right Of The Quad (Left)
		glVertex3f(xb, ya, za); // Top Left Of The Quad (Left)
		glVertex3f(xb, yb, za); // Bottom Left Of The Quad (Left)
		glVertex3f(xb, yb, zb); // Bottom Right Of The Quad (Left)

		//glColor3f(1.0f, 0.0f, 1.0f); // Set The Color To Violet
		glVertex3f(xa, ya, za); // Top Right Of The Quad (Right)
		glVertex3f(xa, ya, zb); // Top Left Of The Quad (Right)
		glVertex3f(xa, yb, zb); // Bottom Left Of The Quad (Right)
		glVertex3f(xa, yb, za); // Bottom Right Of The Quad (Right)

		glEnd();

		
		drawRightArm();

		//head
		glPushMatrix();
		glTranslatef(0, 1.4f, 0);
		Sphere s = new Sphere();
		glColor3f(1.0f, 0.9f, 0.8f);
		s.draw(0.4f,16,16);
		if(drawHat){
			Cylinder c = new Cylinder();
			Disk d = new Disk();
			Disk d2 = new Disk();
			glColor3f(r,g,b);
			glPushMatrix();
			glTranslatef(0, 0.25f, 0);
			glRotatef(-90,1,0,0);
			d.draw(0, 0.5f, 10, 1);
			c.draw(0.3f, 0.3f, 0.3f, 16, 16);
			glPopMatrix();
			glTranslatef(0, 0.55f, 0);
			glRotatef(-90,1,0,0);
			//glTranslatef(0, 0.3f, 0);
			d2.draw(0, 0.3f, 10, 1);
		}
		glPopMatrix();
		
		if(drawHat){
			glColor3f(0.7f, 0.3f, 0.2f);
			glBegin(GL_QUADS);
			glVertex3f(-0.2f, 1.3f, -0.4f); // Bottom Left Of The Quad (Back)
			glVertex3f(0.2f, 1.3f, -0.4f); // Bottom Right Of The Quad (Back)
			glVertex3f(0.1f, 1.1f, -0.4f); // Top Right Of The Quad (Back)
			glVertex3f(-0.1f, 1.1f, -0.4f); // Top Right Of The Quad (Back)
			glEnd();
		}

		//left hand
		glPushMatrix();
		glTranslatef(-0.5f, 0, 0);
		s = new Sphere();
		glColor3f(1.0f, 0.9f, 0.8f);
		s.draw(0.1f,16,16);
		glPopMatrix();

		//right hand
		
		glPushMatrix();
		glTranslatef(0.5f, 0, 0);
		s = new Sphere();
		glColor3f(1.0f, 0.9f, 0.8f);
		s.draw(0.1f,16,16);
		glPopMatrix();
		
		//right foot
		glPushMatrix();
		glTranslatef(0.2f, -1f, -0.2f);
		s = new Sphere();
		glColor3f(1.0f, 0.9f, 0.8f);
		s.draw(0.1f,16,16);
		glPopMatrix();

		//left foot
		glPushMatrix();
		glTranslatef(-0.2f, -1f, -0.2f);
		s = new Sphere();
		glColor3f(1.0f, 0.9f, 0.8f);
		s.draw(0.1f,16,16);
		glPopMatrix();


	}
	
	public void drawRightArm(){
		glPushMatrix();
		//glTranslatef(-0.5f, 0, 0);
		//glRotatef(60, 1, 0, 0);
		//glTranslatef(-0.5f, 0, 0f);
		glBegin(GL_QUADS);
		//RIGHT ARM
		xa = 0.6f;
		xb = 0.4f;
		ya = 1.0f;
		yb = 0.1f;
		za = -0.15f;
		zb = 0.15f;

		//glColor3f(0.0f, 1.0f, 0.0f); // Set The Color To Green
		glVertex3f(xa, ya, za); // Top Right Of The Quad (Top)
		glVertex3f(xb, ya, za); // Top Left Of The Quad (Top)
		glVertex3f(xb, ya, zb); // Bottom Left Of The Quad (Top)
		glVertex3f(xa, ya, zb); // Bottom Right Of The Quad (Top)

		//glColor3f(1.0f, 0.0f, 0.0f); // Set The Color To Orange
		glVertex3f(xa, yb, zb); // Top Right Of The Quad (Bottom)
		glVertex3f(xb, yb, zb); // Top Left Of The Quad (Bottom)
		glVertex3f(xb, yb, za); // Bottom Left Of The Quad (Bottom)
		glVertex3f(xa, yb, za); // Bottom Right Of The Quad (Bottom)

		//glColor3f(1.0f, 0.0f, 0.0f); // Set The Color To Red
		glVertex3f(xa, ya, zb); // Top Right Of The Quad (Front)
		glVertex3f(xb, ya, zb); // Top Left Of The Quad (Front)
		glVertex3f(xb, yb, zb); // Bottom Left Of The Quad (Front)
		glVertex3f(xa, yb, zb); // Bottom Right Of The Quad (Front)

		//glColor3f(1.0f, 1.0f, 0.0f); // Set The Color To Yellow
		glVertex3f(xa, yb, za); // Bottom Left Of The Quad (Back)
		glVertex3f(xb, yb, za); // Bottom Right Of The Quad (Back)
		glVertex3f(xb, ya, za); // Top Right Of The Quad (Back)
		glVertex3f(xa, ya, za); // Top Left Of The Quad (Back)

		//glColor3f(0.0f, 0.0f, 0.2f); // Set The Color To Blue
		glVertex3f(xb, ya, zb); // Top Right Of The Quad (Left)
		glVertex3f(xb, ya, za); // Top Left Of The Quad (Left)
		glVertex3f(xb, yb, za); // Bottom Left Of The Quad (Left)
		glVertex3f(xb, yb, zb); // Bottom Right Of The Quad (Left)

		//glColor3f(1.0f, 0.0f, 1.0f); // Set The Color To Violet
		glVertex3f(xa, ya, za); // Top Right Of The Quad (Right)
		glVertex3f(xa, ya, zb); // Top Left Of The Quad (Right)
		glVertex3f(xa, yb, zb); // Bottom Left Of The Quad (Right)
		glVertex3f(xa, yb, za); // Bottom Right Of The Quad (Right)

		glEnd(); // Done Drawing The Quad
		

		/*
		glTranslatef(0.5f, 0, 0);
		Sphere s = new Sphere();
		glColor3f(1.0f, 0.9f, 0.8f);
		s.draw(0.1f,16,16);
		*/
		glPopMatrix();
	}

	public void spin(){
		glPushMatrix();
		glTranslatef(-5.5f,0,-10);
		if(this.testAngle >= 270){
			glRotatef(270, 0, 1, 0);
		}
		else if (this.testAngle >= 180){
			glRotatef(180, 0, 1, 0);
		}
		else if (this.testAngle >= 90){
			glRotatef(90, 0, 1, 0);
		}
		// System.out.println(testAngle);
		this.testAngle = this.testAngle + this.testDelta;

		if(this.testAngle > 360){
			this.testAngle = 0;
		}
		this.draw();
		glPopMatrix();
	}


}
