package com.jimphieffer.graphics;

import org.joml.Matrix4f;
import org.lwjgl.system.*;

import java.util.*;

import static org.lwjgl.opengl.GL30.*;

public class Uniforms {
    private static final Map<String, Integer> uniforms = new HashMap<>();
    public static void createUniform(String uniformName, int programId) {
        int uniformLocation = glGetUniformLocation(programId, uniformName);
        if (uniformLocation < 0) {
            System.err.println("Warning: Could not find uniform \"" + uniformName + "\".");
        } else {
            uniforms.put(uniformName, uniformLocation);
        }
    }

    public static void setUniform(String uniformName, int value) {
        glUniform1i(uniforms.get(uniformName), value);
    }
    public static void setUniform(String uniformName, Matrix4f value) {
        // Dump the matrix into a float buffer
        try (MemoryStack stack = MemoryStack.stackPush()) {
            glUniformMatrix4fv(uniforms.get(uniformName), false,
                    value.get(stack.mallocFloat(16)));
        } catch (Exception e) {
            System.err.println("Warning: Could not set uniform \"" + uniformName + "\".");
        }
    }
}
