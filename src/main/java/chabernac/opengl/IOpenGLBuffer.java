package chabernac.opengl;

public interface IOpenGLBuffer extends IOpengGLObject {
    public void setVertexBufferIndex( int aBufferIndex );
    public void setVertexArrayIndex(int aVertexArrayIndex);
    public void setElementBufferIndex(int elementBufferIndex);
}
