varying vec3 normalInterp;
varying vec3 vertPos;
varying vec3 xypos;
 
const vec3 lightPos = vec3(1.0,1.0,1.0);

const vec3 blackAmbient = vec3(0.1,0.1,0.1);
const vec3 blackDiffuse = vec3(0.1,0.1,0.1);

const vec3 whiteAmbient = vec3(0.1, 0.1, 0.1);
const vec3 whiteDiffuse = vec3(0.5, 0.5, 0.5);

const vec3 specColor = vec3(1.0, 1.0, 1.0);
 
const float checker = 0.5;

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
 
    vec3 position = xypos / checker;
    
    float topAngle = (atan(-position.z,position.x)*180/3.1415)/50;
     
    if (mod(floor(position.y) + floor(topAngle),2) == 1 ) {
        gl_FragColor = vec4(blackAmbient + diffuseTerm * blackDiffuse + specTerm * specColor, 1.0);
    }
    else {
        gl_FragColor = vec4(whiteAmbient + diffuseTerm * whiteDiffuse + specTerm * specColor, 1.0);
    }
   
 	 
}