#version 330 core

in vec3 fragmentColor;
in vec2 textureCoord;

out vec4 FragColor;

uniform sampler2D ourTexture;

void main()
{
	//FragColor = vec4(fragmentColor, 1.0);
    //FragColor = vec4(0f, 0f, 1.0f, 1.0f);
    FragColor = texture(ourTexture, textureCoord);
} 