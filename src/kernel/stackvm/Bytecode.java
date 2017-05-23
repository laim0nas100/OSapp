package kernel.stackvm;

public class Bytecode {
        
    
    public static class Instruction {
        public final String name; // E.g., "iadd", "call"
        public int n = 0;
        public Instruction(String name) {
            this(name,0); 
        }
        public Instruction(String name, int nargs) {
                this.name = name;
                this.n = nargs;
        }
    }

    // INSTRUCTION BYTECODES (byte is signed; use a int to keep 0..255)
    public static final int NOP       = 0;
    public static final int IADD      = 1;    // int add
    public static final int ISUB      = 2;
    public static final int IMUL      = 3;
    public static final int ILT       = 4;    // int less than
    public static final int IEQ       = 5;    // int equal
    public static final int BR        = 6;    // branch
    public static final int BRT       = 7;    // branch if true
    public static final int BRF       = 8;    // branch if false
    public static final int ICONST    = 9;    // push constant integer
    public static final int GLOAD     = 10;   // load from global memory
    public static final int GSTORE    = 11;   // store in global memory
    public static final int POP       = 12;   // throw away top of stack
    
    public static final int INT       = 15;   // interrupt


    public static Instruction[] instructions = new Instruction[] {
        new Instruction("nop"), // no operation
        new Instruction("iadd"), // index is the opcode
        new Instruction("isub"),
        new Instruction("imul"),
        new Instruction("ilt"),

        new Instruction("ieq"),
        new Instruction("br", 1),
        new Instruction("brt", 1),
        new Instruction("brf", 1),
        new Instruction("iconst", 1),

        new Instruction("gload", 1),
        new Instruction("gstore", 1),
        new Instruction("pop"),
        null,
        null,
        
        new Instruction("int", 1),
        null,
        null,
        null,
        null,

    };
}
