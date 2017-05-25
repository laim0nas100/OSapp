/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misc;

import LibraryLB.FileManaging.FileReader;
import LibraryLB.Parsing.Lexer;
import LibraryLB.Parsing.Literal;
import LibraryLB.Parsing.Token;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import kernel.stackvm.Bytecode;
import kernel.stackvm.Bytecode.Instruction;

/**
 *
 * @author lemmin
 */
public class MiniCompiler {
    private static boolean print = false;
  
    public static Instruction[] instructions = Bytecode.instructions;
    
    public static HashMap<String,Integer> instrMap = new HashMap<>();
    public static HashMap<String,Integer> instrArg = new HashMap<>();
    
    public static Integer[] compile(String filePath) throws FileNotFoundException, IOException, Lexer.NoSuchLexemeException, Lexer.StringNotTerminatedException{
        ArrayList<Integer> code = new ArrayList<>();
        
        ArrayList<String> readFromFile = new ArrayList<>(FileReader.readFromFile(filePath,"#"));
        Lexer lex = new Lexer(readFromFile);
        int j = 0;
        
        for(Bytecode.Instruction instr:Bytecode.instructions){
            if(instr!=null){
                lex.addToken(instr.name);
                instrMap.put(instr.name, j);
                instrArg.put(instr.name, instr.n);
            }
            j++;
        }
        lex.addToken("label");
        lex.skipWhitespace = true;
        
        ArrayList<Token> tokens = new ArrayList<>(lex.getRemainingTokens());
        if(print){
            for(Token t:tokens){
                System.out.println(t);
            }
        }
        
        HashSet<String> jumps = new HashSet<>();
        jumps.add(instructions[Bytecode.BR].name);
        jumps.add(instructions[Bytecode.BRF].name);
        jumps.add(instructions[Bytecode.BRT].name);
        HashMap<String,Integer> jumpMap = new HashMap<>();
        
        int codeIndex = 0;
        for(int i = 0; i < tokens.size(); i++){
            Token t = tokens.get(i);
            if(t.id.equals("label")){
                i++;
                Literal label = (Literal) tokens.get(i);
                jumpMap.put(label.value, codeIndex);
            }
            codeIndex++;
        }
        Literal l = (Literal) tokens.get(0);
        code.add(Integer.parseInt(l.value)); // set number of global variables
        for(int i = 1; i < tokens.size(); i++){
            Token t = tokens.get(i);
            String id = t.id;
            if(id.equals("label")){
                code.add(instructions[Bytecode.NOP].n);
                i++;//skip instruction
            }else{
                code.add(instrMap.get(id));
                Integer narg = instrArg.get(id);
                if(narg == null){
                    continue;
                }
                if(jumps.contains(id)){//jump
                    i++;
                    Literal label = (Literal) tokens.get(i);
                    code.add(jumpMap.get(label.value));
                }else if(narg > 0){                    
                    //variable argument amount
                    int size = i;
                    while(i < size + narg){
                        i++;
                        Token get = tokens.get(i);
                        Literal val = (Literal) get;
                        Integer v = Integer.parseInt(val.value);
                        code.add(v);
                    }
                }
            }
        }
        int errorIndex = code.indexOf(null)+1;
        if(errorIndex>0){
            System.out.println("Code contains errors at "+errorIndex);
        }
        if(print){
            j = 0;
            for(Integer i:code){
                System.out.printf("%02d: %02d\n",j++,i);
            }
        }
        
        Integer[] cod = code.toArray(new Integer[code.size()]);
        return cod;
    }
}
