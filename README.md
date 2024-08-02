# Registry

![registry](https://img.shields.io/maven-central/v/com.hipoom/registry)    ![](https://img.shields.io/github/last-commit/hipoom/registry?color=blue)     ![](https://img.shields.io/github/license/hipoom/registry)

[简体中文](https://github.com/hipoom/registry/blob/main/README-CN.md) | ENGLISH


## 💡 Introduction
If you have a multi-module project, or even a big project consisting of multiple sub projects, you may have encountered the following problem:
1. In the underlying module, an interface is defined, such as `OnStartupCompletedAction`;
2. There are one or more modules that implements this interface, such as `UpgradeResourceAction`, `SyncDataAction`, etc;
3. At a certain point, you need to obtain all implementation classes that implement the OnStartupCompletedAction interface, such as after the application is launched.

![](./doc-resources/case.jpg)

How can you obtain the implementation classes of this interface?  
A common practice is to define a method in a higher-level module. In this method, all implementation classes are added to a set:  
```
public Set<Class<OnStartupCompletedAction>> getAllFruitImpls() {
    Set<Class<OnStartupCompletedAction>> classes = new HashSet<>();
    classes.add(UpgradeResourceAction.class);
    classes.add(SyncDataAction.class);
    // more ...
    return classes;
}
```

There are many deficiencies to doing this, such as:  
* This module must depend on all modules that implement the interface;
* Every time an implementation class is added to a sub module, it needs to be manually added in the higher-level module.

The Registry library is designed to reslove these issues. 🎉 🎉 🎉  
The Registry library can collect which classes have added a certain annotation or which classes have implemented a certain interface, during compilation.  
When needed, obtain it through the following methods:  
```
// if the OnStartupCompletedAction is an interface
Registry.getClassesImplements(OnStartupCompletedAction.class);

// if the OnStartupCompletedAction is an annotation
Registry.getClassesAnnotatedWith(OnStartupCompletedAction.class);
```


## 🔨 Getting started
### 1. Configure the build.gradle of the project level
In the build.gradle file in the project level directory, add the classpath:
```
buildscript {
    dependencies {
        classpath "com.hipoom:processor-gradle6:0.0.4"
    }
}
```
The latest version is：
![processor](https://img.shields.io/maven-central/v/com.hipoom/processor-gradle6) 

### 2. Configure the build.gradle of the app-module level
Add plugin configuration to the build.gradle:
```
apply plugin: 'hipoom'
hipoom {
    registry {
        
        // Add interfaces you want to collect here.
        interfaces = [
            "com.hipoom.demo.TestInterface"
        ]

        // Configure whether to track the parent class of the class.
        // for example: ClassA --extends--> ClassB --implements--> InterfaceC.
        // If this field is set to false, ClassA will not be collected;
        // If this field is set to true, ClassA will be collected;
        needTrackSuperClassForInterface = false

        //Add annotations you want to collect here.
        annotations = [
            "com.hipoom.processor.TestAnnotation"
        ]
    }
}
```

### 3. Use in code
Now, you can use Registry in your code:
```
// if the OnStartupCompletedAction is an interface
Registry.getClassesImplements(OnStartupCompletedAction.class);

// if the OnStartupCompletedAction is an annotation
Registry.getClassesAnnotatedWith(OnStartupCompletedAction.class);
```

You can also manually register the mapping:
```
Registry.addAnnotation(YourAnnotation.class, YourClass.class);

Registry.addInterface(YourInterface.class, YourClass.class);
```
