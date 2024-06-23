public class Credit {
    private int credit;
    //private String id;

    /*public Credit(String id) {
        this.id = id;
        this.credit = 10;
    }*/

    public Credit() {
        this.credit = 10;
    }

    public boolean isGood() {
        return this.credit >= 0;
    }

    public void succeedReturn() {
        // 上限为20（即积分增加时，更新后信用积分=min(更新前信用积分+x，20)）
        // 用户每次还书期限内还书成功信用分立即+1，包括归还从书架上借阅的正式图书与图书漂流角内的非正式图书。
        this.credit = Math.min(this.credit + 1, 20);
        //System.out.println(id+"****" + credit+"for succeedReturn");
    }

    public void overdue() {
        this.credit -= 2;
        //System.out.println(id+"****" + credit+"for overdue");
    }

    public int getNumber() {
        return this.credit;
    }

    public void nonPick() {
        this.credit -= 3;
        //System.out.println(id+"****" + credit+"for nonPick");
    }

    public void donate() {
        this.credit = Math.min(this.credit + 2, 20);
        //System.out.println(id+"****" + credit+"for donate");
    }

    public void contributeUpgrade() {
        this.credit = Math.min(this.credit + 2, 20);
        //System.out.println(id+"****" + credit+"for contributeUpgrade");
    }
}
