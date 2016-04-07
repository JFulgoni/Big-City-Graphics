import static org.lwjgl.opengl.GL11.glCallList;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;


public class Police {
	
	public float turnAngle;
	public int listnum;
	
	public float positionZ;
	public float positionX;
	public float velocity;
	
	//public int driveCycle;
	public boolean turn1;
	public boolean turn2;
	public boolean turn3;
	
	public Police(int listnum){
		turnAngle = -90f;
		//235 = north
		//48 = south
		
		positionZ = -2f;
		positionX = -1f;
		this.listnum = listnum;
		
		velocity = 0.1f;
		
		turn1 = false;
		turn2 = false;
		turn3 = false;
	}
	
	public void draw(){
		glPushMatrix();
		glScalef(1.2f, 1.2f, 1.2f);
		glTranslatef(this.positionX, -1.8f, this.positionZ);
		glRotatef(turnAngle, 0, 1, 0);
		glCallList(this.listnum);
		glPopMatrix();
	}

	
	public void cruise(){

		float x = this.positionX;
		float z = this.positionZ;
		
		if(z < 30 && !turn1){ //south
			this.positionX = -1;
			turnAngle = 270;
			this.positionZ = this.positionZ + this.velocity;
		}
		else if(x < 50 && !turn2){ //east
			this.positionZ = 40;
			turn1 = true;
			turnAngle = 0;
			this.positionX = this.positionX + this.velocity;
		}
		else if(z > -30 && !turn3){ //north
			this.positionX = 60;
			turn2 = true;
			turnAngle = 90;
			this.positionZ = this.positionZ - this.velocity;
		}
		else if(x > 10){ //west
			this.positionZ = -30;
			turn3 = true;
			turnAngle = 180;
			this.positionX = this.positionX - this.velocity;
		}
		else{
			turn1 = false;
			turn2 = false;
			turn3 = false;
		}
				
		this.draw();
	}

}
