package main;

import main.utilities.Constants;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class Test extends JFrame {
    public Test() {
        MigLayout layout = new MigLayout(
                "wrap, fill, debug",
                "[]",
                "[][][]"
        );
        JPanel panel = new JPanel(layout);

        panel.add(new JEditorPane(), new CC().width(String.valueOf(Constants.GUI_WIDTH)).height(String.valueOf(Constants.GUI_HEIGHT * 0.9)));

        JPanel actionPanel = new JPanel(new MigLayout());
        actionPanel.add(new JButton("B1"));
        actionPanel.add(new JButton("B2"));
        actionPanel.add(new JButton("B3"));
        panel.add(actionPanel, new CC());

        JPanel inputPanel = new JPanel(new MigLayout("fill", "[grow][]", ""));
        inputPanel.add(new JTextField(), new CC().growX().height("100%"));
        inputPanel.add(new JButton("SEND"), new CC().height("100%"));
        panel.add(inputPanel, new CC().width("100%").height("16px"));

        setContentPane(panel);

        setVisible(true);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public static void main(String[] args) {
        new Test();
    }
}
