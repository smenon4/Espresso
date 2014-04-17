/*
 * Copyright 2012 Espresso Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.espresso.token;

import org.espresso.FunctionExtension;
import org.espresso.SqlNodeVisitor;
import org.espresso.eval.SqlComparisonEvaluator;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

/**
 * Represents the possible comparisons (less than, greater than, etc.)
 *
 * @author <a href="mailto:antenangeli@yahoo.com">Alberto Antenangeli</a>
 * 
 * ### Change History ###
 * 
* @author      <a href="mailto:santosh.menon1@gmail.com">Santosh Menon</a>
 * @version     1.0.3  
 * @since       2014-04-10
 * 
 * Added functionality to convert date in different formats. 
 * Convert expression to SqlDate<E> if left expression is Date and right expression is String
 * 
 * 
 */
public class SqlComparisonExpression<E> extends SqlExpression<E> {
    private final SqlComparisonOperator operator;
    private SqlComparisonEvaluator evaluator; // Memoize how to evaluate the comparison
    /**
     */
    public SqlComparisonExpression(final SqlComparisonOperator operator) {
        this.operator = operator;
    }

    public SqlComparisonOperator getRawOperator() {
        return operator;
    }

    @Override
    public String getOperator() {
        return operator.toString();
    }

    public Object eval(final E row, final Map<String, FunctionExtension> functions) throws SQLException {
        try {
            final Object left = operands.get(0).eval(row, functions);
            final Object right = rightExpression(row, functions, left);
            if (null == evaluator)
                evaluator = SqlComparisonEvaluator.pickEvaluator(left, right);
            return operator.eval(evaluator.compare(left, right));
        } catch (ClassCastException e) {
            throw new SQLException("At least one comparison side was not a comparable");
        } catch (final IndexOutOfBoundsException e) {
            throw new SQLException("Comparison requires two operators");
        }
    }

    /**
     * Accept method for the visitor pattern, turn around and call visit on the visitor.
     * Pretty standard, nothing new here...
     *
     * @param visitor the visitor to this class
     */
    public void accept(final SqlNodeVisitor<E> visitor) throws SQLException {
        visitor.visit(this);
    }
    
    private Object rightExpression(final E row, final Map<String, FunctionExtension> functions, final Object left) throws SQLException{
    	Object right = operands.get(1).eval(row, functions);
    	 if(!(left instanceof String) && right instanceof String){
         	if(left instanceof Date)
         		right = (new SqlDate<E>(right.toString())).eval(row, functions);
         	else if (left instanceof Character){
         		right = (new SqlCharacter<E>(right.toString())).eval(row, functions);
         	}
         }
    	return right;
    }
}
