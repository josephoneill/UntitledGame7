#ifdef GL_ES
	precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;
uniform vec3 u_color;

void main() {
	vec4 color = texture2D(u_texture, v_texCoords);
	
	if(color.a != 0) {
		gl_FragColor = vec4(u_color, 1.0);
	}
}
