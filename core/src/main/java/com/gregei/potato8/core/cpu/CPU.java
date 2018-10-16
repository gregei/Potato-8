package com.gregei.potato8.core.cpu;

import com.gregei.potato8.core.memory.Memory;

import java.util.Arrays;
import java.util.Random;

public class CPU {
    /**
     * Instance of Memory class
     */
    private Memory memory;

    /**
     * Array holding the graphics of the emulator.
     */
    private int[] graphics = new int[64 * 32];

    /**
     * Flag to hold if the cpu is running.
     */
    private boolean cpuRunning;

    /**
     * Flag to tell if emulator is ready to draw.
     */
    public boolean drawFlag;

    /**
     * 16 8-bit general purpose registers.
     */
    private char[] v = new char[16];

    /**
     * 16 16-bit values, used to store addresses that the
     * interpreter should return when finished with subroutine.
     */
    private char[] stack = new char[16];

    /**
     * 16-bit register generally used to store memory addresses, so only the first
     * 12 bits are used
     */
    private char I;

    /**
     * 8-bit register which points to the topmost level of the stack.
     */
    private char sp;

    /**
     * 16 register used to store the currently executing address.
     */
    private char pc;

    /**
     * The currently executing opcode.
     */
    private char opcode;

    /**
     * Two 8-bit registers used for delay and sound timers.
     */
    private char delayTimer, soundTimer;

    /**
     * The keypad keys which holds whether the are pressed or not.
     */
    private int[] keys = new int[16];

