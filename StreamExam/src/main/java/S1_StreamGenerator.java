import java.util.*;
import java.util.function.Consumer;
import java.util.stream.*;

/**
 * Created by jianganlan on 2017/4/16.
 */
public class S1_StreamGenerator {
    public static void main(String[] args) {


       /*
       * 创建流
       * 1.使用Stream，IntStream..接口提供的一些静态方法
       * 2.Streams 中提供了一些方法
       * */
        Stream<String> stream1 = Stream.of("a", "b");
        Stream<Object> stream2 = Stream.empty();
        Stream<Object> stream3 = Stream.concat(stream1, stream2);
        /**
         * 使用<code>Supplier<T></code>方法来提供 stream中流对象提供
         */
        Stream.generate(() -> "a");

        /**
         * 使用builder模式进行流的创建
         */
        Stream.Builder<Object> builder = Stream.builder();
        builder.accept("a");
        builder.accept("b");
        Stream<Object> stream4 = builder.build();


        IntStream intStream = IntStream.of(1);
        DoubleStream doubleStream = DoubleStream.of(1.2);
        LongStream longStream = LongStream.of(2L);


        IntStream intSteam = Arrays.stream(new int[]{1, 2, 3});
        Stream<String> stream5 = Arrays.stream(new String[]{"jal"});

        //Collection中创建stream
        new ArrayList<String>().stream();
        new HashSet<String>().stream();




    }
}
