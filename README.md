# FrameWorkDemo
> FrameWorkDemo是对framework2的使用说明，包含了我们移动开发android小组代码规范具体要求。
## 一、开源框架的使用
>开源框架的选择条件：1、符合我们应用业务实现要求；2、框架作者维护频率高；
1. 网络层 [okhttp](https://github.com/square/okhttp)
2. 网络请求 [retrofit](https://github.com/square/retrofit)
3. 图片加载 [Glide](https://github.com/bumptech/glide)
4. 数据库 [LitePal](https://github.com/LitePalFramework/LitePal)
5. 响应式编程[RxJava](https://github.com/ReactiveX/RxJava)、[RxAndroid](https://github.com/ReactiveX/RxAndroid)
6. 日志输出 [Logger](https://github.com/orhanobut/logger)
* okhttp是作为应用数据请求和图片资源请求的公共的==唯一入口==，个人观点来看，okhttp的扩展使用的精髓在于拦截器[Interceptor]，目前实现了3种拦截器：日志拦截器[LoggerInterceptor]、网络图片请求拦截器[GlideInterceptor]和Http头信息拦截器[HeadersInterceptor]。
* retrofit用于网络请求，方面我们把更多的精力放在应用的业务逻辑上，只需要花些许的时间去编写一个接口的请求方法。
* Glide作为目前android平台图片加载最优秀的开源框架之一，在过去1年多的实际使用中，已经证明了这一点。
* LitePal是ORM数据库工具，简化数据库操作，更专注业务逻辑；
* RxJava和RxAndroid是兴起不久的编程方式，非常方便的线程切换，RxJava配合retrofit就2个字，酸爽！
* Logger在日志输出视图的阅读便捷比原生的强太多了。
## 二、一个完整的Http请求步骤
1. 准备工作，配置OkHttp，OkHttpClient使用单例模式，应用的全部请求使用==同一个Client==

```
new OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.MILLISECONDS)//连接超时
                .readTimeout(10000, TimeUnit.MILLISECONDS)//读取超时
                .addInterceptor(new HeadersInterceptor())//头信息
                .addInterceptor(new LoggerInterceptor(LoggerInterceptor.Level.HEADERS))//日志输出
                .cache(new Cache(new File(getCacheDir(), "http"), 10 * 1024 * 1024))//http缓存
                .build();
OkHttpClient4Api.initClient(okHttpClient);//初始化单例模式
```
2. 构造公共的Retrofit生成方法
```
new Retrofit.Builder()
            .baseUrl(baseUrl)//baseUrl：host地址
            .addConverterFactory(GsonConverterFactory.create())//Gson解析方式
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//RxJava适配器
            .mFactory(OkHttpClient4Api.getInstance().getOkHttpClient())//设置OkHttpClient
            .build();
```
3. 构建"Root"层级的Response实体
>我们应用的数据“Root”层级响应实体具有约定的协议规范，以例子中的“干货集中营”（下文"干货集中营"以“干货”简称）的API的响应和响应实体如下：
```
{
    "error": false,
    "results": []//此处为jsonObject或jsonArray
}
```
对应的java对象
```
public class GHResponse<T> {

    private boolean error;
    private T results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }
}
```
4. 根据具体的接口的results内容生成对应的java对象，可直接使用插件[GsonFromat](https://github.com/zzz40500/GsonFormat)自动生成。
5. 创建interface，参考***GHApi***
6. 根据API帮助文档创建接口，以“干货”分类接口为例
>API说明：分类数据：http://gank.io/api/data/数据类型/请求个数/第几页

根据API的响应内容生成具体的results实体***MeiZiBO***,接下来可以书写对应的接口，**同一个接口我们可以根据需求创建多个调用方法**
```
/**
     * 干货资源
     *
     * @param source 资源分类{@link com.centanet.frameworkdemo.enums.GHCategory}
     * @see <a href="http://gank.io/api">干活集中营api</a>
     */
    @GET("data/{source}/10/1")
    Observable<GHResponse<ArrayList<MeiZiBO>>> category(
            @Path("source") String source);
```
or
```
/**
     /**
     * 干货资源
     *
     * @param source 资源分类{@link com.centanet.frameworkdemo.enums.GHCategory}
     * @param page   页码，1开始
     * @see <a href="http://gank.io/api">干活集中营api</a>
     */
    @GET("data/{source}/10/{page}")
    Observable<GHResponse<ArrayList<MeiZiBO>>> category(
            @Path("source") String source,
            @Path("page") int page);
```
or
```
/**
     * 干货资源
     *
     * @param source   资源分类{@link com.centanet.frameworkdemo.enums.GHCategory}
     * @param pagesize 每页数量
     * @param page     页码，1开始
     * @see <a href="http://gank.io/api">干活集中营api</a>
     */
    @GET("data/{source}/{pagesize}/{page}")
    Observable<GHResponse<ArrayList<MeiZiBO>>> category(
            @Path("source") String source,
            @Path("pagesize") int pagesize,
            @Path("page") int page);
```
7. 接口调用、使用数据
>接下来，RxJava和retrofit搭配使用的神奇效果才正式展现
```
ApiCreate
        .gh()
        .category(GHCategory.FULI.getSource(), 1)
        .subscribeOn(Schedulers.io())//网络请求切换到io线程
        .doOnSubscribe(new Action0() {
            @Override
            public void call() {
            //订阅开始时，可以做些准备操作，如：显示加载对话框
            //showLoadingDialog();
            }
        })
        .observeOn(AndroidSchedulers.mainThread())//数据回调到ui线程
        .compose(this.<GHResponse<ArrayList<MeiZiBO>>>bindUntilEvent(ActivityEvent.DESTROY))//绑定Activity生命周期，此处为Activity销毁时，取消未完成的请求，防止内存泄漏或其他异常导致崩溃
        .doOnNext(new Action1<GHResponse<ArrayList<MeiZiBO>>>() {
            @Override
            public void call(GHResponse<ArrayList<MeiZiBO>> arrayListGHResponse) {
            //数据返回，注意此处依然是io线程，可以把数据存储到数据库
            }
           })
        .subscribe(new Subscriber<GHResponse<ArrayList<MeiZiBO>>>() {
            @Override
            public void onCompleted() {
            //整个请求正常完成时调用，此前会调用onNext
            }

            @Override
            public void onError(Throwable e) {
            //此处为RxJava能够捕获的错误，地位等同onCompleted，不会调用onNext
            }

            @Override
            public void onNext(GHResponse<ArrayList<MeiZiBO>> arrayListGHResponse) {
            //业务逻辑部分
            }
        });
```
retrofit基础使用、RxJava和retrofit搭配使用，请参考 ==***HttpActivity***、***HttpOperatorsActivity***==
## 二、图片加载Glide
>简书[《Glide入门教程》](http://www.jianshu.com/p/7610bdbbad17)，请自行阅读学习。
1. Glide本身可以直接使用的，为了统一应用的网络层入口，我们将给Glide设置Client，并且添加网络图片请求限制的拦截器。
```
/**
 * Created by vctor2015 on 16/7/11.
 * <p>
 * 描述:Glide网络图片加载拦截器
 */

public abstract class GlideInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (cancelRequest()) {
            //取消请求
            Request.Builder requestBuilder = chain.request().newBuilder();
            requestBuilder.mGlideUrl("");
            return chain.proceed(requestBuilder.build());
        } else {
            return chain.proceed(chain.request());
        }
    }

    /**
     * 取消图片的网络请求
     *
     * @return true表示取消
     */
    protected abstract boolean  cancelRequest();
}
```
为了区分API请求，为Glide分配一个独立使用的OkHttpClient
```
new OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .readTimeout(10000, TimeUnit.MILLISECONDS)
                .addInterceptor(new GlideLimitInterceptor())
                .build();
```
此时，Glide并没有使用我们分配的Glide，我们需要实现**GlideModule**接口，并且在应用的**AndroidManifest.xml**配置好
```
public class AppGlideModule extends OkHttpGlideModule {

    @Override
    public void registerComponents(Context context, Glide glide) {
        glide.register(GlideUrl.class,
                InputStream.class,
                new OkHttpUrlLoader.Factory(OkHttpClient4Glide.getInstance().getOkHttpClient()));
    }
}
```
**AndroidManifest.xml**中配置，==value必须为GlideModule==。
```
<meta-data
            android:name="com.centanet.frameworkdemo.AppGlideModule"
            android:value="GlideModule"/>
```
接下来，我们就可以按照Glide的API自由玩耍了。
## 三、下拉刷新的自定义控件SwipeRecyclerView
>**SwipeRecyclerView**是由android基本控件**SwipeRefreshLayout**、**RecyclerView**组合实现的。
1. ==先注意**SwipeRefreshLayout**的一个坑，当我们页面初始化完成立即调用**SwipeRefreshLayout**的setRefreshing(true)方法打算开启下拉刷新动画时，什么事情都没发生，原因是此时的**MSwipeRefreshLayout**的onMeasure(int widthMeasureSpec, int heightMeasureSpec)还未执行，解决方案如下：==
```
public class MSwipeRefreshLayout extends SwipeRefreshLayout {

    private boolean mMeasured;
    private boolean mPreMeasureRefreshing;

    public MSwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public MSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!mMeasured) {
            mMeasured = true;
            setRefreshing(mPreMeasureRefreshing);
        }
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        if (mMeasured) {
            super.setRefreshing(refreshing);
        } else {
            mPreMeasureRefreshing = refreshing;
        }
    }
}
```
填好坑之后，**SwipeRecyclerView**的逻辑就简单了，直接找到这个class了解即可。
2. 列表“加载更多”的功能是由Adapter来完成的，只要继承**SwipeAdapter1**即可。
3. 主要的public方法
- 加载数据load(List<E> temp, int total)，temp为分组数据（可为null），total是列表的总数，内部会判断当前加载的数量与总数的关系来显示是否有更多
- 加载书籍load(List<E> temp, boolean hasMore)，temp为分组数据（可为null），hasMore需要告诉Adapter有没更多
- 加载出错error()，网络不可用，或者解析异常时调用
- 重置数据reset()，可以清空列表中数据，==下拉刷新时不需要主动调用该方法==
4. 可重写修改显示文本的方法
- defText()，没有数据时显示文本
- defErrorText()，调用error()时显示的文本
- moreText()，加载更多显示的文本
5. 基本使用方法参考**SwipeRecyclerViewActivity**
## 四、JavaScriptBridge
>[jsbridge](https://github.com/lzyzsd/JsBridge)是**JS**和**Native**双边通信的桥梁
1. 引入compile 'com.github.lzyzsd:jsbridge:1.0.4'；
2. 把**BridgeWebView**替代**WebView**使用；
3. 注意==setWebViewClient==时，需要使用==BridgeWebViewClient==
```
mWvBridge.setWebViewClient(new BridgeWebViewClient(mWvBridge) {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String mGlideUrl) {
                Logger.t(TAG).d("shouldOverrideUrlLoading : %s", mGlideUrl);
                return super.shouldOverrideUrlLoading(view, mGlideUrl);
            }
        });
```
4. 其他使用说明可参考**JsBridgeActivity**

