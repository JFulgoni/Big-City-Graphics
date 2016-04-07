import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_COLOR_MATERIAL;
import static org.lwjgl.opengl.GL11.GL_COMPILE;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_DIFFUSE;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_LIGHT0;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.opengl.GL11.GL_PERSPECTIVE_CORRECTION_HINT;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glCallList;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glColorMaterial;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glEndList;
import static org.lwjgl.opengl.GL11.glGenLists;
import static org.lwjgl.opengl.GL11.glHint;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glNewList;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glReadBuffer;
import static org.lwjgl.opengl.GL11.glReadPixels;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glShadeModel;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL11.glViewport;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Random;
import java.util.Vector;

import javax.imageio.ImageIO;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL12;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;

public class BigCity {

	String windowTitle = "New Big City";
	public boolean closeRequested = false;

	long lastFrameTime; // used to calculate delta

	float triangleAngle; // Angle of rotation for the triangles
	float quadAngle; // Angle of rotation for the quads

	float testDistancex = 0.0f;
	float testDistancez = 0.0f;
	float testDistancey = 0.0f;

	ShaderProgram shader, shader2, shader3, shader4, checker;

	float bodyAngle = 0.0f;
	char directionPressed = 'X';

	float xa;
	float xb;
	float ya;
	float yb;
	float za;
	float zb;
	Random rand = new Random();

	private int cityList, roadList, officeList, graveList, burjList, bankList;
	private int carList, copList, cactusList, volksList, hawkList, poolList, residenceList;
	private int snakeList;

	public boolean moveLeft = false;
	public boolean moveRight = false;
	public boolean moveUp = false;
	public boolean moveDown = false;
	public boolean jump = false;
	public float moveSpeed;

	public Skybox skybox;

	public Streetlamp lamp, lamp2;

	Person barry;
	Person charlie;
	Person danny;
	Person eddie;
	Person walter;

	Gravestone gus, fring;
	
	Truck mater;
	Police hank, jesse;
	Blackhawk earl;
	
	Vector<Texture> road;
	
	Snake snakey, harry;

	
	private static enum State{
		CITY,GAME;
	}

	private State state = State.CITY;

	public void run() {

		createWindow();
		initShaders();
		setUpDisplayLists(); //initialize display lists
		getDelta(); // Initialise delta timer
		initGL();
		initChars();

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
		GLU.gluPerspective(45.0f, ((float) width / (float) height), 0.1f, 1000.0f); // Calculate The Aspect Ratio Of The Window
		glMatrixMode(GL_MODELVIEW); // Select The Modelview Matrix
		glLoadIdentity(); // Reset The Modelview Matrix
//		glEnable(GL_TEXTURE_2D);


		glShadeModel(GL_SMOOTH); // Enables Smooth Shading
		glClearColor(0.47f, 0.52f, 0.48f, 0.0f); // Sand
		glClearDepth(1.0f); // Depth Buffer Setup
		glEnable(GL_DEPTH_TEST); // Enables Depth Testing

		glEnable(GL_COLOR_MATERIAL);
		glColorMaterial(GL_FRONT,GL_DIFFUSE);
		
		
		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);

		float [] lightposition = new float[]{0f, 18f, 0f, 1};
		glLightModel(GL_LIGHT_MODEL_AMBIENT, asFlippedBuffer(new float[]{0.2f, 0.2f, 0.2f, 1.0f}));
		glLight(GL_LIGHT0,GL_POSITION, asFlippedBuffer(lightposition));
		glLight(GL_LIGHT0,GL_SPECULAR, asFlippedBuffer(new float[]{0.3f,0.3f,0.3f,1.0f}));
		

		glDepthFunc(GL_LEQUAL); // The Type Of Depth Test To Do
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // Really Nice Perspective Calculations

		skybox = new Skybox();

