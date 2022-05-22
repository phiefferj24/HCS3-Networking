//package com.jimphieffer.audio;
//
//import org.lwjgl.openal.*;
//import static org.lwjgl.openal.ALC10.*;
//import static org.lwjgl.openal.ALCCapabilities.*;
//import org.lwjgl.BufferUtils;
//
//import java.nio.*;
//
//public class AudioPlayer {
//    public AudioPlayer() {
//        //Start by acquiring the default device
//        long device = ALC10.alcOpenDevice((ByteBuffer)null);
//
//        //Create a handle for the device capabilities, as well.
//        ALCCapabilities deviceCaps = ALC.createCapabilities(device);
//        // Create context (often already present, but here, necessary)
//        IntBuffer contextAttribList = BufferUtils.createIntBuffer(16);
//
//        // Note the manner in which parameters are provided to OpenAL...
//        contextAttribList.put(ALC_REFRESH);
//        contextAttribList.put(60);
//
//        contextAttribList.put(ALC_SYNC);
//        contextAttribList.put(ALC_FALSE);
//
//        // Don't worry about this for now; deals with effects count
//        contextAttribList.put(ALC_MAX_AUXILIARY_SENDS);
//        contextAttribList.put(2);
//
//        contextAttribList.put(0);
//        contextAttribList.flip();
//
//        //create the context with the provided attributes
//        long newContext = ALC10.alcCreateContext(device, contextAttribList);
//
//        if(!ALC10.alcMakeContextCurrent(newContext)) {
//            System.err.println("Failed to create audio context");
//        }
//
//        AL.createCapabilities(deviceCaps);
//
//
//        //define listener
//        AL10.alListener3f(AL10.AL_VELOCITY, 0f, 0f, 0f);
//        AL10.alListener3f(AL10.AL_ORIENTATION, 0f, 0f, -1f);
//
//
//        IntBuffer buffer = BufferUtils.createIntBuffer(1);
//        AL10.alGenBuffers(buffer);
//
//        //We'll get to this next!
//        long time = createBufferData(buffer.get(0));
//
//        //Define a source
//        int source = AL10.alGenSources();
//        AL10.alSourcei(source, AL10.AL_BUFFER, buffer.get(0));
//        AL10.alSource3f(source, AL10.AL_POSITION, 0f, 0f, 0f);
//        AL10.alSource3f(source, AL10.AL_VELOCITY, 0f, 0f, 0f);
//
//        //fun stuff
//        AL10.alSourcef(source, AL10.AL_PITCH, 1);
//        AL10.alSourcef(source, AL10.AL_GAIN, 1f);
//        AL10.alSourcei(source, AL10.AL_LOOPING, AL10.AL_FALSE);
//
//        //Trigger the source to play its sound
//        AL10.alSourcePlay(source);
//
//        try {
//            Thread.sleep(time); //Wait for the sound to finish
//        } catch(InterruptedException ex) {}
//
//        AL10.alSourceStop(source); //Demand that the sound stop
//
//        //and finally, clean up
//        AL10.alDeleteSources(source);
//
//
//    }
//}
