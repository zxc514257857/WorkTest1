package com.example.case4

import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.LongAdder
import java.util.function.IntBinaryOperator
import kotlin.concurrent.thread

/**
 * 1、lateinit的使用规则，只适用于引用数据类型
 * 2、return作用域问题，只作用在本方法内，不作用于整个应用程序中
 * 3、gradle插件依赖加速
 * 4、AS中的搜索栏，AWR字母筛选
 * 5、AS中git的回滚和暂存功能的使用
 * 6、AS中lib module和application module的功能区别和显示区别
 * 7、List中的remove first和 remove last的功能
 * 8、Android中Pire ， Triple和 Map的区别
 * 9、Android中 Atomic 常量自增功能（变量原子性）
 * 10、Android中 Volatile 修饰的方法，保证变量的原子性
 * 11、Android中 longAdder比AtomicLong的性能会更好一点，也是保证变量的原子性
 * 12、如果Bean中有公用的内容，可以抽出来一个基类Bean，其他的Bean继承这个Bean即可
 * 13、kotlin中的代码简化逻辑研究
 * 14、Android中Shell命令学习
 */
class MainActivity : AppCompatActivity() {

    // lateinit 后面跟的是引用数据类型，跟基本数据类型不能用lateinit
//    lateinit var a: Int
//    lateinit var b: TextView
//    lateinit var c: String

    lateinit var tv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv = findViewById(R.id.tv)

        // 测试return 作用的区域
        // retrun 作用在单个的方法中 而不是作用在整个应用程序中
        testReturn()

        // gradle插件依赖使用阿里云远程依赖替换国外服务器依赖；gradle wrapper使用本地gradle替换，可以增加编译速度

        // as的搜索，注意左侧的下拉箭头；以及搜索时的 A W R字母筛选 ； A表示 大小写匹配  W表示 全字符匹配
        // R表示 正则匹配；前中后位置，大小写字母不一致，字母缺失都可以匹配到（应该是默认的匹配方式）

        // git的 rollback 回滚以及 Stash和UnStash 暂存功能的使用
        // git的新创建分支是在目前现有的分支的基础上创建起来的

        // Android中创建Lib module和application module的区别
        // lib module是需要依赖主module而存在的  application module是每个可以单独存在的
        // application module左下角是一个点  lib module左下角是一本书

        // 测试list的removeFirst 和 removeLast
        val lists = LinkedList<String>()
        lists.add("111")
        lists.add("222")
        lists.add("333")
        lists.add("444")
        val changeEnum = ChangeEnum.DEFAULT
        when (changeEnum) {
            ChangeEnum.REMOVE_LAST -> {
                lists.removeLast()
                // onCreate: 删除最后,,,lists: [111, 222, 333]
                Log.e("TAG", "onCreate: ${changeEnum.value},,,lists: $lists")
            }
            ChangeEnum.REMOVE_FIRST -> {
                lists.removeFirst()
                // onCreate: 删除第一,,,lists: [222, 333, 444]
                Log.e("TAG", "onCreate: ${changeEnum.value},,,lists: $lists")
            }
            else -> {
                // onCreate: 不变,,,lists: [111, 222, 333, 444]
                Log.e("TAG", "onCreate: ${changeEnum.value},,,lists: $lists")
            }
        }

        // android 中的Pair使用
        // Pair 和triple的区别，Map保存的是多个key value，Pair保存的是一个Key value
        val pair = Pair("aaa", "bbb")
        Log.e("TAG", "pair: ${pair.first},,,${pair.second}")  // pair: aaa,,,bbb
        val triple = Triple("111", "222", "333")
        Log.e(
            "TAG",
            "triple: ${triple.first},,,${triple.second},,,${triple.third}"
        )  // triple: 111,,,222,,,333

