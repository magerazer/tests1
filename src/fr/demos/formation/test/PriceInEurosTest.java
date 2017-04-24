package fr.demos.formation.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.demos.formation.PriceInEuros;
import fr.demos.formation.PriceInEuros.PriceOperationException;

public class PriceInEurosTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

	@Test
	public void testprixInEurosString_arrondi() {
		PriceInEuros prix = new PriceInEuros("99.999");
		Assert.assertEquals("anomalie arrondi", "100.00", prix.toString());
	}

	
	@Test
	public void testAddition() {
		PriceInEuros prix1 = new PriceInEuros("13");
		PriceInEuros prix2 = new PriceInEuros("13.555");
		Assert.assertEquals("addition", "26.56", prix1.add(prix2).toString());
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testPrixEnEuroString_negatif() {
		PriceInEuros prix = new PriceInEuros("-1");
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testSoustraction() throws PriceOperationException {
		PriceInEuros prix1 = new PriceInEuros("14");
		PriceInEuros prix2 = new PriceInEuros("13");
		Assert.assertEquals("soustraction", "1.00", prix1.subtract(prix2).toString());
	}
	
	
	
}
