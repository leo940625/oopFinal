package dao;
import model.Train;

public interface TrainDAO {
    void addTrain(Train train);
    Train getTrainByNumber(int trainNumber);
}
