package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.Iterables;
import com.google.common.collect.ImmutableList; 


public abstract class Player {

    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;

    Player(final Board board,
            final Collection<Move> legMoves,
            final Collection<Move> opponentMoves) {

        this.board = board;
        this.playerKing = establishKing();

        // Iterable<Move> combined = Iterables
        //         .unmodifiableIterable(Iterables.concat(legalMoves, calculateKingCastles(legalMoves, opponentMoves)));
        // this.legalMoves = Lists.newArrayList(combined);

        this.legalMoves = ImmutableList.copyOf(Iterables.concat(legMoves, calculateKingCastles(legMoves, opponentMoves)));

    }

    public Board getBoard() {
        return this.board;
    }

    public King getPlayerKing() {
        return (King) this.playerKing;
    }

    private King establishKing() {
        for (final Piece piece : getActivePieces()) {
            if (piece.isKing()) {
                return (King) piece;
            }
        }

        throw new RuntimeException("Should Not Reach Here: NOT a valid board");
    }

    public boolean isLegalMove(final Move move) {
        return this.legalMoves.contains(move);
    }

    public boolean isInCheck() {
        return false;
    }

    public boolean isInCheckMate() {
        return false;
    }

    public boolean isInStaleMate() {
        return false;
    }

    public boolean isCastled() {
        return false;
    }

    public Collection<Move> getLegalMoves() {
        return this.legalMoves;
    }

    protected boolean hasEscapeMoves(){
        for (final Move move : this.legalMoves) {
            final MoveTransition transition = makeMove(move);
            if (transition.getMoveStatus().isDone()) {
                return true;
            }
        }
        return false;
    }

    public MoveTransition makeMove(final Move move) {
        if (!isLegalMove(move)) {
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }

        final Board transitionBoard = move.execute();

        final Collection<Move> kingAttacks = Player.calculateAttacksOnTile(
                transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                transitionBoard.currentPlayer().getLegalMoves());

        if (!kingAttacks.isEmpty()) {
            return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }

        return new MoveTransition(transitionBoard, move, MoveStatus.DONE);

    }

    protected static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> moves) {
        final List<Move> attackMoves = new ArrayList<>();
        for (final Move move : moves) {
            if (piecePosition == move.getDestinationCoordinate()) {
                attackMoves.add(move);
            }
        }

        return Collections.unmodifiableCollection(attackMoves);
    }

    public abstract Collection<Piece> getActivePieces();

    public abstract Alliance getAlliance();

    public abstract Player getOpponent();

    protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals,
            Collection<Move> opponentLegals);

}
