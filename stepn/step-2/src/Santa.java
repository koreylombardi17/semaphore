import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Santa implements Runnable {
	enum SantaState {SLEEPING, READY_FOR_CHRISTMAS, WOKEN_UP_BY_ELVES, WOKEN_UP_BY_REINDEER};
	private SantaState state;
	private List<Elf> elvesAtDoor;
	private volatile boolean finished = false;
	
	public Santa(SantaScenario scenario) {
		this.state = SantaState.SLEEPING;
		this.elvesAtDoor = new ArrayList<>();
	}

	public void addElfToDoor(Elf elf) {
		this.elvesAtDoor.add(elf);
	}

	public int numElvesAtDoor() {
		return elvesAtDoor.size();
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
	private void solveElvesProblems() {
		Iterator iterator = elvesAtDoor.iterator();
		while(iterator.hasNext()) {
			Object elfObj = iterator.next();
			Elf elf = (Elf)elfObj;
			elf.setState(Elf.ElfState.WORKING);
			iterator.remove();
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
