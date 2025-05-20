package dao;

import model.BlockSection;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * BlockSectionDAO 接口的實現類，用於管理 BlockSection 數據的訪問操作。
 * 此類提供了與數據庫中 BlockSection 表交互的方法。
 */
public class BlockSectionDAOImpl implements BlockSectionDAO {
    private Connection conn;

    public BlockSectionDAOImpl(Connection conn) {
        this.conn = conn;
    }
    /**
     * 從數據庫中檢索所有區間段。
     *
     * @return 包含數據庫中所有區間段的 BlockSection 對象列表。
     *         如果未找到區間段或發生錯誤，則返回空列表。
     */
    @Override
    public List<BlockSection> getAllBlockSections() {
        List<BlockSection> list = new ArrayList<>();
        String sql = "SELECT * FROM BlockSection";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new BlockSection(
                        rs.getInt("section_id"),
                        rs.getInt("from_station"),
                        rs.getInt("to_station"),
                        rs.getDouble("length_km"),
                        rs.getInt("pass_time_min")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    @Override
    public int getPassTime(int fromStationId, int toStationId) {
        String sql = "SELECT pass_time_min FROM BlockSection WHERE from_station = ? AND to_station = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, fromStationId);
            stmt.setInt(2, toStationId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("pass_time_min");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("查無對應區間資料: " + fromStationId + " -> " + toStationId);
    }
}