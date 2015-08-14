package ElevatorSystem; 
import java.util.ArrayList; 
import java.util.Arrays; 
import java.util.List; 


public   class  Elevator {
	
	//@Symbolic("false")
	Environment env;

	
	//@Symbolic("false")
	boolean verbose;

	
	//@Symbolic("false")
	int currentFloorID;

	
	public enum  Direction {
		up { 
			@Override public Direction reverse() {return down;}	
		} ,  
		down { 
			@Override public Direction reverse() {return up;}
		}; 
		public abstract Direction reverse();}

	
	//@Symbolic("false")
	Direction currentHeading;

	
	//@Symbolic("false")
	private List<Person> persons = new ArrayList<Person>();

	
	 enum  DoorState {open ,  close}

	;

	
	//@Symbolic("false")
	DoorState doors;

	
	//@Symbolic("false")
	boolean[] floorButtons;

	
	
	public Elevator(Environment env, boolean verbose) {
		this.verbose = verbose;
		this.currentHeading = Direction.up;
		this.currentFloorID = 0;
		this.doors = DoorState.open;
		this.env = env;
		this.floorButtons = new boolean[env.floors.length];
	}

	
	public Elevator(Environment env, boolean verbose, int floor, boolean headingUp) {
		this.verbose = verbose;
		this.currentHeading = (headingUp ? Direction.up : Direction.down);
		this.currentFloorID = floor;
		this.doors = DoorState.open;
		this.env = env;
		this.floorButtons = new boolean[env.floors.length];
	}

	
	public boolean isBlocked  () {
		return blocked;
	}

	
	
	 private void  enterElevator__wrappee__Base  (Person p) {
		persons.add(p);
		p.enterElevator(this);
		if (verbose) System.out.println(p.getName() + " entered the Elevator at Landing " + this.getCurrentFloorID() + ", going to " + p.getDestination());
	}

	
	public void enterElevator(Person p) {
		enterElevator__wrappee__Base(p);
		weight+=p.getWeight();
	}

	
	
	 private boolean  leaveElevator__wrappee__Base  (Person p) {
		if (persons.contains(p)) {
			persons.remove(p);
			p.leaveElevator();
			if (verbose) System.out.println(p.getName() + " left the Elevator at Landing " + currentFloorID);
			return true;
		} else return false;
	}

	
	
	 private boolean  leaveElevator__wrappee__Weight  (Person p) {
		if (leaveElevator__wrappee__Base(p)) {
			weight-=p.getWeight();
			return true;
		} else return false;
	}

	
	public boolean leaveElevator(Person p) { // empty
		if (leaveElevator__wrappee__Weight(p)) {
			if (this.persons.isEmpty())
				Arrays.fill(this.floorButtons, false);
			return true;
		} else return false;
	}

	
	
	/**
	 * Activates the button for the given floor in the lift.
	 * @param floorID
	 */
	public void pressInLiftFloorButton(int floorID) {
		floorButtons[floorID] = true;
	}

	
	private void resetFloorButton(int floorID) {
		floorButtons[floorID] = false;
	}

	
	public int getCurrentFloorID() {
		return currentFloorID;
	}

	
	
	public boolean areDoorsOpen() {
		return doors == DoorState.open;
	}

	
	// pre: elevator arrived at the current floor, next actions to be done
	 private void  timeShift__wrappee__Base  () {
		//System.out.println("--");
		
		if (stopRequestedAtCurrentFloor()) {
			//System.out.println("Arriving at " +  currentFloorID + ", Doors opening");
			doors = DoorState.open;
			// iterate over a copy of the original list, avoids concurrent modification exception
			for (Person p: new ArrayList<Person>(persons)) {
				if (p.getDestination() == currentFloorID) {
					leaveElevator(p);					
				}
			}
			env.getFloor(currentFloorID).processWaitingPersons(this);
			resetFloorButton(currentFloorID);
		} else {
			if (doors == DoorState.open)  {
				doors = DoorState.close;
				//System.out.println("Doors Closing");
			}
			if (stopRequestedInDirection(currentHeading, true, true)) {
				//System.out.println("Arriving at " + currentFloorID + ", continuing");
				// continue
				continueInDirection(currentHeading);
			} else if (stopRequestedInDirection(currentHeading.reverse(), true, true)) {
				//System.out.println("Arriving at " + currentFloorID + ", reversing direction because of call in other direction");
				// revert direction
				continueInDirection(currentHeading.reverse());
			} else {
				//idle
				//System.out.println("Arriving at " + currentFloorID + ", idle->continuing");
				continueInDirection(currentHeading);
			}
		}
	}

	
	public void timeShift() {
		if (areDoorsOpen() && weight > maximumWeight) {
			blocked = true;
			if (verbose) System.out.println("Elevator blocked due to overloading (weight:" + weight + " > maximumWeight:" + maximumWeight + ")");
		} else {
			blocked = false;
			timeShift__wrappee__Base();
		}
	}

	
	 private boolean  stopRequestedAtCurrentFloor__wrappee__Base  () {
		return env.getFloor(currentFloorID).hasCall() 
				|| floorButtons[currentFloorID] == true;
	}

	
	// alternative implementation: subclass of "ExecutiveFloor extends Floor"
	 private boolean  stopRequestedAtCurrentFloor__wrappee__ExecutiveFloor  () { //executive
		if (isExecutiveFloorCalling() && !isExecutiveFloor(currentFloorID)) {
			return false;
		} else return stopRequestedAtCurrentFloor__wrappee__Base();
	}

	
	private boolean stopRequestedAtCurrentFloor() {
		if (weight > maximumWeight*2/3) {
			return floorButtons[currentFloorID] == true;
		} else return stopRequestedAtCurrentFloor__wrappee__ExecutiveFloor();
	}

	
	
