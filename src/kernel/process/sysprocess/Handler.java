/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernel.process.sysprocess;

import java.util.ArrayList;
import kernel.resource.Job;

/**
 *
 * @author lemmin
 * Simulates system process, i.e. handles jobs and does it's routine
 */
public interface Handler{
    public final ArrayList<Job> jobs = new ArrayList<>();
}
