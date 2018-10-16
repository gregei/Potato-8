package com.gregei.potato8.core;

import com.gregei.potato8.core.cpu.CPU;
import com.gregei.potato8.core.memory.Memory;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Chip8 {
    /**
     * The CPU class instance.
     */
    private CPU cpu;

    /**
     * The Memory class instance.
     */
    private Memory memory;

    public Chip8(){
        cpu = new CPU();
        memory = new Memory();

        cpu.setMemory(memory);
    }

    /**
     * Runs one cpu cycle.
     */
    public void runCycle(){
        cpu.runCycle();
    }

    /**
     * Load a game to the emulator.
     *
     * @param path the string path of the rom file.
     * @return true if the game is found, false if not found or is invalid
     */
    public boolean loadGame(String path){
        reset();

        try {
            byte[] rom = Files.readAllBytes(Paths.get(path));

            for (int i = 0; i < rom.length; ++i){
                memory.getMemory()[i + 512] = (char)(rom[i] & 0xFF);
            }
        } catch (Exception e) {
            //e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Sets the draw flag.
     *
     * @param value boolean to set the draw flag
     */
    public void setDraw(boolean value){
        cpu.drawFlag = value;
    }

    /**
     * Sets the selected key to the boolean passed.
     *
     * @param key which key is being set
     * @param pressed boolean value to set key
     */
    public void setKey(int key, boolean pressed){
        cpu.getKeys()[key] = pressed ? 1 : 0;
    }

    /**
     * Return the graphics in an array.
     *
     * @return the int[] containing the graphics to be drawn.
     */
    public int[] getFrameBuffer(){
        return cpu.getGraphics();
    }

    /**
     * Returns the draw flag.
     *
     * @return If the game is ready to draw.
     */
    public boolean isDrawReady(){
        return cpu.getDrawFlag();
    }

    /**
     * Returns the cpu running boolean.
     *
     * @return true if the cpu is running, otherwise halted.
     */
    public boolean isCpuRunning(){
        return cpu.getCpuRunning();
    }

    /**
     * Sets the cpu running boolean
     *
     * @param value boolean to set cpu running boolean
     */
    public void setCpuRunning(boolean value){
        cpu.setCpuRunning(value);
    }

    /**
     * Resets the emulator
     */
    private void reset(){
        cpu.reset();
        memory.reset();
    }
}