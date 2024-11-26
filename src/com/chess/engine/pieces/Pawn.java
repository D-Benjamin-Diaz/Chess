package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.PawnAttackMove;
import com.chess.engine.board.Move.PawnEnPassantAttackMove;
import com.chess.engine.board.Move.PawnJumpMove;
import com.chess.engine.board.Move.PawnMove;
import com.chess.engine.board.Move.PawnPromotion;

public class Pawn extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = { 7, 8, 9, 16 };

    public Pawn(final int pPos, final Alliance pAlliance) {
        super(PieceType.PAWN, pPos, pAlliance, true);
    }

    public Pawn(final int pPos, final Alliance pAlliance, final boolean isFirstMove) {
        super(PieceType.PAWN, pPos, pAlliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for (int currentCandidateCoordinateOffset : CANDIDATE_MOVE_COORDINATES) {
            int candidateDestinationCoordinate = this.piecePosition
                    + (currentCandidateCoordinateOffset * this.pieceAlliance.getDirection());

            if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                continue;
            }
            // REGULAR FORWARD MOVE
            if (currentCandidateCoordinateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isOccupied()) {
                if (this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                    legalMoves.add(new PawnPromotion(new PawnMove(board, this, candidateDestinationCoordinate)));
                } else {
                    legalMoves.add(new PawnMove(board, this, candidateDestinationCoordinate));
                }
                // FIRST MOVE PAWN CONDITION
            } else if (currentCandidateCoordinateOffset == 16 &&
                    this.isFirstMove &&
                    ((BoardUtils.SECOND_ROW[this.piecePosition] && this.getPieceAlliance().isWhite()) ||
                            (BoardUtils.SEVENTH_ROW[this.piecePosition] && this.getPieceAlliance().isBlack()))) {
                final int behindCandidateCoordinate = this.piecePosition + (8 * this.pieceAlliance.getDirection());

                if (!board.getTile(candidateDestinationCoordinate).isOccupied() &&
                        !board.getTile(behindCandidateCoordinate).isOccupied()) {
                    legalMoves.add(new PawnJumpMove(board, this, candidateDestinationCoordinate));
                }
                // ATTACKING MOVES & Restrictions
            } else if (currentCandidateCoordinateOffset == 7 &&
                    !((BoardUtils.EIGHT_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) ||
                            (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))) {
                if (board.getTile(candidateDestinationCoordinate).isOccupied()) {
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (pieceOnCandidate.getPieceAlliance() != this.pieceAlliance) {
                        if (this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                            legalMoves.add(new PawnPromotion(
                                    new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate)));
                        } else {
                            legalMoves.add(
                                    new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    } else if (board.getEnPassant() != null) {

                        if (board.getEnPassant().getPiecePosition() == (this.getPiecePosition()
                                + (this.pieceAlliance.getOppositeDirection()))) {
                            final Piece pieceCandidate = board.getEnPassant();
                            if (this.pieceAlliance != pieceCandidate.getPieceAlliance()) {
                                legalMoves.add(new PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate,
                                        pieceCandidate));
                            }
                        }
                    }
                }
            } else if (currentCandidateCoordinateOffset == 9 &&
                    !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() ||
                            (BoardUtils.EIGHT_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))) {
                if (board.getTile(candidateDestinationCoordinate).isOccupied()) {
                    final Piece pieceAtDestinationCoordinate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (pieceAtDestinationCoordinate.pieceAlliance != this.pieceAlliance) {
                        if (this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                            legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this,
                                    candidateDestinationCoordinate, pieceAtDestinationCoordinate)));
                        } else {
                            legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate,
                                    pieceAtDestinationCoordinate));
                        }
                    }
                } else if (board.getEnPassant() != null) {
                    if (board.getEnPassant().getPiecePosition() == (this.getPiecePosition()
                            - (this.pieceAlliance.getOppositeDirection()))) {
                        final Piece pieceCandidate = board.getEnPassant();
                        if (this.pieceAlliance != pieceCandidate.getPieceAlliance()) {
                            legalMoves.add(new PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate,
                                    pieceCandidate));
                        }
                    }
                }
            }
        }
        return Collections.unmodifiableCollection(legalMoves);
    }

    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }

    @Override
    public Pawn movePiece(final Move move) {
        return new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    public Piece getPromPiece() {
        return new Queen(this.piecePosition, this.pieceAlliance, false);
    }

}
