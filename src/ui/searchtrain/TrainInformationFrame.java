package ui.searchtrain;

import model.StopTime;
import model.Station;
import model.Train;
import ui.HomeFrame;

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

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10)); // ← 就是這行在控制位置！
        tableContainer.setOpaque(false);
        tableContainer.add(buildTableScrollPanel(), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        buttonPanel.setBackground(Color.WHITE);

        JButton backButton = createStyledButton("回首頁");
        backButton.addActionListener(e -> {
            dispose();
            new HomeFrame().setVisible(true);
        });

        JButton retryButton = createStyledButton("繼續查詢");
        retryButton.addActionListener(e -> {
            dispose();
            new TrainSearchChoiceFrame().setVisible(true);
        });

        buttonPanel.add(retryButton);
        buttonPanel.add(backButton);

        tableContainer.add(buttonPanel, BorderLayout.SOUTH);

        add(tableContainer, BorderLayout.CENTER);

    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        Color baseColor = new Color(255, 182, 193);
        Color hoverColor = new Color(255, 105, 180);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(baseColor);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(baseColor);
            }
        });
        return button;
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
                    "更多資訊" // 按鈕文字
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
            /**
             * 點「更多資訊」跳出整台車的資訊
             */
            if (clicked) {
                Train train = result.get(currentRow);
                new TrainDetailFrame(train, fromStation, toStation).setVisible(true);
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



