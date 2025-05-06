package model;
/**
 * Station 類別用來表示鐵路系統中的一個車站。
 *
 * 每個 Station 包含：
 * - 車站的唯一序號（例如：1, 2, 3）
 * - 車站名稱（例如：南港站、台中站）
 *
 * 使用範圍：
 * - 在 StopTime 類別中標記列車的停靠位置。
 * - 在路線規劃、時刻表、區間計算等功能中，識別特定車站。
 */
public class Station {
    private int stationId;       // 車站序號
    private String stationName;  // 車站名稱

    // Constructor
    public Station(int stationId, String stationName) {
        this.stationId = stationId;
        this.stationName = stationName;
    }

    // Getter and Setter
    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }
    @Override
    public String toString() {
        return "Station{" +
                "stationId=" + stationId +
                ", stationName='" + stationName + '\'' +
                '}';
    }
}