/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2016 Gary F. Pollice
 *******************************************************************************/

package hanto.studentrlangelo.beta;

import hanto.common.*;
import hanto.studentrlangelo.common.HantoCoordinateImpl;
import hanto.studentrlangelo.common.HantoPieceImpl;

import static hanto.common.MoveResult.*;

import java.util.LinkedList;
import java.util.List;

import static hanto.common.HantoPieceType.*;
import static hanto.common.HantoPlayerColor.*;

/**
 * <<Fill this in>>
 * @version Mar 16, 2016
 */
public class BetaHantoGame implements HantoGame
{
	private boolean firstMove = true;
	private int moveCounter = 1;
	private final HantoPiece blueButterfly  = new HantoPieceImpl(BLUE, BUTTERFLY);
	private final HantoPiece blueSparrow = new HantoPieceImpl(BLUE, SPARROW);
	private final HantoPiece redButterfly = new HantoPieceImpl(RED, BUTTERFLY);
	private final HantoPiece redSparrow = new HantoPieceImpl(RED, SPARROW);
	private int blueSparrowCounter = 0;
	private int redSparrowCounter = 0;
	private HantoCoordinateImpl blueButterflyHex = null;
	private HantoCoordinateImpl redButterflyHex = null;
	private List<HantoCoordinate> blueSparrowHexes = new LinkedList<HantoCoordinate>();
	private List<HantoCoordinate> redSparrowHexes = new LinkedList<HantoCoordinate>();
	private List<HantoCoordinate> validLocations = new LinkedList<HantoCoordinate>();
	private boolean bluePlayedButterfly = false;
	private boolean redPlayedButterfly = false;
	private boolean gameOver = false;
	
	/*
	 * @see hanto.common.HantoGame#makeMove(hanto.common.HantoPieceType, hanto.common.HantoCoordinate, hanto.common.HantoCoordinate)
	 */
	@Override
	public MoveResult makeMove(HantoPieceType pieceType, HantoCoordinate from,
			HantoCoordinate to) throws HantoException
	{
		if (gameOver) {
			throw new HantoException("You cannot move after the game is finished");
		}
		MoveResult mr = null;
		final HantoCoordinateImpl dest = new HantoCoordinateImpl(to);
		if (firstMove) {
			HantoCoordinateImpl origin = new HantoCoordinateImpl(0, 0);
			if (dest.equals(origin)) {
				if (pieceType == BUTTERFLY) {
					blueButterflyHex = dest;
					mr = OK;
					bluePlayedButterfly = true;
				}
				else {
					blueSparrowHexes.add(dest);
					mr = OK;
					blueSparrowCounter += 1;
				}
			}
			else {
				throw new HantoException("First piece has to be placed at origin");
			}
		}
		else {
			//Even move counter means it is second player's turn
			if (moveCounter%2 == 0)
			{
				if (isValidMove(dest)) {
					if (pieceType == BUTTERFLY) {
						if (!redPlayedButterfly) {
							redButterflyHex = dest;
							mr = OK;
							redPlayedButterfly = true;
						}
						else {
							throw new HantoException("Red already placed Butterfly!");
						}
					}
					else {
						if (redSparrowCounter == 3 && redButterflyHex == null) {
							throw new HantoException("Red must place the Butterfly by fourth turn!");
						}
						else {
							redSparrowHexes.add(dest);
							mr = OK;
							redSparrowCounter += 1;
						}
					}
				}
				else {
					throw new HantoException("Invalid location to place piece");
				}
			}
			else {
				if (isValidMove(dest)) {
					if (pieceType == BUTTERFLY) {
						if (!bluePlayedButterfly) {
							blueButterflyHex = dest;
							mr = OK;
							bluePlayedButterfly = true;
						}
						else {
							throw new HantoException("Blue already placed Butterfly!");
						}
					}
					else {
						if (blueSparrowCounter == 3 && blueButterflyHex == null) {
							throw new HantoException("Blue must place the Butterfly by fourth turn!");
						}
						else {
							blueSparrowHexes.add(dest);
							mr = OK;
							blueSparrowCounter += 1;
						}
					}
				}
				else {
					throw new HantoException("Invalid location to place piece");
				}
			}
				
		}
		firstMove = false;
		moveCounter += 1;
		MoveResult result = checkWin();
		if (mr == OK) {
			adjustValidMoves(to);
		}
		if (result != null) {
			mr = result;
			gameOver = true;
		}
		else if (moveCounter == 13) {
			mr = DRAW;
			gameOver = true;
		}
		return mr;
	}

	/*
	 * @see hanto.common.HantoGame#getPieceAt(hanto.common.HantoCoordinate)
	 */
	@Override
	public HantoPiece getPieceAt(HantoCoordinate where)
	{
		final HantoCoordinateImpl coord = new HantoCoordinateImpl(where);
		if (coord.equals(blueButterflyHex)) {
			return blueButterfly;
		}
		else if (blueSparrowHexes.contains(coord)) {
			return blueSparrow;
		}
		else if (coord.equals(redButterflyHex)) {
			return redButterfly;
		}
		else if (redSparrowHexes.contains(coord)) {
			return redSparrow;
		}
		else {
			return null;
		}
	}

