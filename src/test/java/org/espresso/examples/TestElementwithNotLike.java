package org.espresso.examples;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.espresso.SqlEngine;
import org.espresso.index.Getter;
import org.espresso.index.HashIndex;
import org.espresso.index.Indices;


public class TestElementwithNotLike {
    private final String name;
    private final int age;
    private final String type;
    private final Character some_char;
    final static int STORE_SIZE = 1000;

    public TestElementwithNotLike(final String name, final int age, final Character some_char, final String type) {
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
        final List<TestElementwithNotLike> store = new ArrayList<TestElementwithNotLike>(STORE_SIZE);
        final Indices<TestElementwithNotLike> indices = new Indices<TestElementwithNotLike>(HashIndex.newIndex(String.class, "name",
                new Getter<TestElementwithNotLike, String>() {
            @Override
            public String get(final TestElementwithNotLike object) {
                return object.getName();
            }
        }));
        for (int i = 0; i < STORE_SIZE; i++) {
            final TestElementwithNotLike element = new TestElementwithNotLike("Name_" + (i % 1000), i % 100,'B', "AB");
            store.add(element);
            indices.addToIndices(element);
        }
        final TestElementwithNotLike element1 = new TestElementwithNotLike("Name_0", 0,'B', "DE");
        store.add(element1);
        indices.addToIndices(element1);



        long start = System.currentTimeMillis();
        SqlEngine<TestElementwithNotLike> engine = new SqlEngine<TestElementwithNotLike>(TestElementwithNotLike.class,
                "select * from TestElement where name = 'Name_0' and (type like ('A%')) and some_char='A';");
        List<TestElementwithNotLike> result = engine.execute(store.iterator(), indices);
        long end = System.currentTimeMillis();
        System.out.println("Returned " + result.size() + " in " + (end - start) + "ms.");


    }
}