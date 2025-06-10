package model;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import dao.*;

/**
 * 表示一列火車及其相關時刻與行駛資訊的類別。
 * 此類別包含列車編號、行駛方向以及停靠站等資訊，
 * 並可透過 calculateSchedule 方法根據行車區間資訊計算各站的到發時刻。
 */
public class Train {
    /** 列車編號 */
    private int trainNumber;

    /** 停靠站資訊與對應的到達與出發時間 */
    private List<StopTime> stopTimes;

    /** 行駛方向，true 表示北上，false 表示南下 */
    private boolean direction;

    /**
     * 建構子，初始化列車編號、停靠站與行駛方向。
     *
     * @param trainNumber 列車編號
     * @param stopTimes   停靠站列表
     * @param direction   行駛方向（true: 北上，false: 南下）
     */
    public Train(int trainNumber, List<StopTime> stopTimes, boolean direction) {
        this.trainNumber = trainNumber;
        this.stopTimes = stopTimes != null ? stopTimes : new ArrayList<>();
        this.direction = direction;
    }

    /**
     * 建構子，僅指定列車編號與方向，停靠站預設為空列表。
     *
     * @param trainNumber 列車編號
     * @param direction   行駛方向（true: 北上，false: 南下）
     */
    public Train(int trainNumber, boolean direction) {
        this.trainNumber = trainNumber;
        this.stopTimes = new ArrayList<>();
        this.direction = direction;
    }

    /**
     * 取得列車行駛方向。
     *
     * @return true 表示北上，false 表示南下
     */
    public boolean getDirection() {
        return direction;
    }

    /**
     * 設定列車行駛方向。
     *
     * @param direction true 表示北上，false 表示南下
     */
    public void setDirection(boolean direction) {
        this.direction = direction;
    }

    /**
     * 取得列車編號。
     *
     * @return 列車編號
     */
    public int getTrainNumber() {
        return trainNumber;
    }

    /**
     * 設定列車編號。
     *
     * @param trainNumber 列車編號
     */
    public void setTrainNumber(int trainNumber) {
        this.trainNumber = trainNumber;
    }

    /**
     * 取得停靠站時刻表。
     *
     * @return 停靠站列表
     */
    public List<StopTime> getStopTimes() {
        return stopTimes;
    }

    /**
     * 設定停靠站時刻表。
     *
     * @param stopTimes 停靠站列表
     */
    public void setStopTimes(List<StopTime> stopTimes) {
        this.stopTimes = stopTimes;
    }

    /**
     * 傳回此列車的詳細資訊字串，包括列車編號、方向與停靠站時刻表。
     *
     * @return 列車資訊的字串表示
     */
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

    /**
     * 根據提供的路線資訊與起始時間計算列車的各站到發時間。
     * 假設列車從第一個設定的停靠站出發，依據行車區段時間推算後續各站的時刻。
     *
     * @param blockSectionDAO 區間通過時間資料存取物件
     * @param fullRoute       全路線的車站列表
     * @param time            起點站的出發時間
     * @throws IllegalStateException 若停靠站不足或未設定起始站時間
     */
    public void calculateSchedule(BlockSectionDAO blockSectionDAO, List<Station> fullRoute, LocalTime time) {
        if (stopTimes == null || stopTimes.size() < 2) return;

        // 根據行駛方向調整站序
        if (this.direction) {
            stopTimes.sort(Comparator.comparingInt((StopTime st) -> st.getStation().getStationId()).reversed());
            Collections.reverse(fullRoute);
        } else {
            stopTimes.sort(Comparator.comparingInt(st -> st.getStation().getStationId()));
        }

        // 設定起點站出發時間
        stopTimes.getFirst().setDepartureTime(time);

        // 轉為 Map 以方便查找
        Map<Integer, StopTime> stopTimeMap = stopTimes.stream()
                .collect(Collectors.toMap(st -> st.getStation().getStationId(), st -> st));

        // 取得起點站
        Station startingStation = fullRoute.stream()
                .filter(st -> stopTimeMap.containsKey(st.getStationId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("無法在 fullRoute 中找到此列車的起點"));

        // 取得出發時間
        LocalTime currentTime = stopTimeMap.get(startingStation.getStationId()).getDepartureTime();
        if (currentTime == null) {
            throw new IllegalStateException("出發站的出發時間尚未設定");
        }

        // 開始依序處理後續車站
        int startIdx = fullRoute.indexOf(startingStation);
        for (int i = startIdx + 1; i < fullRoute.size(); i++) {
            int fromId = fullRoute.get(i - 1).getStationId();
            int toId = fullRoute.get(i).getStationId();

            int passMin = blockSectionDAO.getPassTime(fromId, toId);
            currentTime = currentTime.plusMinutes(passMin);

            if (stopTimeMap.containsKey(toId)) {
                StopTime currentStop = stopTimeMap.get(toId);
                currentStop.setArrivalTime(currentTime);
                currentTime = currentTime.plusMinutes(1); // 停靠 → 加 1 分鐘
                currentStop.setDepartureTime(currentTime);
            }
        }
    }
}
