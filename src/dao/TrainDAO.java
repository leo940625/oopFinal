package dao;
import model.StopTime;
import model.Train;

import java.util.List;

public interface TrainDAO {
    void addTrain(Train train);
    Train getTrainByNumber(int trainNumber);
    public List<Train> getAllTrains();
}
