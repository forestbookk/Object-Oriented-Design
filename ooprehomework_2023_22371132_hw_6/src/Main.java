public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        MyScanner myScanner = new MyScanner();
        myScanner.handleInput(manager.getCmdUtilArray());
        manager.handleOp();
    }
}
