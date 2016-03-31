/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package hanto.studentrlangelo.beta;

import static hanto.common.HantoPieceType.*;
import static hanto.common.MoveResult.*;
import static hanto.common.HantoPlayerColor.*;
import static org.junit.Assert.*;
import hanto.common.*;
import hanto.studentrlangelo.HantoGameFactory;

import org.junit.*;

/**
 * Test cases for Beta Hanto.
 * @version Sep 14, 2014
 */
public class BetaHantoMasterTest
{
	/**
	 * Internal class for these test cases.
	 * @version Sep 13, 2014
	 */
	class TestHantoCoordinate implements HantoCoordinate
	{
		private final int x, y;
		
		public TestHantoCoordinate(int x, int y)
		{
			this.x = x;
			this.y = y;
		}
		/*
		 * @see hanto.common.HantoCoordinate#getX()
		 */
		@Override
		public int getX()
		{
			return x;
		}

		/*
		 * @see hanto.common.HantoCoordinate#getY()
		 */
		@Override
		public int getY()
		{
			return y;
		}

	}
	
	private static HantoGameFactory factory;
	private HantoGame game;
	private HantoGame game1;
	
	@BeforeClass
	public static void initializeClass()
	{
		factory = HantoGameFactory.getInstance();
	}
	
	@Before
	public void setup()
	{
		// By default, blue moves first.
		game1 = factory.makeHantoGame(HantoGameID.BETA_HANTO);
		game = factory.makeHantoGame(HantoGameID.BETA_HANTO, BLUE);
	}
	
	@Test	// 1
	public void bluePlacesInitialButterflyAtOrigin() throws HantoException
	{
		final MoveResult mr = game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		assertEquals(OK, mr);
		final HantoPiece p = game.getPieceAt(makeCoordinate(0, 0));
		assertEquals(BLUE, p.getColor());
		assertEquals(BUTTERFLY, p.getType());
	}
	
	@Test  // 2
	public void bluePlacesASparrowAtOrigin() throws HantoException
	{
		final MoveResult mr = game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		assertEquals(OK, mr);
		final HantoPiece p = game.getPieceAt(makeCoordinate(0, 0));
		assertEquals(BLUE, p.getColor());
		assertEquals(SPARROW, p.getType());
	}
	
