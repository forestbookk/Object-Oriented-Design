import com.oocourse.library1.LibraryBookId;
import com.oocourse.library1.LibraryCommand;
import com.oocourse.library1.LibraryRequest;
import com.oocourse.library1.LibrarySystem;

import java.time.LocalDate;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<LibraryBookId, Integer> inventory = LibrarySystem.SCANNER.getInventory();
        BS.getInstance().init(inventory);
        LocalDate date;
        while (true) {
            LibraryCommand<?> command = LibrarySystem.SCANNER.nextCommand();
            if (command == null) {
                break;
            }
            date = command.getDate();
            if (command.getCmd().equals("OPEN")) {
                Library.getInstance().openOrClose(date, false);
            } else if (command.getCmd().equals("CLOSE")) {
                Library.getInstance().openOrClose(date, true);
            } else {
                LibraryRequest request = (LibraryRequest) command.getCmd();
                LibraryRequest.Type requestType = request.getType();
                Library.getInstance().prepare(request.getStudentId());
                if (requestType == LibraryRequest.Type.BORROWED) {
                    Library.getInstance().borrowed(date, request);
                } else if (requestType == LibraryRequest.Type.RETURNED) {
                    Library.getInstance().returned(date, request);
                } else if (requestType == LibraryRequest.Type.ORDERED) {
                    Library.getInstance().ordered(date, request);
                } else if (requestType == LibraryRequest.Type.PICKED) {
                    Library.getInstance().picked(date, request);
                } else if (requestType == LibraryRequest.Type.QUERIED) {
                    Library.getInstance().queried(date, request);
                }
            }
        }
    }
}
