import com.oocourse.library1.LibraryBookId;

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
}