    /**
     * Runs one cpu cycle.
     */
    public void runCycle(){
        opcode = (char)((memory.getMemory()[pc++] << 8) | memory.getMemory()[pc++]);

        switch (opcode & 0xF000){
            case 0x0000:
                switch (opcode & 0x00FF){
                    case 0x00E0:
                        /*
                        * 00E0 - CLS
                        * Clear the display.
                        */

                        Arrays.fill(graphics, 0);
                        break;

                    case 0x00EE:
                        /*
                        * 00EE - RET
                        * Return from a subroutine.
                        * The interpreter sets the program counter to the address at the top of the stack, then
                        * subtracts 1 from the stack pointer.
                        */

                        pc = stack[sp--];
                        break;

                    default:
                        System.out.println("Unimplemented 0000 Opcode " + Integer.toHexString(opcode));
                        cpuRunning = false;
                        break; // End of Internal 0000 switch
                }
                break; // End of 0000 switch

            case 0x1000:
                /*
                * 1nnn - JP addr
                * Jump to location nnn.
                *
                * The interpreter sets the program counter to nnn.
                */

                pc = getNNN();
                break;

            case 0x2000:
                /*
                * 2nnn - CALL addr
                * Call subroutine at nnn.
                *
                * The interpreter increments the stack pointer, then puts the current PC on the top of the
                * stack. The PC is then set to nnn.
                */

                stack[++sp] = pc;
                pc = getNNN();
                break;

            case 0x3000:
                /*
                * 3xkk - SE Vx, byte
                * Skip next instruction if Vx = kk.
                *
                * The interpreter compares register Vx to kk, and if they are equal, increments the program
                * counter by 2.
                */

                if (v[getX()] == getKK()){
                    pc += 2;
                }
                break;

            case 0x4000:
                /*
                * 4xkk - SNE Vx, byte
                * Skip next instruction if Vx != kk.
                *
                * The interpreter compares register Vx to kk, and if they are not equal, increments the program
                * counter by 2
                */

                if (v[getX()] != getKK()){
                    pc += 2;
                }
            break;

            case 0x5000:
                /*
                * 5xy0 - SE Vx, Vy
                * Skip next instruction if Vx = Vy.
                *
                * The interpreter compares register Vx to register Vy, and if they are equal, increments the
                * program counter by 2.
                */

                if (v[getX()] == v[getY()]){
                    pc += 2;
                }
                break;

            case 0x6000:
                /*
                * 6xkk - LD Vx, byte
                * Set Vx = kk.
                *
                * The interpreter puts the value kk into register Vx.
                */

                v[getX()] = getKK();
                break;

            case 0x7000:
                /*
                * 7xkk - ADD Vx, byte
                * Set Vx = Vx + kk.
                *
                * Adds the value kk to the value of register Vx, then stores the result in Vx.
                */

                v[getX()] = (char)((v[getX()] + getKK()) & 0xFF);
                break;

            case 0x8000:
                switch (opcode & 0x000F){
                    case 0x0000:
                        /*
                        * 8xy0 - LD Vx, Vy
                        * Set Vx = Vy.
                        *
                        * Stores the value of register Vy in register Vx.
                        */

                        v[getX()] = v[getY()];
                        break;

                    case 0x0001:
                        /*
                        * 8xy2 - AND Vx, Vy
                        * Set Vx = Vx AND Vy.
                        *
                        * Performs a bitwise AND on the values of Vx and Vy, then stores the result in Vx. A bitwise
                        * AND compares the corresponding bits from two values, and if both bits are 1, then the same
                        * bit in the result is also 1. Otherwise, it is 0.
                        */

                        v[getX()] = (char)(v[getX()] | v[getY()]);
                        break;

                    case 0x0002:
                        /*
                        * 8xy2 - AND Vx, Vy
                        * Set Vx = Vx AND Vy.
                        *
                        * Performs a bitwise AND on the values of Vx and Vy, then stores the result in Vx. A bitwise AND
                        * compares the corrseponding bits from two values, and if both bits are 1, then the same bit in
                        * the result is also 1. Otherwise, it is 0.
                        */

                        v[getX()] = (char)((v[getX()] & v[getY()]) & 0xFF);
                        break;

                    case 0x0003:
                        /*
                        * 8xy3 - XOR Vx, Vy
                        * Set Vx = Vx XOR Vy.
                        *
                        * Performs a bitwise exclusive OR on the values of Vx and Vy, then stores the result in Vx. An
                        * exclusive OR compares the corrseponding bits from two values, and if the bits are not both the
                        * same, then the corresponding bit in the result is set to 1. Otherwise, it is 0.
                        */

                        v[getX()] = (char)(v[getX()] ^ v[getY()]);
                        break;

                    case 0x0004:
                        /*
                        * 8xy4 - ADD Vx, Vy
                        * Set Vx = Vx + Vy, set VF = carry.
                        *
                        * The values of Vx and Vy are added together. If the result is greater than 8 bits (i.e., >
                        * 255,) VF is set to 1, otherwise 0. Only the lowest 8 bits of the result are kept, and stored
                        * in Vx.
                        */

                        int result = (v[getX()] + v[getY()]);

                        if (result > 255){
                            v[0xF] = 1;
                        }
                        else{
                            v[0xF] = 0;
                        }

                        v[getX()] = (char)(result & 0xFF);
                        break;

                    case 0x0005:
                        /*
                        * 8xy5 - SUB Vx, Vy
                        * Set Vx = Vx - Vy, set VF = NOT borrow.
                        *
                        * (If Vy > Vx, then VF is set to 0, otherwise 1) - Corrected from documentation. Then Vy is subtracted from Vx, and the results
                        * stored in Vx.
                        */

                        if (v[getY()] > v[getX()]){
                            v[0xF] = 0;
                        }
                        else{
                            v[0xF] = 1;
                        }

                        v[getX()] = (char)((v[getX()] - v[getY()]) & 0xFF);
                        break;

                    case 0x0006:
                        /*
                        * 8xy6 - SHR Vx {, Vy}
                        * Set Vx = Vx SHR 1.
                        *
                        * If the least-significant bit of Vx is 1, then VF is set to 1, otherwise 0. Then Vx is divided by 2
                        */

                        if ((v[getX()] & 1) != 0){
                            v[0xF] = 1;
                        }
                        else{
                            v[0xF] = 0;
                        }

                        v[getX()] = (char)(v[getX()] >>> 1);
                        break;

                    case 0x0007:
                        /*
                        * 8xy7 - SUBN Vx, Vy
                        * Set Vx = Vy - Vx, set VF = NOT borrow.
                        *
                        * If Vy > Vx, then VF is set to 1, otherwise 0. Then Vx is subtracted from Vy, and the
                        * results stored in Vx.
                        */

                        if (v[getY()] > v[getX()]){
                            v[0xF] = 1;
                        }
                        else{
                            v[0xF] = 0;
                        }

                        v[getX()] = (char)((v[getY()] - v[getX()]) & 0xFF);
                        break;

                    case 0x000E:
                        /*
                        * 8xyE - SHL Vx {, Vy}
                        * Set Vx = Vx SHL 1.
                        *
                        * If the most-significant bit of Vx is 1, then VF is set to 1, otherwise to 0. Then Vx is
                        * multiplied by 2.
                        */

                        if (((v[getX()] >>> 7) & 1) != 0){
                            v[0xF] = 1;
                        }
                        else{
                            v[0xF] = 0;
                        }

                        v[getX()] = (char)((v[getX()] << 1) & 0xFF);
                        break;

                    default:
                        System.out.println("Unimplemented 8000 Opcode " + Integer.toHexString(opcode));
                        cpuRunning = false;
                        break; // End of Internal 8000 switch
                }
                break; // End of 8000 switch

            case 0x9000:
                /*
                * 9xy0 - SNE Vx, Vy
                * Skip next instruction if Vx != Vy.
                *
                * The values of Vx and Vy are compared, and if they are not equal, the program counter is
                * increased by 2.
                */

                if (v[getX()] != v[getY()]){
                    pc += 2;
                }

                break;

            case 0xA000:
                /*
                * Annn - LD I, addr
                * Set I = nnn.
                *
                * The value of register I is set to nnn.
                */

                I = getNNN();
                break;

            case 0xB000:
                /*
                * Bnnn - JP V0, addr
                * Jump to location nnn + V0.
                *
                * The program counter is set to nnn plus the value of V0.
                */

                pc = (char)((getNNN() + v[0]) & 0xFF);
                break;

            case 0xC000:
                /*
                * Cxkk - RND Vx, byte
                * Set Vx = random byte AND kk.
                *
                * The interpreter generates a random number from 0 to 255, which is then ANDed with the
                * value kk. The results are stored in Vx.
                */

                v[getX()] = (char)((new Random().nextInt(256) & getKK()) & 0xFF);
                break;

            case 0xD000:
                /*
                * Dxyn - DRW Vx, Vy, nibble
                * Display n-byte sprite starting at memory location I at (Vx, Vy), set VF = collision.
                * The interpreter reads n bytes from memory, starting at the address stored in I. These
                * bytes are then displayed as sprites on screen at coordinates (Vx, Vy). Sprites are XORed
                * onto the existing screen. If this causes any pixels to be erased, VF is set to 1,
                * otherwise it is set to 0. If the sprite is positioned so part of it is outside the
                * coordinates of the display, it wraps around to the opposite side of the screen.
                */

                int x = v[getX()];
                int y = v[getY()];
                int numOfBytes = getN();

                v[0xF] = 0;
                for (int line = 0; line < numOfBytes; ++line){
                    char data = memory.getMemory()[I + line];

                    for (int xPixel = 0; xPixel < 8; ++xPixel){
                        if ((data & (0x80 >>> xPixel)) != 0){

                            int xLoc = x + xPixel;
                            int yLoc = y + line;

                            if (xLoc < 64 && yLoc < 32){
                                if (graphics[(x + xPixel + ((y + line) * 64))] != 0){
                                    v[0xF] = 1;
                                }

                                graphics[(x + xPixel + ((y + line) * 64))] ^= 1;
                            }
                        }
                    }
                }

                drawFlag = true;
                break;

            case 0xE000:
                switch (opcode & 0x00FF){
                    case 0x009E:
                        /*
                        * Ex9E - SKP Vx
                        * Skip next instruction if key with the value of Vx is pressed.
                        *
                        * Checks the keyboard, and if the key corresponding to the value of Vx is currently in the down
                        * position, PC is increased by 2.
                        */

                        if (keys[v[getX()]] != 0){
                            pc += 2;
                        }
                        break;

                    case 0x00A1:
                        /*
                        * ExA1 - SKNP Vx
                        * Skip next instruction if key with the value of Vx is not pressed.
                        *
                        * Checks the keyboard, and if the key corresponding to the value of Vx is currently in the
                        * up position, PC is increased by 2.
                        */

                        if (keys[v[getX()]] == 0){
                            pc += 2;
                        }
                        break;

                    default:
                        System.out.println("Unimplemented E000 Opcode " + Integer.toHexString(opcode));
                        cpuRunning = false;
                        break; // End of internal E000 switch
                }
                break; // End of E000 switch

            case 0xF000:
                switch (opcode & 0x00FF){
                    case 0x0007:
                        /*
                        * Fx07 - LD Vx, DT
                        * Set Vx = delay timer value.
                        *
                        * The value of DT is placed into Vx.
                        */

                        v[getX()] = (char)(delayTimer & 0xFF);
                        break;

                    case 0x000A:
                        /*
                        * Fx0A - LD Vx, K
                        * Wait for a key press, store the value of the key in Vx.
                        *
                        * All execution stops until a key is pressed, then the value of that key is stored in Vx.
                        */

                        pc -= 2;

                        for (int i = 0; i < keys.length; ++i){
                            if (keys[i] == 1){
                                v[getX()] = (char)i;
                                pc += 2;
                                break;
                            }
                        }
                        break;

                    case 0x0015:
                        /*
                        * Fx15 - LD DT, Vx
                        * Set delay timer = Vx.
                        *
                        * DT is set equal to the value of Vx.
                        */

                        delayTimer = v[getX()];
                        break;

                    case 0x0018:
                        /*
                        * Fx18 - LD ST, Vx
                        * Set sound timer = Vx.
                        *
                        * ST is set equal to the value of Vx.
                        */

                        soundTimer = v[getX()];
                        break;

                    case 0x001E:
                        /*
                        * Fx1E - ADD I, Vx
                        * Set I = I + Vx.
                        *
                        * The values of I and Vx are added, and the results are stored in I.
                        */

                        I = (char) ((I + v[getX()]) & 0xFFF);
                        break;

                    case 0x0029:
                        /*
                        * Fx29 - LD F, Vx
                        * Set I = location of sprite for digit Vx.
                        *
                        * The value of I is set to the location for the hexadecimal sprite corresponding to the value of Vx.
                        */

                        I = (char)(v[getX()] * 5);
                        break;

                    case 0x0033:
                        /*
                        * Fx33 - LD B, Vx
                        * Store BCD representation of Vx in memory locations I, I+1, and I+2.
                        *
                        * The interpreter takes the decimal value of Vx, and places the hundreds digit in memory at
                        * location in I, the tens digit at location I+1, and the ones digit at location I+2.
                        */

                        memory.getMemory()[I] = (char)((v[getX()] / 100) & 0xFF);
                        memory.getMemory()[I + 1] = (char)(((v[getX()] % 100) / 10) & 0xFF);
                        memory.getMemory()[I + 2] = (char)(((v[getX() % 100]) % 10) & 0xFF);
                        break;

                    case 0x0055:
                        /*
                        * Fx55 - LD [I], Vx
                        * Store registers V0 through Vx in memory starting at location I.
                        *
                        * The interpreter copies the values of registers V0 through Vx into memory, starting at the
                        * address in I.
                        */

                        for (int i = 0; i <= getX(); ++i){
                            memory.getMemory()[I + i] = v[i];
                        }

                        break;

                    case 0x0065:
                        /*
                        * Fx65 - LD Vx, [I]
                        * Read registers V0 through Vx from memory starting at location I.
                        *
                        * The interpreter reads values from memory starting at location I into registers V0 through Vx
                        */

                        for (int i = 0; i <= getX(); ++i){
                            v[i] = (char)(memory.getMemory()[I + i] & 0xFF);
                        }
                        break;

                        default:
                            System.out.println("Unimplemented F000 Opcode " + Integer.toHexString(opcode));
                            cpuRunning = false;
                            break; // End of internal F000 switch
                }
                break; // End of F000 switch

            default:
                System.out.println("Unimplemented Opcode " + Integer.toHexString(opcode));
                cpuRunning = false;
                break;
        }

        if (delayTimer > 0){
            delayTimer--;
        }

        if (soundTimer > 0){
            if (soundTimer == 1){
                System.out.println("BEEP");
                soundTimer--;
            }
        }
    }

