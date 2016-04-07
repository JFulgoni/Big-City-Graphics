import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.awt.List;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

//got from tutorial at https://www.youtube.com/watch?v=izKAvSV3qk0&list=PL19F2453814E0E315&index=24

public class Model {
	public ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
	public ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
	public ArrayList<Face> faces = new ArrayList<Face>();
	
	public void draw(float r, float g, float b){
		glBegin(GL_TRIANGLES);
		for (Face face : this.faces){
			glColor3f(r, g, b);
			Vector3f n1 = this.normals.get((int) face.normal.x - 1);
			glNormal3f(n1.x, n1.y, n1.z);
			Vector3f v1 = this.vertices.get((int) face.vertex.x - 1);
			glVertex3f(v1.x, v1.y, v1.z);

			Vector3f n2 = this.normals.get((int) face.normal.y - 1);
			glNormal3f(n2.x, n2.y, n2.z);
			Vector3f v2 = this.vertices.get((int) face.vertex.y - 1);
			glVertex3f(v2.x, v2.y, v2.z);

			Vector3f n3 = this.normals.get((int) face.normal.z - 1);
			glNormal3f(n3.x, n3.y, n3.z);
			Vector3f v3 = this.vertices.get((int) face.vertex.z - 1);
			glVertex3f(v3.x, v3.y, v3.z);

		}
		glEnd();
	}
}
