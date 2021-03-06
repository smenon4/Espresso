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
package org.espresso;

import org.espresso.grammar.SqlGrammarLexer;
import org.espresso.grammar.SqlGrammarParser;
import org.espresso.token.SqlStatement;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;

/**
 * Convenient interface to a SQL Parser. This class holds no state - it simply parses a string
 * representing a SQL statement and returns the corresponding tree, throwing a {@code SQLException}
 * in case of errors.
 *
 * @author <a href="mailto:antenangeli@yahoo.com">Alberto Antenangeli</a>
 */
public final class SqlParser {
    /**
     * Parses the given SQL Statement
     *
     * @param sqlStatement The statement to be parsed
     *
     * @return the tree representing the SQL statement
     *
     * @throws java.sql.SQLException in case of parsing errors
     */
    public static SqlStatement parse(final String sqlStatement)
            throws SQLException {
        final ANTLRStringStream in = new ANTLRStringStream(sqlStatement);
        final SqlGrammarLexer lexer = new SqlGrammarLexer(in);
        final CommonTokenStream tokens = new CommonTokenStream(lexer);
        final SqlGrammarParser parser = new SqlGrammarParser(tokens);
        try {
            return parser.eval();
        } catch (final RecognitionException e) {
            throw new SQLSyntaxErrorException(e);
        }
    }

    private SqlParser() {
    }
}
