package com.chess.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import com.chess.engine.Alliance;
import com.chess.engine.player.Player;
import com.chess.gui.Table.PlayerType;

public class GameSetup extends JDialog {

    private PlayerType whitePlayerType;
    private PlayerType blackPlayerType;
    private JSpinner searchDepthSpinner;

    private static final String HUMAN_TEXT = "Human";
    private static final String COMPUTER_TEXT = "Computer";

    GameSetup(final JFrame frame, final boolean modal){

        super(frame, modal);

        final JPanel myPanel = new JPanel(new GridLayout(0,1));
        final JRadioButton whiteHumanButton = new JRadioButton(HUMAN_TEXT);
        final JRadioButton whiteComputerButtom = new JRadioButton(COMPUTER_TEXT);
        final JRadioButton blackHumanButton = new JRadioButton(HUMAN_TEXT);
        final JRadioButton blackComputerButton = new JRadioButton(COMPUTER_TEXT);
        whiteHumanButton.setActionCommand(HUMAN_TEXT);
        final ButtonGroup whiteGroup = new ButtonGroup();
        whiteGroup.add(whiteHumanButton);
        whiteGroup.add(whiteComputerButtom);
        whiteHumanButton.setSelected(true);

        final ButtonGroup blackGroup = new ButtonGroup();
        blackGroup.add(blackComputerButton);
        blackGroup.add(blackHumanButton);
        blackHumanButton.setSelected(true);

        getContentPane().add(myPanel);
        myPanel.add(new JLabel("White"));
        myPanel.add(whiteHumanButton);
        myPanel.add(whiteComputerButtom);
        myPanel.add(new JLabel("Black"));
        myPanel.add(blackHumanButton);
        myPanel.add(blackComputerButton);

        myPanel.add(new JLabel("Search"));

        this.searchDepthSpinner = new JSpinner(new SpinnerNumberModel(6,0,Integer.MAX_VALUE, 1));

        final JButton cancelButton = new JButton("Cancel");
        final JButton okButton = new JButton("OK");

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                whitePlayerType = whiteComputerButtom.isSelected() ? PlayerType.COMPUTER : PlayerType.HUMAN;
                blackPlayerType = blackComputerButton.isSelected() ? PlayerType.COMPUTER : PlayerType.HUMAN;
            }
        });

        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e){
                System.out.println("Cancel");
                GameSetup.this.setVisible(false);
            }
        });

        myPanel.add(cancelButton);
        myPanel.add(okButton);

        setLocationRelativeTo(frame);
        pack();
        setVisible(false);
    }

    void promptUser(){
        setVisible(true);
        repaint();
    }

    boolean isAIPlayer(final Player player){
        if (player.getAlliance() == Alliance.WHITE) {
            return getWhitePlayerType() == PlayerType.COMPUTER;
        }
        return getBlackPlayerType() == PlayerType.COMPUTER;
    }

    PlayerType getWhitePlayerType(){
        return this.whitePlayerType;
    }

    PlayerType getBlackPlayerType(){
        return this.blackPlayerType;
    }

    int getSearchDepth(){
        return (Integer) this.searchDepthSpinner.getValue();
    }
    
}
