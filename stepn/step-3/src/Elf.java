import java.util.Random;

public class Elf implements Runnable {
	enum ElfState {
		WORKING, TROUBLE, AT_SANTAS_DOOR
	};

	private ElfState state;
	/**
	 * The number associated with the Elf
	 */
	private int number;
	private Random rand = new Random();
	private SantaScenario scenario;
	private volatile boolean finished = false;

	public Elf(int number, SantaScenario scenario) {
		this.number = number;
		this.scenario = scenario;
		this.state = ElfState.WORKING;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public ElfState getState() {
		return state;
	}

	/**
	 * Santa might call this function to fix the trouble
	 * @param state
	 */
	public void setState(ElfState state) {
		this.state = state;
	}


	@Override
	public void run() {
		while (!finished) {
      	// wait a day
  		try {
  			Thread.sleep(100);
  		} catch (InterruptedException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
			switch (state) {
			case WORKING: {
				// at each day, there is a 1% chance that an elf runs into
				// trouble.
				if (rand.nextDouble() < 0.01) {
					state = ElfState.TROUBLE;
				}
				break;
			}
			case TROUBLE:
				// Add elf to waitlist if less than 3 elves currently waiting
				if (scenario.elvesWaitList.size() < 3 && !(scenario.elvesWaitList.contains(this))) {
					scenario.addElfToWaitList(this);
				}
				break;
			case AT_SANTAS_DOOR:
				// If santa is asleep and there are 3 elves at his door, wake up Santa
				if(scenario.santa.getState() == Santa.SantaState.SLEEPING &&
						scenario.santa.numElvesAtDoor() == 3
				) {
					wakeUpSanta();
				}
				break;
			}
		}
	}

	private void wakeUpSanta() {
		scenario.santa.setState(Santa.SantaState.WOKEN_UP_BY_ELVES);
	}

	/**
	 * Report about my state
	 */
	public void report() {
		System.out.println("Elf " + number + " : " + state);
	}
}