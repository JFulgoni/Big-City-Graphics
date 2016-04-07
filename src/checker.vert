varying vec3 normalInterp;
varying vec3 vertPos;
varying vec3 xypos;

void main(){
	xypos = gl_Vertex.xyz;
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
    vec4 vertPos4 = gl_ModelViewMatrix * gl_Vertex;
    vertPos = vec3(vertPos4) / vertPos4.w;
    normalInterp = gl_NormalMatrix * gl_Normal;
}