package com.chess.engine.board;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.chess.engine.pieces.Piece;

/**
 * Class Is abstract because tiles can be occupied or not
 */
public abstract class Tile {

    // Coordinate of tile
    protected final int tileCoord;

    // A Map that relates tiles with its respective number
    private static final Map<Integer, EmptyTile> emptyTylesCache = createAllPossibleEmptyTyles();

    /**
     * The method that creates the map of tile to tile number
     * 
     * @return a copy of an empty map
     */
    private static Map<Integer, EmptyTile> createAllPossibleEmptyTyles() {

        // Creates and fills up the map
        final Map<Integer, EmptyTile> emptyTyleMap = new HashMap<>();

        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            emptyTyleMap.put(i, new EmptyTile(i));
        }

        return Collections.unmodifiableMap(emptyTyleMap);

    }

    /**
     * A methid created to call the constructors. Either occupied or not
     * 
     * @param coord coordinate of tile
     * @param pc    piece in that tile
     * @return Either creates an occupied tile or gets the empty tile
     */
    public static Tile createTile(final int coord, final Piece pc) {
        return pc != null ? new OccupiedTile(coord, pc) : emptyTylesCache.get(coord);
    }

    /**
     * Tile constructor
     * 
     * @param coord coordinate of the tile
     */
    private Tile(final int coord) {

        tileCoord = coord;
    }

    // Couple of abstract methods created, declared better on child classes
    public abstract boolean isOccupied();

    public abstract Piece getPiece();
    

    public int getTileCoordinate() {
        return this.tileCoord;
    }

    /**
     * First Child Class with respective constructor and methods
     */
    public static final class EmptyTile extends Tile {

        private EmptyTile(final int coord) {
            super(coord);
        }

        @Override
        public String toString() {
            return "-";
        }

        @Override
        public boolean isOccupied() {
            return false;
        };

        @Override
        public Piece getPiece() {
            return null;
        };
    }

    /**
     * Second schild class with respective constructor and methods
     */
    public static final class OccupiedTile extends Tile {

        private final Piece pieceOnTile;

        private OccupiedTile(final int coord, final Piece pc) {

            super(coord);
            this.pieceOnTile = pc;
        }

        @Override
        public String toString() {
            return getPiece().getPieceAlliance().isBlack() ? getPiece().toString().toLowerCase()
                    : getPiece().toString();
        }

        @Override
        public boolean isOccupied() {
            return true;
        }

        @Override
        public Piece getPiece() {
            return pieceOnTile;
        }
    }
}
