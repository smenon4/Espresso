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
import java.util.Map;

/**
 * Represents a string that is part of a where clause
 *
 * @author <a href="mailto:antenangeli@yahoo.com">Alberto Antenangeli</a>
 */
public class SqlString<E> implements SqlExpressionNode<E> {
    private final String string;
    private final String noQuotes;

    /**
     * Builds the string representation
     * @param string the string, never null
     * @throws IllegalArgumentException if a null is supplied
     */
    public SqlString(final String string) {
        if (null == string)
            throw new IllegalArgumentException("SqlString: cannot be null");
        this.string = string;
        this.noQuotes = string.substring(1, string.length()-1);
    }

    /**
     * Accessor to the string object
     * @return the string
     */
    public String getString() {
        return noQuotes;
    }

    @Override
    public String toString() {
        return string;
    }

    /**
     * Returns the current string without the quotes
     * @param row Object from where to get the data (similar to a DB row)
     * @param functions Function extensions, passed down the expression tree
     * @return the string with no quotes
     * @throws SQLException
     */
    @Override
    public Object eval(final E row, final Map<String, FunctionExtension> functions) throws SQLException {
        return noQuotes;
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