	private void continueInDirection(Direction dir) {
		currentHeading = dir;
		if (currentHeading == Direction.up) {
			if (env.isTopFloor(currentFloorID)) {
				//System.out.println("Reversing at Top Floor");
				currentHeading = currentHeading.reverse();
			}
		} else { 
			if (currentFloorID == 0) {
				//System.out.println("Reversing at Basement Floor");
				currentHeading = currentHeading.reverse();
			}
		}
		if (currentHeading == Direction.up) {
			currentFloorID = currentFloorID + 1;
		} else {
			currentFloorID = currentFloorID - 1;
		}
	}

	

	
	private boolean isAnyLiftButtonPressed() {
		for (int i = 0; i < this.floorButtons.length; i++) {
			if (floorButtons[i]) return true;
		}
		return false;
	}

	
	
	 private boolean  stopRequestedInDirection__wrappee__Base  (Direction dir, boolean respectFloorCalls, boolean respectInLiftCalls) {
		Floor[] floors = env.getFloors();
		if (dir == Direction.up) {
			if (env.isTopFloor(currentFloorID)) return false;
			for (int i = currentFloorID+1; i < floors.length; i++) {
				if (respectFloorCalls && floors[i].hasCall()) return true;
				if (respectInLiftCalls && this.floorButtons[i]) return true; 
			}
			return false;
		} else {
			if (currentFloorID == 0) return false;
			for (int i = currentFloorID-1; i >= 0; i--) {
				if (respectFloorCalls && floors[i].hasCall()) return true;
				if (respectInLiftCalls && this.floorButtons[i]) return true;
			}
			return false;
		}
	}

	
	 private boolean  stopRequestedInDirection__wrappee__ExecutiveFloor  (Direction dir, boolean respectFloorCalls, boolean respectInLiftCalls) {
		if (isExecutiveFloorCalling()) {
			if (verbose) System.out.println("Giving Priority to Executive Floor");
			return ((this.currentFloorID < executiveFloor)  == (dir == Direction.up));
			
		} else return stopRequestedInDirection__wrappee__Base(dir, respectFloorCalls, respectInLiftCalls);
	}

	
	private boolean stopRequestedInDirection (Direction dir, boolean respectFloorCalls, boolean respectInLiftCalls) {
		if (weight > maximumWeight*2/3 && isAnyLiftButtonPressed()) {
			if (verbose) System.out.println("over 2/3 threshold, ignoring calls from FloorButtons until weight is below 2/3*threshold");
			return stopRequestedInDirection__wrappee__ExecutiveFloor(dir, false, respectInLiftCalls);
		} else return stopRequestedInDirection__wrappee__ExecutiveFloor(dir, respectFloorCalls, respectInLiftCalls);
	}

	
	private boolean anyStopRequested () {
		Floor[] floors = env.getFloors();
		for (int i = 0; i < floors.length; i++) {
			if (floors[i].hasCall()) return true;
			else if (this.floorButtons[i]) return true; 
		}
		return false;		
	}

	

	public boolean buttonForFloorIsPressed(int floorID) {
		return this.floorButtons[floorID];
	}

	

	public Direction getCurrentDirection() {
		return currentHeading;
	}

	
	public Environment getEnv() {
		return env;
	}

	
	public boolean isEmpty() {
		return this.persons.isEmpty();
	}

	
	public boolean isIdle() {
		return !anyStopRequested();
	}

	
	
	@Override
	public String toString() {
		return "Elevator " + (areDoorsOpen() ? "[_]" :  "[] ") + " at " + currentFloorID + " heading " + currentHeading;
	}

	
	//TODO: implement the weight property in Persons in this Feature
	int weight;

	
	private static final int maximumWeight = 100;

	
	int executiveFloor = 4;

	
	public boolean isExecutiveFloor(int floorID) {return floorID == executiveFloor; }

	
	//private boolean isExecutiveFloor(Floor floor) {return floor.getFloorID() == executiveFloor; }
	public boolean isExecutiveFloorCalling() {
		for (Floor f : env.floors) 
			if (f.getFloorID() == executiveFloor && f.hasCall()) return true;
		return false;
	}

	
	private boolean blocked = false;


}
