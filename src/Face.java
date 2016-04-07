import org.lwjgl.util.vector.Vector3f;

//got from tutorial at https://www.youtube.com/watch?v=izKAvSV3qk0&list=PL19F2453814E0E315&index=24

public class Face {
	public Vector3f vertex = new Vector3f(); //three indices
	public Vector3f normal = new Vector3f(); 
	
	public Face(Vector3f vertex, Vector3f normal){
		this.vertex = vertex;
		this.normal = normal;
	}
}
