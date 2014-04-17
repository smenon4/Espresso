package org.espresso.examples;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.espresso.SqlEngine;
import org.espresso.index.Getter;
import org.espresso.index.HashIndex;
import org.espresso.index.Indices;


public class TestElementwithNotIn {
    private final String name;
    private final int age;
    private final String type;
    private final Character some_char;
    final static int STORE_SIZE = 1000;

    public TestElementwithNotIn(final String name, final int age, final Character some_char, final String type) {
        this.name = name;
        this.age = age;
        this.some_char = some_char;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
    
    public Character getSomeChar() {
		return some_char;
	}
    
    public String getType() {
		return type;
	}

	public static void main(final String... args) throws SQLException {
        final List<TestElementwithNotIn> store = new ArrayList<TestElementwithNotIn>(STORE_SIZE);
        final Indices<TestElementwithNotIn> indices = new Indices<TestElementwithNotIn>(HashIndex.newIndex(String.class, "name",
                new Getter<TestElementwithNotIn, String>() {
            @Override
            public String get(final TestElementwithNotIn object) {
                return object.getName();
            }
        }));
        for (int i = 0; i < STORE_SIZE; i++) {
            final TestElementwithNotIn element = new TestElementwithNotIn("Name_" + (i % 1000), i % 100,'A', "AB");
            store.add(element);
            indices.addToIndices(element);
        }
        final TestElementwithNotIn element1 = new TestElementwithNotIn("Name_0", 0,'A', "DE");
        store.add(element1);
        indices.addToIndices(element1);



        long start = System.currentTimeMillis();
        SqlEngine<TestElementwithNotIn> engine = new SqlEngine<TestElementwithNotIn>(TestElementwithNotIn.class,
                "select * from TestElement where name = 'Name_0' and type not in ('DE');");
        List<TestElementwithNotIn> result = engine.execute(store.iterator(), indices);
        long end = System.currentTimeMillis();
        System.out.println("Returned " + result.size() + " in " + (end - start) + "ms.");


    }
}