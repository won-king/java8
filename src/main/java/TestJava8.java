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
        //List<Integer> lengths=map(strings, (String s)->s.length());

        //为什么这里可以直接写成 String::length，因为到这里，Function的第一个泛型参数已经可以确定是String类型的了
        //所以可以确定 apply(T t)的参数一定是一个String对象，那么接下来就只需要知道调用这个对象的哪个方法了
        //所以我们只需要告诉编译器，我将要调用这个类的方法名就行了，编译器会自动替换为对该方法的调用
        //所以Java就在语言层面上加了这样一个语法糖
        //需要注意的是，这种写法只适用于被调用的方法没有参数
        List<Integer> lengths=map(strings, String::length);
        //如果想调用一个带参数的方法，就得这么写
        //那为什么不能用上面的语法糖，写成，String::chatAt(0)这种
        //因为这种语法糖的格式就只是: Type::method 这种，并不包含括号以及里面的参数，更深层次的原因，可能是技术上实现比较困难
        map(strings, (String s)->s.charAt(0));

        List<byte[]> list1=map(strings, String::getBytes);

        System.out.println(lengths);
        //这里是这样理解的，这里的sort接收一个Comparator
        //所以我们需要创建一个Comparator对象，问题在于，流式对象，实现的都是Comparable接口
        //那么现在的问题就变成了，如何将Comparable变成Comparator
        //Comparator是接收两个参数t1, t2(这两个参数是可比较的)，Comparable接收一个参数，所以只要给定类型，实现了Comparable接口即可
        //Function的作用是，将t1,t2变成两个Comparable，然后调用t1.compareTo(t2)即可

        //在分析这种调用时，应该用左结合的方式来看，从左到右，就像剥洋葱一样，从外面一层一层拨开，从内层看比较难理解
        lengths.sort(comparing(Integer::intValue));

        List<UserSon> users=new ArrayList<>();
        //解释为什么Function<? super T, ? extends U>
        //因为可以用父类引用指向子类对象，所以List<UserSon>可以转换成List<User>
        //但是还有第二个限制，就是父类中的某个方法的返回值，是U的子类，具体到这个例子，就是myself方法的返回值实现了Comparable接口
        //Comparable接口的实现，还有个泛型，就是实现它的类，只能跟它或它的父类比较
        //下面来解释，为什么实现它的接口只能跟它或它的父类比较
        //就像现在这种情况，如果T被确定了是User类型，那么，User必然实现了Comparable接口，
        // 而这个Comparable接口的泛型只能是它本身或他的父类，因为他自己都没出生，怎么跟儿子比呢，而既然他出生了，肯定他的父亲也是确定存在的
        //其实最终的目的只有一个，就是保证，运行时，所有类型都是确定的
        users.sort(comparing(User::myself));
        //users.sort(comparing());

        lengths.sort((i1,i2)-> i1.compareTo(i2));
        System.out.println(lengths);
        //从这里看，lambda表达式就是接口的一个实现类的最核心的代码实现，并且此时，他们的泛型也是确定的
        //这里s1, s2是形参名，无法确定类型，所以这里他们运行时的类型，其实是根据接收值的类型来推断的
        Comparator<String> c1=(s1,s2)->s1.compareTo(s2);
        Comparator<String> c2=(s1,s2)->s1.charAt(0)-s2.charAt(0);
    }

    //一个类或方法声明的泛型，其实最终都是根据运行时参数推断出来的
    // 注意，这里一定是根据参数推断出来的，而不是返回值的接收对象
    // 所以要理解lambda表达式，就要从参数推断来理解起，很多函数式的高阶玩法，你之所以看不懂参数为什么要这么传，就是因为不会进行参数推断
    // 如下的一个例子，第一个参数是List<String>，就可以推断出T是String类型，进而可以推出，Function的第一个泛型参数也是String类型
    // 那么返回值的泛型R，就取决于String类中的某个方法的返回值类型，这个R类型可以灵活变化，见上传入 String::getBytes 的例子
    public static <T,R> List<R> map(List<T> list, Function<T,R> f){
        List<R> result=new ArrayList<>(list.size());
        for(T t:list){
            result.add(f.apply(t));
        }
        return result;
    }
    //根据以上分析，泛型参数中的S，在参数中没有出现过，仅根据返回类型，是推断不出来其运行时类型的
    //所以这个S没有任何意义，里面创建的List<S>类的数组也什么都存不进去
    public static <T,R,S> List<S> map1(List<T> list, Function<T,R> f){
        List<S> result=new ArrayList<>(list.size());
        result.add(null);
        //这里0存不进去
        //result.add(0);
        //强转为S类型也不行，因为强转的类型必须已知，而这里S是无法确定的
        //result.add((S)0);
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
