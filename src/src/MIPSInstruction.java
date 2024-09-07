public class MIPSInstruction {
    private final String mipsInstruction;
    private String binString;
    private String instType;
    private String instName;
    private final int[] vals = new int[3];

    /**
     * Create a new instance of a MIPS instruction
     *
     * @param inst the hexadecimal representation of the MIPS instruction
     */
    public MIPSInstruction(String inst) {
        this.mipsInstruction = inst;
        convertToBinary();
        values();
    }

    /**
     * Return the name of the instruction
     *
     * @return name of this instruction
     */
    public String getInstName() {
        return this.instName;
    }


    /**
     * Convert the MIPS instruction to a 32-bit binary number
     */
    private void convertToBinary() {
        if (this.mipsInstruction.equals("0000000c")) {
            this.instType = "syscall";
            return;
        }
        StringBuilder hexToBin1, hexToBin2, hexToBin3, hexToBin4, hexToBin5, hexToBin6, hexToBin7, hexToBin8;
        hexToBin1 = new StringBuilder(Integer.toBinaryString(Integer.parseInt(String.valueOf(this.mipsInstruction.charAt(0)), 16)));
        while (hexToBin1.length() < 4) {
            hexToBin1.insert(0, "0");
        }
        hexToBin2 = new StringBuilder(Integer.toBinaryString(Integer.parseInt(String.valueOf(this.mipsInstruction.charAt(1)), 16)));
        while (hexToBin2.length() < 4) {
            hexToBin2.insert(0, "0");
        }
        hexToBin3 = new StringBuilder(Integer.toBinaryString(Integer.parseInt(String.valueOf(this.mipsInstruction.charAt(2)), 16)));
        while (hexToBin3.length() < 4) {
            hexToBin3.insert(0, "0");
        }
        hexToBin4 = new StringBuilder(Integer.toBinaryString(Integer.parseInt(String.valueOf(this.mipsInstruction.charAt(3)), 16)));
        while (hexToBin4.length() < 4) {
            hexToBin4.insert(0, "0");
        }
        hexToBin5 = new StringBuilder(Integer.toBinaryString(Integer.parseInt(String.valueOf(this.mipsInstruction.charAt(4)), 16)));
        while (hexToBin5.length() < 4) {
            hexToBin5.insert(0, "0");
        }
        hexToBin6 = new StringBuilder(Integer.toBinaryString(Integer.parseInt(String.valueOf(this.mipsInstruction.charAt(5)), 16)));
        while (hexToBin6.length() < 4) {
            hexToBin6.insert(0, "0");
        }
        hexToBin7 = new StringBuilder(Integer.toBinaryString(Integer.parseInt(String.valueOf(this.mipsInstruction.charAt(6)), 16)));
        while (hexToBin7.length() < 4) {
            hexToBin7.insert(0, "0");
        }
        hexToBin8 = new StringBuilder(Integer.toBinaryString(Integer.parseInt(String.valueOf(this.mipsInstruction.charAt(7)), 16)));
        while (hexToBin8.length() < 4) {
            hexToBin8.insert(0, "0");
        }
        this.binString = hexToBin1.toString() + hexToBin2 + hexToBin3 + hexToBin4 + hexToBin5 + hexToBin6 + hexToBin7 + hexToBin8;
        if (this.binString.startsWith("000000")) {
            this.instType = "R-Type";
        } else if (this.binString.startsWith("000010")) {
            this.instType = "J-Type";
        } else {
            this.instType = "I-Type";
        }
    }

    public int[] getValues()
    {
        return this.vals;
    }

    public void values() {
        if (this.instType.equals("J-Type")) { //j
            vals[0] = Integer.parseInt(binString.substring(6,32),2);
            this.instName = "j";
        }
        if (this.instType.equals("syscall")) { //syscall
            this.instName = "syscall";
        }
        if (this.instType.equals("R-Type")) {
            if (binString.startsWith("100000", 26)) { //add
                vals[2] = Integer.parseInt(binString.substring(6, 11), 2);
                vals[1] = Integer.parseInt(binString.substring(11,16), 2);
                vals[0] = Integer.parseInt(binString.substring(16,21), 2);
                this.instName = "add";
            }
            if (binString.startsWith("100100", 26)) //and
            {
                vals[1] = Integer.parseInt(binString.substring(6, 11), 2);
                vals[2] = Integer.parseInt(binString.substring(11, 16), 2);
                vals[0] = Integer.parseInt(binString.substring(16, 21), 2);
                this.instName = "and";
            }
            if (binString.startsWith("100101", 26)) { //or
                vals[1] = Integer.parseInt(binString.substring(6, 11), 2);
                vals[2] = Integer.parseInt(binString.substring(11, 16), 2);
                vals[0] = Integer.parseInt(binString.substring(16, 21), 2);
                this.instName = "or";
            }
            if (binString.startsWith("101010", 26)) { //slt
                vals[1] = Integer.parseInt(binString.substring(6, 11), 2);
                vals[2] = Integer.parseInt(binString.substring(11, 16), 2);
                vals[0] = Integer.parseInt(binString.substring(16, 21), 2);
                this.instName = "slt";
            }
            if (binString.startsWith("100010", 26)) { //sub
                vals[1] = Integer.parseInt(binString.substring(6, 11), 2);
                vals[2] = Integer.parseInt(binString.substring(11, 16), 2);
                vals[0] = Integer.parseInt(binString.substring(16, 21), 2);
                this.instName = "sub";
            }
        }
        if (this.instType.equals("I-Type")) {
            if (binString.startsWith("001001")) { //addiu
                vals[0] = Integer.parseInt(binString.substring(6, 11), 2);
                vals[1] = Integer.parseInt(binString.substring(11, 16), 2);
                vals[2] = Integer.parseInt(binString.substring(16, 32), 2);
                this.instName = "addiu";
            }
            if (binString.startsWith("001100")) { //andi
                vals[1] = Integer.parseInt(binString.substring(6, 11), 2);
                vals[0] = Integer.parseInt(binString.substring(11, 16), 2);
                vals[2] = Integer.parseInt(binString.substring(16, 32), 2);
                this.instName = "andi";
            }
            if (binString.startsWith("000100")) { //beq
                vals[0] = Integer.parseInt(binString.substring(6, 11), 2);
                vals[1] = Integer.parseInt(binString.substring(11, 16),2);
                vals[2] = Integer.parseInt(binString.substring(16, 32),2);
                this.instName = "beq";
            }
            if (binString.startsWith("000101")) { //bne
                vals[0] = Integer.parseInt(binString.substring(6, 11), 2);
                vals[1] = Integer.parseInt(binString.substring(11, 16), 2);
                vals[2] = Integer.parseInt(binString.substring(16, 32), 2);
                this.instName = "bne";
            }

            if (binString.startsWith("001111")) { //lui
                vals[0] = Integer.parseInt(binString.substring(11, 16), 2);
                vals[1] = Integer.parseInt(binString.substring(16, 32), 2);
                this.instName = "lui";
            }
            if (binString.startsWith("100011")) { //lw
                vals[2] = Integer.parseInt(binString.substring(6, 11), 2);
                vals[0] = Integer.parseInt(binString.substring(11, 16), 2);
                vals[1] = Integer.parseInt(binString.substring(16, 32), 2);
                this.instName = "lw";
            }
            if (binString.startsWith("001101")) { //ori
                vals[0] = Integer.parseInt(binString.substring(6, 11), 2);
                vals[1] = Integer.parseInt(binString.substring(11, 16), 2);
                vals[2] = Integer.parseInt(binString.substring(16, 32), 2);
                this.instName = "ori";
            }
            if (binString.startsWith("101011")) { //sw
                vals[2] = Integer.parseInt(binString.substring(6, 11), 2);
                vals[0] = Integer.parseInt(binString.substring(11, 16), 2);
                vals[1] = Integer.parseInt(binString.substring(16, 32), 2);
                this.instName = "sw";
            }
        }
    }
}

