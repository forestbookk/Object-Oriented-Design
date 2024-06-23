import java.util.ArrayList;

public class CommandUtil {
    private int operator;
    private int advId;
    private String name;
    private int itemId;
    private int itemFeature;
    private long price;
    private String type;
    private double ratio;
    private int critical;
    private ArrayList<String> advNameFight;
    private int mmPeo;
    private int kkLog;
    private ArrayList<String> partLog;
    private String date;

    public void setType(String type) {
        this.type = type;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public void setItemFeature(int itemFeature) {
        this.itemFeature = itemFeature;
    }

    public void setPartLog(ArrayList<String> partLog) {
        this.partLog = partLog;
    }

    public void setCritical(int critical) {
        this.critical = critical;
    }

    public void setMmPeo(int mmPeo) {
        this.mmPeo = mmPeo;
    }

    public void setKkLog(int kkLog) {
        this.kkLog = kkLog;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public long getPrice() {
        return price;
    }

    public int getOperator() {
        return operator;
    }

    public int getMmPeo() {
        return mmPeo;
    }

    public int getKkLog() {
        return kkLog;
    }

    public ArrayList<String> getPartLog() {
        return partLog;
    }

    public int getItemId() {
        return itemId;
    }

    public int getItemFeature() {
        return itemFeature;
    }

    public int getCritical() {
        return critical;
    }

    public double getRatio() {
        return ratio;
    }

    public ArrayList<String> getAdvNameFight() {
        return advNameFight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAdvNameFight(ArrayList<String> advNameFight) {
        this.advNameFight = advNameFight;
    }

    public int getAdvId() {
        return advId;
    }

    public void setAdvId(int advId) {
        this.advId = advId;
    }
}
