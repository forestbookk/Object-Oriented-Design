import com.oocourse.library1.LibraryBookId;
import com.oocourse.library1.LibraryMoveInfo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BRo {
    private static BRo instance = new BRo();
    private HashMap<LibraryBookId, Integer> numbers = new HashMap<>();

    private BRo() {
    }

    public static BRo getInstance() {
        return instance;
    }

    public void addBook(LibraryBookId bookId) {
        numbers.put(bookId, 1 + numbers.getOrDefault(bookId, 0));
        //System.out.println("delete bro adding:" + bookId + " " + numbers.get(bookId));
    }

    public void removeBook(LibraryBookId bookId) {
        numbers.put(bookId, -1 + numbers.get(bookId));
        //System.out.println("delete bro removing:" + bookId + " " + numbers.get(bookId));
    }

    public void transferBookToShelf(LocalDate date, ArrayList<LibraryMoveInfo> printRes) {
        for (Map.Entry<LibraryBookId, Integer> entry : numbers.entrySet()) {
            LibraryBookId bookId = entry.getKey();
            int num = entry.getValue();
            if (num > 0) {
                // 放入書架
                BS.getInstance().addBook(bookId, num);
                for (int i = 0; i < num; i++) {
                    printRes.add(new LibraryMoveInfo(bookId, "bro", "bs"));
                    /*printRes.add("[" + date + "] move " + bookId + " from bro to bs");*/
                }
            }
        }
        // 清空借还台
        numbers.clear();
    }

    public boolean isAnyBookOnBro(LibraryBookId bookId) {
        return numbers.containsKey(bookId) && (numbers.get(bookId) > 0);
    }
}
