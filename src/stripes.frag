varying vec3 normalInterp;
varying vec3 vertPos;
varying vec3 xypos;
 
const vec3 lightPos = vec3(1.0,1.0,1.0);

const vec3 blueAmbient = vec3(0.1,0.1,0.1);
const vec3 blueDiffuse = vec3(0.1,0.1,0.1);

const vec3 grayAmbient = vec3(0.1, 0.1, 0.1);
const vec3 grayDiffuse = vec3(1.0, 0.9, 0.6);

const vec3 specColor = vec3(1.0, 1.0, 1.0);
 
const float stripes = 1.8;

void main() {
 
  vec3 normal = normalize(normalInterp);
  vec3 lightDir = normalize(lightPos - vertPos);
 
  float diffuseTerm = max(dot(lightDir,normal), 0.0);
  float specTerm = 0.0;
 
  if(diffuseTerm > 0.0) {
 
    vec3 viewDir = normalize(-vertPos);
 
    vec3 halfDir = normalize(lightDir + viewDir);
    float specAngle = max(dot(halfDir, normal), 0.0);
    specTerm = pow(specAngle, 16.0);
  }
 
    vec3 position = xypos / stripes;

  	if( fract(position.y) < 0.8){
    	gl_FragColor = vec4(blueAmbient + diffuseTerm * blueDiffuse + specTerm * specColor, 1.0);
 	}
  	if( fract(position.y) > 0.2){
   	 	gl_FragColor = vec4(grayAmbient + diffuseTerm * grayDiffuse + specTerm * specColor, 1.0);
  	}
 	 
}