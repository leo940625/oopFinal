import dao.*;
import model.*;
import util.*;
import java.time.LocalTime;
import java.util.*;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try {
            // 建立資料庫連線
            Connection conn = DBConnection.getConnection();

            TrainDAO trainDAO = new TrainDAOImpl(conn);
            StopTimeDAO stopTimeDAO = new StopTimeDAOImpl(conn);

            // 建立停靠站資料
            List<StopTime> stopTimes = new ArrayList<>();
            stopTimes.add(new StopTime(new Station(1, "南港"), null, LocalTime.of(8, 0)));
            stopTimes.add(new StopTime(new Station(2, "台北"), LocalTime.of(8, 15), LocalTime.of(8, 12)));
            stopTimes.add(new StopTime(new Station(3, "板橋"), LocalTime.of(8, 20), null));
            // 建立列車
            Train train = new Train(1120, stopTimes, false);

            // 插入資料
            trainDAO.addTrain(train); // 假設 insertTrain 內部會連帶使用 StopTimeDAO 插入停靠資料
            System.out.println("Train inserted successfully.");
            System.out.println(trainDAO.getTrainByNumber(train.getTrainNumber()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
