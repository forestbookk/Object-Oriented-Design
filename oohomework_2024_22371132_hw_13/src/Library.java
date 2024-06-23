import com.oocourse.library1.LibraryBookId;
import com.oocourse.library1.LibraryMoveInfo;
import com.oocourse.library1.LibraryRequest;

import static com.oocourse.library1.LibrarySystem.PRINTER;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Library {
    private static final Library instance = new Library();
    private final BS bs = BS.getInstance();
    private final BRo bro = BRo.getInstance();
    private final AO ao = AO.getInstance();
    private final Machine machine = Machine.getInstance();
    private final HashMap<String, Student> students = new HashMap<>();
    private final HashMap<String, ArrayList<LibraryBookId>> orderedLists = new HashMap<>();

    private Library() {
    }

    public void prepare(String studentId) {
        if (!students.containsKey(studentId)) {
            students.put(studentId, new Student(studentId));
        }
    }

    public void openOrClose(LocalDate date, boolean isClose) {
        final ArrayList<LibraryMoveInfo> sumRes = new ArrayList<>();

        // 将失效的预约本放回书架
        ArrayList<LibraryMoveInfo> printAoRes = new ArrayList<>();
        ao.clearOrders(date, printAoRes, isClose);
        // 将借还处的书送回书架
        ArrayList<LibraryMoveInfo> printBroToBsRes = new ArrayList<>();
        bro.transferBookToShelf(date, printBroToBsRes);
        // 检查orderLists，将bs中可能的书送入ao,只在闭馆送入ao
        ArrayList<LibraryMoveInfo> printBsRes = new ArrayList<>();
        if (isClose) {
            bs.tryCollectOrders(date, printBsRes, orderedLists);
        }


        // output
        sumRes.addAll(printAoRes); // 1.
        sumRes.addAll(printBroToBsRes); // 2.
        sumRes.addAll(printBsRes); // 3.

        PRINTER.move(date, sumRes);
    }

    public static Library getInstance() {
        return instance;
    }

    public boolean canBorrow(LibraryBookId bookId, Student student) {
        if (bookId.isTypeB()) {
            // B 类书：一人同一时刻仅能持有一个 B 类书的副本。
            return !student.ifHoldB();
        } else if (bookId.isTypeC()) {
            // C 类书：对于每一个书号，一人同一时刻仅能持有一个具有该书号的书籍副本。
            return !student.ifHoldIdC(bookId);
        } else {
            // A 类书：仅能在图书馆阅览，不能外借，不可预约。
            return false;
        }
    }

    public void borrowed(LocalDate date, LibraryRequest request) {
        LibraryBookId bookId = request.getBookId();
        String studentId = request.getStudentId();
        boolean res = false;
        if (bs.isAnyBookOnShelf(bookId) && !bookId.isTypeA()) { // 有余本且（B或C）
            // 前往借还处登记 bro
            Student curStudent = students.get(studentId);
            res = canBorrow(bookId, curStudent);
            // 书架失去一本书
            bs.removeOneBook(bookId);
            if (res) {
                // 符合借阅数量限制（见后）,则借阅成功，该用户从此刻起持有该书
                curStudent.addBook(bookId);
            } else {
                // 否则借阅失败，书籍被扣在借还处，借还处从此刻起持有该书；
                bro.addBook(bookId);
            }
        }
        if (res) {
            PRINTER.accept(date, request);
        } else {
            PRINTER.reject(date, request);
        }
        /*System.out.println("[" + date + "] [" + (res ? "accept" : "reject") + "] "
                + studentId + " borrowed " + bookId);*/
    }

    public void returned(LocalDate date, LibraryRequest request) {
        LibraryBookId bookId = request.getBookId();
        String studentId = request.getStudentId();
        // 在本次作业中还书立即成功，
        boolean res = true;
        // 从此刻起，借还处持有该书.
        bro.addBook(bookId);
        // 用户不再持有该书。
        students.get(studentId).removeBook(bookId);
        if (res) {
            PRINTER.accept(date, request);
        } else {
            PRINTER.reject(date, request);
        }
        /*System.out.println("[" + date + "] [" + (res ? "accept" : "reject") + "] "
                + studentId + " returned " + bookId);*/
    }

    public void queried(LocalDate date, LibraryRequest request) {
        LibraryBookId bookId = request.getBookId();
        PRINTER.info(date, bookId, machine.queryNumberOnBs(bookId));
        /*System.out.println("[" + date + "] " + bookId + " " +
                machine.queryNumberOnBs(bookId));*/
    }

    public void ordered(LocalDate date, LibraryRequest request) {
        // 借阅优先级高于预约
        LibraryBookId bookId = request.getBookId();
        String studentId = request.getStudentId();
        // 前往预约处登记 ao 1.
        Student curStudent = students.get(studentId);
        boolean res = canBorrow(bookId, curStudent);
        if (res) {
            // 預約成功
            // 放入清单
            if (orderedLists.containsKey(studentId)) {
                orderedLists.get(studentId).add(bookId);
            } else {
                ArrayList<LibraryBookId> array = new ArrayList<>();
                array.add(bookId);
                orderedLists.put(studentId, array);
            }
        }
        if (res) {
            PRINTER.accept(date, request);
        } else {
            PRINTER.reject(date, request);
        }
        /*System.out.println("[" + date + "] [" + (res ? "accept" : "reject") + "] " + studentId +
                " ordered " + bookId);*/
    }

    public void picked(LocalDate date, LibraryRequest request) {
        LibraryBookId bookId = request.getBookId();
        String studentId = request.getStudentId();
        // 用户只能去预约处取书。 ao
        boolean res = false;
        if (canBorrow(bookId, students.get(studentId))) {
            // 取书后该用户持有的书仍然满足借阅数量限制
            res = ao.tryPick(bookId, studentId, students.get(studentId));
        }
        if (res) {
            PRINTER.accept(date, request);
        } else {
            PRINTER.reject(date, request);
        }
        /*System.out.println("[" + date + "] [" + (res ? "accept" : "reject") +
                "] " + studentId + " picked " + bookId);*/
    }

    /*public void printOrderLists() {
        System.out.println("********");
        for (Map.Entry<String, ArrayList<LibraryBookId>> entry : orderedLists.entrySet()) {
            System.out.println(entry.getKey());
            for (LibraryBookId bookId : entry.getValue()) {
                System.out.println(bookId);
            }
        }
        System.out.println("********");
    }*/
}
