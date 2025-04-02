最近AI辅助编程很火， 有以插件形式存在的Github Copilot， 阿里的通义零码， 字节的MarsCode等， 还有自带AI的集成开发环境，如国外的Cursor和字节推出的Trae等。

自带的AI集成开发环境， 优点是上下文基于整个工程， 可以基于整个工程的业务逻辑， 代码风格。给出合适的提示， 并能根据需求找出需要修改的位置并作出合理的修改。

作为一名Android开发者，我们本次来使用 Cursor来帮我们生成一款记账的APP， 来探索Cursor的能力和边界。

![image.png](https://p0-xtjj-private.juejin.cn/tos-cn-i-73owjymdk6/f0338fd4db7e4e4eb07500d51e398765~tplv-73owjymdk6-jj-mark-v1:0:0:0:0:5o6Y6YeR5oqA5pyv56S-5Yy6IEAg5rKJ6buY546L6LSw:q75.awebp?policy=eyJ2bSI6MywidWlkIjoiMjcyMzM0NjEyMzQ2Mjk2In0%3D&rk3s=f64ab15b&x-orig-authkey=f32326d3454f2ac7e96d3d06cdbb035152127018&x-orig-expires=1744185410&x-orig-sign=jv%2F5HiWg7N4Q5iz8KAjOFw15Fbs%3D)

先来看看最终的效果：

<https://www.bilibili.com/video/BV1qSfPYwEzu/?vd_source=92fd00bf1fea71cbeac9cb3fe83129b2>

APK体验： <https://github.com/stevenwsg/Charge/blob/main/app-debug.apk>

附上源码：

<https://github.com/stevenwsg/Charge>

好了，让我们开始动手吧

# 1、Prompt 编写

```

###目标
设计实现一个记账app

###需求
设计一款记账APP， 可以针对日常的消费支出进行记录， 当用户发生消费时， 可以用我们的APP进行记录， 然后我们的APP 可以针对 用户的 消费记录 进行按照日，星期， 月度， 年度的统计功能。

###UI
1. 使用 Google Material Design 风格
2. 颜色搭配柔和，美观
3、可以适当加入一些动画

###功能
1.支持记录消费支出， 比提供几种消费类型供用户选择， 比如住房，数码， 餐饮， 教育，购物等
2.支持统计功能， 支持用户选择某一天， 某一星期， 某一个月的消费记录进行查看。 并做出统计图标或者圆形图标机型直观展示


###实现
1. 使用Kotlin语言
2、使用JetPak Room 数据库进行存储消费记录
3、可以引入一些图表库进行展示统计的消费记录
4、可以适当增加一些动画
5、生成的代码能够正常运行

###角色
1. 你是一个安卓专家、精通app的设计实现, 同时你对UI有高深的造诣，能够画出让大多数满意舒服的UI
2. 可以自主根据需求设计实现精美的app的能力


```

<p align="center"><img src="https://p0-xtjj-private.juejin.cn/tos-cn-i-73owjymdk6/d7977757c7484a7da21ffd41b007c01f~tplv-73owjymdk6-jj-mark-v1:0:0:0:0:5o6Y6YeR5oqA5pyv56S-5Yy6IEAg5rKJ6buY546L6LSw:q75.awebp?policy=eyJ2bSI6MywidWlkIjoiMjcyMzM0NjEyMzQ2Mjk2In0%3D&rk3s=f64ab15b&x-orig-authkey=f32326d3454f2ac7e96d3d06cdbb035152127018&x-orig-expires=1744185410&x-orig-sign=Hrox3P3Nem8ANKF%2BQnWOzz2a87U%3D" alt="image.png"></p>

最新版的Cursor， 我们选择Agent 模式， 模型我们选择 claude-3.7-sonnet。 我们将上述的prompt 投喂给Cursor， 让Cursor 帮我们生成代码。

# 2、 生成源码

![image.png](https://p0-xtjj-private.juejin.cn/tos-cn-i-73owjymdk6/e92c213bc4ec4c2392d6227a7b142f83~tplv-73owjymdk6-jj-mark-v1:0:0:0:0:5o6Y6YeR5oqA5pyv56S-5Yy6IEAg5rKJ6buY546L6LSw:q75.awebp?policy=eyJ2bSI6MywidWlkIjoiMjcyMzM0NjEyMzQ2Mjk2In0%3D&rk3s=f64ab15b&x-orig-authkey=f32326d3454f2ac7e96d3d06cdbb035152127018&x-orig-expires=1744185410&x-orig-sign=AAnAP%2BPBU3AmUsj%2BGJmDT3meqHU%3D)

经过一段时间等待， Cursor APP 帮我们生成了。

可以看到Cursor帮我们出了一些处理：

1.  使用了Google Material UI 风格设计
2.  使用Room 数据库来进行存储账单
3.  可以支持对账单的 增删改查
4.  可以设置这笔消费账单关联的类型
5.  可以针对日，周， 月，年 进行统计
6.  使用图表对账单数据进行统计
7.  可以查看每笔账单的详情
8.  使用 MVVM 结构模式 构建
9.  使用kotlin 协程来处理异步操作
10. 使用 Navcation来进行导航

可以看出新增了 54个文件 2643行代码 ， 减少了83行代码。 我们点击 Accet all按钮， 将所有的更改应用到项目中。 在切换到 Android Studio中 试着运行一下。

![image.png](https://p0-xtjj-private.juejin.cn/tos-cn-i-73owjymdk6/6d0b85f9a57b4149ad3e4f99ee11cb24~tplv-73owjymdk6-jj-mark-v1:0:0:0:0:5o6Y6YeR5oqA5pyv56S-5Yy6IEAg5rKJ6buY546L6LSw:q75.awebp?policy=eyJ2bSI6MywidWlkIjoiMjcyMzM0NjEyMzQ2Mjk2In0%3D&rk3s=f64ab15b&x-orig-authkey=f32326d3454f2ac7e96d3d06cdbb035152127018&x-orig-expires=1744185410&x-orig-sign=NQhEsTCd2xEH6crnjcIeS3iMmt8%3D)

首先 Android Studio 提示报错，可以看出是ViewBinding 导致的， 我们先sync 一下试试

![image.png](https://p0-xtjj-private.juejin.cn/tos-cn-i-73owjymdk6/0e9d51d1f4904b25a4b7e6b2245361da~tplv-73owjymdk6-jj-mark-v1:0:0:0:0:5o6Y6YeR5oqA5pyv56S-5Yy6IEAg5rKJ6buY546L6LSw:q75.awebp?policy=eyJ2bSI6MywidWlkIjoiMjcyMzM0NjEyMzQ2Mjk2In0%3D&rk3s=f64ab15b&x-orig-authkey=f32326d3454f2ac7e96d3d06cdbb035152127018&x-orig-expires=1744185410&x-orig-sign=E69UkJ6P5l9PpeZPS9obbh0KVHc%3D)

Sync完成后， 没有了报错。 我们运行一下试试

![image.png](https://p0-xtjj-private.juejin.cn/tos-cn-i-73owjymdk6/6b2a59ff73694202a590a740d93b659c~tplv-73owjymdk6-jj-mark-v1:0:0:0:0:5o6Y6YeR5oqA5pyv56S-5Yy6IEAg5rKJ6buY546L6LSw:q75.awebp?policy=eyJ2bSI6MywidWlkIjoiMjcyMzM0NjEyMzQ2Mjk2In0%3D&rk3s=f64ab15b&x-orig-authkey=f32326d3454f2ac7e96d3d06cdbb035152127018&x-orig-expires=1744185410&x-orig-sign=y7AUxHIUSo1I8wndnjFnwmoBZ4k%3D)

Android Studio 遇到了运行报错， 我们先试试将错误信息扔给Cursor， 看看 Cursor能不能帮我们解决。

![image.png](https://p0-xtjj-private.juejin.cn/tos-cn-i-73owjymdk6/69d26499e07f48329ab093c603634dec~tplv-73owjymdk6-jj-mark-v1:0:0:0:0:5o6Y6YeR5oqA5pyv56S-5Yy6IEAg5rKJ6buY546L6LSw:q75.awebp?policy=eyJ2bSI6MywidWlkIjoiMjcyMzM0NjEyMzQ2Mjk2In0%3D&rk3s=f64ab15b&x-orig-authkey=f32326d3454f2ac7e96d3d06cdbb035152127018&x-orig-expires=1744185410&x-orig-sign=SEdCavqy8ehI%2FxaRhJXkqNkuoy8%3D)

![image.png](https://p0-xtjj-private.juejin.cn/tos-cn-i-73owjymdk6/8d1f9c27661d419eabf49a322ac5f61d~tplv-73owjymdk6-jj-mark-v1:0:0:0:0:5o6Y6YeR5oqA5pyv56S-5Yy6IEAg5rKJ6buY546L6LSw:q75.awebp?policy=eyJ2bSI6MywidWlkIjoiMjcyMzM0NjEyMzQ2Mjk2In0%3D&rk3s=f64ab15b&x-orig-authkey=f32326d3454f2ac7e96d3d06cdbb035152127018&x-orig-expires=1744185410&x-orig-sign=FbSJtmWmcVkTjE%2B3k2j0yTm3Y%2FA%3D)

好了 ， 解决完了， 这次看我们能不能运行成功。 不出意外 又有一些新的报错， 我们扔给Cursor

![image.png](https://p0-xtjj-private.juejin.cn/tos-cn-i-73owjymdk6/b687fa445c38495d9e26e7641b8a93f8~tplv-73owjymdk6-jj-mark-v1:0:0:0:0:5o6Y6YeR5oqA5pyv56S-5Yy6IEAg5rKJ6buY546L6LSw:q75.awebp?policy=eyJ2bSI6MywidWlkIjoiMjcyMzM0NjEyMzQ2Mjk2In0%3D&rk3s=f64ab15b&x-orig-authkey=f32326d3454f2ac7e96d3d06cdbb035152127018&x-orig-expires=1744185410&x-orig-sign=ofkPXr6ylg%2F%2Bs6F6o1lBdZF3Eds%3D)

![image.png](https://p0-xtjj-private.juejin.cn/tos-cn-i-73owjymdk6/96f9b1ffe8ba4dc580b50db27447bcdb~tplv-73owjymdk6-jj-mark-v1:0:0:0:0:5o6Y6YeR5oqA5pyv56S-5Yy6IEAg5rKJ6buY546L6LSw:q75.awebp?policy=eyJ2bSI6MywidWlkIjoiMjcyMzM0NjEyMzQ2Mjk2In0%3D&rk3s=f64ab15b&x-orig-authkey=f32326d3454f2ac7e96d3d06cdbb035152127018&x-orig-expires=1744185410&x-orig-sign=1BkF6geh0tzGF7I%2FJ3WdsAU01sE%3D)

Cursor 又修改了一版， 我们再试试

这次运行起来了， 让我们看看结果

# 3、 运行结果

## 3.1 记账页面

首先是记账页面， 可以选择金额， 消费类型， 描述 和日期选择

![image.png](https://p0-xtjj-private.juejin.cn/tos-cn-i-73owjymdk6/fc1d4e843f0c4f88aa88438170bd2563~tplv-73owjymdk6-jj-mark-v1:0:0:0:0:5o6Y6YeR5oqA5pyv56S-5Yy6IEAg5rKJ6buY546L6LSw:q75.awebp?policy=eyJ2bSI6MywidWlkIjoiMjcyMzM0NjEyMzQ2Mjk2In0%3D&rk3s=f64ab15b&x-orig-authkey=f32326d3454f2ac7e96d3d06cdbb035152127018&x-orig-expires=1744185410&x-orig-sign=heie5%2FcFcaOvLvFCUDtv2f%2FbzlU%3D)

## 3.2 首页

![image.png](https://p0-xtjj-private.juejin.cn/tos-cn-i-73owjymdk6/8e2d092e1b5e48fdab8ad7289dd16aa5~tplv-73owjymdk6-jj-mark-v1:0:0:0:0:5o6Y6YeR5oqA5pyv56S-5Yy6IEAg5rKJ6buY546L6LSw:q75.awebp?policy=eyJ2bSI6MywidWlkIjoiMjcyMzM0NjEyMzQ2Mjk2In0%3D&rk3s=f64ab15b&x-orig-authkey=f32326d3454f2ac7e96d3d06cdbb035152127018&x-orig-expires=1744185410&x-orig-sign=%2FfC2RF4dEExoQ9x3Ouga4h26s%2BQ%3D)

然后是首页， 以列表的形式 展示 最近的消费记录. 不过列表好像没有展示出来

## 3.3 消费统计页面

![image.png](https://p0-xtjj-private.juejin.cn/tos-cn-i-73owjymdk6/d2858cac95634a52acb84f4351c698c2~tplv-73owjymdk6-jj-mark-v1:0:0:0:0:5o6Y6YeR5oqA5pyv56S-5Yy6IEAg5rKJ6buY546L6LSw:q75.awebp?policy=eyJ2bSI6MywidWlkIjoiMjcyMzM0NjEyMzQ2Mjk2In0%3D&rk3s=f64ab15b&x-orig-authkey=f32326d3454f2ac7e96d3d06cdbb035152127018&x-orig-expires=1744185410&x-orig-sign=LTRY7yrb1j7bhI%2BWVrtOZAd6eBM%3D)

可以看出，可以根据， 今天，这周，这个月，今年来列出消费统计， 并按照圆形图标的方式展示出每种消费所对应的比例

# 4、优化

目前可以明显看出， 存在两个问题

1.  三个页面的沉浸式状态栏处理的不太好， 页面的Title 距离顶部的时间太近了
2.  按照设计和源码来看， 首页的消费列表 RecyclerView没有显示出UI

我们根据这两个问题， 让Cursor 在帮我们在修改一下。

![image.png](https://p0-xtjj-private.juejin.cn/tos-cn-i-73owjymdk6/99efba7d1563429d82b1c2ce7c0c25f8~tplv-73owjymdk6-jj-mark-v1:0:0:0:0:5o6Y6YeR5oqA5pyv56S-5Yy6IEAg5rKJ6buY546L6LSw:q75.awebp?policy=eyJ2bSI6MywidWlkIjoiMjcyMzM0NjEyMzQ2Mjk2In0%3D&rk3s=f64ab15b&x-orig-authkey=f32326d3454f2ac7e96d3d06cdbb035152127018&x-orig-expires=1744185410&x-orig-sign=RF0j8kr7pHZf68wNW2TFXVOWSRM%3D)

经过和Cursor的几次沟通 改好了。

![image.png](https://p0-xtjj-private.juejin.cn/tos-cn-i-73owjymdk6/e83cab698f18496e84a6580264db94f5~tplv-73owjymdk6-jj-mark-v1:0:0:0:0:5o6Y6YeR5oqA5pyv56S-5Yy6IEAg5rKJ6buY546L6LSw:q75.awebp?policy=eyJ2bSI6MywidWlkIjoiMjcyMzM0NjEyMzQ2Mjk2In0%3D&rk3s=f64ab15b&x-orig-authkey=f32326d3454f2ac7e96d3d06cdbb035152127018&x-orig-expires=1744185410&x-orig-sign=8ZSMdq4NYyHRv0TtmZlWshwg4kU%3D)

首页也显示出来了账单列表。 点击账单列表的Item。 可以进入账单编辑页面。可以对账单进行编辑 和删除

![image.png](https://p0-xtjj-private.juejin.cn/tos-cn-i-73owjymdk6/2874986bad5c45c9a9b1de8bcb502190~tplv-73owjymdk6-jj-mark-v1:0:0:0:0:5o6Y6YeR5oqA5pyv56S-5Yy6IEAg5rKJ6buY546L6LSw:q75.awebp?policy=eyJ2bSI6MywidWlkIjoiMjcyMzM0NjEyMzQ2Mjk2In0%3D&rk3s=f64ab15b&x-orig-authkey=f32326d3454f2ac7e96d3d06cdbb035152127018&x-orig-expires=1744185410&x-orig-sign=OKpl0iiamaFChJjaLKVd9VNpQYQ%3D)

好了， 到这里，我全程一行代码度没有写， 体会到Cursor的强大之处了嘛

可以查看视频 看看最终效果： <https://www.bilibili.com/video/BV1qSfPYwEzu/?vd_source=92fd00bf1fea71cbeac9cb3fe83129b2>

# 5、源码分析

我没有写一行代码， Cursor 开发加调试 不到一个小时生成的。 速度是已经很快了， 让我们简单的看看部分代码， 看下代码质量，代码风格， 以及Cursor到底怎么做到的

首先看下Cursor 帮我们引入了那些库

```
dependencies {
    // Core dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    
    // Navigation Component
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    
    // Room Database
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    
    // MPAndroidChart for statistics visualization
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    
    // ViewModel and LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    
    // DatePicker for date selection
    implementation("com.google.android.material:material:1.11.0")
    
    // Coroutines for async programming
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    
    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

```

引入 了

*   navigation 导航
*   room 实现数据库
*   MPAndroidChart 实现图标
*   material 实现md风格
*   lifecycle viewmodel livedata 实现MMVM框架
*   kotlinx-coroutines-android Kotlin 协程

可以看出 Cursor 是用MMVM 模式帮我们写的。

项目结构其实也是蛮清晰的

![image.png](https://p0-xtjj-private.juejin.cn/tos-cn-i-73owjymdk6/6e433297ad424b6cbc6894ab95348969~tplv-73owjymdk6-jj-mark-v1:0:0:0:0:5o6Y6YeR5oqA5pyv56S-5Yy6IEAg5rKJ6buY546L6LSw:q75.awebp?policy=eyJ2bSI6MywidWlkIjoiMjcyMzM0NjEyMzQ2Mjk2In0%3D&rk3s=f64ab15b&x-orig-authkey=f32326d3454f2ac7e96d3d06cdbb035152127018&x-orig-expires=1744185410&x-orig-sign=4BbSBBZGWpmwhLKgBlXZi91yR%2F8%3D)

具体的源码不带大家看了， 我上传到Github， 大家如果感兴趣的话， 可以拉下来看看：<https://github.com/stevenwsg/Charge>

# 7、总结

用Cursor 不到一个小时生成的APP， 虽然是一个很简单的APP， 我觉得已经很厉害，效率很高了。 至此， 我们看到了Cursor的能力， 至于上限和边界还远远没有触及到。我是被震撼到了， 可以看到这个模型一直在进化， 以后进化的会更厉害。

我觉得，  对于我们普通的开发者， 不仅要借助这些AI辅助编程工具， 提升我们的工作效率， 还是要在提高自己的架构能力，沟通能力， 业务需求能力。 现在的AI确实是可以替代一些工作量的，但总有些东西是AI代替不了的
