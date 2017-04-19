import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by jianganlan on 2017/4/16.
 */
public class S4_Pipleline {
    public static void main(String[] args) {
        //Stream这个概念侧重于 数据的流动形式，来源和一系列结果
        //Pipleline 数据流所处的地方，数据流动场所

        //Stream->ReferencePipleline
        //IntStream->IntPipleline
        //DoubleStream->DoublePipleline


        //ReferencePipleline->AbstractPipleline->PiplelineHelper 集成路线

        /**
         * field :
         *      sourceStage, head-PipleLine
         *      previousStage,上一个pipleLine
         *      nextStage,下一个pipleLine
         *      depth,当前的深度
         *      sourceOrOpFlags, 数据源flag或者当前操作的flag
         *      combinedFlags, 当前pipleline阶段的 连接flag
         *
         *      sourceSpliterator,数据源的spileterator（头部有效）
         *      sourceSupplier,数据源的spileterator提供者（头部有效）
         *
         *      linkedOrConsumed 数据是否被消费
         *
         *      parallel pipeline是否是并行的
         *
         *
         *
         *  操作流程：
         *      1.数据创建->传入Spliterator或者Spliterator的Supplier
         *      2.数据的瞬时操作
         *          ->返回新的Pipeline
         *          PipeLine->[stateless Op ,无状态操作,filter,map
         *                     stateful Op,有状态操作,distinct,sort]
         *
         *         同时返回的新的pipeline中填入prev前一个的pipeline信息
         *          if (previousStage.linkedOrConsumed)
                            throw new IllegalStateException(MSG_STREAM_LINKED);
                            previousStage.linkedOrConsumed = true;
                        previousStage.nextStage = this;
                        this.previousStage = previousStage;
                        this.sourceOrOpFlags = opFlags & StreamOpFlag.OP_MASK;
                        this.combinedFlags = StreamOpFlag.combineOpFlags(opFlags, previousStage.combinedFlags);
                        this.sourceStage = previousStage.sourceStage;
                        if (opIsStateful())
                            sourceStage.sourceAnyStateful = true;
                            this.depth = previousStage.depth + 1;


         *         注意流程的方法的方法：
         *          Sink opWrapSink(int flag,Sink sink)
         *          Sink wrapSink(Sink sink)
         *          copyInto(Sink sink,Spliterator spliterator);
         *
         *          wrapAndCopyInto(Sink sink,Spliterator spliterator)
         *
         *          wrapSpliterator(Spliterator spliterator)
         *
         *          每次新的瞬时操作 主要重载方法
         *              Sink opWrapSink(int flag,Sink sink)
         *
         *          Sink 是Consumer的一次包装，表示当前的pipeline的操作具体内容
         *           begin()->accept()->end()
         *
         *           SourcePipe->filterPipe->MapPipe
         *
         *
         *           进行最终操作的时候，
         *
         *           Pileline  调用evaluate（TerminalOp op）来进行操作
         *
         *           TermimalOp:
         *           evaluateSequential
         *           evaluateParallel
         *
         *           ForEachOp:
         *              helper.wrapAndCopyInto(this, spliterator);
         *                 ->copyInto(wrapSink(Objects.requireNonNull(sink)), spliterator);
         *                 wrapSink:
         *                 {
                                for (AbstractPipeline p=AbstractPipeline.this; p.depth > 0; p=p.previousStage) {
                                    sink = p.opWrapSink(p.previousStage.combinedFlags, sink);
         }
         *                 }
         *
         *                 copyInto:
         *                 {
         *                              wrappedSink.begin(spliterator.getExactSizeIfKnown());
                                        spliterator.forEachRemaining(wrappedSink);
                                        wrappedSink.end();
         *                 }
         *
         *                 我们可以看到最终的操作是在这2个步骤进行的
         *
         *                 1.先通过最终pipeline中的prevStage和depth 来往前 得到之前的OpSnk方法
         *                 从源头的op开始，对每一次操作的 Op进行包装
         *                 类似这种：多层嵌套的形式
         *                  forEach.apply(map.apply(filter.apply(t)))
         *
         *                  然后通过spliterator 的 forEachRemaining 来遍历
         *
         *                  这是串行的方式
         *
         *                  看看并行的方式
         *                  new ForEachTask<>(helper, spliterator, helper.wrapSink(this)).invoke();
         *
         *                  并行的方式，主要采用的JDK1.7中 ForkJoin 并行计算的框架开计算
         *
         *                  在普通ForkJoinTask的基础上实现了 CountedCompleter
         *                  这个类似于 Future + CountDown 的形式，能保证异步任务在全部完成之前阻塞
         *
         *                  FJ框架，使用了分治算法的思想，将任务分为 树状的小任务结构
         *
         *                  同时配合FJPool 的工作窃取算法，可以在很小的线程池开销的基础上完成并行任务
         *
         *
         *                  具体的FJ的实现和操作下次再议。
         *
         *
         *
         *
         *
         *
         */


        StreamSupport.stream(Spliterators.emptySpliterator(),false);

        //new ReferencePipeline.Head<>(spliterator,
          //      StreamOpFlag.fromCharacteristics(spliterator),
            //    parallel);



    }
}
