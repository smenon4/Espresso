package org.espresso.token;

import org.espresso.FunctionExtension;
import org.espresso.SqlNodeVisitor;
import org.espresso.eval.NumberWrapper;

import java.sql.SQLException;
import java.util.Map;

/**
 * Represents a number, which could be a long or a double.
 *
 * @author <a href="mailto:Alberto.Antenangeli@tbd.com">Alberto Antenangeli</a>
 */
public class SqlNumber<E> implements SqlExpressionNode<E> {
    private final NumberWrapper number;

    /**
     * Builds the Number given a string representation
     * @param number the string representation of the number, never null
     * @throws IllegalArgumentException if a null is supplied
     */
    public SqlNumber(final String number) {
        if (null == number)
            throw new IllegalArgumentException("SqlNumber: null is not a valid argument");
        if (number.contains("."))
            this.number = new NumberWrapper(Double.parseDouble(number));
        else
            this.number = new NumberWrapper(Long.parseLong(number));
    }

    public NumberWrapper getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return number.toString();
    }

    /**
     * Returns the number, already stored as a BigDecimal
     * @param row Object from where to get the data (similar to a DB row)
     * @param functions Function extensions, passed down the expression tree
     * @return the number
     * @throws SQLException never happens in this case
     */
    @Override
    public Object eval(final E row, final Map<String, FunctionExtension> functions) throws SQLException {
        return number;
    }

    /**
     * Accept method for the visitor pattern, turn around and call visit on the visitor.
     * Pretty standard, nothing new here...
     *
     * @param visitor the visitor to this class
     */
    @Override
    public void accept(final SqlNodeVisitor<E> visitor) throws SQLException {
        visitor.visit(this);
    }
}
