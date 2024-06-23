import com.oocourse.spec1.main.Runner;

public class MainClass {
    public static void main(String[] args) throws Exception {
        //long startStamp = System.currentTimeMillis();
        Runner runner = new Runner(MyPerson.class, MyNetwork.class);
        runner.run();
        //long endStamp = System.currentTimeMillis();
        //System.out.println("SUM TIME:"+ (endStamp - startStamp));
    }
}
