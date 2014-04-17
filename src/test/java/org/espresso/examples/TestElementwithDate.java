package org.espresso.examples;

import static org.espresso.extension.AmericanDateExtension.AMERICAN_DATE_EXTENSION;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.espresso.SqlEngine;
import org.espresso.index.Getter;
import org.espresso.index.HashIndex;
import org.espresso.index.Indices;


public class TestElementwithDate {
    private final String name;
    private final int age;
    private final Date some_date;
    final static int STORE_SIZE = 1000;

    public TestElementwithDate(final String name, final int age, final Date some_date) {
        this.name = name;
        this.age = age;
        this.some_date = some_date;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
    
    public Date getsomedate() {
		return some_date;
	}

    public static void main(final String... args) throws SQLException, ParseException {
        final List<TestElementwithDate> store = new ArrayList<TestElementwithDate>(STORE_SIZE);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date testDate = sdf.parse("20140110"); 
        final Indices<TestElementwithDate> indices = new Indices<TestElementwithDate>(HashIndex.newIndex(String.class, "name",
                new Getter<TestElementwithDate, String>() {
            @Override
            public String get(final TestElementwithDate object) {
                return object.getName();
            }
        }));
        for (int i = 0; i < STORE_SIZE; i++) {
            final TestElementwithDate element = new TestElementwithDate("Name_" + (i % 1000), i % 100,testDate);
            store.add(element);
            indices.addToIndices(element);
        }



        long start = System.currentTimeMillis();
        SqlEngine<TestElementwithDate> engine = new SqlEngine<TestElementwithDate>(TestElementwithDate.class,
                "select * from TestElement where name = 'Name_0' and some_date='20140110';", AMERICAN_DATE_EXTENSION);
        List<TestElementwithDate> result = engine.execute(store.iterator(), indices);
        long end = System.currentTimeMillis();
        System.out.println("Returned " + result.size() + " in " + (end - start) + "ms.");


    }
}