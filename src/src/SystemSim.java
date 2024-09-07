import java.util.*;
import java.io.*;

public class SystemSim {
    private final int[] regVals = new int[32];
    private int pc = 4194304;
    private int dataAddVal = 268500992;
    private final HashMap<Integer, String> addresses = new HashMap<>();
    private final ArrayList<String> dataHexValues = new ArrayList<>();
    private final Queue<String> textHexValues = new LinkedList<>();
    private int prevDataAddress = 268500992;
    Scanner userIn = new Scanner(System.in);

    /**
     * Initialize the registers to 0 and read in .data and .text files
     * $zero = regVals[0]
     * $a0 = regVals[1]
     * $v0-v1 = regVals[2-3]
     * $a0-a3 = regVals[4-7]
     * $t0-t7 = regVals[8-15]
     * $s0-s7 = regVals[16-23]
     * $t8-t9 = regVals[24-25]
     * $k0-$k1 = regVals[26-27]
     * $gp = regVals[28]
     * $sp = regVals[29]
     * $fp = regVals[30]
     * $ra = regVals[31]
     */
    public void init(File dataIn, File textIn) throws IOException {
        Arrays.fill(regVals, 0);
        BufferedReader brData = new BufferedReader(new FileReader(dataIn));
        BufferedReader brText = new BufferedReader(new FileReader(textIn));
        String ln = brData.readLine();
        while (!ln.equals("00000000")) {
            dataHexValues.add(ln);
            ln = brData.readLine();
        }
        StringBuilder output = new StringBuilder();
        for (String h : dataHexValues) { //Read each value in .DATA and determine the ascii values
            for (int i = h.length(); i > 0; i -= 2) {
                String c = h.substring(i - 2, i);
                int dec = Integer.parseInt(c, 16);
                if (dec != 0) {
                    output.append((char) dec);
                } else {
                    output.append("null"); //Null terminator byte
                }
            }
        }
        String[] splitOut = output.toString().split("null");
        for (String s : splitOut) {
            addresses.put(dataAddVal, s);
            dataAddVal += s.length() + 1;
        }
        brData.close();
        ln = brText.readLine();
        while (ln != null) {
            textHexValues.add(ln);
            ln = brText.readLine();
        }
    }

    /**
     * Simulates the execution of a MIPS assembly file
     */
    public void run() {
        String v = textHexValues.poll();
        while (!textHexValues.isEmpty()) {
            if (v != null) {
                if (v.equals("00000000")) {
                    System.out.println("\n-- program finished running (dropped off bottom) --");
                    System.exit(0);
                }
                performInstruction(new MIPSInstruction(v));
                pc += 4;
                v = textHexValues.poll();
            }
        }
        System.out.println("\n-- program is finished running --");
        System.exit(0);
    }

