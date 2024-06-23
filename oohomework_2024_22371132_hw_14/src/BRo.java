import com.oocourse.library2.LibraryBookId;
import com.oocourse.library2.LibraryMoveInfo;
import com.oocourse.library2.annotation.Trigger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BRo {
    private static BRo instance = new BRo();
    private HashMap<LibraryBookId, Integer> numbers = new HashMap<>();
    private HashMap<LibraryBookId, Integer> brTimes = new HashMap<>(); // only for bdc
    /********* for transition ***********/
    private int fo;
    private int needUpdate;

    private BRo() {
    }

    public static BRo getInstance() {
        return instance;
    }

    public void addBook(LibraryBookId bookId) {
        numbers.put(bookId, 1 + numbers.getOrDefault(bookId, 0));
        //System.out.println("delete bro adding:" + bookId + " " + numbers.get(bookId));
    }

    @Trigger(from = "BRO", to = {"BS", "BDC"})
    public void transferBookToBsAndBdc(LocalDate date, ArrayList<LibraryMoveInfo> printRes) {
        for (Map.Entry<LibraryBookId, Integer> entry : numbers.entrySet()) {
            LibraryBookId bookId = entry.getKey();
            int bookNum = entry.getValue();
            // 判断bookId类型
            if (bookId.isFormal()) {
                // 放入書架
                BS.getInstance().addBook(bookId, bookNum);
                for (int i = 0; i < bookNum; i++) {
                    printRes.add(new LibraryMoveInfo(bookId, "bro", "bs"));
                }
            } else {
                // 开馆统计漂流角借还次数
                if (brTimes.containsKey(bookId) && brTimes.get(bookId) >= 2) {
                    LibraryBookId.Type formalType = null;
                    if (bookId.isTypeBU()) {
                        formalType = LibraryBookId.Type.B;
                    } else if (bookId.isTypeCU()) {
                        formalType = LibraryBookId.Type.C;
                    }
                    LibraryBookId formalBookId = new LibraryBookId(formalType, bookId.getUid());
                    // 当某书的借还次数达到2次，则必须将其送往书架，该书即刻起成为图书馆的正式在架书籍，类别号去掉U，序列号不变（如BU-0001->B-0001）
                    // TO/DO: 在转换前，此种书可能在bro，bdc，和用户
                    // 此处将在bro的书放到书架. 用户的书最终也会通过还书来到借还台。所以brTimes不清空！

                    // 新id转生，放入書架
                    BS.getInstance().addBook(formalBookId, bookNum);
                    // 最后一次使用原id移动
                    for (int i = 0; i < bookNum; i++) {
                        printRes.add(new LibraryMoveInfo(bookId, "bro", "bs"));
                    }

                    // 将bdc的书放到书架
                    BDc.getInstance().transferFormal(bookId, formalBookId, printRes);
                } else {
                    // 借还若未达到2次，则必须将其送回漂流角。
                    // 放入漂流角
                    BDc.getInstance().addBook(bookId, bookNum);
                    for (int i = 0; i < bookNum; i++) {
                        printRes.add(new LibraryMoveInfo(bookId, "bro", "bdc"));
                    }
                }
            }
        }
        // 清空借还台
        numbers.clear();
    }

    public void addBrTime(LibraryBookId bookId) {
        brTimes.put(bookId, 1 + brTimes.getOrDefault(bookId, 0));
    }

}
