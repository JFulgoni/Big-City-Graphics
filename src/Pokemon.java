import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Random;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Quadric;
import org.lwjgl.util.glu.Sphere;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;

public class Pokemon {

	String windowTitle = "The Big City";
	public boolean closeRequested = false;

	long lastFrameTime; // used to calculate delta

	float triangleAngle; // Angle of rotation for the triangles
	float quadAngle; // Angle of rotation for the quads

	float testDistancex = 0.0f;
	float testDistancez = 0.0f;

	float testAngle = 0.0f;
	float testDelta = 1.0f;

	float bodyAngle = 0.0f;
	char directionPressed = 'X';

	float xa;
	float xb;
	float ya;
	float yb;
	float za;
	float zb;
	Random rand = new Random();

	Texture road;
	private int cityList;
	private int statueList;
	private int bunnyList;

	public boolean moveLeft = false;
	public boolean moveRight = false;
	public boolean moveUp = false;
	public boolean moveDown = false;
	public float moveSpeed;

	public float cMovement = 0.0f;
	public float cVelocity = -0.04f;
	public float cZ = -15f;
	public boolean turnAroundC = false;

	public float dMovement = 15.0f;
	public float dVelocity = -0.02f;
	public float dZ = -15f;
	public boolean turnAroundD = false;

	//public float[] lightme;
	//public FloatBuffer fb;

	private static enum State{
		INTRO, MAIN_MENU, GAME;;
	}

	private State state = State.INTRO;

	public void run() {

		createWindow();
		setUpDisplayLists(); //initialize display lists
		getDelta(); // Initialise delta timer
		initGL();

		while (!closeRequested) {
			pollInput();
			updateLogic(getDelta());
			renderGL();

			Display.update();
		}

		cleanup();
	}

	private void initGL() {

		/* OpenGL */
		int width = Display.getDisplayMode().getWidth();
		int height = Display.getDisplayMode().getHeight();

		glViewport(0, 0, width, height); // Reset The Current Viewport
		glMatrixMode(GL_PROJECTION); // Select The Projection Matrix
		glLoadIdentity(); // Reset The Projection Matrix
		GLU.gluPerspective(45.0f, ((float) width / (float) height), 0.1f, 100.0f); // Calculate The Aspect Ratio Of The Window
		glMatrixMode(GL_MODELVIEW); // Select The Modelview Matrix
		glLoadIdentity(); // Reset The Modelview Matrix
		glEnable(GL_TEXTURE_2D);


		glShadeModel(GL_SMOOTH); // Enables Smooth Shading
		glClearColor(0.0f, 0.0f, 0.3f, 0.0f); // Night Sky
		glClearDepth(1.0f); // Depth Buffer Setup
		glEnable(GL_DEPTH_TEST); // Enables Depth Testing

		//new code
		//glEnable(GL_LIGHTING);
		//glEnable(GL_LIGHT7);
		/*
		float[] light1 = new float[]{0.05f, 0.05f, 0.05f, 1f};
		FloatBuffer fb1 = BufferUtils.createFloatBuffer(light1.length);
		fb1 = (FloatBuffer) fb1.put(light1).flip();
		glLightModel(GL_LIGHT_MODEL_AMBIENT, fb1);
		 */
		//glLightModel(GL_LIGHT_MODEL_AMBIENT, fb1);
		//end new code

		glDepthFunc(GL_LEQUAL); // The Type Of Depth Test To Do
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // Really Nice Perspective Calculations

		Camera.create();        
	}

	private void updateLogic(int delta) {
		triangleAngle += 0.1f * delta; // Increase The Rotation Variable For The Triangles
		quadAngle -= 0.05f * delta; // Decrease The Rotation Variable For The Quads
	}

	private void renderGL() {

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear The Screen And The Depth Buffer
		//glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		glLoadIdentity(); // Reset The View
		glTranslatef(0.0f, -3.0f, -7.0f); // Move Right And Into The Screen;
		/*
		switch(state) {
		case INTRO:
			glClearColor(1.0f, 0.5f, 0.0f, 0.0f); // Title Screen?
			// If you want to project text onto the screen
			break;
		case GAME:
			// meh
			break;
		}//end switch
		//can also put Camera.apply() in here if we want to make a start screen;
		*/

		Camera.apply();

		//make grid of the city
		//includes call to callLists(cityList)
		drawCity();
		
		//drawBunnies();
		glCallList(bunnyList);

		//char 2 
		//probably a better way to time things out
		glPushMatrix();
		glTranslatef(-5.5f,0,-10);
		if(testAngle >= 270){
			glRotatef(270, 0, 1, 0);
		}
		else if (testAngle >= 180){
			glRotatef(180, 0, 1, 0);
		}
		else if (testAngle >= 90){
			glRotatef(90, 0, 1, 0);
		}
		// System.out.println(testAngle);
		testAngle = testAngle + testDelta;

		if(testAngle > 360){
			testAngle = 0;
		}
		drawCharacter();
		glPopMatrix();
		//end char 2

		//char 3
		glPushMatrix();
		glTranslatef(5.5f,0,-15);
		glTranslatef(0,0,cMovement);
		if(cZ > -25 && !turnAroundC){
			cMovement = cMovement + cVelocity;
			cZ = cZ + cVelocity;
			//System.out.println(ceeZ);
		}
		else if (cZ < 30){
			turnAroundC = true;
			glRotatef(180, 0, 1, 0);
			cMovement = cMovement - cVelocity;
			cZ = cZ - cVelocity;
		}
		else if (cZ > 30){
			turnAroundC = false;
		}
		drawCharacter();
		glPopMatrix();
		//endChar3

		//char 4
		glPushMatrix();
		glTranslatef(5.5f,0,-41);
		glTranslatef(dMovement,0,0);
		glRotatef(-90,0,1,0);


		if(dZ > -25 && !turnAroundD){
			glRotatef(180, 0, 1, 0);
			dMovement = dMovement + dVelocity;
			dZ = dZ + dVelocity;
			//System.out.println(ceeZ);
		}
		else if (dZ < 30){
			turnAroundD = true;
			dMovement = dMovement - dVelocity;
			dZ = dZ - dVelocity;
		}
		else if (dZ > 30){
			turnAroundD = false;
		}
		drawCharacter();
		glPopMatrix();
		//endChar4

		//Movement of character
		glPushMatrix();
		moveSpeed = 0.1f;
		if(moveLeft){
			testDistancex = testDistancex - moveSpeed;
		}
		if(moveRight){
			testDistancex = testDistancex + moveSpeed;
		}
		if(moveUp){
			testDistancez = testDistancez - moveSpeed;
		}
		if(moveDown){
			testDistancez = testDistancez + moveSpeed;
		}
		glTranslatef(testDistancex, 0, testDistancez);

		if(directionPressed == 'L'){
			bodyAngle = 90.0f;
		}
		else if(directionPressed == 'R'){
			bodyAngle = -90.0f;
		}
		else if(directionPressed == 'D'){
			bodyAngle = 180.0f;
		}
		else{
			bodyAngle = 0.0f;
		}

		glRotatef(bodyAngle, 0, 1, 0);
		// testAngle = testAngle + testDelta;

		drawCharacter();
		glPopMatrix();
		//end Movement of Character

	}


