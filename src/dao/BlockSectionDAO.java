package dao;

import model.BlockSection;

import java.sql.Connection;
import java.util.List;

public interface BlockSectionDAO {
    List<BlockSection> getAllBlockSections();
    int getPassTime(int fromStationId, int toStationId);
}
