package fr.demos.formation;


import java.io.Serializable;
import java.math.BigDecimal;
import static java.math.RoundingMode.* ;
//import java.util.Currency;

/**
 * A simplified "Value" object representing a price.
 * Normally a price will be based on a currency but here the code is simplified
 * and we suppose that we use a single currency with default fraction digit (scale) of 2.
 *<BR>
 * See BigDecimal as to why float/double values are a problem.
 * <P>
 * Important points to remember: 
 * <UL>
 * <LI> why "value" objects are immutable ;
 * <LI> why some operations throw runtime exceptions and other checked exceptions.
 * </UL>
 */
public final class PriceInEuros   implements Comparable<PriceInEuros>, Serializable {
    // see later explanation about serialization
	private static final long serialVersionUID = 4305698571315163901L;
	
	private final BigDecimal value ;
    /* static Currency currency = Currency.getInstance(Locale.getDefault());
     * but a Price class base on Currency is much more complex to write
     * (for instance comparison is only Prices with the same currency)
     */
    private int scale = 2 ; // bogus default value
    public static final PriceInEuros ZERO = new PriceInEuros("0");
    //////////////////////////////////////////////////////////////
    // INNER CLASSES
    //////////////////////////////////////////////////////////////

    /**
     * A checked exception that reports an operation that cannot be performed.
     * note : strong "coupling" with Price class: should be static inner class
     * Price.OperationException. This exception designed as a "data object" with no accessor/mutator
     * (that means that since members are part of the A.P.I then no modifications
     * are to be allowed on future versions of this class).
     */
    
    public static class PriceOperationException extends java.lang.Exception implements Serializable{
		private static final long serialVersionUID = -2302188998699361112L;
		
		public final String operation ;
        public final PriceInEuros op1 ;
        public final PriceInEuros op2 ;
        
        public PriceOperationException(String op, PriceInEuros op1, PriceInEuros op2){
            this.operation = op ;
            this.op1 = op1 ;
            this.op2 = op2 ;
        }
        
        public String toString() {
            return "operation: " + this.operation + " : for " + op1 + " : with : "
                   + this.op2 ;
        }
        
    
    }
    
    /////////////////////////////////////////////////////////////// 
    // CONSTRUCTORS
    /////////////////////////////////////////////////////////////////
    /**
     * Creates a Price from a BigDecimal.
     * Checks wether it is negative and scale it to default (using HALF_EVEN).
     * @exception IllegalArgumentException if argument a negative value 
     */
    public PriceInEuros(BigDecimal val) throws IllegalArgumentException  {
        if( -1 == val.signum()){
            throw new IllegalArgumentException("negative: " + val) ;
        }
        // scale to change
        value = val.setScale(this.getScale(), HALF_EVEN);
    }
    
    /**
     * creates a price from a string (recommended operation).
     * @exception NumberFormatException if the String is not a valid number representation 
     * @exception NullPointerException if null String as argument
     * @exception IllegalArgumentException if the number value is negative
     *
     */
     
    public PriceInEuros(String str) throws NumberFormatException, IllegalArgumentException {
        this(new BigDecimal(str) );
       
    }
    
    
    /////////////////////////////////////////////////////////////
    // METHODS
    ////////////////////////////////////////////////////////////
    /**
     * test for equality of two prices.
     */
    @Override public boolean equals(Object obj){
        return (obj instanceof PriceInEuros) && (((PriceInEuros) obj).value.equals(this.value)) ;
    }
    
    /**
     * note: unlikely that this would be needed, but here to remind about
     * the notions of equals/hashcode compatibility.
     */
    @Override public int hashCode()  {
        return value.hashCode() ;
    }
    
    /**
     * prices are "naturally" ordered.
     */
    public int compareTo(PriceInEuros other) {
        return this.value.compareTo(other.value);
    }
    
    /**
     * note: should be internationalised . (but since we do not use Currency
     * the class is simplified.
     */
    @Override public String toString() {
        return value.toPlainString(); // + " " + money.getSymbol();
    }
    
    /** get the scale of this value.
     * Note : is here fixed to 2 (but should not be in an international context
     * see Currency.
     */
    public int getScale() {
       // return currency.getDefaultFractionDigits();
        return this.scale ;
    }
    
    /** get the value as a BigDecimal.
     */
    public BigDecimal asBigDecimal() {
        return this.value ;
    }
    
    /**
     * "internal" operation that should never fail.
     */
    public PriceInEuros add(PriceInEuros other) {
        return new PriceInEuros(value.add(other.value));
    }
    
    /** 
     * "internal" operation that may fail with a checked exception.
     * note: compare with other operations that throw only runtime exceptions!
     */
    public PriceInEuros subtract(PriceInEuros other) throws PriceOperationException{
        try {
           return  new PriceInEuros(value.subtract(other.value));
        } catch (IllegalArgumentException exc){
            PriceOperationException newExc = new PriceOperationException("subtract", this, other);
            // there should be a proper constructor for PriceOperationException
            // but this is left "as is" for pedagogical purposes
            newExc.initCause(exc) ;
            throw newExc ;
        }
    }
     
    /**
     * "external" operation. 
     */
    public PriceInEuros multiply(BigDecimal arg) throws IllegalArgumentException{
        return new PriceInEuros(this.value.multiply(arg));
    }
    
    /**
     * "external" operation.
     */
    public PriceInEuros multiply(int val) throws IllegalArgumentException, NumberFormatException        {
        return this.multiply(new BigDecimal(val));
    }
}