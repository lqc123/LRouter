# LRouter

### 项目介绍

LRouter是处理组件化模块之间Activity、Fragment、Service等调用以及传值的路由框架，提供组件化中model过多导致打包耗时长的解决方案，控件之间的传值采取url和bundle两种方式，控件注册使用apt注解处理器在打包生成注册类代码信息，以及使用asm在编译时动态插入注册信息，避免反射方式耗时，使用简单高效

### 用法

1.启用插件必须要在项目根目录build.gradle调用

```
apply plugin: "router.plugin"
```

2.在model build.gradle中添加以下依赖

```
implementation 'com.lrouter:router-annotation:1.0.0'
implementation 'com.lrouter:router-core:1.0.0'
//注解处理器
annotationProcessor 'com.lrouter:router-compiler:1.0.0'
```

3.在root project build.gradle中添加以下配置

```
//启用插装gradle插件
apply plugin: "router.plugin"
buildscript {
    repositories {
        //添加此仓库
        maven { url "https://dl.bintray.com/lqc/maven" }
    }

    dependencies {
        classpath 'com.lrouter:router-plugin:1.0.0'
      
    }
}

allprojects {
    repositories {
        maven { url "https://dl.bintray.com/lqc/maven" }
    }
}

```


4.路由初始化

```
//在application中初始化，只需要调用一次
LRouter.getInstance().init(true);
```

5.需要调用类或方法添加注解

```
@Router(value = "test")
public class TestActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Intent intent = getIntent();
        String a = intent.getStringExtra("a");
        String b = intent.getStringExtra("b");
        TextView tv = findViewById(R.id.tv_content);
        tv.setText(a+b);
    }
}
```

```
@Router("callMethod")
public  void call(Context context, Bundle bundle){
    Toast.makeText(context,bundle.getString("test"),Toast.LENGTH_LONG).show();
}
```

6.调用注解activity

```
LRouter.getInstance().build("module?a=true&b=1").startActivity(v.getContext());
```

7.调用添加注解方法

```
LRouter.getInstance().build("callMethod?test=hhh").invokerMethod(v.getContext());
```
