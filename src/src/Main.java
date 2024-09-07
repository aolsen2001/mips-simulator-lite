/*
Andrew Olsen

This program takes in two arguments when run from the command line: the paths to a .data file and a .text file of a MIPS assembly program.
The contents of both files are read and processed, simulating the execution of the MIPS program.
 */
import java.io.*;
public class Main {
    public static void main(String[] args) throws IOException {
        File dataIn = new File(args[0]);
        File textIn = new File(args[1]);
        SystemSim sys = new SystemSim();
        sys.init(dataIn, textIn);
        sys.run();
    }
}