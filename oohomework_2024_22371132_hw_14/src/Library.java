import com.oocourse.library2.LibraryBookId;
import com.oocourse.library2.LibraryMoveInfo;
import com.oocourse.library2.LibraryReqCmd;
import com.oocourse.library2.annotation.Trigger;

import static com.oocourse.library2.LibrarySystem.PRINTER;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Library {
    private static final Library instance = new Library();
    private final BS bs = BS.getInstance();
    private final BRo bro = BRo.getInstance();
    private final AO ao = AO.getInstance();
    private final BDc bdc = BDc.getInstance();
    private final Machine machine = Machine.getInstance();
    private final HashMap<String, Student> students = new HashMap<>();
    private final HashMap<String, ArrayList<LibraryBookId>> orderedLists = new HashMap<>();
    private final HashMap<LibraryBookId, Integer> ordersQueriedId = new HashMap<>();
    /********* for transition ***********/
    private int bor;
    private int canBor;
    private int ubor;
    private int ucanBor;

    private Library() {
    }

    public void prepare(String studentId) {
        if (!students.containsKey(studentId)) {
            students.put(studentId, new Student(studentId));
        }
    }

    public void open(LocalDate date) {
        final ArrayList<LibraryMoveInfo> sumRes = new ArrayList<>();

        // 整理
        // 将失效的预约本放回书架
        ArrayList<LibraryMoveInfo> printAoRes = new ArrayList<>();
        ao.clearOrders(date, printAoRes, false);
        // 将借还处的书送回书架 // 借还处会统计所有来自于漂流角的书籍的借还次数，
        ArrayList<LibraryMoveInfo> printBroToBsRes = new ArrayList<>();
        bro.transferBookToBsAndBdc(date, printBroToBsRes);

        // output
        sumRes.addAll(printAoRes); // 1.
        sumRes.addAll(printBroToBsRes); // 2.

        PRINTER.move(date, sumRes);
    }

    public void close(LocalDate date) {
        final ArrayList<LibraryMoveInfo> sumRes = new ArrayList<>();

        // 整理
        // 将失效的预约本放回书架
        ArrayList<LibraryMoveInfo> printAoRes = new ArrayList<>();
        ao.clearOrders(date, printAoRes, true);
        // 将借还处的书送回书架/bdc
        ArrayList<LibraryMoveInfo> printBroToBsRes = new ArrayList<>();
        bro.transferBookToBsAndBdc(date, printBroToBsRes);
        // 检查orderLists，将bs中可能的书送入ao,只在闭馆送入ao
        ArrayList<LibraryMoveInfo> printBsRes = new ArrayList<>();
        bs.tryCollectOrders(date, printBsRes, orderedLists);

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
            // BU 类书：一人同一时刻仅能持有一个 BU 类书的副本。
            return !student.ifHoldB();
        } else if (bookId.isTypeBU()) {
            return !student.ifHoldBU();
        } else if (bookId.isTypeC()) {
            // C 类书和CU类书：对于每一个书号，一人同一时刻仅能持有一个具有该书号的书籍副本。
            return !student.ifHoldIdC(bookId);
        } else if (bookId.isTypeCU()) {
            return !student.ifHoldIdCU(bookId);
        } else {
            // A 类书和AU类书：仅能在图书馆阅览，不可借阅，不可预约。
            return false;
        }
    }

    @Trigger(from = "BS", to = {"STUDENT", "BRO"})
    @Trigger(from = "BDC", to = {"STUDENT", "BRO"})
    public void borrowed(LibraryReqCmd reqCmd) {
        LibraryBookId bookId = reqCmd.getBookId();
        String studentId = reqCmd.getStudentId();
        boolean res = false;
        // 书架
        if (bs.isAnyBookOnShelf(bookId) && !bookId.isTypeA()) { // 有余本且（B或C）
            // 前往借还处登记 bro
            Student curStudent = students.get(studentId);
            res = canBorrow(bookId, curStudent);
            // 书架失去一本书
            bs.removeOneBook(bookId);
            if (res) {
                // 符合借阅数量限制（见后）,则借阅成功，该用户从此刻起持有该书
                curStudent.addBook(reqCmd.getDate(), bookId);
            } else {
                // 否则借阅失败，书籍被扣在借还处，借还处从此刻起持有该书；
                bro.addBook(bookId);
            }
        } else if (bdc.isAnyBookInBDc(bookId) && !bookId.isTypeAU()) {
            // 前往借还处登记 bro
            Student curStudent = students.get(studentId);
            res = canBorrow(bookId, curStudent);
            // BDC失去一本书
            bdc.removeBook(bookId);
            if (res) {
                // 符合借阅数量限制（见后）,则借阅成功，该用户从此刻起持有该书
                curStudent.addBook(reqCmd.getDate(), bookId);
            } else {
                // 否则借阅失败，书籍被扣在借还处，借还处从此刻起持有该书；
                bro.addBook(bookId);
            }
        }
        if (res) {
            PRINTER.accept(reqCmd);
        } else {
            PRINTER.reject(reqCmd);
        }
        // TO/DO: 新增：若欲借的书籍为图书漂流角的非正式书籍，借阅规则见后。
    }

    @Trigger(from = "STUDENT", to = "BRO")
    public void returned(LibraryReqCmd reqCmd) {
        LibraryBookId bookId = reqCmd.getBookId();
        String studentId = reqCmd.getStudentId();
        Student curStudent = students.get(studentId);
        // 在本次作业中还书立即成功，
        boolean res = true;
        // 从此刻起，借还处持有该书.
        bro.addBook(bookId);
        // 用户借阅了一本漂流角的书后，需要前往借还处归还，归还成功后，记录该书在漂流角被完整借还一次。
        if (!bookId.isFormal() && res) {
            bro.addBrTime(bookId);
        }

        String addition = curStudent.isOverdue(reqCmd.getDate(), bookId)
                ? "overdue" : "not overdue";
        if (res) {
            PRINTER.accept(reqCmd, addition);
        } else {
            PRINTER.reject(reqCmd, addition);
        }
        // 用户不再持有该书。
        curStudent.removeBook(bookId);
        // TO/DO: 新增：注意还书时需要确认是否逾期，关于逾期的输出见后
    }

    public void queried(LibraryReqCmd reqCmd) {
        LibraryBookId bookId = reqCmd.getBookId();
        int num = 0;
        if (bookId.isFormal()) {
            // 对于书架上的正式图书，查询某书号的书的在架余本数。
            num = machine.queryNumberOnBs(bookId);
        } else {
            // TO/DO: 新增：图书漂流角的非正式书籍不可预约，规则见后。
            num = machine.queryNumberOnBDc(bookId);
        }
        PRINTER.info(reqCmd.getDate(), bookId, num);

    }

    public boolean canOrder(LibraryBookId bookId, Student student) {
        if (bookId.isTypeB()) {
            // 若当前没有持有 B 类书，可以预约任意数量的 B 类书。
            // 若已经持有一本 B 类书，则无法再预约任何 B 类书。
            return !student.ifHoldB();
        } else if (bookId.isTypeC()) {
            // 若当前没有持有某书号的 C 类书，就可以预约任意数量该书号的 C 类书。
            // 若已经持有某书号的 C 类书，则不能预约该书号的 C 类书。
            return !student.ifHoldIdC(bookId);
        } else {
            // A 类书：仅能在图书馆阅览，不能外借，不可预约。
            // 漂流角图书：不能被预约
            return false;
        }
    }

    public void ordered(LibraryReqCmd reqCmd) {
        // 借阅优先级高于预约
        LibraryBookId bookId = reqCmd.getBookId();
        String studentId = reqCmd.getStudentId();
        // 前往预约处登记 ao 1.
        Student curStudent = students.get(studentId);
        // TO/DO: 新增：图书漂流角的非正式书籍不可预约，规则见后。
        boolean res = canOrder(bookId, curStudent);
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
            ordersQueriedId.put(bookId, 1 + ordersQueriedId.getOrDefault(bookId, 0));
        }
        if (res) {
            PRINTER.accept(reqCmd);
        } else {
            PRINTER.reject(reqCmd);
        }
    }

    public void picked(LibraryReqCmd reqCmd) {
        LibraryBookId bookId = reqCmd.getBookId();
        String studentId = reqCmd.getStudentId();
        // 用户只能去预约处取书。 ao
        boolean res = false;
        if (canBorrow(bookId, students.get(studentId))) {
            // 取书后该用户持有的书仍然满足借阅数量限制
            res = ao.tryPick(reqCmd.getDate(), bookId, studentId, students.get(studentId));
        }
        if (res) {
            PRINTER.accept(reqCmd);
        } else {
            PRINTER.reject(reqCmd);
        }
    }

    public void renewed(LibraryReqCmd reqCmd) {
        // TO/DO
        LibraryBookId bookId = reqCmd.getBookId();
        Student curStudent = students.get(reqCmd.getStudentId());

        // 限制0.漂流角图书的借阅期限与正式图书有所不同，且不能被预约和续借。
        boolean res = false;
        if (bookId.isFormal()) {
            // 限制1.窗口期
            // 还书窗口期：(left，right)
            LocalDate right = curStudent.getDue(bookId).plusDays(1);
            LocalDate left = right.minusDays(4).minusDays(2);
            LocalDate date = reqCmd.getDate();
            res = date.isAfter(left) && date.isBefore(right);
            //System.out.println(res+" left:"+left+" right:"+right);
            // 限制2.若该书无在架余本
            if (machine.queryNumberOnBs(bookId) == 0) {
                // 从预约成功开始，直到取书成功预约完成前，或送书后一直未取书导致预约失效前
                // 检查orderLists和AO里的orderSystem
                if (ordersQueriedId.containsKey(bookId) && ordersQueriedId.get(bookId) > 0) {
                    res = false;
                } else if (ao.containsOrder(bookId)) {
                    res = false;
                }
            }
            //System.out.println(res);
        }

        if (res) {
            // 续借成功：该书的借阅期限延长30天
            curStudent.renewDue(bookId, 30);
            PRINTER.accept(reqCmd);
        } else {
            PRINTER.reject(reqCmd);
        }
    }

    @Trigger(from = "InitState", to = "BDC")
    public void donated(LibraryReqCmd reqCmd) {
        // TO/DO
        LibraryBookId bookId = reqCmd.getBookId();
        // 捐献一定成功。
        bdc.addBook(bookId, 1);
        PRINTER.accept(reqCmd);
    }

    public void removeOrdersQueriedId(LibraryBookId bookId) {
        ordersQueriedId.put(bookId, -1 + ordersQueriedId.getOrDefault(bookId, 0));
    }

}
