package dao;

import model.Station;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * StationDAO 接口的實現類，用於管理 Station 數據的數據庫訪問操作。
 * 此類提供與數據庫中 Station 表交互的方法，包括檢索所有車站、根據 ID 或名稱查詢車站。
 */
public class StationDAOImpl implements StationDAO {
    private Connection conn;

    /**
     * 構造函數，初始化數據庫連接。
     *
     * @param conn 數據庫連接對象
     */
    public StationDAOImpl(Connection conn) {
        this.conn = conn;
    }

    /**
     * 從數據庫中檢索所有車站信息。
     *
     * @return 包含所有車站的 {@link Station} 對象列表。如果未找到車站或發生錯誤，則返回空列表。
     */
    @Override
    public List<Station> getAllStations() {
        List<Station> list = new ArrayList<>();
        String sql = "SELECT * FROM Station";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("station_id");
                String name = rs.getString("station_name");
                list.add(new Station(id, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 根據車站 ID 從數據庫中檢索單個車站信息。
     *
     * @param stationId 車站的唯一標識 ID
     * @return 對應 ID 的 {@link Station} 對象，如果未找到則返回 {@code null}
     * @throws SQLException 如果數據庫操作失敗
     */
    @Override
    public Station getStationById(int stationId) {
        String sql = "SELECT station_id, station_name FROM Station WHERE station_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, stationId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("station_id");
                String name = rs.getString("station_name");
                return new Station(id, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根據車站名稱從數據庫中檢索單個車站信息。
     *
     * @param stationName 車站名稱
     * @return 對應名稱的 {@link Station} 對象，如果未找到則返回 {@code null}
     * @throws SQLException 如果數據庫操作失敗
     */
    @Override
    public Station getStationByName(String stationName) {
        String sql = "SELECT station_id, station_name FROM Station WHERE station_name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, stationName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("station_id");
                String name = rs.getString("station_name");
                return new Station(id, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}