import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.lang.System.out;

/**
 * Created by kewangk on 2018/1/31.
 */
public class TestStream {
    public static void main(String[] args) {
        List<String> strings = Arrays.asList("hehe", null, "", "wonking", "memeda", "zdd", "fc", "hello", "world", "a", "ab", "abc", "abcd", "wqnmlgb",
                "hallo", "wolrd", "haha", "gaga");
        List<Integer> numbers = Arrays.asList(-3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        int[] sum = numbers.stream()
                .filter(i -> i > 0)
                .collect(() -> new int[]{0}, (a, b) -> a[0] += b, (s1, s2) -> s1[0] += s2[0]);
        System.out.println(sum[0]);
        testFilter(strings);
        testGrouping(strings);
        testMax(strings);
    }

    //测试并行计算与顺序计算的性能
    //很多时候，并行计算并不能提高效率，反而会降低效率，甚至出现错误的结果
    public static void testLongStreamParannal(long n){
        long now=System.currentTimeMillis();
        long result= LongStream.rangeClosed(1,n).parallel()
                .reduce(0L, Long::sum);
        out.println("result->"+result+"  cost time->"+(System.currentTimeMillis()-now));
    }
    public static void testParannal(long n){
        long now=System.currentTimeMillis();
        long[] result=Stream.iterate(0L, i->i+1).limit(n)
                .collect(()->new long[]{0L}, (a,b)->a[0]+=b, (s1,s2)->s1[0]+=s2[0]);
        out.println("result->"+result[0]+"  cost time->"+(System.currentTimeMillis()-now));
    }
    public static void testAccumulate(long n){
        long now=System.currentTimeMillis();
        long sum=0;
        for(int i=0;i<=n;++i){
            sum+=i;
        }
        out.println("result->"+sum+"  cost time->"+(System.currentTimeMillis()-now));
    }

    //生成无限流的方法示例
    public static void testIterate(){
        out.println("-----test iterate-----");
        Stream.iterate(new int[]{0,1}, a->new int[]{a[1], a[0]+a[1]})
                .limit(60)
                .flatMap(a->Stream.of(a[0]))
                .forEach(a->{
                            out.println(a);
                            try {
                                TimeUnit.SECONDS.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                );
    }

    //生成范围流的示例
    public static void testRange(){
        out.println("-----test range-----");
        /*IntStream.rangeClosed(1,20).boxed()
                 .flatMap(a->IntStream.rangeClosed(a,20)
                                      .filter(b->Math.sqrt(a*a+b*b)%1==0)
                                      .mapToObj(b->new int[]{a,b,(int)Math.sqrt(a*a+b*b)})
                 ).forEach(a-> System.out.println(a[0]+","+a[1]+","+a[2]));*/
        //
        IntStream.rangeClosed(1,20).boxed()
                .flatMap(a->IntStream.rangeClosed(a,20)
                        .mapToObj(b->new double[]{a,b,Math.sqrt(a*a+b*b)})
                        .filter(r->r[2]%1==0))
                .forEach(a-> System.out.println(a[0]+","+a[1]+","+a[2]));
    }

    //将单词进行分类过滤，统计文明用语和非文明用语，统计各分组的每个单词的长度
    public static void testPartitionAndCount(List<String> resource){
        out.println("-----test partition and count-----");
        List<Integer> result=resource.parallelStream()
                .filter(Predicate.isEqual(null).negate())
                .filter(s->s.length()>0)
                .sorted(Comparator.comparingInt(String::length))
                .map(String::length)
                .collect(Collectors.toList())
                .stream()
                .map(i->i*i)
                .collect(Collectors.toList());
        System.out.println(result);
        Map<Boolean,List<Integer>> map=result.stream().collect(Collectors.groupingBy(i->i%2==0?Boolean.TRUE:Boolean.FALSE));
        //迭代map的一种方式，注意这里只能用flatMap不能用map
        //用map会为每一条生成一个stream，之间是互不相通的，只能用flatMap把所有stream汇成一个流
        Stream.of(map).flatMap(e->e.entrySet().stream()).forEach(out::println);
    }

    //过滤器测试
    public static void testFilter(List<String> resource){
        out.println("-----test filter-----");
        Map<Integer,List<String>> result=resource.stream()
                .filter(Predicate.isEqual(null).negate())
                //如果要表达非语义的话，不要把布尔表达式包装成Predicate，再调negate，直接用lambda，再用非操作符-!
                //((Predicate<? super String>) String::isEmpty).negate()
                .filter(s->!s.isEmpty())
                .filter(s->!s.toLowerCase().contains("wqnmlgb"))
                .collect(Collectors.groupingBy(String::length));
        //迭代map的一种方式(也是Java8才出现的)
        Stream.of(result).map(e->e.entrySet().stream()).forEach(e->e.forEach(out::println));
        //result.forEach((i,l)->out.println(i+"->"+l));
    }

    //内置Collector测试-max
    public static void testMax(List<String> resource){
        out.println("-----test max-----");
        Optional<String> o=resource.stream()
                            .filter(s->s!=null)
                            .max(Comparator.comparingInt(String::length));
                            //.collect(Collectors.maxBy((s1,s2)->s1.length()-s2.length()));
        out.println(o.isPresent()?o.get():"");
    }

    public static void testGrouping(List<String> resource){
        //按照字符长度分组
        out.println("-----test grouping-----");
        Map<Integer,Map<Character,List<String>>> result=resource.stream()
                                                 .filter(Predicate.isEqual(null).negate())
                                                 .filter(s->!s.isEmpty())
                                                 .collect(Collectors.groupingBy(String::length, Collectors.groupingBy(s->s.charAt(0))));
        result.forEach((i,l)->out.println(i+"->"+l));
    }

    public static void testConcat(List<String> resource){
        //拼接字符串
        out.println("-----test concat-----");
        StringBuilder stringBuilder=resource.stream().collect(StringBuilder::new, (sb,s)->sb.append(s), (s1,s2)->s1.append(s2));
        String string=resource.stream().collect(Collectors.joining("-"));
    }
}
