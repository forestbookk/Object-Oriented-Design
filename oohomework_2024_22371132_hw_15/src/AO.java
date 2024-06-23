import com.oocourse.library3.LibraryBookId;
import com.oocourse.library3.LibraryMoveInfo;
import com.oocourse.library3.annotation.Trigger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class AO {
    private static AO instance = new AO();
    private HashMap<String, LinkedList<Stamp>> orderSystem = new HashMap<>();
    private HashMap<LibraryBookId, Integer> ordersQueriedId = new HashMap<>();
    /********* for transition ***********/
    private int clear;
    private int overdue;

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
        ordersQueriedId.put(bookId, 1 + ordersQueriedId.getOrDefault(bookId, 0));
    }

    @Trigger(from = "AO", to = "STUDENT")
    public boolean tryPick(LocalDate date, LibraryBookId bookId,
                           String studentId, Student student) {
        if (orderSystem.containsKey(studentId)) {
            Iterator<Stamp> it = orderSystem.get(studentId).iterator();
            while (it.hasNext()) {
                Stamp item = it.next();
                if (item.getId().equals(bookId)) {
                    // 從預約台中取出
                    it.remove();
                    ordersQueriedId.put(bookId, -1 + ordersQueriedId.getOrDefault(bookId, 0));
                    // 放入用戶
                    student.addBook(date, bookId);
                    return true;
                }
            }
        }
        return false;
    }

    @Trigger(from = "AO", to = "BS")
    public void clearOrders(LocalDate date, ArrayList<LibraryMoveInfo> printRes, boolean isClose) {
        for (Map.Entry<String, LinkedList<Stamp>> item : orderSystem.entrySet()) {
            String studentId = item.getKey();
            Iterator<Stamp> itBookIds = item.getValue().iterator();
            while (itBookIds.hasNext()) {
                Stamp stamp = itBookIds.next();
                LibraryBookId bookId = stamp.getId();
                LocalDate due = stamp.getDate().plusDays(5);
                if ((isClose && due.equals(date)) ||
                        due.isBefore(date)) {
                    // 失效，用户每次预约图书成功，且图书馆已经将图书为该用户送至预约处后，
                    // 若该用户在规定的时间内未能取走该书，书籍逾期的时刻该用户信用分-3。
                    Library.getInstance().getStudents().get(studentId).getCredit().nonPick();
                    // 從預約台中取出
                    itBookIds.remove();
                    ordersQueriedId.put(bookId, -1 + ordersQueriedId.getOrDefault(bookId, 0));
                    // 放入書架
                    BS.getInstance().addBook(bookId, 1);
                    printRes.add(new LibraryMoveInfo(bookId, "ao", "bs"));
                } else {
                    break;
                }
            }
        }
    }

    public boolean containsOrder(LibraryBookId bookId) {
        return ordersQueriedId.containsKey(bookId) && ordersQueriedId.get(bookId) > 0;
    }

    public boolean isOrderOnForSpecific(LibraryBookId bookId, String studentId) {
        if (bookId.isTypeB()) {
            // 检查某B类书
            if (orderSystem.containsKey(studentId)) {
                for (Stamp stamp : orderSystem.get(studentId)) {
                    if (stamp.isTypeB()) {
                        return true;
                    }
                }
            }
        } else {
            // 检查某书号C类书
            if (orderSystem.containsKey(studentId)) {
                for (Stamp stamp : orderSystem.get(studentId)) {
                    if (stamp.getId().equals(bookId)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
