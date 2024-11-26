package com.chess.engine;

import com.chess.engine.board.BoardUtils;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;

public enum Alliance {

    WHITE {
        @Override
        public int getDirection() {
            return -1;
        }

        @Override
        public boolean isBlack() {
            return false;
        }

        @Override
        public boolean isWhite() {
            return true;
        }

        @Override
        public Player choosePlayer(Player whitePlayer, BlackPlayer blackPlayer) {
            return whitePlayer;
        }

        @Override
        public int getOppositeDirection() {
            return 1;
        }

        @Override
        public boolean isPawnPromotionSquare(final int pos) {
            return BoardUtils.EIGHTH_RANK[pos];
        }
    },

    BLACK {
        @Override
        public int getDirection() {
            return 1;
        }

        @Override
        public boolean isBlack() {
            return true;
        }

        @Override
        public boolean isWhite() {
            return false;
        }

        @Override
        public Player choosePlayer(Player whitePlayer, BlackPlayer blackPlayer) {
            return blackPlayer;
        }

        @Override
        public int getOppositeDirection() {
            return -1;
        }

        @Override
        public boolean isPawnPromotionSquare(final int pos) {
            return BoardUtils.FIRST_RANK[pos];
        }

    };

    public abstract int getDirection();

    public abstract int getOppositeDirection();

    public abstract boolean isBlack();

    public abstract boolean isWhite();

    public abstract boolean isPawnPromotionSquare(int position);

    public abstract Player choosePlayer(Player whitePlayer, BlackPlayer blackPlayer);

}
