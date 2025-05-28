package model;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import dao.*;

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

    public void calculateSchedule(BlockSectionDAO blockSectionDAO, List<Station> fullRoute, LocalTime time) {
        if (stopTimes == null || stopTimes.size() < 2) return;

        // 根據方向排序停靠站與 fullRoute
        if (this.direction) {
            stopTimes.sort(Comparator.comparingInt((StopTime st) -> st.getStation().getStationId()).reversed());
            Collections.reverse(fullRoute);
        } else {
            stopTimes.sort(Comparator.comparingInt(st -> st.getStation().getStationId()));
        }

        // 設定實際起點站出發時間
        stopTimes.getFirst().setDepartureTime(time);

        // 轉成 Map 方便查詢
        Map<Integer, StopTime> stopTimeMap = stopTimes.stream()
                .collect(Collectors.toMap(st -> st.getStation().getStationId(), st -> st));

        // 找出 fullRoute 中第一個有出現在 stopTimeMap 的站當作起點
        Station startingStation = fullRoute.stream()
                .filter(st -> stopTimeMap.containsKey(st.getStationId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("無法在 fullRoute 中找到此列車的起點"));

        // 取得起點的出發時間
        LocalTime currentTime = stopTimeMap.get(startingStation.getStationId()).getDepartureTime();
        if (currentTime == null) {
            throw new IllegalStateException("出發站的出發時間尚未設定");
        }

        // 從起點站開始往下走 fullRoute
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
