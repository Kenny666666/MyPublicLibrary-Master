# MyPublicLibrary-Master
自己的开发库及demo，技术分享。--持续更新中..
===
项目预想
==
一、开发工具的选择
=
    Android Studio
二、App设计风格
=
    Material Design
三、版本支持
=
    2.3.3+
四、App集成框架
=
    相信大家都有体会，随着功能模块的增加，App越来越大，如果没有良好的架构设计，则代码将会变得臃肿且不易维护，各功能模块的耦合度会越来越高。因此可以把App模块化，将一个完整的App划分成几个相对独立的模块，这样即可以降低模块间的耦合也利于复用。

  1. 网络模块

    GitHub上的开源网络框架也特别多，个人认为可以使用开源框架，目前我会选okHttp或者Volley，也许以后会有更好的网络框架出现。注意如果使用开源框架，则必须要阅读其源码，必须能够驾驭它，这样就不至于当bug出现时束手无策。
  2. 图片管理模块

    目前采用大家熟悉的Android-Universal-Image-Loader貌似这个最火吧，之前个人的App中也用了它。之后集成Glide或Fresco做一些案例。
  3. 本地数据库模块

    采用流行的ORM框架，第三方库会大大方便你对sqlite的操作。
  4. 文件管理模块

    一个App，肯定会涉及到一些文件，如配置文件、图片、视频、音频、SharedPreferences文件等。我们可以提供一个全局的文件管理模块，负责文件的增、删、改、查等操作。另外还需支持文件压缩，文件的上传与下载操作，对于下载需要支持多线程并发下载、断点续传等功能。
  5. 组件内、组件间通信机制

    对于一个App，组件通信必不可少，通信类型可以分为点对点和点对面的的通信，点对点即只有唯一的接收者可以响应消息，点对面则类似于消息广播，即所有注册过的都可以响应消息。在Android中，通常使用消息机制来实现，但消息机制的耦合度比较高。目前也有一些通信框架，如EventBus、Otto等事件总线框架，这些框架可以极大地降低组件间的耦合。
  6. 数据处理

    JSON、xml格式
  7. 业务层

    业务层大概就是四大组件、Fragment、View了，建议尽可能地使用原生组件，少用自定义组件，因为原生组件性能是最好的。另外建议使用MVC模式就好，只要设计管理好自己的逻辑，至于MVP、MVVM等模式个人认为都有缺陷，总之寻求一个折中吧，有得必有失。
  8. APK动态加载机制

    随着App的增大，功能的扩展，很多App已经采用了APK动态加载的机制，也可以叫做插件化。

我想如果按照上述原则，至少可以开发出一款不错的APP~
===
现已集成
=
1、首页初步完成，MD设计风格，Toolbar、DrawerLayout/NavigationView左侧菜单、viewPage+fragment、沉寝式状态栏。

2、网络模块volley(功能待完善)

3、ORM数据库框架

4、EventBus 3.0 组件间通信使用案例

5、某大神最强适配方案AutoLayoutActivity

6、腾讯bugly，异常捕获及异常回传。本地全局异常捕获机制。

7、AndroidPN消息推送，集成github上的AndroidPN框架扩展：1、加入心跳和断线重连2、优化断线重连3、离线推送4、别名和标签推送功能5、富媒体推送6、丰富客户端功能、查看历史、开机自启。

8、PullToRefresh、RecyclerView 上下拉刷新，加载更多。案例等。

9、通用工具等。
效果图
=
![](https://github.com/Kenny666666/MyPublicLibrary-Master/blob/master/1.gif) ![](https://github.com/Kenny666666/MyPublicLibrary-Master/blob/master/2.gif) 
![](https://github.com/Kenny666666/MyPublicLibrary-Master/blob/master/3.gif) 
![](https://github.com/Kenny666666/MyPublicLibrary-Master/blob/master/4.gif) 
