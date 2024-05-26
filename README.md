# Files 


## 💡 Introduction
这是一个关于疲劳度控制的库。


## 🔨 How to use
通过 gradle 添加依赖:
```groovy
repositories {
  mavenCentral()
}

dependencies {
  implementation 'com.hipoom:fatigue:0.0.1'
}
```

使用示例：
```
// 使用前，需要初始化：
File workspace = ...
Fatigue.init(workspace.getAbsolutePath());

// 判断是否可以触发
boolean canTrigger = Fatigue.of("业务名称").canTrigger(
	// 条件：1周最多1次 且 1周最多5次
    FatiguePolicy.maxTimes(1).inDays(1),
    FatiguePolicy.maxTimes(5).inWeeks(1)
);

// 标记刚触发了一次
long now = System.currentTimeMillis();
// 记录最大保留天数
int maxKeepDays = 30;
business.markTriggerOnce(now, maxKeepDays);
```
