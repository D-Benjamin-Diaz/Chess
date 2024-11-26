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

public class Knight extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = { -17, -15, -10, -6, 6, 10, 15, 17 };

    public Knight(final int piecePos, final Alliance pieceAlliance) {
        super(PieceType.KNIGHT, piecePos, pieceAlliance, true);
    }

    public Knight(int piecePos, Alliance pieceAlliance, final boolean isFirstMove) {
        super(PieceType.KNIGHT, piecePos, pieceAlliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentOffset : CANDIDATE_MOVE_COORDINATES) {
            final int candidateCoordinate = this.piecePosition + currentOffset;

            if (BoardUtils.isValidTileCoordinate(candidateCoordinate)) {
                final Tile candidateDestinationTile = board.getTile(candidateCoordinate);

                if (isFirstColumnExclusion(this.piecePosition, currentOffset) ||
                        isSecondtColumnExclusion(this.piecePosition, currentOffset) ||
                        isSeventhColumnExclusion(this.piecePosition, currentOffset) ||
                        isEightColumnExclusion(this.piecePosition, currentOffset)) {
                    continue;
                }
                if (!candidateDestinationTile.isOccupied()) {
                    legalMoves.add(new MajorMove(board, this, candidateCoordinate));
                } else {
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAtDestinationAlliance = pieceAtDestination.getPieceAlliance();

                    if (pieceAtDestinationAlliance != this.pieceAlliance) {
                        legalMoves.add(new MajorAttackMove(board, this, candidateCoordinate, pieceAtDestination));
                    }
                }
            }
        }

        return Collections.unmodifiableList(legalMoves);

    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {

        return BoardUtils.FIRST_COLUMN[currentPosition] && ((candidateOffset == -17) || (candidateOffset == -10)
                || (candidateOffset == 6) || candidateOffset == 15);

    }

    private static boolean isSecondtColumnExclusion(final int currentPosition, final int candidateOffset) {

        return BoardUtils.SECOND_COLUMN[currentPosition] && ((candidateOffset == -10) || (candidateOffset == 6));

    }

    private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset) {

        return BoardUtils.SEVENTH_COLUMN[currentPosition] && ((candidateOffset == 10)
                || (candidateOffset == -6));

    }

    private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset) {

        return BoardUtils.EIGHT_COLUMN[currentPosition] && ((candidateOffset == 10) || (candidateOffset == -6));

    }

    @Override
    public String toString() {
        return PieceType.KNIGHT.toString();
    }

    @Override
    public Knight movePiece(final Move move) {
        return new Knight(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }
}
