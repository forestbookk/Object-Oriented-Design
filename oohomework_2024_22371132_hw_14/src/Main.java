import com.oocourse.library2.LibraryBookId;
import com.oocourse.library2.LibraryCloseCmd;
import com.oocourse.library2.LibraryCommand;
import com.oocourse.library2.LibraryOpenCmd;
import com.oocourse.library2.LibraryReqCmd;
import com.oocourse.library2.LibraryRequest;
import com.oocourse.library2.LibrarySystem;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<LibraryBookId, Integer> inventory = LibrarySystem.SCANNER.getInventory();
        BS.getInstance().init(inventory);
        while (true) {
            LibraryCommand command = LibrarySystem.SCANNER.nextCommand();
            if (command == null) {
                break;
            }
            if (command instanceof LibraryOpenCmd) {
                Library.getInstance().open(command.getDate());
            } else if (command instanceof LibraryCloseCmd) {
                Library.getInstance().close(command.getDate());
            } else {
                LibraryReqCmd request = (LibraryReqCmd) command;
                LibraryRequest.Type requestType = request.getType();
                Library.getInstance().prepare(request.getStudentId());
                if (requestType == LibraryRequest.Type.BORROWED) {
                    Library.getInstance().borrowed(request);
                } else if (requestType == LibraryRequest.Type.RETURNED) {
                    Library.getInstance().returned(request);
                } else if (requestType == LibraryRequest.Type.ORDERED) {
                    Library.getInstance().ordered(request);
                } else if (requestType == LibraryRequest.Type.PICKED) {
                    Library.getInstance().picked(request);
                } else if (requestType == LibraryRequest.Type.QUERIED) {
                    Library.getInstance().queried(request);
                } else if (requestType == LibraryRequest.Type.RENEWED) {
                    Library.getInstance().renewed(request);
                } else if (requestType == LibraryRequest.Type.DONATED) {
                    Library.getInstance().donated(request);
                }
            }
        }
    }
}
