package DAO_Implementation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BlockSectionDAOImpl implements BlockSectionDAO {

    @Override
    public List<BlockSection> getAllBlockSections() {
        List<BlockSection> list = new ArrayList<>();
        String sql = "SELECT * FROM BlockSection";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("section_id");
                int from = rs.getInt("from_station");
                int to = rs.getInt("to_station");
                double length = rs.getDouble("length_km");
                int passTime = rs.getInt("pass_time");
                list.add(new BlockSection(id, from, to, length, passTime));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public BlockSection getBlockSectionById(int id) {
        String sql = "SELECT * FROM BlockSection WHERE section_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new BlockSection(
                        rs.getInt("section_id"),
                        rs.getInt("from_station"),
                        rs.getInt("to_station"),
                        rs.getDouble("length_km"),
                        rs.getInt("pass_time")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addBlockSection(BlockSection section) {
        String sql = "INSERT INTO BlockSection (section_id, from_station, to_station, length_km, pass_time) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, section.getSectionId());
            ps.setInt(2, section.getFromStation());
            ps.setInt(3, section.getToStation());
            ps.setDouble(4, section.getLengthKm());
            ps.setInt(5, section.getPassTime());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateBlockSection(BlockSection section) {
        String sql = "UPDATE BlockSection SET from_station = ?, to_station = ?, length_km = ?, pass_time = ? WHERE section_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, section.getFromStation());
            ps.setInt(2, section.getToStation());
            ps.setDouble(3, section.getLengthKm());
            ps.setInt(4, section.getPassTime());
            ps.setInt(5, section.getSectionId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteBlockSection(int id) {
        String sql = "DELETE FROM BlockSection WHERE section_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
