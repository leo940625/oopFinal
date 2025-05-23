package ui.searchtrain;

import model.StopTime;
import model.Station;
import model.Train;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 * 顯示列車查詢結果的畫面（支援漸層背景、動態白底列車清單）
 */
public class TrainInformationFrame extends JFrame {
    private final List<Train> result;
    private final String fromStation;
    private final String toStation;

    public TrainInformationFrame(List<Train> result, String fromStation, String toStation) {
        this.result = result;
        this.fromStation = fromStation;
        this.toStation = toStation;

        setTitle("查詢結果");
        setSize(850, 600); // 850 600
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("查詢結果", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

        //add(buildTableScrollPane(), BorderLayout.CENTER);
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10)); // ← 就是這行在控制位置！
        tableContainer.setOpaque(false);
        tableContainer.add(buildTableScrollPanel(), BorderLayout.CENTER);

        add(tableContainer, BorderLayout.CENTER);

    }


    private JScrollPane buildTableScrollPanel() {
        String[] columnNames = {"車次", "全路線", "起始站", "出發時間", "抵達站", "抵達時間", "詳細"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // 僅允許按鈕欄可互動
            }
        };

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (Train train : result) {
            List<StopTime> stops = train.getStopTimes();
            if (stops.size() < 2) continue;

            String routeStart = stops.get(0).getStation().getStationName();
            String routeEnd = stops.get(stops.size() - 1).getStation().getStationName();
            String route = routeStart + " → " + routeEnd;

            StopTime fromStop = findStop(stops, fromStation);
            StopTime toStop = findStop(stops, toStation);

            String departureTime = fromStop != null && fromStop.getDepartureTime() != null ?
                    fromStop.getDepartureTime().format(timeFormatter) : "-";
            String arrivalTime = toStop != null && toStop.getArrivalTime() != null ?
                    toStop.getArrivalTime().format(timeFormatter) : "-";

            model.addRow(new Object[]{
                    train.getTrainNumber(),
                    route,
                    fromStation,
                    departureTime,
                    toStation,
                    arrivalTime,
                    "經過車站" // 按鈕文字
            });
        }

        JTable table = new JTable(model) {
            Color hoverColor = new Color(255, 182, 193);
            int hoveredRow = -1;

            {
                addMouseMotionListener(new MouseAdapter() {
                    @Override
                    public void mouseMoved(MouseEvent e) {
                        int row = rowAtPoint(e.getPoint());
                        if (row != hoveredRow) {
                            hoveredRow = row;
                            repaint();
                        }
                    }
                });
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseExited(MouseEvent e) {
                        hoveredRow = -1;
                        repaint();
                    }
                });
            }

            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    if (row == hoveredRow) {
                        c.setBackground(hoverColor);
                    } else {
                        c.setBackground(row % 2 == 0 ? new Color(255, 240, 245) : new Color(255, 228, 225));
                    }
                } else {
                    c.setBackground(new Color(255, 204, 229)); // 自訂選取色
                }
                c.setForeground(Color.BLACK);
                if (c instanceof JComponent jc) {
                    jc.setBorder(null); // 取消藍色框選邊
                }
                return c;
            }
        };

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(255, 105, 180));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("SansSerif", Font.BOLD, 16));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 16));
        table.setGridColor(Color.LIGHT_GRAY);
        table.setFocusable(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(i).setPreferredWidth(120); // 每欄固定寬度
        }

        table.getColumn("詳細").setCellRenderer(new ButtonRenderer());
        table.getColumn("詳細").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }

    private StopTime findStop(List<StopTime> stops, String stationName) {
        return stops.stream()
                .filter(st -> st.getStation().getStationName().equals(stationName))
                .findFirst()
                .orElse(null);
    }

    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean clicked;
        private int currentRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            clicked = true;
            currentRow = row;
            return button;
        }

        public Object getCellEditorValue() {
            if (clicked) {
                Train train = result.get(currentRow);
                List<StopTime> stopTimes = train.getStopTimes();

                List<String> passedStations = new ArrayList<>();
                boolean between = false;

                for (StopTime st : stopTimes) {
                    String name = st.getStation().getStationName();
                    if (name.equals(fromStation)) between = true;
                    if (between) passedStations.add(name);
                    if (name.equals(toStation)) break;
                }

                JOptionPane.showMessageDialog(null,
                        String.join(" → ", passedStations),
                        "經過站：" + fromStation + " 至 " + toStation,
                        JOptionPane.INFORMATION_MESSAGE);
            }
            clicked = false;
            return label;
        }

        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
}

    /*
    // TODO: 功能錯ㄌ 用起訖站
    String start = stoplist.get(0).getStation().getStationName();
    String end = stoplist.get(stoplist.size() - 1).getStation().getStationName();
    String directionText = train.getDirection() ? "北上" : "南下";

    String info = train.getTrainNumber() + "　" + directionText + "　" + start + "→" + end;

    JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
            row.setOpaque(false);

    JLabel label = new JLabel(info);
    JButton detailButton = new JButton("經過車站");

            detailButton.addActionListener(e -> {
        String stationList = stoplist.stream()
                .map(st -> st.getStation().getStationName())
                .collect(Collectors.joining(" → "));
        JOptionPane.showMessageDialog(this, stationList, "經過車站", JOptionPane.INFORMATION_MESSAGE);
    });

            row.add(label);
            row.add(detailButton);
            whitePanel.add(row);
    */ //TODO:上面的搬家感覺很笨但應該有要用的程式碼？嗎

    /*
     * 🟧 未來與資料庫連結版本：
     * 你可以從查詢畫面這樣呼叫：
     * List<Train> result = trainDAO.findTrainsBetween(from, to);
     * new TrainInformationFrame(result).setVisible(true);
     */




