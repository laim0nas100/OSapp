package kernel.stackvm;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import kernel.CPU.Reg;
import kernel.Kernel;
import kernel.memory.Paging;
import kernel.process.PagedMemoryAccess;

/** A simple stack-based interpreter */
public class VM extends AbstractVM{

        
    // memory
    public Integer[] stack;		// Operand stack, grows upwards

    public Reg tp = new Reg(0);
    public Reg codeEnd = new Reg(0);
    public Reg p;
    /** Metadata about the functions allows us to refer to functions by
     * 	their index in this table. It makes code generation easier for
     * 	the bytecode compiler because it doesn't have to resolve
     *  addresses for forward references. It can generate simply
     *  "CALL i" where i is the index of the function. Later, the
     *  compiler can store the function address in the metadata table
     *  when the code is generated for that function.
     */

    public VM(Integer[] code, int nglobals) {
        stack = new Integer[100];
        for(int i=0; i<stack.length; i++){
            if(i<code.length){
                stack[i] = code[i];
            }
            else{
                stack[i] = 0;
            }
        }
        String n = "";
        String in = "";
        int ok = 0;
        for(Integer i:stack){
            n+= (i+"\t");
            in+=(ok++ +"\t");
        }
        System.err.println(in);
        System.err.println(n);
        codeEnd.val = code.length;
        sp.inc(nglobals);
        System.err.println(sp.val +" "+codeEnd.val +" "+ ip.val);

    }



    @Override
    public void userMemorySet(int va, int val){
//        System.err.println("UMS "+va);
//        System.err.println(this.codeEnd.val+ " "+ va);
        this.stack[va + this.codeEnd.val] = val;
    }
    @Override
    public int userMemoryAccess(int va){
        try{
            return this.stack[va + this.codeEnd.val];
        }catch (Exception e){
            System.err.println("UMA "+va);
            System.err.println(this.codeEnd.val+ " "+ va);
            return -1;
        }
    }
    public int codeAccess(int va){
//        System.err.println("Code Access "+va + " "+stack[va]);
        return this.stack[va];
    }

    @Override
    public int codeSpaceSize() {
        return this.codeEnd.val;
    }

    @Override
    public void INT(int id) {
        
        System.out.println("Interrupt "+id);
        
    }


}
