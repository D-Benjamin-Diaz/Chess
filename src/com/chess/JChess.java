package com.chess;

import com.chess.engine.board.Board;
import com.chess.gui.Table;

public class JChess {
    @SuppressWarnings("unused")
    public static void main(String[] args) {

        Board board = Board.createStandardBoard();

        Table.get().show();
    }
}