	private void setUpDisplayLists(){
		cityList = glGenLists(1);
		glNewList(cityList, GL_COMPILE);
		{
			drawGrass();

			glPushMatrix();
			//drawRoad(road,10f,10f);
			glRotatef(90, 0, 1, 0);
			glTranslatef(35,0,-12);
			drawBuildings();
			glPopMatrix();

			glPushMatrix();
			//drawRoad(road,10f,10f);
			glRotatef(-90, 0, 1, 0);
			glTranslatef(-35,0,-12);
			drawBuildings();
			glPopMatrix();

			glPushMatrix();
			//drawRoad(road,10f,10f);
			glRotatef(180, 0, 1, 0);
			glTranslatef(0,0,70);
			drawBuildings();
			glPopMatrix();


			drawBuildings();

		}
		glEndList();

		//next list
		statueList = glGenLists(1);
		//System.out.println(statueList);
		String filename = "bunny.obj";
		glNewList(statueList,GL_COMPILE);
		{
			Model m = null;
			try {
				m = OBJLoader.loadModel(new File("res/"+filename));
				//System.out.println("Loaded: "+filename);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Display.destroy();
				System.exit(1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Display.destroy();
				System.exit(1);
			}
			
			//not sure if coloring this way is efficient, but it looks nice!
			//speckled texture-esc look for the rabbit
			
			glBegin(GL_TRIANGLES);
			for (Face face : m.faces){
				glColor3d(Math.random(), Math.random(), Math.random());
				Vector3f n1 = m.normals.get((int) face.normal.x - 1);
				glNormal3f(n1.x, n1.y, n1.z);
				Vector3f v1 = m.vertices.get((int) face.vertex.x - 1);
				glVertex3f(v1.x, v1.y, v1.z);
				
				Vector3f n2 = m.normals.get((int) face.normal.y - 1);
				glNormal3f(n2.x, n2.y, n2.z);
				Vector3f v2 = m.vertices.get((int) face.vertex.y - 1);
				glVertex3f(v2.x, v2.y, v2.z);
				
				Vector3f n3 = m.normals.get((int) face.normal.z - 1);
				glNormal3f(n3.x, n3.y, n3.z);
				Vector3f v3 = m.vertices.get((int) face.vertex.z - 1);
				glVertex3f(v3.x, v3.y, v3.z);
				
			}
			glEnd();


		}
		glEndList();
		
		//next list
		bunnyList = glGenLists(1);
		glNewList(bunnyList,GL_COMPILE);
		{
			glPushMatrix();
			glTranslatef(-30,-1,0);
			glScalef(2.0f, 2.0f, 2.0f);
			glCallList(statueList);
			glPopMatrix();
			
			int rot = rand.nextInt((359 - 0) + 1);
			glPushMatrix();
			glTranslatef(-30,-1,5);
			glRotatef(rot, 0,1,0);
			glScalef(0.5f, 0.5f, 0.5f);
			glCallList(statueList);
			glPopMatrix();
			
			rot = rand.nextInt((359 - 0) + 1);
			glPushMatrix();
			glTranslatef(-35,-1,4);
			glRotatef(rot, 0,1,0);
			glScalef(0.5f, 0.5f, 0.5f);
			glCallList(statueList);
			glPopMatrix();
			
			rot = rand.nextInt((359 - 0) + 1);
			glPushMatrix();
			glTranslatef(-25,-1,4);
			glRotatef(rot, 0,1,0);
			glScalef(0.5f, 0.5f, 0.5f);
			glCallList(statueList);
			glPopMatrix();
		}
		glEndList();
	}
	
	public void drawCity(){
		//right
		glPushMatrix();
		glTranslatef(70,0,0);
		glCallList(cityList);
		glPopMatrix();
		//left
		glPushMatrix();
		glTranslatef(-70,0,0);
		glCallList(cityList);
		glPopMatrix();
		//back
		glPushMatrix();
		glTranslatef(0,0,85);
		glCallList(cityList);
		glPopMatrix();
		//back left
		glPushMatrix();
		glTranslatef(-70,0,85);
		glCallList(cityList);
		glPopMatrix();
		//back right
		glPushMatrix();
		glTranslatef(70,0,85);
		glCallList(cityList);
		glPopMatrix();

		glCallList(cityList); //original city, aka starting place for character
	}

	private static void drawGrass(){
		glBegin(GL_QUADS);
		// MY Ground Plane - points have to go in counter clockwise order

		glColor3f(0.5f, 1.0f, 0.5f); // Set The Color To Green
		glVertex3f(-100.0f, -1.01f, -100f); // Top Right Of The Quad (Bottom)
		glVertex3f(100.0f, -1.01f, -100f); // Top Left Of The Quad (Bottom)
		glVertex3f(100.0f, -1.01f, 25f); // Bottom Left Of The Quad (Bottom)
		glVertex3f(-100.0f, -1.01f, 25f); // Bottom Right Of The Quad (Bottom)  
		glEnd(); // Done Drawing The Quad

		//patches of grass
		/*
		for(float i = -10; i <= 10; i = i + 0.05f){
			for(float j = -10; j <= 10; j = j + 0.1f){
				glBegin(GL_LINES);
				glColor3f(0.8f, 1.0f, 0.5f); // Set The Color To Green
				glVertex3f(i, -1f, j);
				glVertex3f(i, -0.5f, j);
				glEnd();
			}
		}
		 */
	}

	private void drawRoad(){
		glPushMatrix();
		glBegin(GL_QUADS);
		glColor3f(0.3f, 0.3f, 0.3f); // Set The Color To Green
		glVertex3f(-5.0f, -1f, -35f); // Top Right Of The Quad (Bottom)
		glVertex3f(5.0f, -1f, -35f); // Top Left Of The Quad (Bottom)
		glVertex3f(5.0f, -1f, 10f); // Bottom Left Of The Quad (Bottom)
		glVertex3f(-5.0f, -1f, 10f); // Bottom Right Of The Quad (Bottom)

		//sidewalk 1
		glColor3f(0.8f, 0.8f, 0.8f); // Set The Color To Green
		glVertex3f(-15.0f, -1.001f, -30f); // Top Right Of The Quad (Bottom)
		glVertex3f(-5.0f, -1.001f, -30f); // Top Left Of The Quad (Bottom)
		glVertex3f(-5.0f, -1.001f, 10f); // Bottom Left Of The Quad (Bottom)
		glVertex3f(-15.0f, -1.001f, 10f); // Bottom Right Of The Quad (Bottom)

		//sidewalk2
		glColor3f(0.8f, 0.8f, 0.8f); // Set The Color To Green
		glVertex3f(5.0f, -1.001f, -30f); // Top Right Of The Quad (Bottom)
		glVertex3f(15.0f, -1.001f, -30f); // Top Left Of The Quad (Bottom)
		glVertex3f(15.0f, -1.001f, 10f); // Bottom Left Of The Quad (Bottom)
		glVertex3f(5.0f, -1.001f, 10f); // Bottom Right Of The Quad (Bottom)

		glEnd(); // Done Drawing The Quad

		glPopMatrix();

	}
	public void drawBuildings(){
		drawRoad();
		//drawStreetSign();

		for(float b = -20f; b <= 0; b = b + 10){
			drawBuilding(-7.0f,b,0);
		}

		for(float b = -20f; b <= 0; b = b + 10){
			drawBuilding(7.0f,b,1);
		}
		//drawBuilding(-7.0f,7.0f,-9.0f);
		//drawBuilding(-12.f,7.0f,-10.0f);
	}
	public void drawBuilding(float xr, float zt, int side){
		//building?

		//float buildingH = (float) (Math.random() * (12 - 4) + 4);
		int buildingH = rand.nextInt((12 - 4) + 1) + 4;

		//need to do this with display list'

		float height = buildingH;
		xa = xr;

		//side of the street
		if(side == 0){ //0 is left
			xb = xr - 5;
		}
		else xb = xr + 5;


		ya = height;
		yb = -1.0f;
		za = zt;
		zb = zt + 5;

		glBegin(GL_QUADS);
		glColor3f(0.0f, 1.0f, 0.0f); // Set The Color To Green
		glVertex3f(xa, ya, za); // Top Right Of The Quad (Top)
		glVertex3f(xb, ya, za); // Top Left Of The Quad (Top)
		glVertex3f(xb, ya, zb); // Bottom Left Of The Quad (Top)
		glVertex3f(xa, ya, zb); // Bottom Right Of The Quad (Top)

		glColor3f(1.0f, 0.0f, 0.0f); // Set The Color To Orange
		glVertex3f(xa, yb, zb); // Top Right Of The Quad (Bottom)
		glVertex3f(xb, yb, zb); // Top Left Of The Quad (Bottom)
		glVertex3f(xb, yb, za); // Bottom Left Of The Quad (Bottom)
		glVertex3f(xa, yb, za); // Bottom Right Of The Quad (Bottom)

		glColor3f(1.0f, 0.0f, 0.0f); // Set The Color To Red
		glVertex3f(xa, ya, zb); // Top Right Of The Quad (Front)
		glVertex3f(xb, ya, zb); // Top Left Of The Quad (Front)
		glVertex3f(xb, yb, zb); // Bottom Left Of The Quad (Front)
		glVertex3f(xa, yb, zb); // Bottom Right Of The Quad (Front)

		glColor3f(1.0f, 1.0f, 0.0f); // Set The Color To Yellow
		glVertex3f(xa, yb, za); // Bottom Left Of The Quad (Back)
		glVertex3f(xb, yb, za); // Bottom Right Of The Quad (Back)
		glVertex3f(xb, ya, za); // Top Right Of The Quad (Back)
		glVertex3f(xa, ya, za); // Top Left Of The Quad (Back)

		glColor3f(0.0f, 0.0f, 0.8f); // Set The Color To Blue
		glVertex3f(xb, ya, zb); // Top Right Of The Quad (Left)
		glVertex3f(xb, ya, za); // Top Left Of The Quad (Left)
		glVertex3f(xb, yb, za); // Bottom Left Of The Quad (Left)
		glVertex3f(xb, yb, zb); // Bottom Right Of The Quad (Left)

		glColor3f(1.0f, 0.0f, 1.0f); // Set The Color To Violet
		glVertex3f(xa, ya, za); // Top Right Of The Quad (Right)
		glVertex3f(xa, ya, zb); // Top Left Of The Quad (Right)
		glVertex3f(xa, yb, zb); // Bottom Left Of The Quad (Right)
		glVertex3f(xa, yb, za); // Bottom Right Of The Quad (Right)

		//door testing code
		float doorA = 0.01f;
		if (side == 1){
			doorA = -0.01f;
		}
		glColor3f(0.8f, 0.3f, 0.1f); // Set The Color To Brown
		glVertex3f(xa+doorA, 1.4f, za+1.5f); // Top Right Of The Quad (Right)
		glVertex3f(xa+doorA, 1.4f, zb-1.5f); // Top Left Of The Quad (Right)
		glVertex3f(xa+doorA, yb, zb-1.5f); // Bottom Left Of The Quad (Right)
		glVertex3f(xa+doorA, yb, za+1.5f); // Bottom Right Of The Quad (Right)
		glEnd();

		drawWindows(buildingH,xa,xb,za+1.5f,zb-1.5f,side);
	}

	public void drawWindows(float buildingH, float x1, float x2, float z1, float z2, int side){
		int windowOn;

		float wind = 0.01f;
		if (side == 1){ //gets the correct side of the building
			wind = -0.01f;
		}
		for (float i = 1.8f; i + 1 < buildingH; i = i + 1.5f){

			windowOn = rand.nextInt((10 - 1) + 1) + 1;

			if(windowOn > 4){
				glColor3f(1.0f, 0.95f, 0.5f); // Set The Color To Yellow
			}
			else{
				glColor3f(0.25f, 0.25f, 0.25f); // Set The Color To Yellow
				/*
				lightme = new float[]{x1+wind,i+1,z11.2f,1f};
				fb = BufferUtils.createFloatBuffer(lightme.length);
				fb = (FloatBuffer) fb.put(lightme).flip();
				 */
			}
			glBegin(GL_QUADS);
			glVertex3f(x1+wind, i+1, z1+1.2f); // Top Right Of The Quad (Right)
			glVertex3f(x1+wind, i+1, z2+1); // Top Left Of The Quad (Right)
			glVertex3f(x1+wind, i, z2+1); // Bottom Left Of The Quad (Right)
			glVertex3f(x1+wind, i, z1+1.2f); // Bottom Right Of The Quad (Right)
			glEnd();

			windowOn = rand.nextInt((10 - 1) + 1) + 1;

			if(windowOn > 4){
				glColor3f(1.0f, 0.95f, 0.5f); // Set The Color To Yellow
			}
			else{
				glColor3f(0.25f, 0.25f, 0.25f); // Set The Color To Yellow
			}
			glBegin(GL_QUADS);
			glVertex3f(x1+wind, i+1, z1-1f); // Top Right Of The Quad (Right)
			glVertex3f(x1+wind, i+1, z2-1.2f); // Top Left Of The Quad (Right)
			glVertex3f(x1+wind, i, z2-1.2f); // Bottom Left Of The Quad (Right)
			glVertex3f(x1+wind, i, z1-1); // Bottom Right Of The Quad (Right)
			glEnd();

			windowOn = rand.nextInt((10 - 1) + 1) + 1;
			if(windowOn > 4){
				glColor3f(1.0f, 0.95f, 0.5f); // Set The Color To Yellow
			}
			else{
				glColor3f(0.25f, 0.25f, 0.25f); // Set The Color To Yellow
			}
			glBegin(GL_QUADS);
			glVertex3f(x2-wind, i+1, z1+1.2f); // Top Right Of The Quad (Right)
			glVertex3f(x2-wind, i+1, z2+1); // Top Left Of The Quad (Right)
			glVertex3f(x2-wind, i, z2+1); // Bottom Left Of The Quad (Right)
			glVertex3f(x2-wind, i, z1+1.2f); // Bottom Right Of The Quad (Right)
			glEnd();

			windowOn = rand.nextInt((10 - 1) + 1) + 1;
			if(windowOn > 4){
				glColor3f(1.0f, 0.95f, 0.5f); // Set The Color To Yellow
			}
			else{
				glColor3f(0.25f, 0.25f, 0.25f); // Set The Color To Yellow
			}
			glBegin(GL_QUADS);
			glVertex3f(x2-wind, i+1, z1-1f); // Top Right Of The Quad (Right)
			glVertex3f(x2-wind, i+1, z2-1.2f); // Top Left Of The Quad (Right)
			glVertex3f(x2-wind, i, z2-1.2f); // Bottom Left Of The Quad (Right)
			glVertex3f(x2-wind, i, z1-1); // Bottom Right Of The Quad (Right)
			glEnd();


		}
	}

	public void drawStreetSign(){
		glPushMatrix();
		glTranslatef(-5.5f,-1,-30f);
		glRotatef(-90,1,0,0);
		glColor3f(0.5f, 0.5f, 0.5f); // Set The Color To Green
		Cylinder c = new Cylinder();
		//base, top, height, slices, stacks
		c.draw(0.2f, 0.2f, 3, 16, 16);
		glPopMatrix();
	}
	public void drawCharacter(){
		glBegin(GL_QUADS); // Start Drawing The Cube

		//TORSO
		//FOR Y AXIS, ALL -1S BECOME 0S

		glColor3f(0.0f, 1.0f, 0.0f); // Set The Color To Green
		glVertex3f(0.4f, 1.0f, -0.2f); // Top Right Of The Quad (Top)
		glVertex3f(-0.4f, 1.0f, -0.2f); // Top Left Of The Quad (Top)
		glVertex3f(-0.4f, 1.0f, 0.2f); // Bottom Left Of The Quad (Top)
		glVertex3f(0.4f, 1.0f, 0.2f); // Bottom Right Of The Quad (Top)

		glColor3f(1.0f, 0.4f, 0.0f); // Set The Color To Orange
		glVertex3f(0.4f, -0.25f, 0.2f); // Top Right Of The Quad (Bottom)
		glVertex3f(-0.4f, -0.25f, 0.2f); // Top Left Of The Quad (Bottom)
		glVertex3f(-0.4f, -0.25f, -0.2f); // Bottom Left Of The Quad (Bottom)
		glVertex3f(0.4f, -0.25f, -0.2f); // Bottom Right Of The Quad (Bottom)

		glColor3f(1.0f, 0.0f, 0.0f); // Set The Color To Red
		glVertex3f(0.4f, 1.0f, 0.2f); // Top Right Of The Quad (Front)
		glVertex3f(-0.4f, 1.0f, 0.2f); // Top Left Of The Quad (Front)
		glVertex3f(-0.4f, -0.25f, 0.2f); // Bottom Left Of The Quad (Front)
		glVertex3f(0.4f, -0.25f, 0.2f); // Bottom Right Of The Quad (Front)

		glColor3f(1.0f, 1.0f, 0.0f); // Set The Color To Yellow
		glVertex3f(0.4f, -0.25f, -0.2f); // Bottom Left Of The Quad (Back)
		glVertex3f(-0.4f, -0.25f, -0.2f); // Bottom Right Of The Quad (Back)
		glVertex3f(-0.4f, 1.0f, -0.2f); // Top Right Of The Quad (Back)
		glVertex3f(0.4f, 1.0f, -0.2f); // Top Left Of The Quad (Back)

		glColor3f(0.0f, 0.0f, 0.2f); // Set The Color To Blue
		glVertex3f(-0.4f, 1.0f, 0.2f); // Top Right Of The Quad (Left)
		glVertex3f(-0.4f, 1.0f, -0.2f); // Top Left Of The Quad (Left)
		glVertex3f(-0.4f, -0.25f, -0.2f); // Bottom Left Of The Quad (Left)
		glVertex3f(-0.4f, -0.25f, 0.2f); // Bottom Right Of The Quad (Left)

		glColor3f(1.0f, 0.0f, 1.0f); // Set The Color To Violet
		glVertex3f(0.4f, 1.0f, -0.2f); // Top Right Of The Quad (Right)
		glVertex3f(0.4f, 1.0f, 0.2f); // Top Left Of The Quad (Right)
		glVertex3f(0.4f, -0.25f, 0.2f); // Bottom Left Of The Quad (Right)
		glVertex3f(0.4f, -0.25f, -0.2f); // Bottom Right Of The Quad (Right)

		//LEFT LEG
		//FOR Y, ALL 1'S BECOME 0S, ALL 0S BECOME -1S

		glColor3f(0.0f, 1.0f, 0.0f); // Set The Color To Green
		glVertex3f(-0.1F, -0.25f, -0.2f); // Top Right Of The Quad (Top)
		glVertex3f(-0.3F, -0.25f, -0.2f); // Top Left Of The Quad (Top)
		glVertex3f(-0.3F, -0.25f, 0.2f); // Bottom Left Of The Quad (Top)
		glVertex3f(-0.1F, -0.25f, 0.2f); // Bottom Right Of The Quad (Top)

		glColor3f(1.0f, 0.0f, 0.0f); // Set The Color To Orange
		glVertex3f(-0.1F, -1.0f, 0.2f); // Top Right Of The Quad (Bottom)
		glVertex3f(-0.3F, -1.0f, 0.2f); // Top Left Of The Quad (Bottom)
		glVertex3f(-0.3F, -1.0f, -0.2f); // Bottom Left Of The Quad (Bottom)
		glVertex3f(-0.1F, -1.0f, -0.2f); // Bottom Right Of The Quad (Bottom)

		glColor3f(1.0f, 0.0f, 0.0f); // Set The Color To Red
		glVertex3f(-0.1F, -0.25f, 0.2f); // Top Right Of The Quad (Front)
		glVertex3f(-0.3F, -0.25f, 0.2f); // Top Left Of The Quad (Front)
		glVertex3f(-0.3F, -1.0f, 0.2f); // Bottom Left Of The Quad (Front)
		glVertex3f(-0.1F, -1.0f, 0.2f); // Bottom Right Of The Quad (Front)

		glColor3f(1.0f, 1.0f, 0.0f); // Set The Color To Yellow
		glVertex3f(-0.1F, -1.0f, -0.2f); // Bottom Left Of The Quad (Back)
		glVertex3f(-0.3F, -1.0f, -0.2f); // Bottom Right Of The Quad (Back)
		glVertex3f(-0.3F, -0.25f, -0.2f); // Top Right Of The Quad (Back)
		glVertex3f(-0.1F, -0.25f, -0.2f); // Top Left Of The Quad (Back)

		glColor3f(0.0f, 0.0f, 0.2f); // Set The Color To Blue
		glVertex3f(-0.3F, -0.25f, 0.2f); // Top Right Of The Quad (Left)
		glVertex3f(-0.3F, -0.25f, -0.2f); // Top Left Of The Quad (Left)
		glVertex3f(-0.3F, -1.0f, -0.2f); // Bottom Left Of The Quad (Left)
		glVertex3f(-0.3F, -1.0f, 0.2f); // Bottom Right Of The Quad (Left)

		glColor3f(1.0f, 0.0f, 1.0f); // Set The Color To Violet
		glVertex3f(-0.1F, -0.25f, -0.2f); // Top Right Of The Quad (Right)
		glVertex3f(-0.1F, -0.25f, 0.2f); // Top Left Of The Quad (Right)
		glVertex3f(-0.1F, -1.0f, 0.2f); // Bottom Left Of The Quad (Right)
		glVertex3f(-0.1F, -1.0f, -0.2f); // Bottom Right Of The Quad (Right)


		//RIGHT LEG
		glColor3f(0.0f, 1.0f, 0.0f); // Set The Color To Green
		glVertex3f(0.3f, -0.25f, -0.2f); // Top Right Of The Quad (Top)
		glVertex3f(0.1f, -0.25f, -0.2f); // Top Left Of The Quad (Top)
		glVertex3f(0.1f, -0.25f, 0.2f); // Bottom Left Of The Quad (Top)
		glVertex3f(0.3f, -0.25f, 0.2f); // Bottom Right Of The Quad (Top)

		glColor3f(1.0f, 0.0f, 0.0f); // Set The Color To Orange
		glVertex3f(0.3f, -1.0f, 0.2f); // Top Right Of The Quad (Bottom)
		glVertex3f(0.1f, -1.0f, 0.2f); // Top Left Of The Quad (Bottom)
		glVertex3f(0.1f, -1.0f, -0.2f); // Bottom Left Of The Quad (Bottom)
		glVertex3f(0.3f, -1.0f, -0.2f); // Bottom Right Of The Quad (Bottom)

		glColor3f(1.0f, 0.0f, 0.0f); // Set The Color To Red
		glVertex3f(0.3f, -0.25f, 0.2f); // Top Right Of The Quad (Front)
		glVertex3f(0.1f, -0.25f, 0.2f); // Top Left Of The Quad (Front)
		glVertex3f(0.1f, -1.0f, 0.2f); // Bottom Left Of The Quad (Front)
		glVertex3f(0.3f, -1.0f, 0.2f); // Bottom Right Of The Quad (Front)

		glColor3f(1.0f, 1.0f, 0.0f); // Set The Color To Yellow
		glVertex3f(0.3f, -1.0f, -0.2f); // Bottom Left Of The Quad (Back)
		glVertex3f(0.1f, -1.0f, -0.2f); // Bottom Right Of The Quad (Back)
		glVertex3f(0.1f, -0.25f, -0.2f); // Top Right Of The Quad (Back)
		glVertex3f(0.3f, -0.25f, -0.2f); // Top Left Of The Quad (Back)

		glColor3f(0.0f, 0.0f, 0.2f); // Set The Color To Blue
		glVertex3f(0.1f, -0.25f, 0.2f); // Top Right Of The Quad (Left)
		glVertex3f(0.1f, -0.25f, -0.2f); // Top Left Of The Quad (Left)
		glVertex3f(0.1f, -1.0f, -0.2f); // Bottom Left Of The Quad (Left)
		glVertex3f(0.1f, -1.0f, 0.2f); // Bottom Right Of The Quad (Left)

		glColor3f(1.0f, 0.0f, 1.0f); // Set The Color To Violet
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
		glColor3f(0.0f, 1.0f, 0.0f); // Set The Color To Green
		glVertex3f(xa, ya, za); // Top Right Of The Quad (Top)
		glVertex3f(xb, ya, za); // Top Left Of The Quad (Top)
		glVertex3f(xb, ya, zb); // Bottom Left Of The Quad (Top)
		glVertex3f(xa, ya, zb); // Bottom Right Of The Quad (Top)

		glColor3f(1.0f, 0.0f, 0.0f); // Set The Color To Orange
		glVertex3f(xa, yb, zb); // Top Right Of The Quad (Bottom)
		glVertex3f(xb, yb, zb); // Top Left Of The Quad (Bottom)
		glVertex3f(xb, yb, za); // Bottom Left Of The Quad (Bottom)
		glVertex3f(xa, yb, za); // Bottom Right Of The Quad (Bottom)

		glColor3f(1.0f, 0.0f, 0.0f); // Set The Color To Red
		glVertex3f(xa, ya, zb); // Top Right Of The Quad (Front)
		glVertex3f(xb, ya, zb); // Top Left Of The Quad (Front)
		glVertex3f(xb, yb, zb); // Bottom Left Of The Quad (Front)
		glVertex3f(xa, yb, zb); // Bottom Right Of The Quad (Front)

		glColor3f(1.0f, 1.0f, 0.0f); // Set The Color To Yellow
		glVertex3f(xa, yb, za); // Bottom Left Of The Quad (Back)
		glVertex3f(xb, yb, za); // Bottom Right Of The Quad (Back)
		glVertex3f(xb, ya, za); // Top Right Of The Quad (Back)
		glVertex3f(xa, ya, za); // Top Left Of The Quad (Back)

		glColor3f(0.0f, 0.0f, 0.2f); // Set The Color To Blue
		glVertex3f(xb, ya, zb); // Top Right Of The Quad (Left)
		glVertex3f(xb, ya, za); // Top Left Of The Quad (Left)
		glVertex3f(xb, yb, za); // Bottom Left Of The Quad (Left)
		glVertex3f(xb, yb, zb); // Bottom Right Of The Quad (Left)

		glColor3f(1.0f, 0.0f, 1.0f); // Set The Color To Violet
		glVertex3f(xa, ya, za); // Top Right Of The Quad (Right)
		glVertex3f(xa, ya, zb); // Top Left Of The Quad (Right)
		glVertex3f(xa, yb, zb); // Bottom Left Of The Quad (Right)
		glVertex3f(xa, yb, za); // Bottom Right Of The Quad (Right)


		//RIGHT ARM
		xa = 0.6f;
		xb = 0.4f;
		ya = 1.0f;
		yb = 0.1f;
		za = -0.15f;
		zb = 0.15f;
		glColor3f(0.0f, 1.0f, 0.0f); // Set The Color To Green
		glVertex3f(xa, ya, za); // Top Right Of The Quad (Top)
		glVertex3f(xb, ya, za); // Top Left Of The Quad (Top)
		glVertex3f(xb, ya, zb); // Bottom Left Of The Quad (Top)
		glVertex3f(xa, ya, zb); // Bottom Right Of The Quad (Top)

		glColor3f(1.0f, 0.0f, 0.0f); // Set The Color To Orange
		glVertex3f(xa, yb, zb); // Top Right Of The Quad (Bottom)
		glVertex3f(xb, yb, zb); // Top Left Of The Quad (Bottom)
		glVertex3f(xb, yb, za); // Bottom Left Of The Quad (Bottom)
		glVertex3f(xa, yb, za); // Bottom Right Of The Quad (Bottom)

		glColor3f(1.0f, 0.0f, 0.0f); // Set The Color To Red
		glVertex3f(xa, ya, zb); // Top Right Of The Quad (Front)
		glVertex3f(xb, ya, zb); // Top Left Of The Quad (Front)
		glVertex3f(xb, yb, zb); // Bottom Left Of The Quad (Front)
		glVertex3f(xa, yb, zb); // Bottom Right Of The Quad (Front)

		glColor3f(1.0f, 1.0f, 0.0f); // Set The Color To Yellow
		glVertex3f(xa, yb, za); // Bottom Left Of The Quad (Back)
		glVertex3f(xb, yb, za); // Bottom Right Of The Quad (Back)
		glVertex3f(xb, ya, za); // Top Right Of The Quad (Back)
		glVertex3f(xa, ya, za); // Top Left Of The Quad (Back)

		glColor3f(0.0f, 0.0f, 0.2f); // Set The Color To Blue
		glVertex3f(xb, ya, zb); // Top Right Of The Quad (Left)
		glVertex3f(xb, ya, za); // Top Left Of The Quad (Left)
		glVertex3f(xb, yb, za); // Bottom Left Of The Quad (Left)
		glVertex3f(xb, yb, zb); // Bottom Right Of The Quad (Left)

		glColor3f(1.0f, 0.0f, 1.0f); // Set The Color To Violet
		glVertex3f(xa, ya, za); // Top Right Of The Quad (Right)
		glVertex3f(xa, ya, zb); // Top Left Of The Quad (Right)
		glVertex3f(xa, yb, zb); // Bottom Left Of The Quad (Right)
		glVertex3f(xa, yb, za); // Bottom Right Of The Quad (Right)

		glEnd(); // Done Drawing The Quad

		//head
		glPushMatrix();
		glTranslatef(0, 1.4f, 0);
		Sphere s = new Sphere();
		glColor3f(1.0f, 0.9f, 0.8f);
		s.draw(0.4f,16,16);
		glPopMatrix();

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


	}
	/**
	 * Poll Input
	 */
	public void pollInput() {
		Camera.acceptInput(getDelta());
		// scroll through key events
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE)
					closeRequested = true;
				else if (Keyboard.getEventKey() == Keyboard.KEY_P)
					snapshot();
				else if (Keyboard.getEventKey() == Keyboard.KEY_LEFT){
					//testDistancex = (float) (testDistancex - 2.0);
					directionPressed = 'L';
					moveLeft = true;
				}
				else if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT){
					//testDistancex = (float) (testDistancex + 2.0);
					directionPressed = 'R';
					moveRight = true;
				}
				else if (Keyboard.getEventKey() == Keyboard.KEY_UP){
					//testDistancez = (float) (testDistancez - 2.0);
					directionPressed = 'U';
					moveUp = true;
				}
				else if (Keyboard.getEventKey() == Keyboard.KEY_DOWN){
					//testDistancez = (float) (testDistancez + 2.0);	
					directionPressed = 'D';
					moveDown = true;
				}
				else if (Keyboard.getEventKey() == Keyboard.KEY_RETURN){
					state = State.GAME;
				}
				//movement = false;
			}
			else{
				moveLeft = false;
				moveRight = false;
				moveUp = false;
				moveDown = false;
			}
		}

		if (Display.isCloseRequested()) {
			closeRequested = true;
		}
	}

	public void snapshot() {
		System.out.println("Taking a snapshot ... snapshot.png");

		glReadBuffer(GL_FRONT);

		int width = Display.getDisplayMode().getWidth();
		int height= Display.getDisplayMode().getHeight();
		int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
		glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, buffer );

		File file = new File("snapshot.png"); // The file to save to.
		String format = "PNG"; // Example: "PNG" or "JPG"
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				int i = (x + (width * y)) * bpp;
				int r = buffer.get(i) & 0xFF;
				int g = buffer.get(i + 1) & 0xFF;
				int b = buffer.get(i + 2) & 0xFF;
				image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
			}
		}

		try {
			ImageIO.write(image, format, file);
		} catch (IOException e) { e.printStackTrace(); }
	}

	/** 
	 * Calculate how many milliseconds have passed 
	 * since last frame.
	 * 
	 * @return milliseconds passed since last frame 
	 */
	public int getDelta() {
		long time = (Sys.getTime() * 1000) / Sys.getTimerResolution();
		int delta = (int) (time - lastFrameTime);
		lastFrameTime = time;

		return delta;
	}

	private void createWindow() {
		try {
			Display.setDisplayMode(new DisplayMode(960, 540));
			Display.setVSyncEnabled(true);
			Display.setTitle(windowTitle);
			Display.create();
		} catch (LWJGLException e) {
			Sys.alert("Error", "Initialization failed!\n\n" + e.getMessage());
			System.exit(0);
		}
	}

	/**
	 * Destroy and clean up resources
	 */
	private void cleanup() {
		Display.destroy();
	}

	public static void main(String[] args) {
		new Pokemon().run();
	}

	public static class Camera {
		public static float moveSpeed = 0.5f;

		private static float maxLook = 250;

		private static float mouseSensitivity = 0.05f;

		private static Vector3f pos;
		private static Vector3f rotation;

		public static void create() {
			pos = new Vector3f(0, 0, 0);
			rotation = new Vector3f(0, 0, 0);
		}

		public static void apply() {
			if (rotation.y / 360 > 1) {
				rotation.y -= 360;
			} else if (rotation.y / 360 < -1) {
				rotation.y += 360;
			}

			//System.out.println(rotation);
			glRotatef(rotation.x, 1, 0, 0);
			glRotatef(rotation.y, 0, 1, 0);
			glRotatef(rotation.z, 0, 0, 1);
			glTranslatef(-pos.x, -pos.y, -pos.z);
		}

		public static void acceptInput(float delta) {
			//System.out.println("delta="+delta);
			acceptInputRotate(delta);
			acceptInputMove(delta);
		}

		public static void acceptInputRotate(float delta) {
			if (Mouse.isInsideWindow() && Mouse.isButtonDown(0)) {
				float mouseDX = Mouse.getDX();
				float mouseDY = -Mouse.getDY();
				//System.out.println("DX/Y: " + mouseDX + "  " + mouseDY);
				rotation.y += mouseDX * mouseSensitivity * delta;
				rotation.x += mouseDY * mouseSensitivity * delta;
				rotation.x = Math.max(-maxLook, Math.min(maxLook, rotation.x));
			}
		}

		public static void acceptInputMove(float delta) {
			boolean keyUp = Keyboard.isKeyDown(Keyboard.KEY_W);
			boolean keyDown = Keyboard.isKeyDown(Keyboard.KEY_S);
			boolean keyRight = Keyboard.isKeyDown(Keyboard.KEY_D);
			boolean keyLeft = Keyboard.isKeyDown(Keyboard.KEY_A);
			boolean keyFast = Keyboard.isKeyDown(Keyboard.KEY_Q);
			boolean keySlow = Keyboard.isKeyDown(Keyboard.KEY_E);
			boolean keyFlyUp = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
			boolean keyFlyDown = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);

			float speed;

			if (keyFast) {
				speed = moveSpeed * 5;
			} else if (keySlow) {
				speed = moveSpeed / 2;
			} else {
				speed = moveSpeed;
			}

			speed = speed * .03f * delta; //slows the movement down significantly

			if (keyFlyUp) {
				pos.y += speed;
			}
			if (keyFlyDown) {
				pos.y -= speed;
			}

			if (keyDown) {
				pos.x -= Math.sin(Math.toRadians(rotation.y)) * speed;
				pos.z += Math.cos(Math.toRadians(rotation.y)) * speed;
			}
			if (keyUp) {
				pos.x += Math.sin(Math.toRadians(rotation.y)) * speed;
				pos.z -= Math.cos(Math.toRadians(rotation.y)) * speed;
			}
			if (keyLeft) {
				pos.x += Math.sin(Math.toRadians(rotation.y - 90)) * speed;
				pos.z -= Math.cos(Math.toRadians(rotation.y - 90)) * speed;
			}
			if (keyRight) {
				pos.x += Math.sin(Math.toRadians(rotation.y + 90)) * speed;
				pos.z -= Math.cos(Math.toRadians(rotation.y + 90)) * speed;
			}
		}

		public static void setSpeed(float speed) {
			moveSpeed = speed;
		}

		public static void setPos(Vector3f pos) {
			Camera.pos = pos;
		}

		public static Vector3f getPos() {
			return pos;
		}

		public static void setX(float x) {
			pos.x = x;
		}

		public static float getX() {
			return pos.x;
		}

		public static void addToX(float x) {
			pos.x += x;
		}

		public static void setY(float y) {
			pos.y = y;
		}

		public static float getY() {
			return pos.y;
		}

		public static void addToY(float y) {
			pos.y += y;
		}

		public static void setZ(float z) {
			pos.z = z;
		}

		public static float getZ() {
			return pos.z;
		}

		public static void addToZ(float z) {
			pos.z += z;
		}

		public static void setRotation(Vector3f rotation) {
			Camera.rotation = rotation;
		}

		public static Vector3f getRotation() {
			return rotation;
		}

		public static void setRotationX(float x) {
			rotation.x = x;
		}

		public static float getRotationX() {
			return rotation.x;
		}

		public static void addToRotationX(float x) {
			rotation.x += x;
		}

		public static void setRotationY(float y) {
			rotation.y = y;
		}

		public static float getRotationY() {
			return rotation.y;
		}

		public static void addToRotationY(float y) {
			rotation.y += y;
		}

		public static void setRotationZ(float z) {
			rotation.z = z;
		}

		public static float getRotationZ() {
			return rotation.z;
		}

		public static void addToRotationZ(float z) {
			rotation.z += z;
		}

		public static void setMaxLook(float maxLook) {
			Camera.maxLook = maxLook;
		}

		public static float getMaxLook() {
			return maxLook;
		}

		public static void setMouseSensitivity(float mouseSensitivity) {
			Camera.mouseSensitivity = mouseSensitivity;
		}

		public static float getMouseSensitivity() {
			return mouseSensitivity;
		}
	}
}
