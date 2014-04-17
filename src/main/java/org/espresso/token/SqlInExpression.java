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

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Representation of a SQL "in" predicate - column IN (option1, option2, ...)
 *
 * @author <a href="mailto:antenangeli@yahoo.com">Alberto Antenangeli</a>
 * 
 *  * ### Change History ###
 * 
 * @author      <a href="mailto:santosh.menon1@gmail.com">Santosh Menon</a>
 * @version     1.0.3  
 * @since       2014-04-10
 * 
 * -- Added support for NOT IN  operator
 * 
 */
public class SqlInExpression<E>
        extends SqlExpression<E> {
    private final SqlColumn column;
    private final boolean isNot;
    private Set inList = null;
    public final static String IN_OPERATOR = "IN";
    public final static String NOT_IN_OPERATOR = "NOT IN";
    private String operator;

    /**
     * Builds an in expression
     *
     * @param column the column that drives the predicate, never null
     *
     * @throws IllegalArgumentException if a null column is supplied
     */
    public SqlInExpression(final SqlColumn column,final boolean isNot) {
        if (null == column)
            throw new IllegalArgumentException("In: column cannot be null");
        this.column = column;
        this.isNot = isNot;
        this.operator = isNot ? NOT_IN_OPERATOR : IN_OPERATOR ;
    }

    /**
     * Accessor to the column object
     *
     * @return the column object
     */
    public SqlColumn getColumn() {
        return column;
    }

    @Override
    public String getOperator() {
        return operator;
    }

    /**
     * Returns the in predicate as (column IN (option1, option2, ...))
     *
     * @return the string representation of the in predicate
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("(");
        builder.append(column.toString()).append(" " + operator + " (");
        final Iterator<SqlExpressionNode> iterator = operands.iterator();
        if (iterator.hasNext())
            builder.append(iterator.next().toString());
        while (iterator.hasNext())
            builder.append(", ").append(iterator.next().toString());
        builder.append("))");
        return builder.toString();
    }

    /**
     * Evaluates the current column and check if it belongs to the supplied in list.
     * Note that the operands from the base class are stored as a list; we convert
     * the list to a set the first time eval is called to speed up the check.
     *
     * @param row Reference to the current object
     * @param functions Function extensions, passed down the expression tree
     * @return true or false whether the value of the current column belongs to the in list
     * @throws SQLException wraps any kind of error that may occur
     */
    @Override
    public Object eval(final E row, final Map<String, FunctionExtension> functions) throws SQLException {
        if (null == inList) {
            // In list initialized a la singleton style, the first time we access it.
            inList = new HashSet(operands.size());
            for (SqlExpressionNode element : operands)
                inList.add(element.eval(row, functions));
        }
        boolean evalResult = inList.contains(column.eval(row, functions));
		return isNot ? !evalResult : evalResult;
    }


    /**
     * Accept method for the visitor pattern. Call pre-, then visit, then post-
     * to give the visitor a chance to push/pop state associated with recursion.
     * @param visitor the visitor to this class.
     */
    @Override
    public void accept(final SqlNodeVisitor<E> visitor) throws SQLException {
        visitor.visit(this);
    }
}
