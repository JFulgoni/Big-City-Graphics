import static org.lwjgl.opengl.GL11.glCallList;
import static org.lwjgl.opengl.GL11.glLoadName;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import org.lwjgl.util.vector.Vector3f;



public class Snake {

	Bone chain1 = new Bone(new Vector3f(1.0f, 0.0f, 0.0f),true);
	Bone chain2 = new Bone(new Vector3f(1.0f, 0.0f, 0.0f),true);
	Bone chain3 = new Bone(new Vector3f(1.0f, 0.0f, 0.0f),true);
	Bone chain4 = new Bone(new Vector3f(1.0f, 0.0f, 0.0f),true);
	Bone chain5 = new Bone(new Vector3f(1.0f, 0.0f, 0.0f),true);
	
	public float movement;
	public float velocity;
	public float walkDist;
	public boolean turnAround = false;
	public boolean sideways = false;
	
	private float x;
	private float z;
	double theta = 0;
	
	static int listnum;
	
	public Snake(float x, float z, int listnum){
		chain1.addChild(chain2);
		chain2.addChild(chain3);
		chain3.addChild(chain4);
		chain4.addChild(chain5);
		
		chain5.drawBall();
		this.listnum = listnum;
		
		this.movement = 0.0f;
		this.velocity = -0.04f;
		this.walkDist = -15f;
		
		this.x = x;
		this.z = z;
	}
	
	public void theta1(float t){
		chain1.setAngle(t);
	}
	
	public void theta2(float t){
		chain2.setAngle(t);
	}
	
	public void theta3(float t){
		chain3.setAngle(t);
	}
	
	public void theta4(float t){
		chain4.setAngle(t);
	}
	
	public void draw(){
		this.slither();
		glPushMatrix();
		//drawHead();
		glRotatef(-90,1,0,0);
		drawBone(chain1);
		glPopMatrix();
	}
	
	public static void drawHead(){
		glPushMatrix();
		glTranslatef(4,0,0);
		glRotatef(90,0,1,0);
		glScalef(2,2,2);
		glCallList(listnum);
		glPopMatrix();
	}
	
	public void slither(){
		
		float rot = (float) (Math.cos(theta) * 10);
		
		theta = theta + 0.05f;
		if(theta > 360){
			theta = 0;
		}
		
		chain5.setAngle(rot);
		chain2.setAngle(-rot);
		chain3.setAngle(rot);
		chain4.setAngle(-rot);
		chain5.setAngle(rot);
	}
	
	
	private void drawBone(Bone bone){
		if(bone.isThumb){
			glRotatef(-30f, 0, 1, 0);
		}
		glTranslatef(bone.getXfromParent(), bone.getYfromParent(), bone.getZfromParent());
		glRotatef(bone.joint.x, 1, 0, 0);
		glRotatef(bone.joint.z,0,0,1);
		glTranslatef(bone.getXfromParent(), bone.getYfromParent(), bone.getZfromParent());

		/*
		if(bone.getName() == selectedBone){
			if(angleUp){
				bone.incrementAngle();
			}
			if(angleDown){
				bone.decrementAngle();
			}
		}
		*/

		glLoadName(bone.getName());
		bone.draw();
		//glPopName();


		for(Bone child : bone.getChildren()){
			glPushMatrix();
			drawBone(child);
			glPopMatrix();
		}
		//System.out.println(bone.getName());
	}
	
	public void highWalk(){
		glPushMatrix();
		glTranslatef(this.x, 0, this.z);

		if(this.sideways){
			glTranslatef(0, 0, this.getMovement());
			glRotatef(-90,0,1,0);
		}
		else{
			glTranslatef(this.getMovement(), 0, 0);
		}

		if(this.getWalk() > -25 && !this.getTurn()){
			glRotatef(180, 0, 1, 0);
			this.incMove();
			this.incWalk();
		}
		else if(this.getWalk() < 30){
			this.turnAround = true;
			//glRotatef(180, 0, 1, 0);
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
	
	public void setSideways(){
		this.sideways = true;
	}
}
