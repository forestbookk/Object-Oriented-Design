import java.util.HashMap;

public class Counter {
    private int classCnt = 0;
    private HashMap<Integer, Integer> idCntMap;

    public int classTick() {
        classCnt++;
        return classCnt;
    }

    public int idTick(int id) {
        if (idCntMap == null) {
            idCntMap = new HashMap<>();
        }
        if (idCntMap.isEmpty() || !idCntMap.containsKey(id)) {
            idCntMap.put(id, 1);
            return 1;
        } else {
            int newCnt = idCntMap.get(id) + 1;
            idCntMap.put(id, newCnt);
            return newCnt;
        }
    }
}
