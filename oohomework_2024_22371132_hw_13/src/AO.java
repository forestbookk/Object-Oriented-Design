import com.oocourse.library1.LibraryBookId;
import com.oocourse.library1.LibraryMoveInfo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class AO {
    private static AO instance = new AO();
    private HashMap<String, LinkedList<Stamp>> orderSystem = new HashMap<>();

    private AO() {
    }

    public static AO getInstance() {
        return instance;
    }

    public void addOrder(LocalDate date, LibraryBookId bookId, String studentId) {
        Stamp stamp = new Stamp(date, bookId);
        if (orderSystem.containsKey(studentId)) {
            orderSystem.get(studentId).addLast(stamp);
        } else {
            LinkedList<Stamp> linkedList = new LinkedList<>();
            linkedList.addLast(stamp);
            orderSystem.put(studentId, linkedList);
        }
    }

    public boolean tryPick(LibraryBookId bookId, String studentId, Student student) {
        if (orderSystem.containsKey(studentId)) {
            Iterator<Stamp> it = orderSystem.get(studentId).iterator();
            while (it.hasNext()) {
                Stamp item = it.next();
                if (item.getId().equals(bookId)) {
                    // 從預約台中取出
                    it.remove();
                    // 放入用戶
                    student.addBook(bookId);
                    return true;
                }
            }
        }
        return false;
    }

    public void clearOrders(LocalDate date, ArrayList<LibraryMoveInfo> printRes, boolean isClose) {
        for (Map.Entry<String, LinkedList<Stamp>> item : orderSystem.entrySet()) {
            Iterator<Stamp> itBookIds = item.getValue().iterator();
            while (itBookIds.hasNext()) {
                Stamp stamp = itBookIds.next();
                LibraryBookId bookId = stamp.getId();
                LocalDate due = stamp.getDate().plusDays(5);
                if ((isClose && due.equals(date)) ||
                        due.isBefore(date)) {
                    // 從預約台中取出
                    itBookIds.remove();
                    // 放入書架
                    BS.getInstance().addBook(bookId, 1);
                    printRes.add(new LibraryMoveInfo(bookId, "ao", "bs"));
                    /*printRes.add("[" + date + "] move " + bookId + " from ao to bs");*/
                } else {
                    break;
                }
            }
        }
    }
}
