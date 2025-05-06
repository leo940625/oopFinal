package DAO_interface;
import java.util.List;

public interface BlockSectionDAO {
    List<BlockSection> getAllBlockSections();
    BlockSection getBlockSectionById(int id);
    void addBlockSection(BlockSection section);
    void updateBlockSection(BlockSection section);
    void deleteBlockSection(int id);
}

