package dao;
import model.StopTime;
import java.util.List;

public interface StopTimeDAO {
    void addStopTimes(int trainNumber, List<StopTime> stopTimes);
    List<StopTime> getStopTimesByTrain(int trainNumber);
}