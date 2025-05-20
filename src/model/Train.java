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

    /**
     * 根據起點站出發時間，完整路線站序，自動計算所有實際停靠站的到達與出發時間。
     * 停靠站加 1 分鐘，過站只加通過時間。
     *
     * @param blockSectionDAO BlockSection 資料來源
     * @param fullRoute       全路線依照方向排序的所有站點清單（ex: 台灣高鐵南下）
     */
    public void calculateSchedule(BlockSectionDAO blockSectionDAO, List<Station> fullRoute) {
        if (stopTimes == null || stopTimes.size() < 2) return;

        // 轉成 Map 方便快速查是否有停靠
        Map<Integer, StopTime> stopTimeMap = stopTimes.stream()
                .collect(Collectors.toMap(st -> st.getStation().getStationId(), st -> st));

        // 根據方向排序路線（避免反向時錯位）
        if (!direction) {
            Collections.reverse(fullRoute);
        }
        LocalTime currentTime = stopTimeMap.get(fullRoute.get(0).getStationId()).getDepartureTime();
        if (currentTime == null) {
            throw new IllegalStateException("出發站的出發時間尚未設定");
        }

        for (int i = 1; i < fullRoute.size(); i++) {
            int fromId = fullRoute.get(i - 1).getStationId();
            int toId = fullRoute.get(i).getStationId();

            int passMin = blockSectionDAO.getPassTime(fromId, toId);
            currentTime = currentTime.plusMinutes(passMin);

            if (stopTimeMap.containsKey(toId)) {
                StopTime currentStop = stopTimeMap.get(toId);
                currentStop.setArrivalTime(currentTime);
                currentTime = currentTime.plusMinutes(1); // 有停靠 → 加 1 分鐘
                currentStop.setDepartureTime(currentTime);
            }
            // 如果沒停靠 → 跳過（只計算通過時間）
        }
    }
}
