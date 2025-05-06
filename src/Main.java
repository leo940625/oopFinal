import DAO_interface.StationDAO;
import DAO_interface.BlockSectionDAO;
import model.Station;
import model.BlockSection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 資料庫連線資訊
        String url = "jdbc:mysql://localhost:3306/TrainSchedulerDB";
        String user = "root";             // 你的 MySQL 使用者名稱
        String password = "你的密碼";     // 你的 MySQL 密碼

        try {
            // 建立連線
            Connection conn = DriverManager.getConnection(url, user, password);

            // 初始化 DAO
            StationDAO stationDAO = new StationDAO(conn);
            BlockSectionDAO blockSectionDAO = new BlockSectionDAO(conn);

            // 載入資料
            List<Station> stations = stationDAO.getAllStations();
            List<BlockSection> sections = blockSectionDAO.getAllBlockSections();

            // 輸出車站
            System.out.println("🚉 Stations:");
            for (Station s : stations) {
                System.out.println(s.getStationId() + ": " + s.getStationName());
            }

            // 輸出閉塞區間
            System.out.println("\n🛤️ Block Sections:");
            for (BlockSection b : sections) {
                System.out.println("Section " + b.getSectionId() +
                        ": " + b.getFromStation() + " -> " + b.getToStation() +
                        ", Length: " + b.getLengthKm() + " km, Time: " + b.getPassTime() + " min");
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
