import com.oocourse.library2.LibraryBookId;

import java.time.LocalDate;
import java.util.HashMap;

public class Student {
    private String id;
    private Stamp bookB;
    private Stamp bookBU;
    private HashMap<LibraryBookId, Stamp> bookCs;
    private HashMap<LibraryBookId, Stamp> bookCUs;

    public Student(String studentId) {
        this.id = studentId;
        this.bookB = null;
        this.bookBU = null;
        this.bookCs = new HashMap<>();
        this.bookCUs = new HashMap<>();
    }

    public boolean addBook(LocalDate date, LibraryBookId bookId) {
        if (bookId.isTypeB() && bookB == null) {
            bookB = new Stamp(30, date, bookId);
        } else if (bookId.isTypeBU() && bookBU == null) {
            bookBU = new Stamp(7, date, bookId);
        } else if (bookId.isTypeC() && !bookCs.containsKey(bookId)) {
            bookCs.put(bookId, new Stamp(60, date, bookId));
        } else if (bookId.isTypeCU() && !bookCUs.containsKey(bookId)) {
            bookCUs.put(bookId, new Stamp(14, date, bookId));
        } else {
            return false;
        }
        return true;
    }

    public void removeBook(LibraryBookId bookId) {
        if (bookId.isTypeB()) {
            bookB = null;
        } else if (bookId.isTypeBU()) {
            bookBU = null;
        } else if (bookId.isTypeC()) {
            bookCs.remove(bookId);
        } else if (bookId.isTypeCU()) {
            bookCUs.remove(bookId);
        }
    }

    public boolean ifHoldB() {
        return bookB != null;
    }

    public boolean ifHoldBU() {
        return bookBU != null;
    }

    public boolean ifHoldIdC(LibraryBookId bookId) {
        return bookCs.containsKey(bookId);
    }

    public boolean ifHoldIdCU(LibraryBookId bookId) {
        return bookCUs.containsKey(bookId);
    }

    public LocalDate getDue(LibraryBookId bookId) {
        LocalDate lastAvailableDate = null;
        if (bookId.isTypeB()) {
            lastAvailableDate = bookB.getDue();
        } else if (bookId.isTypeC()) {
            lastAvailableDate = bookCs.get(bookId).getDue();
        } else if (bookId.isTypeBU()) {
            lastAvailableDate = bookBU.getDue();
        } else if (bookId.isTypeCU()) {
            lastAvailableDate = bookCUs.get(bookId).getDue();
        }
        return lastAvailableDate;
    }

    public boolean isOverdue(LocalDate date, LibraryBookId bookId) {
        if (bookId.isTypeB()) {
            return bookB.isOverdue(date);
        } else if (bookId.isTypeC()) {
            return bookCs.get(bookId).isOverdue(date);
        } else if (bookId.isTypeBU()) {
            return bookBU.isOverdue(date);
        } else if (bookId.isTypeCU()) {
            return bookCUs.get(bookId).isOverdue(date);
        }
        return false;
    }

    public void renewDue(LibraryBookId bookId, int plusPeriod) {
        if (bookId.isTypeB()) {
            bookB.addPeriod(plusPeriod);
        } else if (bookId.isTypeC()) {
            bookCs.get(bookId).addPeriod(plusPeriod);
        } else if (bookId.isTypeBU()) {
            bookBU.addPeriod(plusPeriod);
        } else if (bookId.isTypeCU()) {
            bookCUs.get(bookId).addPeriod(plusPeriod);
        }
    }
}
