package giaodien;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

import csv.ReadCsv;

public class GetRKOL {
    public static List<RankedKol> getRankedKol(String filepath)
    {
        List<String> kolName=ReadCsv.readCsvToList(filepath,"A");
        List<String> kolFollower=ReadCsv.readCsvToList(filepath,"C");
        List<String> kolScore=ReadCsv.readCsvToList(filepath,"B");
        List<RankedKol> rKOL=new ArrayList<>();
        for (int i=0;i<kolName.size();i++)
        {
            rKOL.add(new RankedKol(kolName.get(i),Integer.parseInt(kolFollower.get(i)),Float.parseFloat(kolScore.get(i))));
        }
        return rKOL;
    }

    public static void calculateRanks(List<RankedKol> data) {
        // Sắp xếp rank theo điểm số giảm dần
        data.sort(Comparator.comparingDouble(RankedKol::getScore).reversed());

        int currentRank = 1;
        for (int i = 0; i < data.size(); i++) {
            if (i > 0 && data.get(i).getScore() == data.get(i - 1).getScore()) {
                data.get(i).setRank(data.get(i - 1).getRank());
            } else {

                data.get(i).setRank(currentRank);
            }
            currentRank++;
        }
    }
    public static String searchObject(List<RankedKol> data, String name) {
        for (RankedKol rkol : data) {
            if (rkol.getName().equalsIgnoreCase(name)) {
                return "Điểm: " + rkol.getScore() + ", Rank: " + rkol.getRank();
            }
        }
        return "Đối tượng không tồn tại";
    }
}
