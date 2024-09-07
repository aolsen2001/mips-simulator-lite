# mips-simulator-lite
A MIPS simulator written purely in Java.
## Useage
To use this program, run the JAR file provided in ./out/artifacts/mips_simulator_lite with two arguments: the path to a MIPS .data file, and the path to a MIPS .text file. The MIPS code will be then 'executed' in the terminal. The ./mips_files directory contains an example program, EvenOrOdd.asm and the corresponding .data and .text files.
## Information
The following MIPS operations are currently supported:
- add
- addiu
- and
- andi
- beq
- bne
- j
- lui
- lw
- or
- ori
- slt
- sub
- sw
- syscall  
