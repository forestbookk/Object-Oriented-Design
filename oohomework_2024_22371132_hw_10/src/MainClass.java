import com.oocourse.spec2.main.Runner;

public class MainClass {
    public static void main(String[] args) throws Exception {
        //long startPoint = System.currentTimeMillis();
        Runner runner = new Runner(MyPerson.class, MyNetwork.class, MyTag.class);
        runner.run();
        //long endPoint = System.currentTimeMillis();
    }
}