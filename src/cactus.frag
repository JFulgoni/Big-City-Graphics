varying vec3 normalInterp;
varying vec3 vertPos;
varying vec3 xypos;
 
const vec3 lightPos = vec3(1.0,1.0,1.0);
const vec3 ambientColor = vec3(0.1, 0.1, 0.1);
const vec3 diffuseColor = vec3(0.36, 0.46, 0.37);
const vec3 specColor = vec3(1.0, 1.0, 1.0);
 
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

  gl_FragColor = vec4(ambientColor + diffuseTerm * diffuseColor + specTerm * specColor, 1.0);
}