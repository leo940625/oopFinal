package dao;

import java.util.*;
import model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TrainDAOImpl implements TrainDAO {
    private Connection conn;

    public TrainDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void addTrain(Train train) {
        try {
            String sql = "INSERT INTO Train (train_number, direction) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, train.getTrainNumber());
            ps.setBoolean(2, train.getDirection());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Train getTrainByNumber(int trainNumber) {
        List<StopTime> stopTimes = new StopTimeDAOImpl(conn).getStopTimesByTrain(trainNumber);
        return new Train(trainNumber, stopTimes, true); // 假設方向為 true，實務上要一起查
    }
}
