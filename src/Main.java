import dao.*;
import model.*;
import util.*;
import java.time.LocalTime;
import java.util.*;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {

            TrainDAO trainDAO = new TrainDAOImpl(conn);
            StopTimeDAO stopTimeDAO = new StopTimeDAOImpl(conn);

            // 建立停靠站資料
            List<StopTime> stopTimes = new ArrayList<>();
            stopTimes.add(new StopTime(new Station(1, "南港"), null, LocalTime.of(8, 0)));
            stopTimes.add(new StopTime(new Station(3, "板橋"), LocalTime.of(8, 25), LocalTime.of(8, 27)));
            stopTimes.add(new StopTime(new Station(5, "新竹"), LocalTime.of(9, 5), null));

            // 建立車次
            Train train = new Train(1234, stopTimes, true);

            // 寫入資料庫
            /*trainDAO.addTrain(train);
            stopTimeDAO.addStopTimes(train.getTrainNumber(), stopTimes);
            System.out.println("✅ 車次已成功儲存到資料庫。");*/
            System.out.println(train.getStopTimes() == null ? "stopTimes 是 null" : "stopTimes 有內容");
            Train train1 = trainDAO.getTrainByNumber(train.getTrainNumber());
            System.out.println(train);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
