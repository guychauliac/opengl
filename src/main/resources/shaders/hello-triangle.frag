#version 330 core

in vec3 fragmentColor;

out vec4 FragColor;

void main()
{
	FragColor = vec4(fragmentColor, 1.0);
    //FragColor = vec4(0f, 0f, 1.0f, 1.0f);
} 