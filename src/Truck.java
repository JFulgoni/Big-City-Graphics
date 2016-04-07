import static org.lwjgl.opengl.GL11.*;


public class Truck {
	
	public float turnAngle;
	public int listnum;
	
	public float positionZ;
	public float positionX;
	public float velocity;
	
	//public int driveCycle;
	public boolean turn1;
	public boolean turn2;
	public boolean turn3;
	
	public Truck(int listnum){
		turnAngle = 235f;
		//235 = north
		//48 = south
		
		positionZ = 20f;
		positionX = 3f;
		this.listnum = listnum;
		
		velocity = 0.1f;
		
		turn1 = false;
		turn2 = false;
		turn3 = false;
	}
	
	public void draw(){
		glPushMatrix();
		glScalef(1.2f, 1.2f, 1.2f);
		glTranslatef(this.positionX, -1f, this.positionZ);
		glRotatef(turnAngle, 0, 1, 0);
		glCallList(this.listnum);
		glPopMatrix();
	}

	//235 for north
	//50 for south
	//140 for east
	public void driveNorth(){
		this.positionZ = this.positionZ - this.velocity;
	}
	
	public void driveSouth(){
		this.positionZ = this.positionZ + this.velocity;
	}
	
	public void driveEast(){
		
	}
	
	public void cruise(){

		float x = this.positionX;
		float z = this.positionZ;
		
		if(z > -30 && !turn1){
			turnAngle = 235;
			this.positionZ = this.positionZ - this.velocity;
		}
		else if(x > -60 && !turn2){
			turn1 = true;
			turnAngle = 320;
			this.positionX = this.positionX - this.velocity;	
		}
		else if(z < 44 && !turn3){
			turn2 = true;
			turnAngle = 50;
			this.positionZ = this.positionZ + this.velocity;
		}
		else if(x < 3){
			turn2 = true;
			turnAngle = 140;
			this.positionX = this.positionX + this.velocity;
		}		
		else{
			turn1 = false;
			turn2 = false;
			turn3 = false;
		}
		
				
		this.draw();
	}
}
