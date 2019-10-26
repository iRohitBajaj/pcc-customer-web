package io.pivotal.bookshop;

import java.util.Properties;

/*import org.apache.geode.cache.Declarable;
import org.apache.geode.pdx.PdxReader;
import org.apache.geode.pdx.PdxSerializer;
import org.apache.geode.pdx.PdxWriter;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;

public class MyPdxSerializer implements PdxSerializer, Declarable {
    private final PdxSerializer auto =
            new ReflectionBasedAutoSerializer("io.pivotal.bookshop.domain.*");

    @Override
    public void init(Properties props) {}

    @Override
    public boolean toData(Object o, PdxWriter out) {
        return auto.toData(o, out);
    }

    @Override
    public Object fromData(Class<?> clazz, PdxReader in) {
        return auto.fromData(clazz, in);
    }
}*/
