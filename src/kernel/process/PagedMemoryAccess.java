/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel.process;

/**
 *
 * @author lemmin
 */
public interface PagedMemoryAccess {
    public void userMemorySet(int va, int val);
    public int userMemoryAccess(int va);
    public int codeAccess(int va);
    public int codeSpaceSize();
}
