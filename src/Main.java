import model.*;
import java.util.List;
import java.time.LocalTime;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // 建立車站
        Station taipei = new Station(1, "台北");
        Station taichung = new Station(2, "台中");
        Station kaohsiung = new Station(3, "左營");

        // 建立停靠資訊 (StopTime)
        List<StopTime> stopTimes = new ArrayList<>();

        stopTimes.add(new StopTime(taipei, null, LocalTime.of(8, 0)));           // 台北發車
        stopTimes.add(new StopTime(taichung, LocalTime.of(9, 0), LocalTime.of(9, 5))); // 台中到達 9:00，發車 9:05
        stopTimes.add(new StopTime(kaohsiung, LocalTime.of(10, 30), null));       // 左營到達

        // 建立車次 (Train)
        Train train = new Train(1234, stopTimes);

        // 列印車次資訊
        System.out.println(train);
    }
}