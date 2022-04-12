package com.jimphieffer.graphics;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.system.*;

import java.util.*;

import static org.lwjgl.opengl.GL30.*;

public class Uniforms {
    private static final Map<Integer, Map<String, Integer>> uniforms = new HashMap<>();
    public static void createUniform(String uniformName, int programId) {
        int uniformLocation = glGetUniformLocation(programId, uniformName);
        if (uniformLocation < 0) {
            System.err.println("Warning: Could not find uniform \"" + uniformName + "\".");
        } else {
            if(!uniforms.containsKey(programId)) {
                uniforms.put(programId, new HashMap<>());
            }
            uniforms.get(programId).put(uniformName, uniformLocation);
        }
    }

    public static void setUniform(String uniformName, int value, int programId) {
        glUniform1i(uniforms.get(programId).get(uniformName), value);
    }
    public static void setUniform(String uniformName, Matrix4f value, int programId) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            glUniformMatrix4fv(uniforms.get(programId).get(uniformName), false,
                    value.get(stack.mallocFloat(16)));
        } catch (Exception e) {
            System.err.println("Warning: Could not set uniform \"" + uniformName + "\".");
        }
    }

    public static void setUniform(String uniformName, Vector4f value, int programId) {
        glUniform4f(uniforms.get(programId).get(uniformName), value.x, value.y, value.z, value.w);
    }
}
