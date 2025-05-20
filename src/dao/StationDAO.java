package dao;

import model.Station;

import java.util.List;

public interface StationDAO {
    List<Station> getAllStations();
    public Station getStationById(int stationId);
}
