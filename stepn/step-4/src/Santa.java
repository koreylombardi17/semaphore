import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Santa implements Runnable {
	enum SantaState {SLEEPING, READY_FOR_CHRISTMAS, WOKEN_UP_BY_ELVES, WOKEN_UP_BY_REINDEER};
	public Semaphore semaphore;
	private SantaState state;
	private List<Elf> elvesAtDoor;
	private volatile boolean finished = false;
	
	public Santa(SantaScenario scenario) {
		this.state = SantaState.SLEEPING;
		this.semaphore = new Semaphore(3);
		this.elvesAtDoor = new ArrayList<>();
	}

	public void addElfToDoor(Elf elf) {
		this.elvesAtDoor.add(elf);
	}

	public int numElvesAtDoor() {
		return elvesAtDoor.size();
	}

	public List<Elf> getElvesAtDoor() {
		return elvesAtDoor;
	}

	public SantaState getState() {
		return state;
	}

	public void setState(SantaState state) {
		this.state = state;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	@Override
	public void run() {
		while(!finished) {
			// wait a day...
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			switch(state) {
			case SLEEPING: // if sleeping, continue to sleep
				break;
			case WOKEN_UP_BY_ELVES: 
				// Solve the problems of the elves who are at the door and go back to sleep
				solveElvesProblems();
				break;
			case WOKEN_UP_BY_REINDEER: 
				// FIXME: assemble the reindeer to the sleigh then change state to ready 
				break;
			case READY_FOR_CHRISTMAS: // nothing more to be done
				break;
			}
		}
	}

	// Must use iterator to remove elf from list. If removed in a loop, concurrency exception is thrown
	public void solveElvesProblems() {
		Iterator iterator = elvesAtDoor.iterator();
		while(iterator.hasNext()) {
			Object elfObj = iterator.next();
			Elf elf = (Elf)elfObj;
			elf.setState(Elf.ElfState.WORKING);
			iterator.remove();
			semaphore.release();
		}
		this.state = SantaState.SLEEPING;
	}

	/**
	 * Report about my state
	 */
	public void report() {
		System.out.println("Santa : " + state);
	}
}
