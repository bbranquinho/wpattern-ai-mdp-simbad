package org.wpattern.ai.simbad.window;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;

public class RowHeaderRenderer extends JLabel implements ListCellRenderer<Object> {

	private static final long serialVersionUID = 201412081321L;

	RowHeaderRenderer(JTable table) {
		JTableHeader header = table.getTableHeader();
		this.setOpaque(true);
		this.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
		this.setHorizontalAlignment(CENTER);
		this.setForeground(header.getForeground());
		this.setBackground(header.getBackground());
		this.setFont(header.getFont());
	}

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		this.setText((value == null) ? "" : value.toString());
		return this;
	}
}
