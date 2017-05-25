package misc;

import kernel.Reg;
import kernel.stackvm.AbstractVM;

/** A simple stack-based interpreter */
public class StandaloneVM extends AbstractVM{

        
    // memory
    public Integer[] stack;		// Operand stack, grows upwards

    public Reg tp = new Reg(0);
    public Reg codeEnd = new Reg(0);
    public Reg p;


    public StandaloneVM(Integer[] code) {
        stack = new Integer[100];
        sp.inc(code[0]);//globals
        for(int i=0; i<stack.length; i++){
            if(i<code.length){
                stack[i] = code[i+1];
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
