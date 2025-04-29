package model;

public class BlockSection {
    private int sectionId;
    private int fromStation;   // 起點車站的 station_id
    private int toStation;     // 終點車站的 station_id
    private double lengthKm;   // 區間長度（公里）

    public BlockSection(int sectionId, int fromStation, int toStation, double lengthKm) {
        this.sectionId = sectionId;
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.lengthKm = lengthKm;
    }

    // Getter 與 Setter
    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public int getFromStation() {
        return fromStation;
    }

    public void setFromStation(int fromStation) {
        this.fromStation = fromStation;
    }

    public int getToStation() {
        return toStation;
    }

    public void setToStation(int toStation) {
        this.toStation = toStation;
    }

    public double getLengthKm() {
        return lengthKm;
    }

    public void setLengthKm(double lengthKm) {
        this.lengthKm = lengthKm;
    }

    @Override
    public String toString() {
        return "BlockSection{" +
                "sectionId=" + sectionId +
                ", fromStation=" + fromStation +
                ", toStation=" + toStation +
                ", lengthKm=" + lengthKm +
                '}';
    }
}