	@Test // 3
	public void redMakesValidButterflyMove() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		final MoveResult sM = game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		assertEquals(OK, sM);
		final HantoPiece p = game.getPieceAt(makeCoordinate(0 , 1));
		assertEquals(RED, p.getColor());
		assertEquals(BUTTERFLY, p.getType());
	}
	
	@Test //4
	public void redMakesValidSparrowMove() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		final MoveResult sM = game.makeMove(SPARROW, null, makeCoordinate(1, -1));
		assertEquals(OK, sM);
		final HantoPiece p = game.getPieceAt(makeCoordinate(1, -1));
		assertEquals(RED, p.getColor());
		assertEquals(SPARROW, p.getType());
	}
	
	@Test(expected = HantoException.class) //5
	public void bluePlacesButterflyFirstNotAtOrigin() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
	}
	
	@Test(expected = HantoException.class) //6
	public void bluePlacesSparrowFirstNotAtOrigin() throws HantoException
	{
		game.makeMove(SPARROW, null, makeCoordinate(1, 2));
	}
	
	@Test(expected = HantoException.class) //7
	public void redMakesInvalidButterflyMove() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(3, 3));
	}
	
	@Test //8
	public void blueMakesValidThirdMove() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		final MoveResult tM = game.makeMove(SPARROW, null, makeCoordinate(2, -1));
		assertEquals(OK, tM);
		final HantoPiece p = game.getPieceAt(makeCoordinate(2, -1));
		assertEquals(BLUE, p.getColor());
		assertEquals(SPARROW, p.getType());
	}
	
	@Test(expected = HantoException.class) //9
	public void blueTriesToPlaceTwoButterflies() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, -1));
	}
	
	@Test(expected = HantoException.class) //10
	public void redTriesToPlaceTwoButterflies() throws HantoException
	{
		game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		game.makeMove(BUTTERFLY, null, makeCoordinate(2, 0));
	}
	
	@Test // 11
	public void blueOnlyPlacesButterflyOnFourthMove() throws HantoException
	{
		game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(1, -1));
		game.makeMove(SPARROW, null, makeCoordinate(2, -1));
		game.makeMove(SPARROW, null, makeCoordinate(1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(1, 1));
		game.makeMove(SPARROW,  null, makeCoordinate(0, 1));
		final MoveResult fM = game.makeMove(BUTTERFLY, null, makeCoordinate(-1, 1));
		assertEquals(OK, fM);
		final HantoPiece p = game.getPieceAt(makeCoordinate(-1, 1));
		assertEquals(BLUE, p.getColor());
		assertEquals(BUTTERFLY, p.getType());
	}
	
	@Test // 12
	public void redOnlyPlacesButterflyOnFourthMove() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(SPARROW, null, makeCoordinate(1, -1));
		game.makeMove(SPARROW, null, makeCoordinate(2, -1));
		game.makeMove(SPARROW, null, makeCoordinate(1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(1, 1));
		game.makeMove(SPARROW,  null, makeCoordinate(0, 1));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 1));
		final MoveResult fM = game.makeMove(BUTTERFLY, null, makeCoordinate(-2, 1));
		assertEquals(OK, fM);
		final HantoPiece p = game.getPieceAt(makeCoordinate(-2, 1));
		assertEquals(RED, p.getColor());
		assertEquals(BUTTERFLY, p.getType());
	}
	
	@Test(expected = HantoException.class) // 13
	public void blueTriesToPlaceFourthSparrowOnFourthMove() throws HantoException
	{
		//blue first move
		game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		//red first move
		game.makeMove(BUTTERFLY, null, makeCoordinate(1, -1));
		//blue second move
		game.makeMove(SPARROW, null, makeCoordinate(2, -1));
		//red second move
		game.makeMove(SPARROW, null, makeCoordinate(1, 0));
		//third blue move
		game.makeMove(SPARROW, null, makeCoordinate(1, 1));
		//third red move
		game.makeMove(SPARROW,  null, makeCoordinate(0, 1));
		//fourth blue move
		game.makeMove(SPARROW, null, makeCoordinate(-1, 1));
	}
	
	@Test(expected = HantoException.class) // 14
	public void redTriesToPlaceFourthSparrowOnFourthMove() throws HantoException
	{
		//blue first move
		game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		//red first move
		game.makeMove(SPARROW, null, makeCoordinate(1, -1));
		//blue second move
		game.makeMove(SPARROW, null, makeCoordinate(2, -1));
		//red second move
		game.makeMove(SPARROW, null, makeCoordinate(1, 0));
		//blue third move
		game.makeMove(SPARROW, null, makeCoordinate(1, 1));
		//red third move
		game.makeMove(SPARROW,  null, makeCoordinate(0, 1));
		//blue fourth move
		game.makeMove(BUTTERFLY, null, makeCoordinate(-1, 1));
		//red fourth move
		game.makeMove(SPARROW, null, makeCoordinate(-1, 0));
	}
	
	@Test(expected = HantoException.class) // 15
	public void redTriesToPlaceInOccupiedHex() throws HantoException
	{
		game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
	}
	
	@Test(expected = HantoException.class) // 16
	public void blueTriesToPlaceInOccupiedHex() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(SPARROW, null, makeCoordinate(1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(1, 0));
	}
	
	@Test //17
	public void gameIsOverInADrawAfterSixTurnsWithoutWinner() throws HantoException
	{
		//blue first move
		game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		//red first move
		game.makeMove(SPARROW, null, makeCoordinate(1, 0));
		//blue second move
		game.makeMove(SPARROW, null, makeCoordinate(2, 0));
		//red second move
		game.makeMove(SPARROW, null, makeCoordinate(3, 0));
		//blue third move
		game.makeMove(SPARROW, null, makeCoordinate(4, 0));
		//red third move
		game.makeMove(SPARROW,  null, makeCoordinate(5, 0));
		//blue fourth move
		game.makeMove(BUTTERFLY, null, makeCoordinate(6, 0));
		//red fourth move
		game.makeMove(BUTTERFLY, null, makeCoordinate(7, 0));
		//blue fifth move
		game.makeMove(SPARROW, null, makeCoordinate(8, 0));
		//red fifth move
		game.makeMove(SPARROW, null, makeCoordinate(9, 0));
		//blue last move
		game.makeMove(SPARROW, null, makeCoordinate(10, 0));
		//red last move
		final MoveResult lM = game.makeMove(SPARROW, null, makeCoordinate(11, 0));
		assertEquals(DRAW, lM);
		final HantoPiece p = game.getPieceAt(makeCoordinate(11, 0));
		assertEquals(RED, p.getColor());
		assertEquals(SPARROW, p.getType());
	}
	
	@Test //18
	public void redWinCondition() throws HantoException 
	{
		//blue first move
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		//red first move
		game.makeMove(SPARROW, null, makeCoordinate(0, -1));
		//blue second move
		game.makeMove(SPARROW, null, makeCoordinate(0, -2));
		//red second move
		game.makeMove(BUTTERFLY, null, makeCoordinate(1, -1));
		//blue third move
		game.makeMove(SPARROW, null, makeCoordinate(0, -3));
		//red third move
		game.makeMove(SPARROW,  null, makeCoordinate(1, 0));
		//blue fourth move
		game.makeMove(SPARROW, null, makeCoordinate(0, -4));
		//red fourth move
		game.makeMove(SPARROW, null, makeCoordinate(0, 1));
		//blue fifth move
		game.makeMove(SPARROW, null, makeCoordinate(0, -5));
		//red fifth move
		game.makeMove(SPARROW, null, makeCoordinate(-1, 1));
		//blue last move
		game.makeMove(SPARROW, null, makeCoordinate(0, -6));
		//red last move
		final MoveResult rWins = game.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		assertEquals(RED_WINS, rWins);
	}
	
	@Test //19
	public void blueWinCondition() throws HantoException
	{
		//blue first move
		game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		//red first move
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, -1));
		//blue second move
		game.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		//red second move
		game.makeMove(SPARROW, null, makeCoordinate(0, 1));
		//blue third move
		game.makeMove(BUTTERFLY, null, makeCoordinate(-1, -1));
		//red third move
		game.makeMove(SPARROW,  null, makeCoordinate(0, 2));
		//blue fourth move
		game.makeMove(SPARROW, null, makeCoordinate(0, -2));
		//red fourth move
		game.makeMove(SPARROW, null, makeCoordinate(0, 3));
		//blue fifth move
		game.makeMove(SPARROW, null, makeCoordinate(1, -2));
		//red fifth move
		game.makeMove(SPARROW, null, makeCoordinate(0, 4));
		//blue last move
		final MoveResult bWins = game.makeMove(SPARROW, null, makeCoordinate(1, -1));
		assertEquals(BLUE_WINS, bWins);
	}
	
	@Test(expected = HantoException.class) //20
	public void attemptToPlacePieceAfterGameOver() throws HantoException
	{
		//blue first move
		game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		//red first move
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, -1));
		//blue second move
		game.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		//red second move
		game.makeMove(SPARROW, null, makeCoordinate(0, 1));
		//blue third move
		game.makeMove(BUTTERFLY, null, makeCoordinate(-1, -1));
		//red third move
		game.makeMove(SPARROW,  null, makeCoordinate(0, 2));
		//blue fourth move
		game.makeMove(SPARROW, null, makeCoordinate(0, -2));
		//red fourth move
		game.makeMove(SPARROW, null, makeCoordinate(0, 3));
		//blue fifth move
		game.makeMove(SPARROW, null, makeCoordinate(1, -2));
		//red fifth move
		game.makeMove(SPARROW, null, makeCoordinate(0, 4));
		//blue last move
		game.makeMove(SPARROW, null, makeCoordinate(0, 5));
		//red last move
		game.makeMove(SPARROW, null, makeCoordinate(0, 6));
		game.makeMove(SPARROW, null, makeCoordinate(0, 7));
	}
	
	// Helper methods
	private HantoCoordinate makeCoordinate(int x, int y)
	{
		return new TestHantoCoordinate(x, y);
	}
	
	
	
	
}
