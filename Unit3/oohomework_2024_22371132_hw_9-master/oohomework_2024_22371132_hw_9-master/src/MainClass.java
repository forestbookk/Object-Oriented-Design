import com.oocourse.spec1.main.Runner;

public class MainClass {
    public static void main(String[] args) throws Exception {
        //long startPoint = System.currentTimeMillis();
        Runner runner = new Runner(MyPerson.class, MyNetwork.class);
        runner.run();
        //long endPoint = System.currentTimeMillis();
        //System.out.println("SUM TIME: " + (endPoint - startPoint));
    }
}
