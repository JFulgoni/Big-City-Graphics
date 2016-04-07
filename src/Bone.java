import static org.lwjgl.opengl.GL11.GL_MODELVIEW_MATRIX;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glCallList;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glGetFloat;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.glu.Sphere;
import org.lwjgl.util.vector.Vector3f;

public class Bone {

	public int x, y;
	public float xa, ya, za, xb, yb, zb;
	public Vector3f joint;
	public boolean selected = false;
	public Vector3f transFromParent;
	public int name;
	public boolean drawKnuckle;
	public boolean isThumb;
	public boolean isWrist;

	public float effectorPos = 1; // by default

	public float xEndEffector;
	public float yEndEffector;
	public float xFrontEffector;
	public float yFrontEffector;

	public ArrayList<Bone> children;
	public Bone parent;

	public Boolean drawBall;

	Bone(Vector3f transFromParent, boolean drawKnuckle) {
		this.transFromParent = transFromParent;
		xa = 1;
		xb = -1;
		ya = 0.5f;
		yb = -0.5f;
		za = -0.25f;
		zb = 0.25f;
		joint = new Vector3f(0,0,0);
		children = new ArrayList<Bone>();
		parent = null;
		name = -1; //by default
		this.drawKnuckle = drawKnuckle;
		isThumb = false;
		isWrist = false;
		drawBall = false;
	}

	/*
        public void rotateWithMouse() {
			if (Mouse.isInsideWindow() && Mouse.isButtonDown(0)) {
				float mouseDX = Mouse.getX();
				float mouseDY = -Mouse.getY();

				//In relation to center of the screen
				//Not correct
				double deltaX = (mouseDX - 300);
				double deltaY = (mouseDY + 240);

				double rad = Math.atan2(deltaY, deltaX);

				float deg = (float) (rad * (180 / Math.PI));

				this.joint.z = -deg;
				//System.out.println(deg);
			}
		}
	 */
	/*
	public void rotateWithGuide(Guide guide){

		float gx = guide.getX();
		float gy = guide.getY();

		float ex = this.getXFrontEffector();
		float ey = this.getYFrontEffector();	

		double deltaX = (gx - ex);
		double deltaY = (gy - ey);

		double rad = Math.atan2(deltaY, deltaX);

		float deg = (float) (rad * (180 / Math.PI));

		this.joint.z = deg;

	}
	 */

	public void setAngle(float theta){
		this.joint.z = theta;
	}

	public float getAngle(){
		return this.joint.z;
	}

	public void addChild(Bone child){
		this.children.add(child);
		child.setParent(this);
	}

	public void setParent(Bone parent){
		this.parent = parent;
	}

	public Bone getParent(){
		return this.parent;
	}

	public ArrayList<Bone> getChildren(){
		return this.children;
	}

	public void incrementAngle(){
		this.joint.z = this.joint.z + 2;
	}

	public void decrementAngle(){
		this.joint.z = this.joint.z - 2;
	}

	public void setAlpha(float angle){
		this.joint.x = angle;
	}

	public void setName(int i){
		this.name = i;
	}
	public int getName(){
		return this.name;
	}

	public void setThumb(){
		this.isThumb = true;
	}

	public void setWrist(){
		this.isWrist = true;
	}
	public void setXDimension(float x1, float x2){
		this.xa = x1;
		this.xb = x2;
	}
	public void setYDimension(float x1, float x2){
		this.ya = x1;
		this.yb = x2;
	}
	public void setZDimension(float x1, float x2){
		this.za = x1;
		this.zb = x2;
	}

	public float getXfromParent(){
		return this.transFromParent.x;
	}
	public float getYfromParent(){
		return this.transFromParent.y;
	}
	public float getZfromParent(){
		return this.transFromParent.z;
	}

	public float getXEndEffector(){
		return this.xEndEffector;
	}

	public float getYEndEffector(){
		return this.yEndEffector;
	}

	public float getXFrontEffector(){
		return this.xFrontEffector;
	}

	public float getYFrontEffector(){
		return this.yFrontEffector;
	}

	public void drawBall(){
		this.drawBall = true;
	}

	public void draw() {
		glPushMatrix();
		glScalef(1, 0.75f, 1);
		glBegin(GL_QUADS);
		glColor3f(0.2f, 0.5f, 0.3f); // Set The Color To Green
		glVertex3f(xa, ya, za); // Top Right Of The Quad (Top)
		glVertex3f(xb, ya, za); // Top Left Of The Quad (Top)
		glVertex3f(xb, ya, zb); // Bottom Left Of The Quad (Top)
		glVertex3f(xa, ya, zb); // Bottom Right Of The Quad (Top)

		//glColor3f(1.0f, 0.5f, 0.0f); // Set The Color To Orange
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

		//glColor3f(0.0f, 0.0f, 0.8f); // Set The Color To Blue
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

		glPopMatrix();

		if(drawKnuckle){
			glPushMatrix();
			if(isWrist){
				glTranslatef(1f, 0, -3f);
				Sphere s = new Sphere();
				glColor3f(1.0f, 0.9f, 0.8f);
				s.draw(2f,16,16);
			}
			else{
				//glPushMatrix();
				glTranslatef(1f, 0, 0);
				Sphere s = new Sphere();
				glColor3f(1.0f, 0.9f, 0.8f);
				if(drawBall){
					//s.draw(0.75f,16,16);
					
					glPushMatrix();
					glRotatef(90,1,0,0);
					glRotatef(90,0,1,0);
					glTranslatef(0,-1.5f,1);
					glScalef(2,2,2);
					glCallList(Snake.listnum);
					glPopMatrix();
					//glPushMatrix();
					//glTranslatef(0,-1,0);
					//glRotatef(90,0,1,0);
					//glRotatef(-90,1,0,1);
					//glScalef(2,2,2);
					//Snake.drawHead();
					//glPopMatrix();
				}
				FloatBuffer modelview = BufferUtils.createFloatBuffer(16);
				glGetFloat(GL_MODELVIEW_MATRIX,modelview);

				float x = modelview.get(4*3);
				float y = modelview.get(4*3 + 1);

				this.xEndEffector = x;
				this.yEndEffector = y;
				//glPopMatrix();

				//glPushMatrix();
				glTranslatef(-2f, 0, 0);
				s = new Sphere();
				glColor3f(0.8f, 0.9f, 1.0f);

				//s.draw(0.75f,16,16);
				FloatBuffer modelview2 = BufferUtils.createFloatBuffer(16);
				glGetFloat(GL_MODELVIEW_MATRIX,modelview2);

				float x2 = modelview2.get(4*3);
				float y2 = modelview2.get(4*3 + 1);

				this.xFrontEffector = x2;
				this.yFrontEffector = y2;

				//glPopMatrix();
			}

			glPopMatrix();
		}
	}
}
