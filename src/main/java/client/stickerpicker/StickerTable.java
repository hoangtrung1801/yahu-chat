package client.stickerpicker;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

public class StickerTable extends JTable {

    private static final Collection<Sticker> allStickers = Arrays.stream(Objects.requireNonNull(new File("D:\\Development\\Java\\ClassOOP\\ChatRealtime\\src\\main\\resources\\stickers")
                    .listFiles()))
            .map(file -> {
                try {
                    Sticker sticker = new Sticker(file.getName(), new FileInputStream(file).readAllBytes());
                    return sticker;
                } catch (Exception e ) {
                    e.printStackTrace();
                }
                return null;
            }).toList();

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        StickerTable table = new StickerTable(8, new Font("OpenMoji", Font.PLAIN, 34), true);
        table.setRowHeight(34);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setViewportView(table);

        frame.add(scrollPane);

        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setName("Sticker Picker");
    }

    private static class StickerTableModel extends DefaultTableModel {

        @SuppressWarnings("unchecked")
        public StickerTableModel(int columnCount) {
            super(0, columnCount);
            fireTableStructureChanged();
        }
        @Override
        public Class getColumnClass(int column) {  //Use the EmojiColumnElement as the class for all cells
            return StickerColumnElement.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int mColIndex) {
            return false;
        }

    }

    //Holder class for all cells that holds the sticker
    private static class StickerColumnElement {

        protected final Sticker e;

        public StickerColumnElement(Sticker e) {
            this.e = e;
        }

        @Override
        public String toString() {  //Return the unicode representation to render the emoji in the cell
            return e.toString();
        }

    }

    //Custom render class for the EmojiColumnElement
    private class StickerCellRenderer extends DefaultTableCellRenderer {

        private final Font font;

        public StickerCellRenderer(Font font) {
            this.font = font;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

            //Cells are by default rendered as a JLabel.
            JLabel label = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
//            label.setFont(font);
//            label.setHorizontalAlignment(CENTER);
//            label.setBackground(isSelected ? Color.LIGHT_GRAY : Color.WHITE);
//            label.setForeground(Color.BLACK);
//            label.setBorder(null);

            // set image
            Sticker sticker = ((StickerColumnElement) value).e;
            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/stickers/"+sticker.getStickerName()));
            label.setIcon(imageIcon);

            //Return the JLabel which renders the cell.
            return label;

        }
    }

    public Sticker getSelectedSticker() {

        int row = getSelectedRow();
        int col = getSelectedColumn();

        if (row < 0 || col < 0)
            return null;


        int rowVal = convertRowIndexToModel(row);
        int colVal = convertColumnIndexToModel(col);

        StickerTableModel model = (StickerTableModel)getModel();

        StickerColumnElement ece = (StickerColumnElement)model.getValueAt(rowVal, colVal);
        return ece.e;
    }

    public StickerTable(int columnCount, Font font, boolean pruneRegionalIndicator) {
        super(new StickerTableModel(Math.min(columnCount, allStickers.size())));  //Create a model with columns that's a min of column count or num emojis


        this.setDoubleBuffered(true);
        setDoubleBuffered(true);
        setTableHeader(null);  //remove the header column
        StickerCellRenderer renderer = new StickerCellRenderer(font);
        setDefaultRenderer(StickerColumnElement.class, renderer);


        setCellSelectionEnabled(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        StickerTableModel model = (StickerTableModel)getModel();


        //Add all the emojis to the table
        Iterator<Sticker> it = allStickers.iterator();
        int colCount = 0;
        StickerColumnElement[] columns = null;

        while (it.hasNext()) {

            if (colCount == 0) //Create a column of emojis
                columns = new StickerColumnElement[model.getColumnCount()];


            Sticker e = it.next();

            columns[colCount] = new StickerColumnElement(e);
            colCount++;

            if (colCount == 8) {
                model.insertRow(model.getRowCount(), columns);
                colCount = 0;
                columns = null;
            }
        }

        if (columns != null)
            model.insertRow(model.getRowCount(), columns);



        model.fireTableDataChanged();
        revalidate();


        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
//                listener.userSelectedEmoji(getSelectedEmoji());
                System.out.println(getSelectedSticker());
            }
        });
    }
}
