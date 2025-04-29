package model;
import java.util.List;

public class Train {
    private int trainNumber;
    private List<StopTime> stopTimes;   // 停靠站及到發時刻

    // Constructor
    public Train(int trainNumber, List<StopTime> stopTimes) {
        this.trainNumber = trainNumber;
        this.stopTimes = stopTimes;
    }

    // Getter and Setter
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
        sb.append("Train T").append(trainNumber).append(" schedule:\n"); // 建議加上 T
        for (StopTime st : stopTimes) {
            sb.append(st.getStation().getStationName())
                    .append(" Arrival: ").append(st.getArrivalTime())
                    .append(", Departure: ").append(st.getDepartureTime())
                    .append("\n");
        }
        return sb.toString(); // ✅ 必須補上 return
    }
}
