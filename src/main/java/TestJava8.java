import java.util.ArrayList;
import java.util.Arrays;
import static java.util.Comparator.comparing;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by kewangk on 2018/1/29.
 */
public class TestJava8 {
    public static void main(String[] args) {
        List<Integer> list= Arrays.asList(1,2,3,4,5,6,7);
        List<Integer> result=filterOdd(list, (Integer i)->(i%2==0));
        System.out.println(result);
        new Thread(()-> System.out.println("this is my second Lambda expression")).start();

        List<String> strings=Arrays.asList("hehe","","wonking","memeda");
        List<Integer> lengths=map(strings, (String s)->s.length());
        System.out.println(lengths);
        lengths.sort(comparing(Integer::intValue));
        lengths.sort((i1,i2)-> i1.compareTo(i2));
        System.out.println(lengths);
        Comparator<String> c1=(s1,s2)->s1.compareTo(s2);
        Comparator<String> c2=(s1,s2)->s1.charAt(0)-s2.charAt(0);
    }

    public static <T,R> List<R> map(List<T> list, Function<T,R> f){
        List<R> result=new ArrayList<R>(list.size());
        for(T t:list){
            result.add(f.apply(t));
        }
        return result;
    }

    public static List<Integer> filterOdd(List<Integer> list, Predicate<Integer> p){
        List<Integer> result=new ArrayList<>();
        for(Integer i: list){
            if(p.test(i)){
                result.add(i);
            }
        }
        return result;
    }
}
