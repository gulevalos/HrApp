package com.example.mardiak.marek.hrapp.myOpenGl;

/**
 * Created by mm on 3/23/2016.
 */
public class CubeModel {

    public static float mCubeRotationX = 0.2f;
    public static float mCubeRotationY = 0.2f;
    public static float mCubeRotationZ = 1f;

    public static float verticesCube[] = {
            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f
    };

    static {
        for (int i = 0; i < verticesCube.length; i++)
            verticesCube[i] = verticesCube[i] / 3;
    }

    public static short indicesCube[] = { //order of vertices
            0, 4, 5, 0, 5, 1,
            1, 5, 6, 1, 6, 2,
            2, 6, 7, 2, 7, 3,
            3, 7, 4, 3, 4, 0,
            4, 7, 6, 4, 6, 5,
            3, 0, 1, 3, 1, 2
    };

    public static float colorsCube[] = {
            0.3f, 0.2f, 1.0f, 1.0f,
    };

}
