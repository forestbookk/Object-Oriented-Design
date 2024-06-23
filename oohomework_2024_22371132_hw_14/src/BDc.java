import com.oocourse.library2.LibraryBookId;
import com.oocourse.library2.LibraryMoveInfo;
import com.oocourse.library2.annotation.Trigger;

import java.util.ArrayList;
import java.util.HashMap;

public class BDc {
    private static BDc instance = new BDc();
    private HashMap<LibraryBookId, Integer> numbers = new HashMap<>();
    /********* for transition ***********/
    private int bdcUpdate;

    private BDc() {
    }

    public static BDc getInstance() {
        return instance;
    }

    public void addBook(LibraryBookId bookId, int num) {
        numbers.put(bookId, num + numbers.getOrDefault(bookId, 0));
        //System.out.println("delete bro adding:" + bookId + " " + numbers.get(bookId));
    }

    public void removeBook(LibraryBookId bookId) {
        numbers.put(bookId, -1 + numbers.get(bookId));
        //System.out.println("delete bro removing:" + bookId + " " + numbers.get(bookId));
    }

    @Trigger(from = "BDC", to = "BS")
    public void transferFormal(LibraryBookId bookId, LibraryBookId formalBookId,
                               ArrayList<LibraryMoveInfo> printRes) {
        int curNum = numbers.getOrDefault(bookId, 0);
        for (int i = 0; i < curNum; i++) {
            printRes.add(new LibraryMoveInfo(bookId, "bdc", "bs"));
        }
        // 从bdc移出
        numbers.remove(bookId);
        // 改变编号，放入书架
        BS.getInstance().addBook(formalBookId, curNum);
    }

    public int queryNumber(LibraryBookId bookId) {
        // 对于图书漂流角的非正式图书，查询漂流角内某书号书的余本数。
        return numbers.getOrDefault(bookId, 0);
    }

    public boolean isAnyBookInBDc(LibraryBookId bookId) {
        return (numbers.containsKey(bookId) && numbers.get(bookId) > 0);
    }
}