        // atomic的使用（原子的） 可以解决多线程并发问题
        val atomicBoolean = AtomicBoolean(true)
        // 获取当前的值
        val booleanGet1 = atomicBoolean.get()
        Log.e("TAG", "booleanGet1: $booleanGet1")  // booleanGet1: true
        // set 多线程共享变量的修改对其他线程立即可见
        atomicBoolean.set(false)
        val booleanGet2 = atomicBoolean.get()
        Log.e("TAG", "booleanGet2: $booleanGet2")  // booleanGet2: false
        // lazySet 多线程共享变量的修改对其他线程不会立即可见
        atomicBoolean.lazySet(true)
        // 获取的旧值（返回的旧值），设置的新值
        val booleanPrevGet = atomicBoolean.getAndSet(false)
        Log.e("TAG", "booleanPrevGet: $booleanPrevGet")   // booleanPrevGet: true（旧值为true，新值为false）
        // 原值和现值进行比较，如果比较一致设置新值，不一致则不设置新值
        val booleanCompareSet = atomicBoolean.compareAndSet(true, true)
        // booleanCompareSet: false（旧值是false，期望是true，期望相同返回true，期望不同返回false，目前是不同 返回false）
        // 返回的值是 旧值和期望值是否相同，，，相同返回true，不同返回false
        Log.e("TAG", "booleanCompareSet: $booleanCompareSet")
        // atomicInteger 设置默认值
        val atomicInteger = AtomicInteger(0)
        val integerGet1 = atomicInteger.get()
        Log.e("TAG", "integerGet1: $integerGet1")  // integerGet1: 0
        // 返回旧值（0），新增新值（2）
        val integerGet2 = atomicInteger.getAndAdd(2)
        Log.e("TAG", "integerGet2: $integerGet2")  // integerGet2: 0
        // 验证之前的getAndAdd 指令
        val integerGet3 = atomicInteger.get()
        Log.e("TAG", "integerGet3: $integerGet3")  // integerGet3: 2
        // 先加再返回新值（2+2）
        val integerGet4 = atomicInteger.addAndGet(2)
        Log.e("TAG", "integerGet4: $integerGet4")  // integerGet4: 4
        atomicInteger.set(5)
        val integerGet5 = atomicInteger.get()
        Log.e("TAG", "integerGet5: $integerGet5")  // integerGet5: 5
        // 原值（5）和比较值（5）进行比较，如果相同，则设置期望值（6）并返回 ；；返回值的意思是比较值是否相同，相同返回true，不同返回false
        val compareAndSet = atomicInteger.compareAndSet(5, 6)
        Log.e("TAG", "compareAndSet---: $compareAndSet")  // compareAndSet---: true ;;;更新的新值为6
        // 先获取再加一(返回的是旧值)
        val integerGet6 = atomicInteger.getAndIncrement()
        Log.e("TAG", "integerGet6: $integerGet6")  // integerGet6: 6
        // 检测新值是否加上 -- 加上了1
        val integerGet7 = atomicInteger.get()
        Log.e("TAG", "integerGet7: $integerGet7")  // integerGet7: 7
        // 先加一再获取值
        val integerGet8 = atomicInteger.incrementAndGet()
        Log.e("TAG", "integerGet8: $integerGet8")  // integerGet8: 8
//        atomicInteger.getAndDecrement()   // 先获取再减一
//        atomicInteger.decrementAndGet()   // 先减一再获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // updata 表示的是修改
//            atomicInteger.getAndUpdate(IntUnaryOperator {
//                it / 2
//            })
            // 在传参的时候，省略了 (IntUnaryOperator   ) 就变成了这个样子
            val andUpdate = atomicInteger.getAndUpdate {
                // 返回了原值，并更新了现值为原值除以2
                it / 2
            }
            Log.e("TAG", "andUpdate: $andUpdate")  // andUpdate: 8
            val get = atomicInteger.get()
            Log.e("TAG", "get: $get")  // get: 4
            // 表示的是更新并且获取
//            atomicInteger.updateAndGet()

            // Accumulate 表示的是增加
            val accumulateAndGet =
                // x表示现值 ， pre表示前值 ， now表示现值
                atomicInteger.accumulateAndGet(28, IntBinaryOperator { pre, now ->
                    Log.e("TAG", "pre: $pre,,,now:$now")  // pre: 4,,,now: 28
                })
            // 21   为什么算出来的这个值，目前搞不太懂
            Log.e("TAG", "accumulateAndGet: $accumulateAndGet")
        }

        // 模拟一个累加操作
        val atomicInteger1 = AtomicInteger(1)
        var incrementAndGet = 1
        for (a in 1 until 5) {
            Log.e("TAG", "aaaa: $a")
            incrementAndGet = atomicInteger1.incrementAndGet()
        }
        Log.e("TAG", "incrementAndGet: $incrementAndGet")


        // https://zhuanlan.zhihu.com/p/138819184
        // volatile 不稳定的 ，它保护的是变量的安全，不能保护线程的安全
        // 它保证变量对所有线程可见。即其他线程修改了这个值，它保证新值对其他所有线程是可见的；volatile能够禁止指令重排
//        Volatile和Synchronized四个不同点：
//        1 粒度不同，后者锁对象和类，前者针对变量
//        2 syn阻塞，volatile线程不阻塞
//        3 syn保证三大特性，volatile不保证原子性
//        4 syn编译器优化，volatile不优化
        // 高并发三大特性：原子性、有序性和可见性 （volatile 满足可见性和有序性，不满足原子性）

        // 无法重现volatile的作用
        // 不管在主线程还是在子线程对变量值进行修改，加不加volatile的作用都是一样的，都是可以线程同步的；；这说明可能kotlin代码中已经默认进行了线程同步
        // 在java代码中可能是没有实现这个线程同步功能
        testVolatile()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 也是为了解决long类型的多线程同步问题，比AtomaticLong 性能更好一点 ；AtomicLong的性能差一点
            // longAdder能够实现变量的原子性；如果对象值的同步，那是否就能使用sync、以及volatile进行同步了呢？
            val longAdder = LongAdder()
            for (i in 0 until 10) {
                thread {
                    longAdder.increment()
                    Log.e("TAG", "线程:${Thread.currentThread().name}, longAdder的值：${longAdder}")
                }
            }
        }

        // HashMap 和ConCurrentHashMap（线程安全的）  StringBuilder和StringBuffer（线程安全的）

        // xCrash 捕获崩溃，包括java崩溃、native崩溃和anr   爱奇艺提供的崩溃日志捕获工具
        // https://github.com/iqiyi/xCrash

        // 如果一个Bean中有共有的 内容，可以提取出来一个Bean ，其他的Bean继承这个Bean
        val goodsDataBean = GoodsDataBean()
        val mutableList = mutableListOf<String>()
        mutableList.add("111")
        goodsDataBean.code = 1
        goodsDataBean.msg = "0x01"
        goodsDataBean.data = mutableList

        // Kotlin的代码简化逻辑     https://blog.csdn.net/u013064109/article/details/78786646
