/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel.stackvm;

import java.util.ArrayList;
import java.util.List;
import kernel.CPU.Reg;
import kernel.process.Interruptable;
import kernel.process.PagedMemoryAccess;

/**
 *
 * @author lemmin
 */
public abstract class AbstractVM implements PagedMemoryAccess,Interruptable{
	public static final int FALSE = 0;
	public static final int TRUE = 1;
       
	// registers
	public Reg ip = new Reg(0);       // instruction pointer register
	public Reg sp = new Reg(-1);  		// stack pointer register

	public boolean trace;


	/** Simulate the fetch-decode execute cycle */
	public void step() {
            int opcode = codeAccess(ip.val);
            int a,b,addr;
            if (trace){
//                System.err.println("Operation "+opcode);
                System.err.printf("%-35s\n", disInstr());
            }
            ip.inc(); //jump to next instruction or to operand

            switch (opcode) {
                case Bytecode.NOP:
                    break;
                    
                case Bytecode.IADD:
                    b = userMemoryAccess(sp.dec());   			// 2nd opnd at top of stack
                    a = userMemoryAccess(sp.dec()); 			// 1st opnd 1 below top
                    userMemorySet(sp.incBefore(1),a + b);                   // push result
                    break;
                case Bytecode.ISUB:
                    b = userMemoryAccess(sp.dec());
                    a = userMemoryAccess(sp.dec());
                    userMemorySet(sp.incBefore(1), a - b);
                    break;
                case Bytecode.IMUL:
                    b = userMemoryAccess(sp.dec());
                    a = userMemoryAccess(sp.dec());
                    userMemorySet(sp.incBefore(1), a * b);
                    break;
                case Bytecode.ILT :
                    b = userMemoryAccess(sp.dec());
                    a = userMemoryAccess(sp.dec());
                    userMemorySet(sp.incBefore(1),(a < b) ? TRUE : FALSE);
                    break;
                case Bytecode.IEQ :
                    b = userMemoryAccess(sp.dec());
                    a = userMemoryAccess(sp.dec());
                    userMemorySet(sp.incBefore(1),(a == b) ? TRUE : FALSE);
                    break;
                case Bytecode.BR :
                    ip.val = codeAccess(ip.inc());
                    break;
                case Bytecode.BRT :
                    addr = codeAccess(ip.inc());
                    if ( userMemoryAccess(sp.dec())==TRUE ) ip.val = addr;
                    break;
                case Bytecode.BRF :
                    addr = codeAccess(ip.inc());
                    if ( userMemoryAccess(sp.dec())==FALSE ) ip.val = addr;
                    break;
                case Bytecode.ICONST:
                    userMemorySet(sp.incBefore(1),codeAccess(ip.inc())); // push operand
                    break;
                case Bytecode.GLOAD :// load from global memory
                    addr = codeAccess(ip.inc());
                    userMemorySet(sp.incBefore(1),userMemoryAccess(addr));
                    break;
                case Bytecode.GSTORE :
                    addr = codeAccess(ip.inc());
                    userMemorySet(addr,userMemoryAccess(sp.dec()));
                    break;
                case Bytecode.INT :
                    int intNo = codeAccess(ip.inc());
                    this.INT(intNo);
                    break;
                case Bytecode.POP:
                    sp.dec();
                    break;
                default:
                        throw new Error("invalid opcode: "+opcode+" at ip="+(ip.val-1));
            }
            if ( trace ){
                System.err.printf("%-22s \n", stackString());
//                System.err.printf("I %-35s\n", disInstr());
//                System.err.println(stackString());
            }
	}
        
        
	protected String stackString() {
            StringBuilder buf = new StringBuilder();
            buf.append("stack=[");
            for (int i =0; i <= sp.val; i++) {
                Integer o = userMemoryAccess(i);
                buf.append(" ");
                buf.append(o);
            }
            buf.append(" ]");
            return buf.toString();
	}


	protected String disInstr() {
		int opcode = codeAccess(ip.val);
		String opName = Bytecode.instructions[opcode].name;
		StringBuilder buf = new StringBuilder();
		buf.append(String.format("%04d:\t %-11s", ip.val, opName));
		int nargs = Bytecode.instructions[opcode].n;
                if ( nargs > 0 ) {
                    List<String> operands = new ArrayList<String>();
                    for (int i = ip.val + 1; i <= ip.val + nargs; i++) {
                        operands.add(String.valueOf(codeAccess(i)));
                    }
                    for (int i = 0; i < operands.size(); i++) {
                        String s = operands.get(i);
                        if (i > 0) buf.append(", ");
                        buf.append(s);
                    }
		}
		return buf.toString();
	}

}