		Camera.create();        
	}

	private void initTextures(){
		road = new Vector<Texture>();
		try {
			road.add(TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/road.jpg"),GL_LINEAR));
		} catch (IOException e) {
			throw new RuntimeException("texture load errors");
		}
		
	}
	
	//clamp textures, that edges get dont create a line in between
	private void clampToEdge() {
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
		//glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		//glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
	}

	private void initShaders(){

		String vertex_shader = "";
		String fragment_shader = "";
		String vertex_shader2 = "";
		String fragment_shader2 = "";
		String fragment_shader3 = "";
		String fragment_shader4 = "";
		
		String vertex_check = "";
		String fragment_check = "";

		try {
			vertex_shader = readFileAsString("src/cactus.vert");
			fragment_shader = readFileAsString("src/cactus.frag");
			vertex_shader2 = readFileAsString("src/stripes.vert");
			fragment_shader2 = readFileAsString("src/stripes.frag");
			fragment_shader3 = readFileAsString("src/cop.frag");
			fragment_shader4 = readFileAsString("src/burj.frag");
			
			vertex_check = readFileAsString("src/checker.vert");
			fragment_check = readFileAsString("src/checker.frag");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			shader = new ShaderProgram(vertex_shader, fragment_shader);
			shader2 = new ShaderProgram(vertex_shader2, fragment_shader2);
			shader3 = new ShaderProgram(vertex_shader, fragment_shader3);
			shader4 = new ShaderProgram(vertex_shader, fragment_shader4);
			checker = new ShaderProgram(vertex_check, fragment_check);
		} catch (LWJGLException e) {
			System.out.println("NO");
			e.printStackTrace();
			System.exit(1);
		}
	}


	private void initChars(){
		barry = new Person(0.0f, 0.0f, 0.0f, new Vector3f(0.6f, 0.0f, 1.0f));				
		barry.setStart(new Vector3f(-5.5f, 0, -10f));

		charlie = new Person(0.0f, -0.04f, -15f, new Vector3f(1.0f, 1.0f, 0.0f));
		charlie.setStart(new Vector3f(5.5f, 0, -15f));

		danny = new Person(15f, -0.02f, -15f, new Vector3f(1.0f, 0.5f, 0.0f));
		danny.setStart(new Vector3f(5.5f, 0, -41f));
		danny.setSideways();
		
		eddie = new Person(0.0f, -0.04f, -15f, new Vector3f(0.0f, 0.5f, 1.0f));
		eddie.setStart(new Vector3f(-5.5f, 0, 75f));

		walter = new Person(0f, 0f, 0f, new Vector3f(0.0f, 0.0f, 0.0f));

		mater = new Truck(carList);
		hank = new Police(copList);
		jesse = new Police(copList);
		earl = new Blackhawk(hawkList);

		lamp = new Streetlamp(-5.5f, -1, -2.5f);
		lamp2 = new Streetlamp(-5.5f, -1, -12.5f);
		
		gus = new Gravestone(graveList, -15, 0);
		fring = new Gravestone(graveList, -15, -10);
		snakey = new Snake(0, -30, snakeList);
		
		harry = new Snake(40, 10, snakeList);
		harry.setSideways();
	}

	private void renderGL() {

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear The Screen And The Depth Buffer
		//glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		glLoadIdentity(); // Reset The View
		glTranslatef(0.0f, -3.0f, -7.0f); // Move Right And Into The Screen;

		Camera.apply();
		glPushMatrix();
		glTranslatef(Camera.pos.x, 0, Camera.pos.z);
		skybox.draw();
		glPopMatrix();

		switch(state) {
		case CITY:
			//make grid of the city
			//includes call to callLists(cityList)
			drawCity();
			drawHelicopter();
			drawCacti();

			drawCop();
			drawTruck();

			glCallList(roadList);
			
			//glCallList(officeList);
			//roadNS(0);
			//drawNSOffices();
			//drawEWOffices();
			
			lamp.draw();
			lamp2.draw();

			barry.spin();
			charlie.highWalk();
			eddie.highWalk();
			danny.highWalk();
			
			/*
			glPushMatrix();
			glTranslatef(0.5f, 0.5f, -0.8f);
			glRotatef(60,1,0,0);
			barry.drawLimb();
			glPopMatrix();
			*/
			
			glPushMatrix();
			glTranslatef(0, 0, 125);
			drawNSOffices();
			glPopMatrix();
			
			
			drawBurj();
			
			drawBank(0);
			
			
			drawPool();
			glPushMatrix();
			glTranslatef(20,0,0);
			drawPool();
			glPopMatrix();
			
			glPushMatrix();
			glTranslatef(-60, 0, -43);
			drawBank(180);
			glPopMatrix();
			
			
			drawResidence();
			glPushMatrix();
			glTranslatef(-20,0,0);
			drawResidence();
			glPopMatrix();
		//	*/
			break;
		case GAME:
			// meh
			//drawGrass();
			drawCacti();
			drawMoreCacti();
			drawVolks();
			
			/*
			glPushMatrix();
			glScalef(0.75f, 0.75f, 0.75f);
			glTranslatef(-15, -1, 0);
			glCallList(graveList);
			glTranslatef(0, 0, -10);
			glCallList(graveList);
			glPopMatrix();
			*/
			gus.draw();
			fring.draw();
			
			//glPushMatrix();
			//snakey.draw();
			//glPopMatrix();
			snakey.highWalk();
			harry.highWalk();
			
			
			if(gus.isNear(testDistancex,testDistancez)){
				//do something
			}
			
			break;
		}//end switch
		//can also put Camera.apply() in here if we want to make a start screen;

		moveWalter();
		//end Movement of Character

	}
	


	public void moveWalter(){
		//drawCharacter(0.0f, 0.0f, 0.0f);
		//Movement of character
		glPushMatrix();

		moveSpeed = 0.3f;
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

		if(testDistancey > 0){
			testDistancey -= 0.025f;
		}
		//by default y is 0
		glTranslatef(testDistancex, testDistancey, testDistancez);

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

		walter.hat();
		walter.draw();
		glPopMatrix();
	}
	
	
	
	public void drawVolks(){
		shader2.begin();
		shader2.setUniform3f("lightDir", 0, 18, 0);	
		glPushMatrix();
		glScalef(2f, 2f, 2f);
		glTranslatef(5, -0.5f, -5);
		glCallList(volksList);
		glPopMatrix();
		shader2.end();
	}
	
	public void drawCop(){
		shader3.begin();
		shader3.setUniform3f("lightDir", 0, 18, 0);	
		glPushMatrix();
		hank.cruise();
		glTranslatef(-70, 0, 85);
		jesse.cruise();
		glPopMatrix();
		shader2.end();
	}
	
	public void drawHelicopter(){
		glPushMatrix();
		earl.fly();
		glPopMatrix();
	}
	
	public void drawTruck(){
		shader.begin();
		shader.setUniform3f("lightDir", 0, 18, 0);	
		glPushMatrix();
		mater.cruise();
		glPopMatrix();
		shader.end();
	}

	public void drawCacti(){
		shader.begin();
		shader.setUniform3f("lightDir", 0, 18, 0);	
		glPushMatrix();

		//1
		glPushMatrix();
		glTranslatef(-30,-1,5);
		glRotatef(90,0,1,0);
		glCallList(cactusList);
		glPopMatrix();

		//2
		glPushMatrix();
		glTranslatef(-20,-1,8);
		glRotatef(90,0,1,0);
		glCallList(cactusList);
		glPopMatrix();
		
		//3
		glPushMatrix();
		glTranslatef(-40,-1,-15);
		glRotatef(90,0,1,0);
		glCallList(cactusList);
		glPopMatrix();
		
		//4
		glPushMatrix();
		glTranslatef(-50,-1,20);
		glRotatef(90,0,1,0);
		glCallList(cactusList);
		glPopMatrix();
		
		//5
		glPushMatrix();
		glTranslatef(30,-1,-2);
		glRotatef(90,0,1,0);
		glCallList(cactusList);
		glPopMatrix();

		//6
		glPushMatrix();
		glTranslatef(17,-1,30);
		glRotatef(90,0,1,0);
		glCallList(cactusList);
		glPopMatrix();
		
		//7
		glPushMatrix();
		glTranslatef(27,-1,-19);
		glRotatef(90,0,1,0);
		glCallList(cactusList);
		glPopMatrix();
		
		//8
		glPushMatrix();
		glTranslatef(45,-1,14);
		glRotatef(90,0,1,0);
		glCallList(cactusList);
		glPopMatrix();
		
		//9
		glPushMatrix();
		glTranslatef(39,-1,20);
		glRotatef(90,0,1,0);
		glCallList(cactusList);
		glPopMatrix();
		
		//10
		glPushMatrix();
		glTranslatef(-40,-1,77);
		glRotatef(90,0,1,0);
		glCallList(cactusList);
		glPopMatrix();

		glPopMatrix();
		shader.end();

	}
	
	public void drawMoreCacti(){
		shader.begin();
		shader.setUniform3f("lightDir", 0, 18, 0);	
		glPushMatrix();

		//1
		glPushMatrix();
		glTranslatef(0,-1,-25);
		glRotatef(90,0,1,0);
		glCallList(cactusList);
		glPopMatrix();

		//2
		glPushMatrix();
		glTranslatef(-10,-1,22);
		glRotatef(90,0,1,0);
		glCallList(cactusList);
		glPopMatrix();
		
		//3
		glPushMatrix();
		glTranslatef(-12,-1,-15);
		glRotatef(90,0,1,0);
		glCallList(cactusList);
		glPopMatrix();
		
		//4
		glPushMatrix();
		glTranslatef(20,-1,-17);
		glRotatef(90,0,1,0);
		glCallList(cactusList);
		glPopMatrix();
		
		//5
		glPushMatrix();
		glTranslatef(-3,-1,-2);
		glRotatef(90,0,1,0);
		glCallList(cactusList);
		glPopMatrix();

		//6
		glPushMatrix();
		glTranslatef(60,-1,30);
		glRotatef(90,0,1,0);
		glCallList(cactusList);
		glPopMatrix();
		
		//7
		glPushMatrix();
		glTranslatef(-70,-1,-19);
		glRotatef(90,0,1,0);
		glCallList(cactusList);
		glPopMatrix();
		
		//8
		glPushMatrix();
		glTranslatef(5,-1,14);
		glRotatef(90,0,1,0);
		glCallList(cactusList);
		glPopMatrix();
		
		//9
		glPushMatrix();
		glTranslatef(-9,-1,-18);
		glRotatef(90,0,1,0);
		glCallList(cactusList);
		glPopMatrix();
		
		//10
		glPushMatrix();
		glTranslatef(20,-1,-60);
		glRotatef(90,0,1,0);
		glCallList(cactusList);
		glPopMatrix();

		//11
		glPushMatrix();
		glTranslatef(-20,-1,80);
		glRotatef(90,0,1,0);
		glCallList(cactusList);
		glPopMatrix();
		
		//12
		glPushMatrix();
		glTranslatef(-64,-1,-58);
		glRotatef(90,0,1,0);
		glCallList(cactusList);
		glPopMatrix();
		
		//13
		glPushMatrix();
		glTranslatef(32,-1,60);
		glRotatef(90,0,1,0);
		glCallList(cactusList);
		glPopMatrix();
		
		//14
		glPushMatrix();
		glTranslatef(-60,-1,50);
		glRotatef(90,0,1,0);
		glCallList(cactusList);
		glPopMatrix();

		glPopMatrix();
		shader.end();

	}

	public void drawBurj(){
		shader4.begin();
		shader4.setUniform3f("lightDir", 0, 18, 0);	
		glPushMatrix();
		glTranslatef(125, 0, 142);
		glRotatef(270, 0, 1, 0);
		glScalef(5, 5, 5);
		glCallList(burjList);
		glPopMatrix();
		shader3.end();
	}
	
	public void drawPool(){
		shader4.begin();
		shader4.setUniform3f("lightDir", 0, 18, 0);	
		glPushMatrix();
		glTranslatef(18, 1, 114);
		glRotatef(270, 0, 1, 0);
		glScalef(5, 5, 5);
		glCallList(poolList);
		glPopMatrix();
		shader3.end();
	}
	
	public void drawBank(float angle){
		shader4.begin();
		shader4.setUniform3f("lightDir", 0, 18, 0);	
		glPushMatrix();
		glTranslatef(30, -9, 157);
		glRotatef(-15 + angle, 0, 1, 0);
		glScalef(3, 3, 3);
		glCallList(bankList);
		glPopMatrix();
		shader3.end();
	}
	
	public void drawResidence(){
		shader4.begin();
		shader4.setUniform3f("lightDir", 0, 18, 0);	
		glPushMatrix();
		glTranslatef(-25, -1, 165);
		glRotatef(20, 1, 0, 0);
		//glRotatef(10, 0, 1, 0);
		glScalef(10, 10, 10);
		glCallList(residenceList);
		glPopMatrix();
		shader3.end();
	}
	
	private void setUpDisplayLists(){		
		roadList = glGenLists(1);
		glNewList(roadList, GL_COMPILE);
		{
			glDisable(GL_LIGHTING);
			glEnable(GL_TEXTURE_2D);
			
			initTextures();
			roadNS(0);
			roadNS(-70);
			roadNS(70);
			
			roadEW(0);
			roadEW(-85);
			roadEW(85);
			
			glEnable(GL_LIGHTING);
			glDisable(GL_TEXTURE_2D);
		}
		glEndList();

		//next list
		carList = glGenLists(1);
		//System.out.println(statueList);
		String filename = "e102.obj";
		glNewList(carList,GL_COMPILE);
		{
			glEnable(GL_CULL_FACE);
			glCullFace(GL_BACK);
			Model m = null;
			try {
				m = OBJLoader.loadModel(new File("res/"+filename));
				//System.out.println("Loaded: "+filename);
				m.draw(1.0f, 1.0f, 1.0f);
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

			glDisable(GL_CULL_FACE);

		}
		glEndList();
		
		//next list
		bankList = glGenLists(1);
		//System.out.println(statueList);
		filename = "bank.obj";
		glNewList(bankList,GL_COMPILE);
		{
			glEnable(GL_CULL_FACE);
			glCullFace(GL_BACK);
			Model m = null;
			try {
				m = OBJLoader.loadModel(new File("res/"+filename));
				//System.out.println("Loaded: "+filename);
				m.draw(1.0f, 1.0f, 1.0f);
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

			glDisable(GL_CULL_FACE);

		}
		glEndList();
		
		//next list
		poolList = glGenLists(1);
		//System.out.println(statueList);
		filename = "pool.obj";
		glNewList(poolList,GL_COMPILE);
		{
			glEnable(GL_CULL_FACE);
			glCullFace(GL_BACK);
			Model m = null;
			try {
				m = OBJLoader.loadModel(new File("res/"+filename));
				//System.out.println("Loaded: "+filename);
				m.draw(1.0f, 1.0f, 1.0f);
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

			glDisable(GL_CULL_FACE);

		}
		glEndList();
		
		//next list
		
		residenceList = glGenLists(1);
		//System.out.println(statueList);
		filename = "residence.obj";
		glNewList(residenceList,GL_COMPILE);
		{
			glEnable(GL_CULL_FACE);
			glCullFace(GL_BACK);
			Model m = null;
			try {
				m = OBJLoader.loadModel(new File("res/"+filename));
				//System.out.println("Loaded: "+filename);
				m.draw(1.0f, 1.0f, 1.0f);
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

			glDisable(GL_CULL_FACE);

		}
		glEndList();
		
		//next list
		copList = glGenLists(1);
		//System.out.println(statueList);
		filename = "lanser2.obj";
		glNewList(copList,GL_COMPILE);
		{
			glEnable(GL_CULL_FACE);
			glCullFace(GL_BACK);
			Model m = null;
			try {
				m = OBJLoader.loadModel(new File("res/"+filename));
				//System.out.println("Loaded: "+filename);
				m.draw(1.0f, 1.0f, 1.0f);
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
			glDisable(GL_CULL_FACE);

		}
		glEndList();

		//next list
		cactusList = glGenLists(1);
		//System.out.println(statueList);
		filename = "cactus.obj";
		glNewList(cactusList,GL_COMPILE);
		{
			glEnable(GL_CULL_FACE);
			glCullFace(GL_BACK);
			Model m = null;
			try {
				m = OBJLoader.loadModel(new File("res/"+filename));
				//System.out.println("Loaded: "+filename);
				m.draw(1.0f, 1.0f, 1.0f);
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
			glDisable(GL_CULL_FACE);

		}
		glEndList();
		
		//next list
		volksList = glGenLists(1);
		//System.out.println(statueList);
		filename = "volksfinal.obj";
		glNewList(volksList,GL_COMPILE);
		{
			glEnable(GL_CULL_FACE);
			glCullFace(GL_BACK);
			Model m = null;
			try {
				m = OBJLoader.loadModel(new File("res/"+filename));
				//System.out.println("Loaded: "+filename);
				m.draw(1.0f, 1.0f, 1.0f);
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
			glDisable(GL_CULL_FACE);

		}
		glEndList();
		
		//next list
		hawkList = glGenLists(1);
		//System.out.println(statueList);
		filename = "black.obj";
		glNewList(hawkList,GL_COMPILE);
		{
			glEnable(GL_CULL_FACE);
			glCullFace(GL_BACK);
			Model m = null;
			try {
				m = OBJLoader.loadModel(new File("res/"+filename));
				//System.out.println("Loaded: "+filename);
				m.draw(0f, 0f, 0f);
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
			glDisable(GL_CULL_FACE);

		}
		glEndList();
		
		//next list
		officeList = glGenLists(1);
		//System.out.println(statueList);
		filename = "office.obj";
		glNewList(officeList,GL_COMPILE);
		{
			glEnable(GL_CULL_FACE);
			glCullFace(GL_BACK);
			Model m = null;
			try {
				m = OBJLoader.loadModel(new File("res/"+filename));
				//System.out.println("Loaded: "+filename);
				m.draw(0f, 0f, 0f);
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
			glDisable(GL_CULL_FACE);

		}
		glEndList();
		
		cityList = glGenLists(1);
		glNewList(cityList, GL_COMPILE);
		{

			glPushMatrix();
			//drawRoad(road,10f,10f);
			glRotatef(90, 0, 1, 0);
			glTranslatef(35,0,-12);
			drawBuildings();
			//drawOffice();
			glPopMatrix();

			glPushMatrix();
			//drawRoad(road,10f,10f);
			glRotatef(-90, 0, 1, 0);
			glTranslatef(-35,0,-12);
			drawBuildings();
			//drawOffice();
			glPopMatrix();

			glPushMatrix();
			//drawRoad(road,10f,10f);
			glRotatef(180, 0, 1, 0);
			glTranslatef(0,0,70);
			drawBuildings();
			//drawOffice();
			glPopMatrix();


			drawBuildings();
			//drawOffice();

		}
		glEndList();
		
		//next list
		graveList = glGenLists(1);
		//System.out.println(statueList);
		filename = "grave.obj";
		glNewList(graveList,GL_COMPILE);
		{
			glEnable(GL_CULL_FACE);
			glCullFace(GL_BACK);
			Model m = null;
			try {
				m = OBJLoader.loadModel(new File("res/"+filename));
				//System.out.println("Loaded: "+filename);
				m.draw(0.5f, 0.5f, 0.5f);
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
			glDisable(GL_CULL_FACE);

		}
		glEndList();
		
		//next list
		burjList = glGenLists(1);
		//System.out.println(statueList);
		filename = "burj.obj";
		glNewList(burjList,GL_COMPILE);
		{
			glEnable(GL_CULL_FACE);
			glCullFace(GL_BACK);
			Model m = null;
			try {
				m = OBJLoader.loadModel(new File("res/"+filename));
				//System.out.println("Loaded: "+filename);
				m.draw(0.5f, 0.5f, 0.5f);
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
			glDisable(GL_CULL_FACE);

		}
		glEndList();
		
		//next list
		snakeList = glGenLists(1);
		//System.out.println(statueList);
		filename = "snakehead.obj";
		glNewList(snakeList,GL_COMPILE);
		{
			glEnable(GL_CULL_FACE);
			glCullFace(GL_BACK);
			Model m = null;
			try {
				m = OBJLoader.loadModel(new File("res/"+filename));
				//System.out.println("Loaded: "+filename);
				m.draw(0.2f, 0.5f, 0.3f);
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
			glDisable(GL_CULL_FACE);

		}
		glEndList();
		
	}//end method

	//got from LWJGL Website

	private String readFileAsString(String filename) throws Exception {
		StringBuilder source = new StringBuilder();

		FileInputStream in = new FileInputStream(filename);

		Exception exception = null;

		BufferedReader reader;
		try{
			reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));

			Exception innerExc= null;
			try {
				String line;
				while((line = reader.readLine()) != null)
					source.append(line).append('\n');
			}
			catch(Exception exc) {
				exception = exc;
			}
			finally {
				try {
					reader.close();
				}
				catch(Exception exc) {
					if(innerExc == null)
						innerExc = exc;
					else
						exc.printStackTrace();
				}
			}

			if(innerExc != null)
				throw innerExc;
		}
		catch(Exception exc) {
			exception = exc;
		}
		finally {
			try {
				in.close();
			}
			catch(Exception exc) {
				if(exception == null)
					exception = exc;
				else
					exc.printStackTrace();
			}

			if(exception != null)
				throw exception;
		}

		return source.toString();
	}

	public void drawCity(){
		//drawGrass();
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

	private void drawGrass(){
		float de = rand.nextFloat() * -0.01f;
		
		glBegin(GL_QUADS);
		// MY Ground Plane - points have to go in counter clockwise order

		//glColor3f(0.5f, 1.0f, 0.5f); // Set The Color To Green
		glColor3f(0.76f, 0.7f, 0.64f);
		glVertex3f(-100.0f, -1.01f+de, -100f); // Top Right Of The Quad (Bottom)
		glVertex3f(100.0f, -1.01f+de, -100f); // Top Left Of The Quad (Bottom)
		glVertex3f(100.0f, -1.01f+de, 100f); // Bottom Left Of The Quad (Bottom)
		glVertex3f(-100.0f, -1.01f+de, 100f); // Bottom Right Of The Quad (Bottom)  
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

		float de = rand.nextFloat() * -0.01f;
		glPushMatrix();
		glBegin(GL_QUADS);
		glColor3f(0.3f, 0.3f, 0.3f); // Set The Color To Green
		glVertex3f(-5.0f, -1f+de, -35f); // Top Right Of The Quad (Bottom)
		glVertex3f(5.0f, -1f+de, -35f); // Top Left Of The Quad (Bottom)
		glVertex3f(5.0f, -1f+de, 10f); // Bottom Left Of The Quad (Bottom)
		glVertex3f(-5.0f, -1f+de, 10f); // Bottom Right Of The Quad (Bottom)

		//sidewalk 1
		glColor3f(0.8f, 0.8f, 0.8f); // Set The Color To Green
		glVertex3f(-15.0f, -1.001f+de, -30f); // Top Right Of The Quad (Bottom)
		glVertex3f(-5.0f, -1.001f+de, -30f); // Top Left Of The Quad (Bottom)
		glVertex3f(-5.0f, -1.001f+de, 10f); // Bottom Left Of The Quad (Bottom)
		glVertex3f(-15.0f, -1.001f+de, 10f); // Bottom Right Of The Quad (Bottom)

		//sidewalk2
		glColor3f(0.8f, 0.8f, 0.8f); // Set The Color To Green
		glVertex3f(5.0f, -1.001f+de, -30f); // Top Right Of The Quad (Bottom)
		glVertex3f(15.0f, -1.001f+de, -30f); // Top Left Of The Quad (Bottom)
		glVertex3f(15.0f, -1.001f+de, 10f); // Bottom Left Of The Quad (Bottom)
		glVertex3f(5.0f, -1.001f+de, 10f); // Bottom Right Of The Quad (Bottom)

		glEnd(); // Done Drawing The Quad

		glPopMatrix();

	}
	
	public void drawOffice(){
		checker.begin();
		float dir = (float)(1./Math.sqrt(3));
		checker.setUniform3f("lightDir", 0, 18, 0);	
		
		glPushMatrix();
		glScalef(2.5f, 2.5f, 2.5f);
		
		glPushMatrix();
		glTranslatef(-5, -0.5f, -10);
		glRotatef(-90, 0, 1, 0);
		glCallList(officeList);
		glPopMatrix();
		
		glPushMatrix();
		glTranslatef(-5, -0.5f, 20);
		glRotatef(-90, 0, 1, 0);
		glCallList(officeList);
		glPopMatrix();
		
		glPushMatrix();
		glTranslatef(-5, -0.5f, 12);
		glRotatef(90, 0, 1, 0);
		glCallList(officeList);
		glPopMatrix();
		
		glPushMatrix();
		glTranslatef(5, -0.5f, -5);
		glRotatef(90, 0, 1, 0);
		glCallList(officeList);
		glPopMatrix();
		
		glPushMatrix();
		glTranslatef(5, -0.5f, -10);
		glRotatef(-90, 0, 1, 0);
		glCallList(officeList);
		glPopMatrix();
		
		glPushMatrix();
		glTranslatef(5, -0.5f, 13);
		glRotatef(-90, 0, 1, 0);
		glCallList(officeList);
		glPopMatrix();
		
		glPushMatrix();
		glTranslatef(5, -0.5f, 19);
		glRotatef(90, 0, 1, 0);
		glCallList(officeList);
		glPopMatrix();
		
		glPopMatrix();
		checker.end();
	}
	
	public void drawOffice2(){
		checker.begin();
		float dir = (float)(1./Math.sqrt(3));
		checker.setUniform3f("lightDir", 0, 18, 0);	
		
		glPushMatrix();
		glScalef(2, 2, 2);
		
		glPushMatrix();
		glTranslatef(-28, -0.5f, 20);
		glRotatef(0, 0, 1, 0);
		glCallList(officeList);
		glPopMatrix();
		
		glPushMatrix();
		glTranslatef(-22, -0.5f, 20);
		glRotatef(180, 0, 1, 0);
		glCallList(officeList);
		glPopMatrix();
		
		glPushMatrix();
		glTranslatef(-12, -0.5f, 20);
		glRotatef(0, 0, 1, 0);
		glCallList(officeList);
		glPopMatrix();
		
		glPopMatrix();
		checker.end();
	}
	
	
	public void drawNSOffices(){
		drawOffice();
		
		glPushMatrix();
		glTranslatef(-70, 0, 0);
		drawOffice();
		glPopMatrix();
		
		glPushMatrix();
		glTranslatef(70, 0, 0);
		drawOffice();
		glPopMatrix();
	}
	
	
	public void drawEWOffices(){
		drawOffice2();
	}
	private void roadNS(float x){
		glPushMatrix();
		glNormal3f(0,1,0);
		glTranslatef(x, 0, 0);
        clampToEdge();
        road.get(0).bind();
		glBegin(GL_QUADS);
		glColor3f(0.3f, 0.3f, 0.3f); // Set The Color To Green
		glTexCoord2f(1, 1); glVertex3f(15.0f, -1f, 175f); // Top Right Of The Quad (Bottom)
		glTexCoord2f(0, 1); glVertex3f(-15.0f, -1f, 175f); // Top Left Of The Quad (Bottom)
		glTexCoord2f(0, 0); glVertex3f(-15.0f, -1f, -90f); // Bottom Left Of The Quad (Bottom)
		glTexCoord2f(1, 0); glVertex3f(15.0f, -1f, -90f); // Bottom Right Of The Quad (Bottom)
		glEnd();
		glPopMatrix();
		
	}
	
	private void roadEW(float z){
		glPushMatrix();
		glNormal3f(0,1,0);
		glTranslatef(0, 0, z);
        clampToEdge();
		road.get(0).bind();
		glBegin(GL_QUADS);
		glColor3f(0.3f, 0.3f, 0.3f); // Set The Color To Green
		glTexCoord2f(1, 0); glVertex3f(110.0f, -1.1f, 35f); // Top Right Of The Quad (Bottom)
		glTexCoord2f(1, 1); glVertex3f(-110.0f, -1.1f, 35f); // Top Left Of The Quad (Bottom)
		glTexCoord2f(0, 1); glVertex3f(-110.0f, -1.1f, 65f); // Bottom Left Of The Quad (Bottom)
		glTexCoord2f(0, 0); glVertex3f(110.0f, -1.1f, 65f); // Bottom Right Of The Quad (Bottom)
		glEnd();
		glPopMatrix();
	}
	
	public void drawBuildings(){
		//drawRoad();
		//drawStreetSign();

		for(float b = -20f; b <= 0; b = b + 10){
			drawBuilding(-7.0f,b,0);
			//drawOffice(-7, b, 0);
		}

		for(float b = -20f; b <= 0; b = b + 10){
			drawBuilding(7.0f,b,1);
			//drawOffice(7, b, 1);
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
		glColor3f(0.4f, 0.4f, 0.4f); // Set The Color To Green
		glNormal3f(0,1,0);
		glVertex3f(xa, ya, za); // Top Right Of The Quad (Top)
		glVertex3f(xb, ya, za); // Top Left Of The Quad (Top)
		glVertex3f(xb, ya, zb); // Bottom Left Of The Quad (Top)
		glVertex3f(xa, ya, zb); // Bottom Right Of The Quad (Top)

		//glColor3f(1.0f, 0.0f, 0.0f); // Set The Color To Orange
		glNormal3f(0,-1,0);
		glVertex3f(xa, yb, zb); // Top Right Of The Quad (Bottom)
		glVertex3f(xb, yb, zb); // Top Left Of The Quad (Bottom)
		glVertex3f(xb, yb, za); // Bottom Left Of The Quad (Bottom)
		glVertex3f(xa, yb, za); // Bottom Right Of The Quad (Bottom)

		//glColor3f(1.0f, 0.0f, 0.0f); // Set The Color To Red
		glNormal3f(0,0,1);
		glVertex3f(xa, ya, zb); // Top Right Of The Quad (Front)
		glVertex3f(xb, ya, zb); // Top Left Of The Quad (Front)
		glVertex3f(xb, yb, zb); // Bottom Left Of The Quad (Front)
		glVertex3f(xa, yb, zb); // Bottom Right Of The Quad (Front)

		//glColor3f(1.0f, 1.0f, 0.0f); // Set The Color To Yellow
		glNormal3f(0,0,-1);
		glVertex3f(xa, yb, za); // Bottom Left Of The Quad (Back)
		glVertex3f(xb, yb, za); // Bottom Right Of The Quad (Back)
		glVertex3f(xb, ya, za); // Top Right Of The Quad (Back)
		glVertex3f(xa, ya, za); // Top Left Of The Quad (Back)

		//glColor3f(0.0f, 0.0f, 0.8f); // Set The Color To Blue
		glNormal3f(-1,0,0);
		glVertex3f(xb, ya, zb); // Top Right Of The Quad (Left)
		glVertex3f(xb, ya, za); // Top Left Of The Quad (Left)
		glVertex3f(xb, yb, za); // Bottom Left Of The Quad (Left)
		glVertex3f(xb, yb, zb); // Bottom Right Of The Quad (Left)

		//glColor3f(1.0f, 0.0f, 1.0f); // Set The Color To Violet
		glNormal3f(1,0,0);
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
				else if (Keyboard.getEventKey() == Keyboard.KEY_F1){
					state = State.CITY;
				}
				else if (Keyboard.getEventKey() == Keyboard.KEY_F2){
					state = State.GAME;
				}
				else if (Keyboard.getEventKey() == Keyboard.KEY_LCONTROL){
					//jump = true;
					testDistancey = 1;
				}
				//movement = false;
			}
			else{
				moveLeft = false;
				moveRight = false;
				moveUp = false;
				moveDown = false;
				//jump = false;
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

	private void updateLogic(int delta) {
		triangleAngle += 0.1f * delta; // Increase The Rotation Variable For The Triangles
		quadAngle -= 0.05f * delta; // Decrease The Rotation Variable For The Quads
	}

	private FloatBuffer asFlippedBuffer(float[] values){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(values.length);
		buffer.put(values);
		buffer.flip();
		return buffer;
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
		new BigCity().run();
	}

	public static class Camera {
		public static float moveSpeed = 0.5f;

		private static float maxLook = 300;

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
