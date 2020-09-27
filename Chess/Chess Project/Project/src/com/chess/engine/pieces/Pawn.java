package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.google.common.collect.ImmutableList;

public class Pawn extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATE = { 8, 16, 7, 9 };

    public Pawn(final Alliance pieceAlliance, final int piecePosition) {
        super(PieceType.PAWN,pieceAlliance, piecePosition, true);
    
    }

    public Pawn(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
        super(PieceType.PAWN, pieceAlliance, piecePosition, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        
        final List<Move> legalMoves = new ArrayList<>();
        
        for(final int currentCandidateOffset: CANDIDATE_MOVE_COORDINATE){

            final int candidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * currentCandidateOffset);
            
            if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
              continue;  
            }
            if(currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)){
                    legalMoves.add(new Move.PawnPromotion(new Move.PawnMove(board,this,candidateDestinationCoordinate)));
                }else{
                legalMoves.add(new Move.PawnMove(board, this, candidateDestinationCoordinate));
                }
            }else if(currentCandidateOffset == 16 && this.isFirstMove() && 
            ((BoardUtils.SEVENTH_RANK[this.piecePosition] && this.getPieceAlliance().isBlack()) || 
            (BoardUtils.SECOND_RANK[this.piecePosition] && this.getPieceAlliance().isWhite()))){
                final int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 8);
                if(!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() && 
                !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    legalMoves.add(new Move.PawnJump(board,this,candidateDestinationCoordinate));
                    
                }
            }else if(currentCandidateOffset == 7 
         && !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) || (BoardUtils.FIRST_COLUMN[this.piecePosition]
                            && this.pieceAlliance.isBlack()))){
                                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                                    if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                                        if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)){
                                            legalMoves.add(new Move.PawnPromotion(new Move.PawnAttackMove(board, this,
                                                    candidateDestinationCoordinate, pieceOnCandidate)));
                                    }else{
                                        legalMoves.add(new Move.PawnAttackMove(board, this,
                                                candidateDestinationCoordinate, pieceOnCandidate));
                                    }
                                }
                                }else if(board.getEnPassantPawn() != null
                                        && board.getEnPassantPawn().getPiecePosition() == (this.piecePosition
                                                + (this.pieceAlliance.getOppositeDirection()))){
                                                     final Piece pieceOnCandidate = board.getEnPassantPawn();
                                                    if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                                                        legalMoves.add(new Move.PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                                }
                            }

            }else if(currentCandidateOffset == 9 && 
            !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) || 
            (BoardUtils.EIGHTH_COLUMN[this.piecePosition]
                            && this.pieceAlliance.isBlack()))){
                                if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                        if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)){
                            legalMoves.add(new Move.PawnPromotion(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate,
                                    pieceOnCandidate)));
                        }else{
                        legalMoves.add(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate,pieceOnCandidate));
                    }
                }
                }else if (board.getEnPassantPawn() != null && board.getEnPassantPawn()
                        .getPiecePosition() == (this.piecePosition - (this.pieceAlliance.getOppositeDirection()))) {
                    final Piece pieceOnCandidate = board.getEnPassantPawn();
                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                        legalMoves.add(new Move.PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate,
                                pieceOnCandidate));
                    }
                }


            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Pawn movePiece(final Move move) {
        // TODO Auto-generated method stub
        return new Pawn(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }
    
    @Override
    public String toString() {
        return Piece.PieceType.PAWN.toString();
    }

    public Piece getPromotedPiece(){
        return new Queen(this.pieceAlliance,this.piecePosition,false);
    }
}