package ui.searchtrain;

import model.StopTime;
import model.Train;
import ui.GradientPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 顯示列車查詢結果的畫面（支援漸層背景、動態白底列車清單）
 */
public class TrainInformationFrame extends JFrame {
    private List<Train> result;

    public TrainInformationFrame(List<Train> result) {
        this.result = result;
        setTitle("查詢結果");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setContentPane(new GradientPanel());
        setLayout(new BorderLayout());

        add(buildResultPanel(), BorderLayout.CENTER);
    }

    private JPanel buildResultPanel() {
        JPanel outerPanel = new JPanel();
        outerPanel.setOpaque(false);
        outerPanel.setLayout(new BorderLayout());

        // 動態白底容器，放查詢結果
        JPanel whitePanel = new JPanel();
        whitePanel.setBackground(Color.WHITE);
        whitePanel.setLayout(new BoxLayout(whitePanel, BoxLayout.Y_AXIS));

        for (Train train : result) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
            row.setOpaque(false);

            List<StopTime> stoplist = train.getStopTimes();
            int length = stoplist.size();
            String info = train.getTrainNumber() + "　" + train.getDirection() + "　" +
                    stoplist.get(0).getStation().getStationName() + "→" +
                    stoplist.get(length).getStation().getStationName();

            JLabel label = new JLabel(info);
            JButton detailButton = new JButton("經過車站");
            detailButton.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, train.getAllStationsAsString());
            });

            row.add(label);
            row.add(detailButton);
            whitePanel.add(row);
        }

        JScrollPane scrollPane = new JScrollPane(whitePanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);

        outerPanel.add(Box.createVerticalStrut(20), BorderLayout.NORTH);
        outerPanel.add(scrollPane, BorderLayout.CENTER);
        return outerPanel;
    }

    /**
     * ✅ Demo 用主程式（用假資料）：
     * 執行這段可以直接看到 demo 畫面。
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            List<Train> demoList = new ArrayList<>();

            demoList.add(new Train("401", "南下", "台北", "台中", List.of("台北", "板橋", "新竹", "台中")));
            demoList.add(new Train("402", "北上", "高雄", "台南", List.of("高雄", "左營", "台南")));
            demoList.add(new Train("403", "南下", "桃園", "台南", List.of("桃園", "台中", "彰化", "台南")));

            new TrainInformationFrame(demoList).setVisible(true);
        });
    }

    /*
     * 🟧 未來與資料庫連結版本：
     * 你可以從查詢畫面這樣呼叫：
     * List<Train> result = trainDAO.findTrainsBetween(from, to);
     * new TrainInformationFrame(result).setVisible(true);
     */
}

