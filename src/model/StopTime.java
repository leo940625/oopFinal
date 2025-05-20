package model;

import java.time.LocalTime;

/**
 * StopTime 類別用來表示列車在每一個停靠站的到達時間與發車時間。
 * <p>
 * 一個火車行經多個車站，每個車站都有自己的到達與發車時間。
 * 本類別將每個停靠站的資訊（車站名稱、到達時間、發車時間）封裝成一個物件。
 * <p>
 * 使用範圍：
 * - 儲存在 Train 類別的停靠站列表中。
 * - 幫助建立完整的列車時刻表。
 * - 後續可以擴展更多資訊，例如：停車時間、月台編號等。
 */
public class StopTime {
    private Station station;           // 停靠的車站
    private LocalTime arrivalTime;     // 到達該站的時間
    private LocalTime departureTime;   // 從該站發車的時間

    // Constructor
    public StopTime(Station station, LocalTime arrivalTime, LocalTime departureTime) {
        this.station = station;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
    }

    // Getter and Setter
    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    @Override
    public String toString() {
        return "StopTime{" +
                "station=" + station.getStationName() +
                ", arrivalTime=" + arrivalTime +
                ", departureTime=" + departureTime +
                '}';
    }
}
