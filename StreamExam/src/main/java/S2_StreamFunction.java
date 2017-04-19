import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by jianganlan on 2017/4/16.
 */
public class S2_StreamFunction {
    public static void main(String[] args) {
        /**
         * 流操作分为中间操作和终止操作
         * 只有在执行终止操作是，数据才被spliterator进行遍历
         *
         */
        Stream<String> stream = Arrays.asList("jal", "qp", "a", "b", "c", "d", "e")
                .stream()
//                .filter("jal"::equals)
                .map(Function.identity())
                .distinct()
                .peek(System.out::println)
                .skip(10)
                .limit(100)
                .parallel();

//        stream.forEach(System.out::println);
        //迭代 reduce
        //第一个参数是初始的数据，第二个参数传入迭代方法，返回类型和初始类型一致，返回Stream中的类型
//        stream.reduce("", (l, r) -> l.concat(r));

        //不传入初始值，返回结果为Optional包装的数据，当没有数据时候，返回Optional.empty() 返回的就是Stream中的类型
//        stream.reduce((l, r) -> l.concat(r));


        //三个参数的时候，第一个初始值identity，第二个迭代结果 accumulator，第三个表示合并操作 combiner
        //必须保证仍以U  combiner.apply(identity,U)相等
        //保证 accumulator.apply(u,t)==combiner.apply(accumulator.apply(identity,t),u)
        //防止并行计算时候出错
        //可以返回和Stream中不一样的类型
//        stream.reduce("", (l, r) -> l.concat(r), (l, r) -> l.concat(r));

        //坑：
        List<String> collect = stream.reduce(new ArrayList<String>(),
                (l, r) -> {
                    l.add(r);
                    return l;
                },
                (l, r) -> {
                    l.addAll(r);
                    return l;
                });
        System.out.println(collect.size());


        //collect
        //collect是针对java可变类型的一种迭代，使用collect的方式对变对象效率更高
        /**
         * 主要有3个参数
         * supplier 提供一个变量box
         * accumulator<Supplier,T> 返回Supplier类型  Consumer
         * combiner<Supplier,Suppier> 返回Supplier类型 Consumer
         */
        //元素添加到list中


//         stream.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);



        //同时collect中提供了许多便捷的数据操作方法
        /**
         * collctor中有4个参数
         */
        Collectors.counting();//计数
        Collectors.averagingDouble(String::length);//转化double求平均值
        Collectors.joining();//拼接
        Collectors.joining("_");
        Collectors.summingInt(String::length);//转化为int相加
        Collectors.summarizingInt(String::length);//转化为int得到汇总信息
        Collectors.toList();
        Collectors.toSet();
        Collectors.toMap(t->t.toString(),t->t.toString());
        Collectors.mapping(t->"jal",Collectors.toList());
        Collectors.groupingBy(t->t.toString());
        Collectors.groupingBy(t->t.toString(),Collectors.toList());
        Collectors.groupingBy(t->t.toString(),Collectors.mapping(t->t.getClass(),Collectors.counting()));
        Collectors.groupingBy(t->t.toString(),Collectors.groupingBy(t->t.toString()));
        Collectors.partitioningBy(t->t.equals("jal"));
        Collectors.reducing((x,y)->x.toString()+y.toString());

        stream.collect(Collectors.toList());
    }
}
