package Reports;

@FunctionalInterface
public interface ProductSearcher<T, Product> {

    boolean findMatch(T t, Product p);
}

