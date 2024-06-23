import com.oocourse.library3.LibraryBookId;
import com.oocourse.library3.LibraryMoveInfo;
import com.oocourse.library3.annotation.Trigger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BS {
    private static BS instance = new BS();
    //private static HashMap<LibraryBookId, Book> books = new HashMap<>();
    private HashMap<LibraryBookId, Integer> numbers = new HashMap<>(); // 可借阅的活动书籍
    /********* for transition ***********/
    private int isInit;

    private BS() {
    }

    public static BS getInstance() {
        return instance;
    }

    public int queryNumber(LibraryBookId bookId) {
        return numbers.getOrDefault(bookId, 0);
    }

    @Trigger(from = "BS", to = "AO")
    public void tryCollectOrders(LocalDate date, ArrayList<LibraryMoveInfo> printBsRes,
                                 HashMap<String, ArrayList<LibraryBookId>> orderedLists) {

        Iterator<Map.Entry<String, ArrayList<LibraryBookId>>> itStudents =
                orderedLists.entrySet().iterator();
        while (itStudents.hasNext()) {
            Map.Entry<String, ArrayList<LibraryBookId>> entry = itStudents.next();
            Iterator<LibraryBookId> itBookIds = entry.getValue().iterator();
            while (itBookIds.hasNext()) {
                LibraryBookId bookId = itBookIds.next();
                if (isAnyBookOnShelf(bookId)) {
                    // orderedLists一个订单解决
                    itBookIds.remove();
                    Library.getInstance().removeOrdersQueriedId(bookId);
                    // 从书架中取出
                    removeOneBook(bookId);
                    // 送入预约台
                    AO.getInstance().addOrder(date, bookId, entry.getKey());
                    printBsRes.add(new LibraryMoveInfo(bookId, "bs", "ao", entry.getKey()));
                }
            }
            if (entry.getValue().isEmpty()) {
                itStudents.remove();
            }
        }
    }

    public boolean isAnyBookOnShelf(LibraryBookId bookId) {
        return (numbers.containsKey(bookId) && numbers.get(bookId) > 0);
    }

    public void addBook(LibraryBookId bookId, int num) {
        numbers.put(bookId, num + numbers.getOrDefault(bookId, 0));
        //System.out.println("delete BS adding: " + bookId + " " + newNum);
    }

    public void removeOneBook(LibraryBookId bookId) {
        int newNum = -1 + numbers.get(bookId);
        numbers.put(bookId, newNum);
        //System.out.println("delete BS removing: " + bookId + " " + newNum);
        if (newNum < 0) {
            throw new RuntimeException("Num < 0 in BS");
        }
    }

    @Trigger(from = "InitState", to = "BS")
    public void init(Map<LibraryBookId, Integer> books) {
        isInit = 1;
        numbers.putAll(books);
        isInit = 0;
    }

}
