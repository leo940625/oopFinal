package DAO_interface;

import java.util.List;

public interface StationDAO {
    List<Station> getAllStations();
    Station getStationById(int id);
    void addStation(Station station);
    void updateStation(Station station);
    void deleteStation(int id);
}

