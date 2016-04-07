import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopAttrib;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.io.IOException;
import java.util.Vector;

import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

// https://code.google.com/p/lwjgl-water-shader/source/browse/trunk/src/edu/fhooe/mtd360/watershader/objects/SkyBox.java?r=27
// http://www.redsorceress.com/skybox.html
public class Skybox {

	private Vector<Texture> textures;


	public Skybox() {

		textures = new Vector<Texture>();

		//load textures
		try {
			textures.add(TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/skybox/front.jpg"),GL_LINEAR));
			textures.add(TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/skybox/left.jpg"),GL_LINEAR));
			textures.add(TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/skybox/back.jpg"),GL_LINEAR));
			textures.add(TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/skybox/right.jpg"),GL_LINEAR));
			textures.add(TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/skybox/up.jpg"),GL_LINEAR));
			textures.add(TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/skybox/down.jpg"),GL_LINEAR));
		} catch (IOException e) {
			throw new RuntimeException("skybox loading error");
		}

	}

	public void draw() {

		glDisable(GL_LIGHTING);
		glEnable(GL_TEXTURE_2D);
		glPushMatrix();

		glScalef(500, 500, 500);
        // Render the front quad
        clampToEdge();
        textures.get(0).bind();
        glBegin(GL_QUADS);
                glTexCoord2f(1, 1); glVertex3f(  0.5f, -0.5f, -0.5f );
                glTexCoord2f(0, 1); glVertex3f( -0.5f, -0.5f, -0.5f );
                glTexCoord2f(0, 0); glVertex3f( -0.5f,  0.5f, -0.5f );
                glTexCoord2f(1, 0); glVertex3f(  0.5f,  0.5f, -0.5f );
        glEnd();

        // Render the left quad
        clampToEdge();
        textures.get(1).bind();
        glBegin(GL_QUADS);
                glTexCoord2f(1, 1); glVertex3f(  0.5f, -0.5f,  0.5f );
                glTexCoord2f(0, 1); glVertex3f(  0.5f, -0.5f, -0.5f );
                glTexCoord2f(0, 0); glVertex3f(  0.5f,  0.5f, -0.5f );
                glTexCoord2f(1, 0); glVertex3f(  0.5f,  0.5f,  0.5f );
        glEnd();
        
        // Render the back quad
        clampToEdge();
        textures.get(2).bind();
        glBegin(GL_QUADS);
                glTexCoord2f(1, 1); glVertex3f( -0.5f, -0.5f,  0.5f );
                glTexCoord2f(0, 1); glVertex3f(  0.5f, -0.5f,  0.5f );
                glTexCoord2f(0, 0); glVertex3f(  0.5f,  0.5f,  0.5f );
                glTexCoord2f(1, 0); glVertex3f( -0.5f,  0.5f,  0.5f );
     
        glEnd();
        
        // Render the right quad
        clampToEdge();
        textures.get(3).bind();
        glBegin(GL_QUADS);
                glTexCoord2f(1, 1); glVertex3f( -0.5f, -0.5f, -0.5f );
                glTexCoord2f(0, 1); glVertex3f( -0.5f, -0.5f,  0.5f );
                glTexCoord2f(0, 0); glVertex3f( -0.5f,  0.5f,  0.5f );
                glTexCoord2f(1, 0); glVertex3f( -0.5f,  0.5f, -0.5f );
        glEnd();
        
        // Render the top quad
        clampToEdge();
        textures.get(4).bind();
        glBegin(GL_QUADS);
                glTexCoord2f(1, 0); glVertex3f( -0.5f,  0.5f, -0.5f );
                glTexCoord2f(1, 1); glVertex3f( -0.5f,  0.5f,  0.5f );
                glTexCoord2f(0, 1); glVertex3f(  0.5f,  0.5f,  0.5f );
                glTexCoord2f(0, 0); glVertex3f(  0.5f,  0.5f, -0.5f );
        glEnd();
        
        // Render the bottom quad
        clampToEdge();
        textures.get(5).bind();
        glBegin(GL_QUADS);
                glTexCoord2f(1, 1); glVertex3f( -0.5f, -0.5f, -0.5f );
                glTexCoord2f(1, 0); glVertex3f( -0.5f, -0.5f,  0.5f );
                glTexCoord2f(0, 0); glVertex3f(  0.5f, -0.5f,  0.5f );
                glTexCoord2f(0, 1); glVertex3f(  0.5f, -0.5f, -0.5f );
        glEnd();
     
        // Restore enable bits and matrix
        //glPopAttrib();
        glPopMatrix();
		glEnable(GL_LIGHTING);
		glDisable(GL_TEXTURE_2D);

	}

	//clamp textures, that edges get dont create a line in between
	private void clampToEdge() {
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
	}

}