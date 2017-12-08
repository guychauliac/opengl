#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 color;
layout (location = 2) in vec2 aTextureCoord;

out vec3 fragmentColor;
out vec2 textureCoord;

void main()
{
    gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0);
    fragmentColor = color;
    textureCoord = aTextureCoord;
}
