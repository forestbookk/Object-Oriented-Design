import com.oocourse.library1.LibraryBookId;

import java.util.HashSet;

public class Student {
    private String id;
    private LibraryBookId bookB;
    private HashSet<LibraryBookId> bookCs;
    /*private HashMap<LibraryBookId, Integer> reservedBookBs = new HashMap<>();
    private HashMap<LibraryBookId, Integer> reservedBookCs = new HashMap<>();*/

    public Student(String studentId) {
        this.id = studentId;
        this.bookB = null;
        this.bookCs = new HashSet<>();
    }

    public boolean addBook(LibraryBookId bookId) {
        if (bookId.isTypeB() && bookB == null) {
            bookB = bookId;
        } else if (bookId.isTypeC() && !bookCs.contains(bookId)) {
            bookCs.add(bookId);
        } else {
            return false;
        }
        return true;
    }

    public void removeBook(LibraryBookId bookId) {
        if (bookId.isTypeB()) {
            bookB = null;
        } else if (bookId.isTypeC()) {
            bookCs.remove(bookId);
        }
    }

    public boolean ifHoldB() {
        return bookB != null;
    }

    public boolean ifHoldIdC(LibraryBookId bookId) {
        return bookCs.contains(bookId);
    }
}