    /**
     * Simulates the execution of a single line of MIPS assembly code
     *
     * @param inst the MIPS instruction to execute
     */
    public void performInstruction(MIPSInstruction inst) {
        int[] v = inst.getValues();
        int target;
        StringBuilder hexAddress, bin1, bin2;
        if (inst.getInstName().equals("syscall")) {
            switch (regVals[2]) {
                case 1:
                    System.out.println(regVals[1]);
                    break;
                case 4:
                    System.out.print(addresses.get(prevDataAddress));
                    break;
                case 5:
                    int input = userIn.nextInt();
                    regVals[2] = input;
                    break;
            }
        }
        if (inst.getInstName().equals("add")) {
            regVals[v[0]] = regVals[v[1]] + regVals[v[2]];
        }
        if (inst.getInstName().equals("addiu")) {
            regVals[v[1]] = v[0] + v[2];
        }
        if (inst.getInstName().equals("and")) {
            bin1 = new StringBuilder(Integer.toBinaryString(regVals[v[1]]));
            while (bin1.length() < 16) {
                bin1.insert(0, 0);
            }
            bin2 = new StringBuilder(Integer.toBinaryString(v[2]));
            while (bin2.length() < 16) {
                bin2.insert(0, 0);
            }
            for (int i = 0; i < bin1.length(); ++i) {
                if (bin1.charAt(i) != bin2.charAt(i)) {
                    bin1.setCharAt(i, '0');
                }
            }
            regVals[v[0]] = Integer.parseInt(bin1.toString(), 2);
        }
        if (inst.getInstName().equals("andi")) {
            bin1 = new StringBuilder(Integer.toBinaryString(regVals[v[1]]));
            while (bin1.length() < 16) {
                bin1.insert(0, 0);
            }
            if (bin1.length() > 16) {
                while (bin1.length() > 16) {
                    bin1.deleteCharAt(0);
                }
            }
            bin2 = new StringBuilder(Integer.toBinaryString(v[2]));
            while (bin2.length() < 16) {
                bin2.insert(0, 0);
            }
            if (bin2.length() > 16) {
                while (bin2.length() > 16) {
                    bin2.deleteCharAt(0);
                }
            }
            for (int i = 0; i < bin1.length(); ++i) {
                if (bin1.charAt(i) != bin2.charAt(i)) {
                    bin1.setCharAt(i, '0');
                }
            }
            regVals[v[0]] = Integer.parseInt(bin1.toString(), 2);
        }
        if (inst.getInstName().equals("beq")) {
            int offset = v[2] << 2;
            if (regVals[v[0]] == regVals[v[1]]) {
                target = pc + offset;
                while (pc != target) {
                    //Dequeue instructions in queue until target is reached
                    textHexValues.remove();
                    pc += 4;
                }
            }
        }
        if (inst.getInstName().equals("bne")) {
            int offset = v[2] << 2;
            if (regVals[v[0]] != regVals[v[1]]) {
                target = pc + offset;
                while (pc != target) {
                    //Dequeue instructions in queue until target is reached
                    textHexValues.remove();
                    pc += 4;
                }
            }
        }
        if (inst.getInstName().equals("j")) {
            target = v[0] << 2;
            pc += 4;
            while (pc != target) {
                //Dequeue instructions in queue until target is reached
                textHexValues.remove();
                pc += 4;
            }
        }
        if (inst.getInstName().equals("lui")) {
            regVals[v[0]] = v[1];
        }
        if (inst.getInstName().equals("lw")) {
            regVals[v[2]] = v[0] + regVals[v[1]];
        }
        if(inst.getInstName().equals("or"))
        {
            bin1 = new StringBuilder(Integer.toBinaryString(regVals[v[1]]));
            while (bin1.length() < 16) {
                bin1.insert(0, 0);
            }
            bin2 = new StringBuilder(Integer.toBinaryString(v[2]));
            while (bin2.length() < 16) {
                bin2.insert(0, 0);
            }
            for (int i = 0; i < bin1.length(); ++i) {
                if (bin1.charAt(i) == '1' || bin2.charAt(i) == '1') {
                    bin1.setCharAt(i, '1');
                }
                else {
                    bin1.setCharAt(i,'0');
                }
            }
            regVals[v[0]] = Integer.parseInt(bin1.toString(), 2);
        }
        if (inst.getInstName().equals("ori")) {
            hexAddress = new StringBuilder(Integer.toHexString(regVals[v[0]]));
            StringBuilder hex2 = new StringBuilder(Integer.toHexString(v[2]));
            while (hex2.length() < 4) {
                hex2.insert(0, 0);
            }
            hexAddress.append(hex2);
            while (hexAddress.length() < 8) {
                hexAddress.append("0");
            }
            regVals[v[1]] = Integer.parseInt(hexAddress.toString(), 16);
            prevDataAddress = regVals[v[1]];
        }
        if(inst.getInstName().equals("slt"))
        {
            if(regVals[v[1]] < regVals[v[2]])
            {
                regVals[v[0]] = 1;
            }
        }
        if(inst.getInstName().equals("sub"))
        {
            regVals[v[0]] = regVals[v[1]] - regVals[v[2]];
        }
        if(inst.getInstName().equals("sw"))
        {
            regVals[v[1] + regVals[v[2]]] = regVals[v[0]];
        }
    }
}