//        tv.setOnClickListener(View.OnClickListener { it ->
//        })

//        tv.setOnClickListener(object: View.OnClickListener{
//            override fun onClick(v: View?) {
//            }
//        })

//        tv.setOnClickListener { it -> }


        // android shell命令学习
//        1、活用tab键补全命令行
//        2、https://blog.csdn.net/codehxy/article/details/49763701 这个文章写的比较好
//
//        adb shell
//        $ su
//        # cd data  // 在data目录新增的文件一般是可读可写可执行，在sdcard目录新增的文件一般不可执行
//        # exit  // exit能够退出su权限
//        $ ls
//        ls: .: Permission denied   // 不授予su权限连读的权限都没有了
//        $ su
//        # ls -al  // 查看所有文件权限   第一位d表示文件夹，-表示文件，三组表示不同的用户，第一组表示所有者权限，第二组表示所属组权限，第三组表示其他人权限
//        rwx 表示可读可写可执行
//        # cd sdcard
//        # mkdir 111  // 创建文件夹
//        # touch 222.txt  // 创建文件
//        # rm 222.txt  //  删除文件
//        # rm -r 111 333  // 删除文件夹 多个用空格隔开
//        # rm -rf  // 强制删除文件夹
//        # chmod 777 startWifiAdb.sh  // 授予文件可读可写可执行权限（默认sdcard下没有这种权限，执行之后也是授予失败）
//        # exit
//        $ cd ..
//        $ cd data
//        $ su
//        # cp startWifiAdb.sh sss.sh  // 拷贝文件（当前目录下）
//        # cp startWifiAdb.sh /sdcard/sss.sh   // 拷贝文件（其他当前目录下）
//        # exit
//        $ exit
//        adb push C:\Users\Administrator\Desktop\stop.sh /data
//        adb: error: failed to copy 'C:\Users\Administrator\Desktop\stop.sh' to '/data/stop.sh': remote couldn't create file: Permission denied      // 直接push到data目录没有权限，考虑push到sdcard目录再sp到data目录
//        adb push C:\Users\Administrator\Desktop\stop.sh /sdcard
//        1 file pushed, 0 skipped. 0.1 MB/s (68 bytes in 0.000s)
//        adb pull sdcard/stop.sh C:\Users\Administrator\Desktop
//        sdcard/stop.sh: 1 file pulled, 0 skipped. 0.0 MB/s (63 bytes in 0.015s)
//        adb shell
//        $ cd sdcard
//        $ sp stop.sh /data     // 拷贝文件到data目录
//                cp: /data/stop.sh: Permission denied    // stop.sh 从sdcard目录拷贝过来的文件执行权限不足，需要授予权限
//        $ su
//        # cp stop.sh /data    // 从sdcard目录拷贝过来的文件也是权限不足，需要授予权限
//        /data # chmod - R 777 stop.sh   // data目录授予权限成功（data目录文件好授予权限，sdcard目录不好授予， 这里的R必须大写）
//        # cat stop.sh     // 查看文件中的内容
//                adb shell su 0 rm -r data/eee.sh  // adb shell su执行多条指令 这里的0有时为-c； adb shell；su；rm -r data/eee.sh 组合指令；可以通过一个bat脚本执行多个这样的指令
//        # cd system/bin   // 查看这里面的内容就是可以执行的命令
//        # ls
//        # cd system
//        # cat build.prop       // 查看系统可以通过setprop设置的属性
//        # ./startWifiAdb.sh    // 执行startWifiAdb.sh文件，如果文件在当前目录下
//        # data/startWifiAdb.sh    // 执行data目录下的startWifiAdb.sh文件
    }

    @Volatile
    private var click = true
    private fun testVolatile() {
        thread {
            SystemClock.sleep(1000)
            click = false
            Log.e("TAG", "click2:")
        }
        while (click) {
            Log.e("TAG", "click1:")
        }
    }


    private fun testReturn() {
        var a = 1
        test1(a)
        test2(a)
        test3(a)
    }

    private fun test1(a: Int) {
        if (a == 1) return
        // 这里return 返回了 ，不走这里
        if (a == 2) {
            Log.e("TAG", "test1: $a")
        }
    }

    private fun test2(a: Int) {
        Log.e("TAG", "test1: $a") // 1
    }

    private fun test3(a: Int) {
        Log.e("TAG", "test1: $a") // 1
    }
}