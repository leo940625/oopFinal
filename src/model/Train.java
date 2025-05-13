package model;
import java.time.LocalTime;
import java.util.*;

public class Train {
    private int trainNumber;
    private List<StopTime> stopTimes;// 停靠站及到發時刻
    private boolean direction;
    // Constructor
    public Train(int trainNumber, List<StopTime> stopTimes, boolean direction) {
        this.trainNumber = trainNumber;
        this.stopTimes = stopTimes != null ? stopTimes : new ArrayList<>();
        this.direction = direction;
    }
    public Train(int trainNumber, boolean direction) {
        this.trainNumber = trainNumber;
        this.stopTimes = new ArrayList<>();
        this.direction = direction;
    }
    // Getter and Setter
    public boolean getDirection() {
        return direction;
    }

    public void setDirection(boolean direction) {
        this.direction = direction;
    }

    public int getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(int trainNumber) {
        this.trainNumber = trainNumber;
    }

    public List<StopTime> getStopTimes() {
        return stopTimes;
    }

    public void setStopTimes(List<StopTime> stopTimes) {
        this.stopTimes = stopTimes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Train Number: ").append(trainNumber).append("\n");
        sb.append("Direction: ").append(direction ? "Northbound" : "Southbound").append("\n");
        sb.append("Stop Times:\n");
        if (stopTimes != null) {
            for (StopTime st : stopTimes) {
                sb.append(" - ")
                        .append(st.getStation().getStationName())
                        .append(" 到達: ").append(st.getArrivalTime() != null ? st.getArrivalTime() : "null")
                        .append("，發車: ").append(st.getDepartureTime() != null ? st.getDepartureTime() : "null")
                        .append("\n");
            }
        } else {
            sb.append(" [無停靠站資料]\n");
        }
        return sb.toString();
    }
}
