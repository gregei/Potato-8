# Potato-8

[![Build Status](https://travis-ci.org/gregorygaines/Potato-8.svg?branch=master)](https://travis-ci.org/gregorygaines/Potato-8)

Potato-8 is a chip-8 emulator written completely in native Java, built with Gradle. It emulates the core functions of the Chip-8, such as the instruction set and video. It's a small exercise that shows how efficient and clean Java can be with advanced projects.

![Potato-8 running](docs/images/screenshot1.png)

# Building
Potato-8 can be build using the gradle command:

    gradle desktop:jar
    
The built jar will be located in directory `/desktop/build/libs/`

# Usage 
Use:

    java -jar potato8-desktop.jar ROM_PATH [OPTIONAL SCALE]
  
The optional scale parameter will be used to scale the window size by an integer since the default size of the Chip-8 is 64x32 which is pretty small. The default scaling is 10 (640x320).

# Keyboard

The original Chip-8 keyboard layout is a hexadecimal keypad mapped to the keyboard as shown below:

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

# Screenshots

![Potato-8 running](docs/images/screenshot5.jpg)
![Potato-8 running](docs/images/screenshot2.jpg)
![Potato-8 running](docs/images/screenshot3.jpg)
![Potato-8 running](docs/images/screenshot4.jpg)

# Resources

* [Thomas P. Greene's Cowgod Documentation](http://devernay.free.fr/hacks/chip8/C8TECH10.HTM)
