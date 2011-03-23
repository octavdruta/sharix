package GUI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


// Table model used to store traffic activity data.
class SharixTableModel extends DefaultTableModel {
    final static String[] columnNames = {"Source", "Destination",
                                         "Filename", "Progress", "Status"};
    // Constructor.
    public SharixTableModel() {
        super(columnNames, 0);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
    
    // Returns source value for a given row.
    public String getSource(int row) {
        return (String) getValueAt(row, 0);
    }
    
    // Returns destination value for a given row.
    public String getDestination(int row) {
        return (String) getValueAt(row, 1);
    }
    
    // Returns filename value for a given row.
    public String getFilename(int row) {
        return (String) getValueAt(row, 2);
    }
    
    // Sets status for a given row.
    public void setStatus(int row, Object status) {
        setValueAt(status, row, 4);
    }
    
    // Sets progress for a given row.
    public void setProgress(int row, Object progress) {
        setValueAt(progress, row, 3);
    }
}

// Table cell renderer used to display progress bar.
class ProgressRenderer implements TableCellRenderer {
    @Override
    public  Component getTableCellRendererComponent(JTable table, Object value,
                                                    boolean isSelected,
                                                    boolean hasFocus,
                                                    int row, int column) {
        return (Component) value;
    }
}


public class TrafficActivity extends JPanel {
    Object[][] mockData = { { "Andrei", "_me_", "logo.txt", "0", "Receiving" }, 
                            { "_me_", "Cip", "data.txt", "10", "Sending" } };
    SharixTableModel tableModel = new SharixTableModel();
    ProgressRenderer progressRenderer = new ProgressRenderer();
    JTable trafficTable = new JTable(tableModel) {
                public TableCellRenderer getCellRenderer(int row, int column) {
                    if (column == 3) {
                        return progressRenderer;
                    } else {
                        return super.getCellRenderer(row, column);
                    }
                }
    };
        
    // Constructor.
    public TrafficActivity() {
        super(new GridLayout(1, 0));
        trafficTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
        trafficTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(trafficTable);
        add(scrollPane);
    }
    
    // Adds new activity to the traffic table.
    void addTrafficActivity(String source, String dest,
                            String filename, String status, Integer progress) {
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(progress);
        progressBar.setStringPainted(true);
        Object[] rowData = {source, dest, filename, progressBar, status};
        tableModel.addRow(rowData);
    }
    
    // Updates an activity from traffic table.
    public void updateTrafficActivity(String source, String dest,
                               String filename,  String status,
                               Integer progress) {
        for (int row = 0; row < trafficTable.getRowCount(); ++row) {
            if (tableModel.getSource(row).equals(source) &&
                tableModel.getDestination(row).equals(dest) &&
                tableModel.getFilename(row).equals(filename)) {
                if (status != null)
                    tableModel.setStatus(row, status);
                if (progress != null) {
                    JProgressBar progressBar = new JProgressBar(0, 100);
                    progressBar.setValue(progress);
                    progressBar.setStringPainted(true);
                    tableModel.setProgress(row, progressBar);
                }
                return;
            }
        }
        addTrafficActivity(source, dest, filename, status, progress);
    }
}