package chabernac.opengl;

public interface IOpenGLShape extends IOpengGLObject {
    public void setVertexBufferIndex( int aBufferIndex );
    public void setVertexArrayIndex(int aVertexArrayIndex);
    public void setElementBufferIndex(int elementBufferIndex);
}
