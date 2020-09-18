package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Move;
import com.chess.engine.board.Board;

import java.util.Collection;

public abstract class Piece {
    
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;

    public Piece(final Alliance pieceAlliance, final int piecePosition){
        this.pieceAlliance = pieceAlliance;
        this.piecePosition = piecePosition;
        //TODO work here
        this.isFirstMove = false;
    }

    public int getPiecePosition(){
        return this.piecePosition;
    }


    public Alliance getPieceAlliance(){
        return this.pieceAlliance;
    }
    
    public boolean isFirstMove(){
        return this.isFirstMove;
    }
    
    public abstract Collection<Move>calculateLegalMoves(final Board board);
}