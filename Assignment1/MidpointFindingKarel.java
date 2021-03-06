import stanford.karel.*;
public class MidpointFindingKarel extends SuperKarel {

	public void run() {
	    findCenter();
	    turnAround();
	    putBeeper();
	}
	
	public void findCenter() {
	    if(frontIsClear()) {   //
	        move();            //  Two steps forward
	        if(frontIsClear()) //  
	            move();        //
	        findCenter();      // call yourself
	        move();            // one step back
	    } else {
	        turnAround();
	    }
	}
}
