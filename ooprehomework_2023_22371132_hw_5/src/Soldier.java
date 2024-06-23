public class Soldier {
    private String name;
    private String incantation;

    public Soldier(String name, String incantation) {
        this.name = name;
        this.incantation = incantation;
    }

    // this method means "cut" the "incantation"
    public void cutIncantation(int a, int b) {
        int bcpy = b;
        if (a > b || a >= this.incantation.length()) {
            this.incantation = "";
        } else {
            if (b >= this.incantation.length() - 1) {
                incantation = incantation.substring(a);
            } else {
                bcpy = b + 1;
                incantation = incantation.substring(a, bcpy);
            }
            //incantation = incantation.substring(a, Integer.min(b , incantation.length()));
        }
    }

    //  2 means "to" , you will see it in the os codes  next semester
    public void appendStr2Incantation(String str) {
        incantation += str;
    }

    public Soldier cloneSoldier() {
        return new Soldier(this.name, this.incantation);
    }

    // !!!!! Be careful, this method  need to be carefully read and analyzed to identify bugs!
    public boolean notQualifiedByStandard(int standard) {
        int head = 0;
        int tail = 1;
        int totalCount = 0;
        while (tail < incantation.length()) {
            while (incantation.charAt(head) != '@') {
                head++;
                if (head >= incantation.length()) {
                    break;
                }
            }
            tail = head + 1;
            if (tail >= incantation.length()) {
                break;
            }
            while (incantation.charAt(tail) != '@') {
                tail++;
                if (tail >= incantation.length()) {
                    break;
                }
            }
            if (tail >= incantation.length()) {
                break;
            }

            int count = 0;
            for (int i = head; i <= tail; i++) {
                if (incantation.charAt(i) >= 'A' && incantation.charAt(i) <= 'Z') {
                    count++;
                }
                if (incantation.charAt(i) >= 'a' && incantation.charAt(i) <= 'z') {
                    count--;
                }
            }
            if (count >= 0) {
                totalCount++;
            }
            head = tail;
        }
        return totalCount < standard;
    }

    public boolean hasString(String str) {
        return incantation.contains(str);
    }

    public boolean equal(Soldier soldier) {
        return this.name.equals(soldier.name) && this.incantation.equals(soldier.incantation);
    }

}

