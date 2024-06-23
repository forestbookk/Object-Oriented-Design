import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class InputHandler extends Thread {
    private ElevatorFactory eleFact = new ElevatorFactory();
    private HashMap<Integer, RequestTable> requestTables = new HashMap<>();

    public void setRequestTablesEndTag() {
        for (Map.Entry<Integer, RequestTable> entry : requestTables.entrySet()) {
            entry.getValue().setEnd(true);
        }
    }

    public void addElevatorRequest(int eleId, boolean eleDire, RequestTable eleReqTab) {
        new Thread(eleFact.create(eleId, eleDire, eleReqTab), String.valueOf(eleId)).start();
    }

    public void addPersonRequest(Person pr, int eleId) {
        if (requestTables.containsKey(eleId)) {
            requestTables.get(eleId).addRequest(pr);
            //requestTables.get(eleId).printSize();
            return;
        }
        // eleId is not in requestTable
        RequestTable newRequestTable = new RequestTable();
        newRequestTable.addRequest(pr);
        requestTables.put(eleId, newRequestTable);
        addElevatorRequest(eleId, pr.getDirection(), newRequestTable);
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            PersonRequest rq = elevatorInput.nextPersonRequest();
            if (rq == null) {
                break;
            }
            addPersonRequest(new Person(rq.getPersonId(), rq.getFromFloor(),
                    rq.getToFloor()), rq.getElevatorId());
        }
        try {
            // close
            setRequestTablesEndTag();
            elevatorInput.close();
        } catch (IOException e) {
            System.out.println("InputHandler IOException");
        }
    }
}

