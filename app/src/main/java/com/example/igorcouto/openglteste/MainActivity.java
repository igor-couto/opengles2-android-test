package com.example.igorcouto.openglteste;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Render m_render = new Render();

        // Cria a view do OpenGL
        GLSurfaceView glView = new GLSurfaceView(this);
        // Diz a versão do OpenGl a ser utilizada
        glView.setEGLContextClientVersion(2);
        // Configuração dos atributos do OpenGL ( Tamanho dos canais de cor, depth buffer e stencil )
        glView.setEGLConfigChooser( 8, 8, 8, 8, 16, 0);
        // Passa o Render para a view
        glView.setRenderer(m_render);

        setContentView(glView);
    }
}
