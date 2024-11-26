package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

public class BlackPlayer extends Player {

    public BlackPlayer(final Board board, final Collection<Move> whiteStandardLegalMoves,
            final Collection<Move> blackStandardLegalMoves) {

        super(board, blackStandardLegalMoves, whiteStandardLegalMoves);

    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.getBoard().getBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.whitePlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegal,
            final Collection<Move> opponentLegals) {
        final List<Move> kingCastles = new ArrayList<>();

        if (this.playerKing.isFisrtMove() && !this.isInCheck()) {

            // BLACKKingSide
            if (!this.board.getTile(5).isOccupied() && this.board.getTile(6).isOccupied()) {
                final Tile rookTile = this.board.getTile(7);

                if (rookTile.isOccupied() && rookTile.getPiece().isFisrtMove()) {
                    if (Player.calculateAttacksOnTile(5, opponentLegals).isEmpty()
                            && Player.calculateAttacksOnTile(6, opponentLegals).isEmpty()
                            && rookTile.getPiece().isRook()) {

                        kingCastles.add(new Move.CastledKingside(this.board, this.playerKing, 6,
                                (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 5));
                    }
                }
            }

            // WHITEQUEENSIDE
            if (!this.board.getTile(3).isOccupied() &&
                    !this.board.getTile(2).isOccupied()) {

                final Tile rookTile = this.board.getTile(0);
                if (rookTile.isOccupied() && rookTile.getPiece().isFisrtMove()) {
                    if (rookTile.isOccupied() && rookTile.getPiece().isFisrtMove()) {
                        if (Player.calculateAttacksOnTile(3, opponentLegals).isEmpty()
                                && Player.calculateAttacksOnTile(2, opponentLegals).isEmpty()
                                && rookTile.getPiece().isRook()) {

                            kingCastles.add(new Move.CastledKingside(this.board, this.playerKing, 2,
                                    (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 3));

                        }
                    }
                }

            }
        }
        return Collections.unmodifiableCollection(kingCastles);
    }
}
