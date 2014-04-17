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

import java.util.Date;

/**
 * Simplified enriched deal, so we can test queries that closely match the deal cache without
 * introducing a dependency on the deal cache module.
 *
 * @author <a href="mailto:antenangeli@yahoo.com">Alberto Antenangeli</a>
 */
public class TestDeal
        implements Cloneable {
    private String deal_number;
    private int child;
    private int parent;
    private String label;
    private String book;
    private Date deal_date;
    private Date maturity_date;
    private char maturity_type;
    private String deal_type;
    private String database_name;
    private String database_server;

    public String getDatabaseServer() {
        return database_server;
    }

    public void setDatabaseServer(final String database_server) {
        this.database_server = database_server;
    }

    public String getDatabaseName() {
        return database_name;
    }

    public void setDatabaseName(final String database_name) {
        this.database_name = database_name;
    }

    public String getDealType() {
        return deal_type;
    }

    public void setDealType(final String deal_type) {
        this.deal_type = deal_type;
    }

    public String getDealNumber() {
        return deal_number;
    }

    public void setDealNumber(final String deal_number) {
        this.deal_number = deal_number;
    }

    public int getChild() {
        return child;
    }

    public void setChild(final int child) {
        this.child = child;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(final int parent) {
        this.parent = parent;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public String getBook() {
        return book;
    }

    public void setBook(final String book) {
        this.book = book;
    }

    public Date getDealDate() {
        return deal_date;
    }

    public void setDealDate(final Date deal_date) {
        this.deal_date = deal_date;
    }

    public Date getMaturityDate() {
        return maturity_date;
    }

    public void setMaturityDate(final Date maturity_date) {
        this.maturity_date = maturity_date;
    }

    @Override
    public final TestDeal clone() {
        try {
            return TestDeal.class.cast(super.clone());
        } catch (final CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    public char getMaturityType() {
        return maturity_type;
    }

    public void setMaturityType(final char maturity_type) {
        this.maturity_type = maturity_type;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (!(o instanceof TestDeal))
            return false;

        final TestDeal that = (TestDeal) o;

        if (child != that.child)
            return false;
        if (!database_name.equals(that.database_name))
            return false;
        return deal_number.equals(that.deal_number) && label.equals(that.label);

    }

    @Override
    public int hashCode() {
        int result = deal_number.hashCode();
        result = 31 * result + child;
        result = 31 * result + label.hashCode();
        result = 31 * result + database_name.hashCode();
        return result;
    }
}
