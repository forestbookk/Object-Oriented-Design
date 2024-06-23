import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;
import com.oocourse.elevator3.ResetRequest;

import java.io.IOException;

public class InputHandler extends Thread {
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput();
        while (true) {
            Request request = elevatorInput.nextRequest();
            // when request == null
            // it means there are no more lines in stdin
            if (request == null) {
                break;
            } else {
                // a new valid request
                if (request instanceof PersonRequest) {
                    // a PersonRequest
                    PersonRequest pr = (PersonRequest) request;
                    Scheduler.getInstance().addPersonRequest(new Person(pr.getPersonId(),
                            pr.getFromFloor(), pr.getToFloor()));
                } else if (request instanceof ResetRequest) {
                    // an ElevatorRequest
                    Scheduler.getInstance().dealResetRequest((ResetRequest) request);
                }
            }
        }
        try {
            //System.out.println("Input Over");
            Scheduler.getInstance().setInputEnd(true);
            elevatorInput.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
