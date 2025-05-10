import dao.StationDAO;
import dao.StationDAOImpl;
import dao.BlockSectionDAO;
import dao.BlockSectionDAOImpl;
import model.Station;
import model.BlockSection;

import java.util.List;

/**
 * 主類，用於展示車站和區間段數據的檢索和輸出。
 * 該類通過 StationDAO 和 BlockSectionDAO 從數據庫中獲取數據，並將結果打印到控制台。
 */
public class Main {

    /**
     * 程序的入口點，負責初始化 DAO 對象並顯示所有車站和區間段的信息。
     *
     * @param args 命令行參數（本程序未使用）。
     */
    public static void main(String[] args) {
        // 初始化 StationDAO 對象，用於訪問車站數據
        StationDAO stationDAO = new StationDAOImpl();
        // 初始化 BlockSectionDAO 對象，用於訪問區間段數據
        BlockSectionDAO blockDAO = new BlockSectionDAOImpl();

        // 打印車站標題
        System.out.println("車站:");
        // 獲取所有車站數據
        List<Station> stations = stationDAO.getAllStations();
        // 遍歷並打印每個車站的信息
        for (Station s : stations) {
            System.out.println(s);
        }

        // 打印區間段標題
        System.out.println("\n區間段:");
        // 獲取所有區間段數據
        List<BlockSection> blocks = blockDAO.getAllBlockSections();
        // 遍歷並打印每個區間段的信息
        for (BlockSection b : blocks) {
            System.out.println(b);
        }
    }
}