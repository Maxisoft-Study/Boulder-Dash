package boulderdash.utils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * The class <code>PositionTest</code> contains tests for the class
 * <code>{@link Position}</code>.
 * 
 * @generatedBy CodePro at 06/12/12 18:23
 * @author maxime
 * @version $Revision: 1.0 $
 */
public class PositionTest {
	/**
	 * Run the Position() constructor test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 06/12/12 18:23
	 */
	@Test
	public void testPosition_1() throws Exception {

		final Position result = new Position();

		Assert.assertNotNull(result);
		Assert.assertEquals("Pos(0,0)", result.toString());
		Assert.assertEquals(0, result.getX());
		Assert.assertEquals(0, result.getY());
	}

	/**
	 * Run the Position(int[]) constructor test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 06/12/12 18:23
	 */
	@Test
	public void testPosition_2() throws Exception {
		final int[] tab = new int[] { 0, 1 };

		final Position result = new Position(tab);

		Assert.assertNotNull(result);
		Assert.assertEquals("Pos(0,1)", result.toString());
		Assert.assertEquals(0, result.getX());
		Assert.assertEquals(1, result.getY());
	}

	/**
	 * Run the Position(int,int) constructor test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 06/12/12 18:23
	 */
	@Test
	public void testPosition_3() throws Exception {
		final int x = 1;
		final int y = 1;

		final Position result = new Position(x, y);

		Assert.assertNotNull(result);
		Assert.assertEquals("Pos(1,1)", result.toString());
		Assert.assertEquals(1, result.getX());
		Assert.assertEquals(1, result.getY());
	}

	/**
	 * Run the void add(Position) method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 06/12/12 18:23
	 */
	@Test
	public void testAdd_1() throws Exception {
		final Position fixture = new Position(1, 1);
		final Position pos = new Position(1, 1);

		fixture.add(pos);

	}

	/**
	 * Run the Position clone() method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 06/12/12 18:23
	 */
	@Test
	public void testClone_1() throws Exception {
		final Position fixture = new Position(1, 1);

		final Position result = fixture.clone();

		Assert.assertNotNull(result);
		Assert.assertEquals("Pos(1,1)", result.toString());
		Assert.assertEquals(1, result.getX());
		Assert.assertEquals(1, result.getY());
	}

	/**
	 * Run the boolean equals(Object) method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 06/12/12 18:23
	 */
	@Test
	public void testEquals_1() throws Exception {
		final Position fixture = new Position(1, 1);
		final Object obj = new Position(1, 1);

		final boolean result = fixture.equals(obj);

		Assert.assertEquals(true, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 06/12/12 18:23
	 */
	@Test
	public void testEquals_2() throws Exception {
		final Position fixture = new Position(1, 1);
		final Object obj = null;

		final boolean result = fixture.equals(obj);

		Assert.assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 06/12/12 18:23
	 */
	@Test
	public void testEquals_3() throws Exception {
		final Position fixture = new Position(1, 1);
		final Object obj = new Object();

		final boolean result = fixture.equals(obj);

		Assert.assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 06/12/12 18:23
	 */
	@Test
	public void testEquals_4() throws Exception {
		final Position fixture = new Position(1, 1);
		final Object obj = new Position(1, 1);

		final boolean result = fixture.equals(obj);

		Assert.assertEquals(true, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 06/12/12 18:23
	 */
	@Test
	public void testEquals_5() throws Exception {
		final Position fixture = new Position(1, 1);
		final Object obj = new Position(1, 1);

		final boolean result = fixture.equals(obj);

		Assert.assertEquals(true, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 06/12/12 18:23
	 */
	@Test
	public void testEquals_6() throws Exception {
		final Position fixture = new Position(1, 1);
		final Object obj = new Position(1, 1);

		final boolean result = fixture.equals(obj);

		Assert.assertEquals(true, result);
	}

	/**
	 * Run the int getX() method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 06/12/12 18:23
	 */
	@Test
	public void testGetX_1() throws Exception {
		final Position fixture = new Position(1, 1);

		final int result = fixture.getX();

		Assert.assertEquals(1, result);
	}

	/**
	 * Run the int getY() method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 06/12/12 18:23
	 */
	@Test
	public void testGetY_1() throws Exception {
		final Position fixture = new Position(1, 1);

		final int result = fixture.getY();

		Assert.assertEquals(1, result);
	}

	/**
	 * Run the int hashCode() method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 06/12/12 18:23
	 */
	@Test
	public void testHashCode_1() throws Exception {
		final Position fixture = new Position(1, 1);

		final int result = fixture.hashCode();

		Assert.assertEquals(993, result);
	}

	/**
	 * Run the void mul(int) method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 06/12/12 18:23
	 */
	@Test
	public void testMul_1() throws Exception {
		final Position fixture = new Position(1, 1);
		final int coeff = 1;

		fixture.mul(coeff);

	}

	/**
	 * Run the void setX(int) method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 06/12/12 18:23
	 */
	@Test
	public void testSetX_1() throws Exception {
		final Position fixture = new Position(1, 1);
		final int x = 1;

		fixture.setX(x);

	}

	/**
	 * Run the void setY(int) method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 06/12/12 18:23
	 */
	@Test
	public void testSetY_1() throws Exception {
		final Position fixture = new Position(1, 1);
		final int y = 1;

		fixture.setY(y);

	}

	/**
	 * Run the void sub(Position) method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 06/12/12 18:23
	 */
	@Test
	public void testSub_1() throws Exception {
		final Position fixture = new Position(1, 1);
		final Position pos = new Position(1, 1);

		fixture.sub(pos);

	}

	/**
	 * Run the String toString() method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 06/12/12 18:23
	 */
	@Test
	public void testToString_1() throws Exception {
		final Position fixture = new Position(1, 1);

		final String result = fixture.toString();

		Assert.assertEquals("Pos(1,1)", result);
	}

	/**
	 * Perform pre-test initialization.
	 * 
	 * @throws Exception
	 *             if the initialization fails for some reason
	 * 
	 * @generatedBy CodePro at 06/12/12 18:23
	 */
	@Before
	public void setUp() throws Exception {
		// add additional set up code here
	}

	/**
	 * Perform post-test clean-up.
	 * 
	 * @throws Exception
	 *             if the clean-up fails for some reason
	 * 
	 * @generatedBy CodePro at 06/12/12 18:23
	 */
	@After
	public void tearDown() throws Exception {
		// Add additional tear down code here
	}

	/**
	 * Launch the test.
	 * 
	 * @param args
	 *            the command line arguments
	 * 
	 * @generatedBy CodePro at 06/12/12 18:23
	 */
	public static void main(final String[] args) {
		new org.junit.runner.JUnitCore().run(PositionTest.class);
	}
}