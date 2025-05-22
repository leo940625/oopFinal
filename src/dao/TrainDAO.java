package dao;

import model.*;

import java.time.LocalTime;
import java.util.List;

public interface TrainDAO {
    void addTrain(Train train);
    Train getTrainByNumber(int trainNumber);
    public List<Train> getAllTrains();
    public List<Train> findTrainsBetween(Station from, Station to, LocalTime afterTime);
}
