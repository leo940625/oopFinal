package util;
import model.Train;
import model.Station;
import model.StopTime;

import java.util.List;

public class TrainUtils {

    /**
     * 根據給定列車與站名，尋找對應的出發與到達站之 StopTime。
     *
     * @param train            要查詢的列車物件
     * @param departureStation 出發站（Station）
     * @param arrivalStation   到達站（Station）
     * @return 長度為 2 的 StopTime 陣列：{出發站, 到達站}；若任一站無對應資料則為 null
     */
    public static StopTime[] findDepartureAndArrivalStops(Train train, Station departureStation, Station arrivalStation) {
        List<StopTime> stops = train.getStopTimes();

        StopTime departureStop = stops.stream()
                .filter(stop -> stop.getStation().getStationId() == departureStation.getStationId())
                .findFirst()
                .orElse(null);

        StopTime arrivalStop = stops.stream()
                .filter(stop -> stop.getStation().getStationId() == arrivalStation.getStationId())
                .findFirst()
                .orElse(null);

        return new StopTime[]{departureStop, arrivalStop};
    }
}
