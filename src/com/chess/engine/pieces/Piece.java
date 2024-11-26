package com.chess.engine.pieces;

import java.util.Collection;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public abstract class Piece {

    protected final PieceType pieceType;
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;

    Piece(final PieceType pType,
            final int pPos,
            final Alliance pAlliance,
            final boolean isFirstMove) {

        this.pieceType = pType;
        this.pieceAlliance = pAlliance;
        this.piecePosition = pPos;
        this.isFirstMove = isFirstMove;

        computeHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Piece)) {
            return false;
        }
        final Piece otherPiece = (Piece) other;

        return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType() &&
                pieceAlliance == otherPiece.getPieceAlliance();
    }

    @Override
    public int hashCode() {
        return computeHashCode();
    }

    private int computeHashCode() {

        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;
    }

    public int getPieceValue() {
        return this.pieceType.getPieceValue();
    }

    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }

    public PieceType getPieceType() {
        return this.pieceType;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public boolean isFisrtMove() {
        return isFirstMove;
    }

    public boolean isKing() {
        return false;
    }

    public boolean isRook() {
        return false;
    }

    public int getPiecePosition() {
        return this.piecePosition;
    }

    public abstract Piece movePiece(Move move);

    public enum PieceType {

        PAWN("P", 100) {

        },
        KNIGHT("N", 300) {

        },
        BISHOP("B", 300) {

        },
        QUEEN("Q", 900) {

        },
        KING("K", 10000) {

        },
        ROOK("R", 500) {

        };

        private String pieceName;
        private int pieceValue;

        PieceType(String pieceName, int pieceValue) {
            this.pieceName = pieceName;
            this.pieceValue = pieceValue;
        }

        public int getPieceValue() {
            return this.pieceValue;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }

    }
}
