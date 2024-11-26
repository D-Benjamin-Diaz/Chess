//LIBRARIES & PACKAGES
package com.chess.engine.board;

import com.chess.engine.board.Board.Builder;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

/**
 * MOVE CLASS
 */
public abstract class Move {

    // Properties
    final Board board;
    final Piece movedPiece;
    protected final int destinationCoordinate;
    protected final boolean isFirstMove;

    public static final Move NULL_MOVE = new NullMove();

    /**
     * CONSTRUCTOR
     * 
     * @param board                 Board where move is played
     * @param movedPiece            Piece being moved
     * @param destinationCoordinate Destination of the moved Piece
     */
    private Move(final Board board, final Piece movedPiece, final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
        this.isFirstMove = movedPiece.isFisrtMove();
    }

    private Move(final Board brd, final int destCoord) {
        this.board = brd;
        this.destinationCoordinate = destCoord;
        this.movedPiece = null;
        this.isFirstMove = false;
    }

    // Overriden
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + this.movedPiece.getPiecePosition();
        result = prime * result + this.movedPiece.hashCode();
        result = prime * result + this.movedPiece.getPiecePosition();
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Move)) {
            return false;
        }
        final Move otherMove = (Move) other;
        return getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
                getMovedPiece().equals(otherMove.getMovedPiece()) &&
                getCurrentCoordinate() == otherMove.getCurrentCoordinate();
    }

    // Getters
    /**
     * Uses the Moved Piece position to return the initial position of the piece
     * before the move
     * 
     * @return the position of the piece before being moved
     */
    public int getCurrentCoordinate() {
        return this.movedPiece.getPiecePosition();
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    public Piece getMovedPiece() {
        return this.movedPiece;
    }

    public Piece getAttackedPiece() {
        return null;
    }

    public Board getBoard() {
        return this.board;
    }

    // Identity
    public boolean isAttack() {
        return false;
    }

    public boolean isCastled() {
        return false;
    }

    public boolean isCastlingMove() {
        return false;
    }

    
    public Board execute() {
        final Board.Builder builder = new Builder();
        for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
            if (!this.movedPiece.equals(piece)) {
                builder.setPiece(piece);
            }
        }
        for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }
        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
        return builder.build();
    }

    // CHILDREN CLASSES
    public static final class MajorAttackMove extends AttackMove {

        public MajorAttackMove(final Board board, final Piece movPiece, final int destCoord, final Piece attacked) {
            super(board, movPiece, destCoord, attacked);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof MajorAttackMove && super.equals(other);
        }

        @Override
        public String toString() {
            return movedPiece.getPieceType() + BoardUtils.getPositionAtCoordinate(destinationCoordinate);
        }
    }

    public static final class MajorMove extends Move {

        /**
         * CONSTRUCTOR
         * Move that does not affect any other piece both firendly and opponent
         * 
         * @param board                 Board where is being played
         * @param movedPiece            Piece being moved on the board
         * @param destinationCoordinate Coordinate where the piece is gonna move
         */
        public MajorMove(final Board board,
                final Piece movedPiece,
                final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof MajorMove && super.equals(other);
        }

        @Override
        public String toString() {
            return movedPiece.getPieceType().toString()
                    + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    public static class AttackMove extends Move {

        // Properties
        final Piece attackedPiece;

        /**
         * CONSTRUCTOR
         * Move that attacks an enemy piece
         * 
         * @param board                 Board where is being played
         * @param movedPiece            Piece being moved
         * @param destinationCoordinate Coordinate where piece is being moved
         * @param attackedPiece         Piece that is being attacked
         */
        public AttackMove(final Board board,
                final Piece movedPiece,
                final int destinationCoordinate,
                final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        // Overriden
        @Override
        public int hashCode() {
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(final Object other) {

            if (this == other) {
                return true;
            }

            if (!(other instanceof AttackMove)) {
                return false;
            }
            final AttackMove otherAttackMove = (AttackMove) other;
            return super.equals(otherAttackMove) && getAttackedPiece() == otherAttackMove.getAttackedPiece();
        }

        @Override
        public boolean isAttack() {
            return true;
        }

        @Override
        public Piece getAttackedPiece() {
            return this.attackedPiece;
        }
    }

    public static final class PawnMove extends Move {

        /**
         * CONSTRUCTOR
         * Move that moves a Pawn
         * 
         * @param board                 Board where is being played
         * @param movedPiece            Pawn being moved
         * @param destinationCoordinate Destination of the pawn
         */
        public PawnMove(final Board board,
                final Piece movedPiece,
                final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnMove && super.equals(other);
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(destinationCoordinate);
        }
    }

    public static class PawnAttackMove extends AttackMove {

        /**
         * CONSTRUCTOR
         * Attack move from a pawn
         * 
         * @param board                 Board where is being played
         * @param movedPiece            Pawn being moved
         * @param destinationCoordinate Where is the pawn being moved
         * @param attackedPiece         What piece is being attacked
         */
        public PawnAttackMove(final Board board,
                final Piece movedPiece,
                final int destinationCoordinate,
                final Piece attackedPiece) {

            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnAttackMove && super.equals(other);
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).substring(0, 1) + "x" +
                    BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    public static final class PawnEnPassantAttackMove extends PawnAttackMove {

        /**
         * CONSTRUCTOR
         * Potential attack move en passant
         * 
         * @param board                 Board whew is being played
         * @param movedPiece            Pawn that is being moved
         * @param destinationCoordinate Where is the pawn being moved
         * @param attackedPiece         Attacked pawn
         */
        public PawnEnPassantAttackMove(final Board board,
                final Piece movedPiece,
                final int destinationCoordinate,
                final Piece attackedPiece) {

            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnEnPassantAttackMove && super.equals(other);
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder();
            for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
                if (!this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }

            for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                if (!piece.equals(this.getAttackedPiece())) {
                    builder.setPiece(piece);
                }
            }

            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    public static final class PawnPromotion extends Move {

        final Move decMove;
        final Pawn promPawn;

        public PawnPromotion(final Move decMove) {
            super(decMove.getBoard(), decMove.getMovedPiece(), decMove.getDestinationCoordinate());
            this.decMove = decMove;
            this.promPawn = (Pawn) decMove.getMovedPiece();
        }

        @Override
        public Board execute() {
            final Board pawnMovedBoard = this.decMove.execute();
            final Board.Builder builder = new Builder();
            for (final Piece piece : pawnMovedBoard.currentPlayer().getActivePieces()) {
                if (!this.promPawn.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (Piece piece : pawnMovedBoard.currentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }

            builder.setPiece(this.promPawn.getPromPiece().movePiece(this));
            builder.setMoveMaker(pawnMovedBoard.currentPlayer().getAlliance());
            return builder.build();
        }

        @Override
        public boolean isAttack() {
            return this.decMove.isAttack();
        }

        @Override
        public Piece getAttackedPiece() {
            return this.decMove.getAttackedPiece();
        }

        @Override
        public String toString() {
            return "";
        }

        @Override
        public int hashCode() {
            return decMove.hashCode() + (31 * promPawn.hashCode());
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnPromotion && (super.equals(other));
        }
    }

    public static final class PawnJumpMove extends Move {

        /**
         * CONSTRUCTOR
         * When a pawn is movedd two squares at the beginning
         * 
         * @param board                 Board where is being played
         * @param movedPiece            Pawn that is being moved
         * @param destinationCoordinate Coordinate where is being moved
         */
        public PawnJumpMove(final Board board,
                final Piece movedPiece,
                final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        // Overriden
        @Override
        public Board execute() {
            final Builder builder = new Builder();
            for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
                if (!this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(destinationCoordinate);
        }
    }

    static abstract class CastledMove extends Move {

        // PROPERTIES
        protected final Rook castledRook;
        protected final int castledRookStart;
        protected final int castledRookDestination;

        /**
         * CONSTRUCTOR
         * Castling move
         * 
         * @param board                  Board where is being played
         * @param movedPiece             King that is being moved
         * @param destinationCoordinate  Destination coordinate of the moved king
         * @param castledRook            Rook that is being castled
         * @param castledRookStart       Rook starting position before castling
         * @param castledRookDestination Rook final position after castling
         */
        public CastledMove(final Board board,
                final Piece movedPiece,
                final int destinationCoordinate,
                final Rook castledRook,
                final int castledRookStart,
                final int castledRookDestination) {

            super(board, movedPiece, destinationCoordinate);
            this.castledRook = castledRook;
            this.castledRookStart = castledRookStart;
            this.castledRookDestination = castledRookDestination;
        }

        // GETTERS
        public Rook getCastledRook() {
            return this.castledRook;
        }

        // OVERRIDEN
        @Override
        public boolean isCastlingMove() {
            return true;
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder();
            for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
                if (!this.movedPiece.equals(piece) && !this.castledRook.equals(piece)) {
                    builder.setPiece(piece);
                }

            }

            for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }

            builder.setPiece(this.movedPiece.movePiece(this));

            builder.setPiece(new Rook(this.castledRookDestination, this.castledRook.getPieceAlliance()));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());

            return builder.build();

        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + this.castledRook.hashCode();
            result = prime * result + this.castledRookDestination;
            return result;
        }

        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof CastledMove)) {
                return false;
            }
            final CastledMove otherCastledMove = (CastledMove) other;
            return super.equals(otherCastledMove) && this.castledRook.equals(otherCastledMove.getCastledRook());
        }

    }

    public static final class CastledKingside extends CastledMove {

        /**
         * CONSTRUCTOR
         * Castling Kingside move
         * 
         * @param board                  Board where is being played
         * @param movedPiece             King that is being moved
         * @param destinationCoordinate  Destination coordinate of the moved king
         * @param castledRook            Rook that is being castled
         * @param castledRookStart       Rook starting position before castling
         * @param castledRookDestination Rook final position after castling
         */
        public CastledKingside(final Board board,
                final Piece movedPiece,
                final int destinationCoordinate,
                final Rook castledRook,
                final int castledRookStart,
                final int castledRookDestination) {

            super(board, movedPiece, destinationCoordinate, castledRook, castledRookStart, castledRookDestination);
        }

        @Override
        public String toString() {
            return "O-O";
        }

        @Override
        public boolean equals(final Object other) {
            return this == other && other instanceof CastledKingside && super.equals(other);
        }
    }

    public static final class CastledQueenside extends CastledMove {

        /**
         * CONSTRUCTOR
         * Castling queenside move
         * 
         * @param board                  Board where is being played
         * @param movedPiece             King that is being moved
         * @param destinationCoordinate  Destination coordinate of the moved king
         * @param castledRook            Rook that is being castled
         * @param castledRookStart       Rook starting position before castling
         * @param castledRookDestination Rook final position after castling
         */
        public CastledQueenside(final Board board,
                final Piece movedPiece,
                final int destinationCoordinate,
                final Rook castledRook,
                final int castledRookStart,
                final int castledRookDestination) {

            super(board, movedPiece, destinationCoordinate, castledRook, castledRookStart, castledRookDestination);
        }

        @Override
        public String toString() {
            return "O-O-O";
        }

        @Override
        public boolean equals(final Object other) {
            return this == other && other instanceof CastledQueenside && super.equals(other);
        }
    }

    public static final class NullMove extends Move {

        /**
         * Constructor for a null move class
         */
        public NullMove() {
            super(null, 65);
        }

        @Override
        public Board execute() {
            throw new RuntimeException("Null move cannot be executed");
        }

        @Override
        public int getCurrentCoordinate() {
            return -1;
        }
    }

    public static class MoveFactory {

        /**
         * Non instantiable class
         */
        private MoveFactory() {
            throw new RuntimeException("Not Instantiable");
        }

        public static Move createMove(final Board board,
                final int currentCoordinate,
                final int destinationCoordinate) {

            for (final Move move : board.getAllLegalMoves()) {
                if (move.getCurrentCoordinate() == currentCoordinate &&
                        move.getDestinationCoordinate() == destinationCoordinate) {
                    return move;
                }
            }

            return NULL_MOVE;

        }
    }
}
