import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class SantaScenario {
	public Santa santa;
	public List<Elf> elves;
	public static List<Elf> elvesWaitList;
	public List<Reindeer> reindeers;
	public boolean isDecember;
	
	public static void main(String args[]) {
		SantaScenario scenario = new SantaScenario();
		scenario.elvesWaitList = new ArrayList<>();
		scenario.isDecember = false;
		// create the participants
		// Santa
		scenario.santa = new Santa(scenario);
		Thread th = new Thread(scenario.santa);
		th.start();
		// The elves: in this case: 10
		scenario.elves = new ArrayList<>();
		for(int i = 0; i != 10; i++) {
			Elf elf = new Elf(i+1, scenario);
			scenario.elves.add(elf);
			th = new Thread(elf);
			th.start();
		}
		// now, start the passing of time
		for(int day = 1; day < 500; day++) {
			// wait a day
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// turn on December
			if (day > (365 - 31)) {
				scenario.isDecember = true;
			}
			// print out the state:
			System.out.println("***********  Day " + day + " *************************");
			if(day < 370) {
				scenario.santa.report();
				for (Elf elf : scenario.elves) {
					elf.report();
				}
			} else if(day == 370) {
				scenario.santa.setFinished(true);
				for (Elf elf : scenario.elves) {
					elf.setFinished(true);
				}
			}
			if (scenario.santa.numElvesAtDoor() == 3) {
				scenario.santa.solveElvesProblems();
			}
		}
	}

	public static void addElfToWaitList(Elf elf) {
		elvesWaitList.add(elf);
	}
}
