import com.oocourse.library2.LibraryBookId;

public class Machine {
    private static Machine instance = new Machine();

    private Machine() {

    }

    public static Machine getInstance() {
        return instance;
    }

    public int queryNumberOnBs(LibraryBookId bookId) {
        return BS.getInstance().queryNumber(bookId);
    }

    public int queryNumberOnBDc(LibraryBookId bookId) {
        // TO/DO: 查询漂流角内某书号书的余本数
        return BDc.getInstance().queryNumber(bookId);
    }
}
