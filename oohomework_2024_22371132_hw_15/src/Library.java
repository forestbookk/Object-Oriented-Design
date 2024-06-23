import com.oocourse.library3.LibraryBookId;
import com.oocourse.library3.LibraryMoveInfo;
import com.oocourse.library3.LibraryQcsCmd;
import com.oocourse.library3.LibraryReqCmd;
import com.oocourse.library3.annotation.Trigger;

import static com.oocourse.library3.LibrarySystem.PRINTER;

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
    private Order order = new Order();

    private Library() {
    }

    public void prepare(String studentId) {
        if (!students.containsKey(studentId)) {
            students.put(studentId, new Student(studentId));
        }
    }

    public void open(LocalDate date) {
        // TO/DO: 开馆也要小心 在该书应归还日期的当日闭馆后，若用户仍未归还图书，该用户信用积分-2。
        // 检查用户的书是否过期，每本书只扣一次分
        for (Student student : students.values()) {
            student.checkDuesAndCredit(date, false);
        }

        // 整理
        final ArrayList<LibraryMoveInfo> sumRes = new ArrayList<>();

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

        //bro.checkEmpty();

    }

    public void close(LocalDate date) {
        // TO/DO: 开馆也要小心 在该书应归还日期的当日闭馆后，若用户仍未归还图书，该用户信用积分-2。
        // 检查用户的书是否过期，每本书只扣一次分
        for (Student student : students.values()) {
            student.checkDuesAndCredit(date, true);
        }

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
        final boolean isCreditGood = students.get(studentId).isCreditGood();
        boolean res = false;
        // 书架
        if (bs.isAnyBookOnShelf(bookId) && !bookId.isTypeA()) { // 有余本且（B或C）
            // 前往借还处登记 bro
            Student curStudent = students.get(studentId);
            res = isCreditGood && canBorrow(bookId, curStudent);
            // 书架失去一本书
            bs.removeOneBook(bookId);
            if (res) {
                // 符合借阅数量限制且用户信用积分为正（见后）,则借阅成功，该用户从此刻起持有该书
                curStudent.addBook(reqCmd.getDate(), bookId);
            } else {
                // TO/DO: 新增：若用户的信用积分为负，则此次借阅必定失败。
                // 否则借阅失败，书籍被扣在借还处，借还处从此刻起持有该书；
                bro.addBook(bookId);
            }
        } else if (bdc.isAnyBookInBDc(bookId) && !bookId.isTypeAU()) {
            // 前往借还处登记 bro
            Student curStudent = students.get(studentId);
            res = isCreditGood && canBorrow(bookId, curStudent);
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
        // output
        if (res) {
            PRINTER.accept(reqCmd);
        } else {
            PRINTER.reject(reqCmd);
        }
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

        String addition;
        if (curStudent.isOverdue(reqCmd.getDate(), bookId)) {
            // 逾期
            addition = "overdue";

        } else {
            // 期限内
            addition = "not overdue";
            // 用户每次还书期限内还书成功信用分立即+1，包括归还从书架上借阅的正式图书与图书漂流角内的非正式图书。
            curStudent.getCredit().succeedReturn();
        }

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
            // 图书漂流角的非正式书籍不可预约，规则见后。
            num = machine.queryNumberOnBDc(bookId);
        }
        PRINTER.info(reqCmd.getDate(), bookId, num);
    }

    public void queriedCredit(LibraryQcsCmd qcsCmd) {
        // TO/DO: 新增：查询某用户当前的信用分。
        String studentId = qcsCmd.getStudentId();
        PRINTER.info(qcsCmd.getDate(), studentId, students.get(studentId).getCreditNumber());
    }

    public boolean isOrderOnForSpecific(LibraryBookId bookId, String studentId) {
        // check OrderedLists
        if (bookId.isTypeB()) {
            if (orderedLists.containsKey(studentId)) {
                for (LibraryBookId bookId1 : orderedLists.get(studentId)) {
                    if (bookId1.isTypeB()) {
                        return true;
                    }
                }
            }
        } else {
            if (orderedLists.containsKey(studentId)) {
                for (LibraryBookId bookId1 : orderedLists.get(studentId)) {
                    if (bookId1.equals(bookId)) {
                        return true;
                    }
                }
            }
        }
        // check AO
        return ao.isOrderOnForSpecific(bookId, studentId);
    }

    public boolean canOrder(LibraryBookId bookId, Student student, String studentId) {
        //  本次作业中，我们不再允许用户多次预约同一书籍，具体的：
        //  对于B类书，若用户存在对某B类书正在生效的预约，则该用户对于B类书的预约一定失败。
        //  对于C类书，若用户存在对某书号C类书正在生效的预约，则该用户对于该书号C类书的预约一定失败。
        //  正在生效的预约：从预约成功开始起视作该预约生效，预约不再有效的条件有且仅有如下两个：
        //  为该用户送书后，发起该预约的用户取书成功时，预约视作完成不再有效。
        //  为该用户送书后，该用户一直未取书，该书在预约处逾期时，预约视作不再有效。
        if (bookId.isTypeB()) {
            //  若已经持有一本 B 类书，则无法再预约任何 B 类书。
            //  对于B类书，若用户存在对某B类书正在生效的预约，则该用户对于B类书的预约一定失败。
            if (student.ifHoldB()) {
                return false;
            } else if (isOrderOnForSpecific(bookId, studentId)) {
                return false;
            }
            return true;
        } else if (bookId.isTypeC()) {
            // 对于C类书，若用户存在对某书号C类书正在生效的预约，则该用户对于该书号C类书的预约一定失败。
            // 若已经持有某书号的 C 类书，则不能预约该书号的 C 类书。
            if (student.ifHoldIdC(bookId)) {
                return false;
            } else if (isOrderOnForSpecific(bookId, studentId)) {
                return false;
            }
            return true;
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
        //  若用户的信用积分为负，则此次预约必定失败。
        boolean res = curStudent.isCreditGood();
        if (res) {
            res = canOrder(bookId, curStudent, studentId);
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
        }

        // output
        if (res) {
            PRINTER.accept(reqCmd);
        } else {
            PRINTER.reject(reqCmd);
        }
        // TO/DO 新增
        //  若用户的信用积分为负，则此次预约必定失败。
        //  本次作业中，我们不再允许用户多次预约同一书籍，具体的：
        //  对于B类书，若用户存在对某B类书正在生效的预约，则该用户对于B类书的预约一定失败。
        //  对于C类书，若用户存在对某书号C类书正在生效的预约，则该用户对于该书号C类书的预约一定失败。
        //  正在生效的预约：从预约成功开始起视作该预约生效，预约不再有效的条件有且仅有如下两个：
        //  为该用户送书后，发起该预约的用户取书成功时，预约视作完成不再有效。
        //  为该用户送书后，该用户一直未取书，该书在预约处逾期时，预约视作不再有效。
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

    // 检查存在[任意]一位用户对该书正在生效的预约
    public boolean isAnyOrderOn(LibraryBookId bookId) {
        // 从预约成功开始，直到取书成功预约完成前，或送书后一直未取书导致预约失效前
        // 检查orderLists和AO里的orderSystem
        if (ordersQueriedId.containsKey(bookId) && ordersQueriedId.get(bookId) > 0) {
            return true;
        } else {
            return ao.containsOrder(bookId);
        }
    }

    public void renewed(LibraryReqCmd reqCmd) {
        // TO/DO 新增：若用户的信用积分为负，则此次续借必定失败。
        LibraryBookId bookId = reqCmd.getBookId();
        Student curStudent = students.get(reqCmd.getStudentId());

        boolean res = curStudent.isCreditGood();
        if (res) {
            if (bookId.isFormal()) {
                // 限制1.窗口期
                // 还书窗口期：(left，right)
                LocalDate right = curStudent.getDue(bookId).plusDays(1);
                LocalDate left = right.minusDays(4).minusDays(2);
                LocalDate date = reqCmd.getDate();
                res = date.isAfter(left) && date.isBefore(right);
                //System.out.println(res+" left:"+left+" right:"+right);
                // 限制2.若该书无在架余本且存在任意一位用户对该书正在生效的预约，失败
                if (machine.queryNumberOnBs(bookId) == 0) {
                    // 从预约成功开始，直到取书成功预约完成前，或送书后一直未取书导致预约失效前
                    // 检查orderLists和AO里的orderSystem
                    if (isAnyOrderOn(bookId)) {
                        res = false;
                    }
                }
            } else {
                // 限制0.漂流角图书的借阅期限与正式图书有所不同，且不能被预约和续借。
                res = false;
            }
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
        // 用户捐献图书成功信用分+2
        Student curStudent = students.get(reqCmd.getStudentId());
        curStudent.getCredit().donate();
        bdc.addBook(bookId, 1, curStudent, true);
        PRINTER.accept(reqCmd);
    }

    public void removeOrdersQueriedId(LibraryBookId bookId) {
        ordersQueriedId.put(bookId, -1 + ordersQueriedId.getOrDefault(bookId, 0));
    }

    public HashMap<String, Student> getStudents() {
        return students;
    }

    public void orderNewBook() {
    }
}
