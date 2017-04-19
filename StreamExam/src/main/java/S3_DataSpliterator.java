import java.util.*;
import java.util.function.Consumer;
import java.util.stream.BaseStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by jianganlan on 2017/4/16.
 */
public class S3_DataSpliterator {
    public static void main(String[] args) {
        BaseStream<String, Stream<String>> baseStream = Stream.of("jal");
        /**
         * baseStream元素
         * 1.转化成迭代器模式
         * 2.转化成spliterator模式-Spliterator和iterator类似，用来元素的遍历和分类，分类和划分。
         */
        baseStream.iterator();

        /**
         *
         */
        Spliterator<String> spliterator = baseStream.spliterator();

        //尝试元素执行一定操作，返回值为true，false
        spliterator.tryAdvance(System.out::println);
        //为spliterator中所有元素执行操作
        spliterator.forEachRemaining(System.out::println);
        //将spliterator进行划分，划分之后本身改变，同时产生新的spliterator
        Spliterator<String> trySplit = spliterator.trySplit();
        //返回spliterator数据源的一些特征,int标识
        /**
         * 1.ORDERED
         * 2.DISTINCT
         * 3.SORTED
         * 4.SIZED
         * 5.NONNULL
         * 6.IMMUTABLE
         * 7.CONCURRENT
         * 8.SUBSIZED
         */
        spliterator.characteristics();
        spliterator.hasCharacteristics(Spliterator.DISTINCT);
        spliterator.getComparator();

        /**
         * 创建spliterators
         * 1.使用jdk中提供Spliterators中提供的一些便捷的创建方法
         * 2.Arrays，List,Set中实现了自己的Spliterator
         */
        //空的spliterators,使用static的静态对象
        Spliterators.emptyDoubleSpliterator();
        Spliterators.emptyIntSpliterator();
        Spliterator<String> stringSpliterator = Spliterators.<String>emptySpliterator();


        /**
         * 为已知的spliterator添加特征，或者指定数组形式部分元素
         * 使用数组，元素数组下标访问，按照下标顺序进行访问
         * 拆分spliterator时使用二分法，进行划分
         *
         */
        Spliterators.spliterator(new String[]{"jal"}, Spliterator.CONCURRENT);

        //抽象的Spliterator类，可以继承
        new Spliterators.AbstractSpliterator<String>(Long.MAX_VALUE, Spliterator.DISTINCT) {
            @Override
            public boolean tryAdvance(Consumer<? super String> action) {
                return false;
            }
        };
        //Arrays中添加了Characteristics-ORDERED,IMMUTABLE
        Spliterator<String> arraySpliterator = Arrays.spliterator(new String[]{"jal"});

        //注意到ArrayList中spliterator中没有IMMUTABLE这个特征，自已重新实现,同时实现了懒加载，保证数据只有在最终操作的时候确定大小，
        //同时每次的迭代遍历的时候，使用quick-fail机制，modCount来快速检测并发错误
        new ArrayList<String>().spliterator();
        new HashSet<String>().spliterator();


        //SreamSupport中写着推荐库开发者使用，传入spliterator或者提供Spliterator的Supplier方法
        Stream<String> stream = StreamSupport.stream(spliterator, false);

        StreamSupport.stream(() -> spliterator, Spliterator.DISTINCT, false);


    }
}