    /**
     * Returns the lowest 12 bits of the current instruction.
     *
     * @return the l2 lower bits.
     */
    private char getNNN(){
        return (char)(opcode & 0x0FFF);
    }

    /**
     * Returns the lowest 8 bits of the current instruction.
     *
     * @return the 8 lower bits.
     */
    private char getKK(){
        return (char)(opcode & 0xFF);
    }

    /**
     * Returns the lower 4 bits of the high byte.
     *
     * @return the 4 bits of the highest byte.
     */
    private char getX(){
        return (char)((opcode >> 8) & 0xF);
    }

    /**
     * Returns the lower 4 bits of the high byte.
     *
     * @return the lower 4 bits of the highest byte.
     */
    private char getY(){
        return (char)((opcode >> 4) & 0xF);
    }

    /**
     * Returns the upper 4 bits of the high byte.
     *
     * @return the upper 4 bits of the highest byte.
     */
    private char getN(){
        return (char)(opcode & 0xF);
    }

    /**
     * Resets cpu by setting default values.
     */
    public void reset(){
        Arrays.fill(v, (char)0);
        Arrays.fill(graphics, 0);
        Arrays.fill(stack, (char)0);
        Arrays.fill(keys, 0);

        drawFlag = false;
        cpuRunning = false;

        pc = 0x200;
        I = 0;
        opcode = 0;
        sp = 0;

        delayTimer = 0;
        soundTimer = 0;
    }

    /**
     * Returns the draw flag.
     *
     * @return the draw flag.
     */
    public boolean getDrawFlag(){
        return drawFlag;
    }

    /**
     * Return the graphics array.
     *
     * @return the graphics array object.
     */
    public int[] getGraphics(){
        return graphics;
    }

    /**
     * Return the keys array.
     *
     * @return the array containing the keys.
     */
    public int[] getKeys(){
        return keys;
    }

    /**
     * Return the cpu running flag.
     *
     * @return the cpu running flag.
     */
    public boolean getCpuRunning() {
        return cpuRunning;
    }

    /**
     * Sets the memory instance.
     *
     * @param memory the memory instance to be set.
     */
    public void setMemory(Memory memory){
        this.memory = memory;
    }

    /**
     * Sets the cpu running flag.
     *
     * @param value the value to set the cpu running flag.
     */
    public void setCpuRunning(boolean value){
        this.cpuRunning = value;
    }
}