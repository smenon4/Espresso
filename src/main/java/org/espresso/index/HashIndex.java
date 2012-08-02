package org.espresso.index;

/**
 * Index that uses a hash function to determine which bucket to place an element. Since hash number
 * do not have the notion of ordering, this index does not support less than and greater than
 * operations.
 *
 * @author <a href="mailto:antenangeli@yahoo.com">Alberto Antenangeli</a>
 */
public class HashIndex<T, V>
        extends Index<T, V> {


    public static <T, V> Index<T, V> newIndex(final Class<V> type, final String name,
            final Getter<T, V> column, final int totalBuckets) {
        return new HashIndex<T, V>(type, name, column, totalBuckets);
    }

    public static <T, V> Index<T, V> newIndex(final Class<V> type, final String name,
            final Getter<T, V> column) {
        return new HashIndex<T, V>(type, name, column);
    }

    private HashIndex(final Class<V> type, final String name, final Getter<T, V> column,
            final int totalBuckets) {
        super(type, name, column, totalBuckets);
    }

    private HashIndex(final Class<V> type, final String name, final Getter<T, V> column) {
        super(type, name, column);
    }

    @Override
    public Index<T, V> newIndex() {
        return newIndex(getIndexType(), getName(), column, totalBuckets);
    }

    @Override
    public Index<T, V> lessThan(final Object what) {
        return null;
    }

    @Override
    public Index<T, V> greaterThan(final Object what) {
        return null;
    }

    @Override
    protected int whichBucket(final Object object) {
        return Math.abs(object.hashCode() % totalBuckets);
    }
}
