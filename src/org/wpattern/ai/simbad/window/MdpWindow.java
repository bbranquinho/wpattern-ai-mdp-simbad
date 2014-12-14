package org.wpattern.ai.simbad.window;

import javax.swing.AbstractListModel;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

import org.wpattern.ai.simbad.beans.MazeBean;

public final class MdpWindow extends JInternalFrame implements Runnable {

	private static final long serialVersionUID = 201412080750L;

	private static final float framesPerSecond = 2.0f;

	private JTable table;

	private DefaultTableModel tableModel;

	private MazeBean maze;

	public MdpWindow() {
		this.initialize();
		this.initializeWindow();
	}

	private void initializeWindow() {
		this.show();
		new Thread(this).start();
	}

	public MdpWindow(MazeBean maze, int xLocation, int yLocation) {
		this.maze = maze;

		this.initialize();
		this.setLocation(xLocation, yLocation);

		this.initializeWindow();
	}

	private void initialize() {
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("", "[grow]", "[grow]"));
		panel.setBounds(100, 100, 250, 200);

		this.setResizable(true);
		this.setTitle("Markov Decision Processes (MDP)");
		this.setContentPane(panel);

		// Table
		ListModel<String> listModel = new AbstractListModel<String>() {
			private static final long serialVersionUID = 201412081328L;

			@Override
			public int getSize() {
				return MdpWindow.this.maze.getMazeHeight();
			}

			@Override
			public String getElementAt(int index) {
				return "" + index;
			}
		};

		String[] columnNames = this.buildHeaderName(this.maze);

		this.tableModel = new DefaultTableModel(columnNames, 0) {
			private static final long serialVersionUID = 201412091611L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		this.table = new JTable(this.tableModel);
		this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.table.setRowSelectionAllowed(false);
		for (int i = 0; i < this.table.getColumnModel().getColumnCount(); i++) {
			this.table.getColumnModel().getColumn(i).setPreferredWidth(50);
		}
		this.setupRows();

		JList<String> rowHeader = new JList<String>(listModel);
		rowHeader.setFixedCellWidth(20);
		rowHeader.setFixedCellHeight(this.table.getRowHeight());
		rowHeader.setCellRenderer(new RowHeaderRenderer(this.table));

		JScrollPane scroll = new JScrollPane(this.table);
		scroll.setRowHeaderView(rowHeader);
		panel.add(scroll, "cell 0 0,grow");

		this.pack();
	}

	private String[] buildHeaderName(MazeBean maze) {
		if (maze == null) {
			return new String[0];
		}

		String[] headerName = new String[maze.getMazeWidth()];

		for (int i = 0; i < maze.getMazeWidth(); i++) {
			headerName[i] = i + "";
		}

		return headerName;
	}

	@Override
	public void run() {
		while (true) {
			try {
				// don't need precise time tick
				Thread.sleep((int)(1000f / framesPerSecond));
			} catch (InterruptedException e) {
				System.err.println(e.getMessage());
			}
			this.update();
		}
	}

	private void update() {
		if (this.maze == null) {
			return;
		}

	}

	private void setupRows() {
		if (this.maze == null) {
			return;
		}

		for (int i = 0; i < this.maze.getMazeHeight(); i++) {
			String[] item = new String[this.maze.getMazeWidth()];

			for (int j = 0; j < this.maze.getMazeWidth(); j++) {
				item[j] = "";
			}

			this.tableModel.addRow(item);
		}
	}

}
