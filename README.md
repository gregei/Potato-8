# Potato-8

[![Build Status](https://travis-ci.org/gregei/Potato-8.svg?branch=master)](https://travis-ci.org/gregei/Potato-8)

Potato-8 is a Chip-8 emulator written completely in native Java. It's a small exercise that shows how efficient and clean Java can be with advanced projects.

# Building
Potato-8 can be build using gradle command: 

    :desktop:build
    
The build jar will be located in directory desktop/build/libs/potato8-desktop.jar

# Usage 
Use:

    java -jar potato8-desktop.jar ROM_PATH [OPTIONAL SCALE]
  
The optional scale parameter is used to scale the window size by a positive integer since the default size of the Chip-8 is 64x32 which is pretty small so putting the integer 2 will scale the window size to 128(64 * 2)x64(32 * 2). If an 0 or negative number is passed to scale parameter then by default a scaling of 10(640x320) will be used.

# Keyboard

The original Chip-8 keyboard layout is defined as hexadecimal keypad :

|   |   |   |   |
|---|---|---|---|
| 1 | 2 | 3 | C |
| 4 | 5 | 6 | D |
| 7 | 8 | 9 | E |
| A | 0 | B | F |


| Original | Keyboard |
|----------|----------|
| 1        | 1        |
| 2        | 2        |
| 3        | 3        |
| C        | 4        |
| 4        | Q        |
| 5        | W        |
| 6        | E        |
| D        | R        |
| 7        | A        | 
| 8        | S        |
| 9        | D        |
| E        | F        |
| A        | Z        |
| 0        | X        |
| B        | C        |
| F        | V        |
