package kernel;
import kernel.memory.Paging;
import static kernel.process.PIC.*;
import kernel.process.UserProcess;
import kernel.process.Process.State;
import kernel.process.sysprocess.Scheduler;
import kernel.process.sysprocess.Scheduler.ProcStart;
import static kernel.stackvm.Bytecode. *;

public class Test {
	static int[] hello = {
		ICONST, 1,
		ICONST, 2,
		IADD,
		HALT
	};

	static Integer[] loop = {
	// .GLOBALS 2; N, I
	// N = 10						ADDRESS
			ICONST, 5,				// 0
			GSTORE, 0,				// 2
	// I = 0
			ICONST, 0,				// 4
			GSTORE, 1,				// 6
	// WHILE I<N:
	// START (8):
			GLOAD, 1,				// 8
			GLOAD, 0,				// 10
			ILT,					// 12
			BRF, 26,				// 13
	//     I = I + 1
			GLOAD, 1,				// 15
			ICONST, 1,				// 17
			IADD,					// 19
			GSTORE, 1,				// 20
                        INT, WRITE,
			BR, 8,					// 22
	// DONE (24):
	// PRINT "LOOPED "+N+" TIMES."
                        INT, 1,
			INT, HALT					// 24
	};
	

	public static void main(String[] args) throws Kernel.KernelExe {
            Kernel kernel = new Kernel();
            Scheduler.startNewProcess(new ProcStart(loop,2));
//            UserProcess pro = new UserProcess(loop,2,0);
//            UserProcess pro2 = new UserProcess(loop,2,1);

//            vm1.p = kernel.paging;
//            pro.vm.trace = true;
//            pro.cpu = Kernel.cpu;
//            pro2.vm.trace = true;
//            pro2.cpu = Kernel.cpu;
//            try{
//                pro2.state = State.ACTIVE;
//                pro.state = State.ZOMBIE;
//                pro.state = State.ACTIVE;
//            while(pro.state != State.ZOMBIE){
////                System.out.println("STEP");
//                pro.step();
////                pro2.step();
//                System.err.println(Kernel.usableSpace[10]);
////                System.err.println(Kernel.usableSpace[10]);
//
//            }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//            Kernel.dumpMemory();

//            while(!vm1.dead ){//|| !vm2.dead){
//                if(!vm1.dead){
//                    vm1.step();
//                }
//                if(!vm2.dead){
//                    vm2.step();
//                }
//            }
//            System.out.println(kernel.paging.dump());
//            vm2.exec(loop_metadata[0].address);
            
	}
}