	private void adjustValidMoves(HantoCoordinate locationLastPiecePlaced)
	{
		int xValue = locationLastPiecePlaced.getX();
		int yValue = locationLastPiecePlaced.getY();
		locationLastPiecePlaced = new HantoCoordinateImpl(locationLastPiecePlaced);
		if (validLocations.contains(locationLastPiecePlaced)) {
			validLocations.remove(locationLastPiecePlaced);
		}

		HantoCoordinate possibility1 = new HantoCoordinateImpl(xValue-1, yValue+1);
		HantoCoordinate possibility2 = new HantoCoordinateImpl(xValue-1, yValue);
		HantoCoordinate possibility3 = new HantoCoordinateImpl(xValue, yValue-1);
		HantoCoordinate possibility4 = new HantoCoordinateImpl(xValue+1, yValue-1);
		HantoCoordinate possibility5 = new HantoCoordinateImpl(xValue+1, yValue);
		HantoCoordinate possibility6 = new HantoCoordinateImpl(xValue, yValue+1);
		
		if (getPieceAt(possibility1) == null) {
			validLocations.add(new HantoCoordinateImpl(xValue-1, yValue+1));
		}
		if (getPieceAt(possibility2) == null) {
			validLocations.add(new HantoCoordinateImpl(xValue-1, yValue));
		}
		if (getPieceAt(possibility3) == null) {
			validLocations.add(new HantoCoordinateImpl(xValue, yValue-1));
		}
		if (getPieceAt(possibility4) == null) {
			validLocations.add(new HantoCoordinateImpl(xValue+1, yValue-1));
		}
		if (getPieceAt(possibility5) == null) {
			validLocations.add(new HantoCoordinateImpl(xValue+1, yValue));
		}
		if (getPieceAt(possibility6) == null) {
			validLocations.add(new HantoCoordinateImpl(xValue, yValue+1));
		}
	}
	
	private boolean isValidMove(HantoCoordinate location) {

		return validLocations.contains(location);
	}
	
	private MoveResult checkWin() {
		HantoCoordinate blueButterflyLocation = blueButterflyHex;
		HantoCoordinate redButterflyLocation = redButterflyHex;
		boolean redWon = false;
		boolean blueWon = false;
		
		if (blueButterflyLocation != null) {
			int xValue = blueButterflyLocation.getX();
			int yValue = blueButterflyLocation.getY();
		
			HantoCoordinate possibility1 = new HantoCoordinateImpl(xValue-1, yValue+1);
			HantoCoordinate possibility2 = new HantoCoordinateImpl(xValue-1, yValue);
			HantoCoordinate possibility3 = new HantoCoordinateImpl(xValue, yValue-1);
			HantoCoordinate possibility4 = new HantoCoordinateImpl(xValue+1, yValue-1);
			HantoCoordinate possibility5 = new HantoCoordinateImpl(xValue+1, yValue);
			HantoCoordinate possibility6 = new HantoCoordinateImpl(xValue, yValue+1);
		
			if (getPieceAt(possibility1) != null && getPieceAt(possibility2) != null && 
					getPieceAt(possibility3) != null && getPieceAt(possibility4) != null && getPieceAt(possibility5) != null 
					&& getPieceAt(possibility6) != null)
			{
				redWon = true;
			}
		}
		if (redButterflyLocation != null) {
			int xValue = redButterflyLocation.getX();
			int yValue = redButterflyLocation.getY();
		
			HantoCoordinate possibility1 = new HantoCoordinateImpl(xValue-1, yValue+1);
			HantoCoordinate possibility2 = new HantoCoordinateImpl(xValue-1, yValue);
			HantoCoordinate possibility3 = new HantoCoordinateImpl(xValue, yValue-1);
			HantoCoordinate possibility4 = new HantoCoordinateImpl(xValue+1, yValue-1);
			HantoCoordinate possibility5 = new HantoCoordinateImpl(xValue+1, yValue);
			HantoCoordinate possibility6 = new HantoCoordinateImpl(xValue, yValue+1);
		
			if (getPieceAt(possibility1) != null && getPieceAt(possibility2) != null && 
					getPieceAt(possibility3) != null && getPieceAt(possibility4) != null && getPieceAt(possibility5) != null 
					&& getPieceAt(possibility6) != null)
			{
				blueWon = true;
			}
		}
		if (redWon && blueWon)
		{
			return DRAW;
		}
		else if (redWon) {
			return RED_WINS;
		}
		else if (blueWon) {
			return BLUE_WINS;
		}
		else {
			return null;
		}
	}
	
	/*
	 * @see hanto.common.HantoGame#getPrintableBoard()
	 */
	@Override
	public String getPrintableBoard()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
