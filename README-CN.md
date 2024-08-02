# 💡 介绍
如果你有一个多 module 的工程，甚至是一个由多个子工程组成的大工程项目，你可能遇到过这种问题：
1. 在底层模块中，定义了一个接口，例如 OnStartupCompletedAction；
2. 在各个业务层，各自实现了这个接口，例如 UpgradeResourceAction、SyncDataAction 等等；
3. 在某一个时机，你需要获取所有实现了 OnStartupCompletedAction 接口的实现类，例如启动完毕后。

![](./doc-resources/case.jpg)

你要如何获取 OnStartupCompletedAction 有哪些实现类呢？
一个常规的做法，是在一个更顶层的 module 中，定义一个方法，各个子 module 的实现类，都在这个方法中注册：
```
public Set<Class<OnStartupCompletedAction>> getAllFruitImpls() {
    Set<Class<OnStartupCompletedAction>> classes = new HashSet<>();
    classes.add(UpgradeResourceAction.class);
    classes.add(SyncDataAction.class);
    // more ...
    return classes;
}
```

这么做，有很多缺点， 例如：
① 这个 module 必须依赖了所有业务层的 module;
② 每个业务层的 module 新增一个实现类，都需要手动跨工程在上层 module 中手动添加。

Registry 库，就是为了解决这两个问题。
Registry 可以在编译期间，搜集项目中，有哪些类添加了某个注解，或者哪些类实现了某个接口。在需要用的时候，通过：
```
// 如果 OnStartupCompletedAction 是个接口
Registry.getClassesImplements(OnStartupCompletedAction.class);

// 如果 OnStartupCompletedAction 是个注解
Registry.getClassesAnnotatedWith(OnStartupCompletedAction.class);
```
即可获取。


# 🔨 使用方法

## 1. 配置 project 级别的 build.gradle
在根目录的 `build.gradle` 文件中，添加 classpath：

```groovy
buildscript {
    dependencies {
        // 注意把版本号，替换为你需要的版本
        classpath "com.hipoom:processor-gradle6:0.0.4"
    }
}
```
该模块已经发布到 maven 中央仓库中。


## 2. 配置 app module 的 build.gradle
首先，在 dependencies 中，添加依赖：
```
dependencies {
    // 添加这一行
    implementation "com.hipoom:registry:0.0.3"
}
```

然后，在顶部添加 plugin：
```
apply plugin: 'hipoom'
hipoom {
    registry {
        
        // 在这里，添加你想搜集的接口
        interfaces = [
            "com.hipoom.demo.TestInterface"
        ]

        // 这里标记是否需要追踪类的父类。 
        // 例如: ClassA --extends--> ClassB --implements--> InterfaceC
        // 如果把这个配置设置为 false, 则 ClassA 不会被搜集；
        // 如果把这个配置设置为 true, 则 ClassA 会被搜集。
        needTrackSuperClassForInterface = false

        // 在这里，添加你想搜集的注解
        annotations = [
            "com.hipoom.processor.TestAnnotation"
        ]
    }
}
```
