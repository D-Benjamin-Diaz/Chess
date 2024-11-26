package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.board.Move.MajorAttackMove;
import com.chess.engine.board.Move.MajorMove;

public class King extends Piece {

    private final boolean isKingSideCastleCapable;
    private final boolean isQueenSideCastleCapable;
    private final boolean isCastled;

    private static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = { -9, -8, -7, -1, 1, 7, 8, 9 };

    public King(final int pPos, final Alliance pAlliance, final boolean isKingSideCastleCapable,
            final boolean isQueenSideCastleCapable) {
        super(PieceType.KING, pPos, pAlliance, true);
        this.isCastled = false;
        this.isKingSideCastleCapable = isKingSideCastleCapable;
        this.isQueenSideCastleCapable = isQueenSideCastleCapable;
    }

    public King(final int pPos, final Alliance pAlliance, final boolean isFirstMove, final boolean isCastled,
            final boolean isKingSideCastleCapable, final boolean isQueenSideCastleCapable) {
        super(PieceType.KING, pPos, pAlliance, isFirstMove);
        this.isCastled = isCastled;
        this.isKingSideCastleCapable = isKingSideCastleCapable;
        this.isQueenSideCastleCapable = isQueenSideCastleCapable;
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {

        List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES) {

            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {

                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isEightColumnExclusion(this.piecePosition, currentCandidateOffset)) {

                    continue;
                }

                
                else if (!candidateDestinationTile.isOccupied()) {
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                } else {
                    final Piece pieceAtDestinationCoordinate = candidateDestinationTile.getPiece();
                    final Alliance pieceAtDestinationCoordinateAlliance = pieceAtDestinationCoordinate.pieceAlliance;

                    if (this.pieceAlliance != pieceAtDestinationCoordinateAlliance) {
                        legalMoves.add(new MajorAttackMove(board, this, candidateDestinationCoordinate,
                                pieceAtDestinationCoordinate));
                    }
                }
            }
        }
        return Collections.unmodifiableCollection(legalMoves);
    }

    public static boolean isFirstColumnExclusion(final int currentPosition, final int currentCandidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition]
                && (currentCandidateOffset == -9 || currentCandidateOffset == -1 || currentCandidateOffset == 7);
    }

    public static boolean isEightColumnExclusion(final int currentPosition, final int currentCandidateOffset) {
        return BoardUtils.EIGHT_COLUMN[currentPosition]
                && (currentCandidateOffset == 9 || currentCandidateOffset == 1 || currentCandidateOffset == -7);
    }

    @Override
    public String toString() {
        return PieceType.KING.toString();
    }

    @Override
    public King movePiece(final Move move) {
        return new King(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance(), false,
                move.isCastlingMove(), false, false);
    }

    @Override
    public boolean isKing() {
        return true;
    }

    public boolean isKingSideCastleCapable() {
        return this.isKingSideCastleCapable;
    }

    public boolean isQueenSideCastleCapable() {
        return this.isQueenSideCastleCapable;
    }

    public boolean isCastled() {
        return this.isCastled;
    }
}
