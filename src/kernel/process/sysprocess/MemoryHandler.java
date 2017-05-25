/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel.process.sysprocess;

import kernel.process.Proc;

/**
 *
 * @author lemmin
 */
public class MemoryHandler extends Proc implements Handler{

    @Override
    public int stepLogic() {
        return 2;
    }
    
}
