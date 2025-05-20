import ui.HomeFrame;
import javax.swing.SwingUtilities;
import dao.*;
import model.*;
import util.DBConnection;
import java.sql.*;
import java.time.LocalTime;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        //SwingUtilities.invokeLater(() -> new HomeFrame());


        try (Connection conn = DBConnection.getConnection()) {
            // 建立 BlockSectionDAO 以查詢通過時間
            BlockSectionDAO sectionDAO = new BlockSectionDAOImpl(conn);
            StationDAO stationDAO = new StationDAOImpl();

            // 建立停靠站列表（只有站名與 ID，時間先留空）
            List<StopTime> stops = new ArrayList<>();
            stops.add(new StopTime(stationDAO.getStationById(12), null,null )); // 起點站出發時間已知
            stops.add(new StopTime(stationDAO.getStationById(7), null, null));
            stops.add(new StopTime(stationDAO.getStationById(8), null, null));
            stops.add(new StopTime(stationDAO.getStationById(2), null, null));
            stops.add(new StopTime(stationDAO.getStationById(1), null,null));

            // 建立列車物件（trainNumber=1001, direction=true）
            Train train = new Train(1001, stops, false);

            // 自動計算所有時刻表（使用資料庫資料）
            train.calculateSchedule(sectionDAO,stationDAO.getAllStations(),LocalTime.of(8, 0));

            // 印出列車時刻資訊
            System.out.println(train);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
