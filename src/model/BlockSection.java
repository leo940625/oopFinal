package model;

/**
 * BlockSection 類別表示兩個車站之間的閉塞區間。
 * 每個閉塞區間具有起點與終點車站、距離（公里）以及列車通過所需時間（分鐘）。
 *
 * 此類別常用於列車時刻模擬與閉塞控制邏輯中。
 */
public class BlockSection {

    /** 區間編號（唯一識別碼） */
    private int sectionId;

    /** 起點車站的 station_id */
    private int fromStation;

    /** 終點車站的 station_id */
    private int toStation;

    /** 區間長度（單位：公里） */
    private double lengthKm;

    /** 列車通過此閉塞區間所需時間（單位：分鐘） */
    private int passTime;

    /**
     * 建構子：建立一個 BlockSection 物件
     *
     * @param sectionId 區間編號
     * @param fromStation 起點車站的 ID
     * @param toStation 終點車站的 ID
     * @param lengthKm 區間長度（公里）
     * @param passTime 列車通過此區間所需時間（分鐘）
     */
    public BlockSection(int sectionId, int fromStation, int toStation, double lengthKm, int passTime) {
        this.sectionId = sectionId;
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.lengthKm = lengthKm;
        this.passTime = passTime;
    }

    /**
     * 取得此區間的通過時間（分鐘）
     * @return 通過時間（分鐘）
     */
    public int getPassTime() {
        return passTime;
    }

    /**
     * 設定此區間的通過時間（分鐘）
     * @param passTime 通過時間
     */
    public void setPassTime(int passTime) {
        this.passTime = passTime;
    }

    /**
     * 取得區間編號
     * @return 區間 ID
     */
    public int getSectionId() {
        return sectionId;
    }

    /**
     * 設定區間編號
     * @param sectionId 區間 ID
     */
    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    /**
     * 取得起點車站 ID
     * @return 起點車站 ID
     */
    public int getFromStation() {
        return fromStation;
    }

    /**
     * 設定起點車站 ID
     * @param fromStation 起點車站 ID
     */
    public void setFromStation(int fromStation) {
        this.fromStation = fromStation;
    }

    /**
     * 取得終點車站 ID
     * @return 終點車站 ID
     */
    public int getToStation() {
        return toStation;
    }

    /**
     * 設定終點車站 ID
     * @param toStation 終點車站 ID
     */
    public void setToStation(int toStation) {
        this.toStation = toStation;
    }

    /**
     * 取得區間長度（公里）
     * @return 長度（公里）
     */
    public double getLengthKm() {
        return lengthKm;
    }

    /**
     * 設定區間長度（公里）
     * @param lengthKm 長度（公里）
     */
    public void setLengthKm(double lengthKm) {
        this.lengthKm = lengthKm;
    }

    /**
     * 傳回區間的字串描述
     * @return 區間詳細內容字串
     */
    @Override
    public String toString() {
        return "BlockSection{" +
                "sectionId=" + sectionId +
                ", fromStation=" + fromStation +
                ", toStation=" + toStation +
                ", lengthKm=" + lengthKm +
                ", passTime=" + passTime +
                '}';
    }
}
