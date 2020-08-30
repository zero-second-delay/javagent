## javagent
## 1 本工程目的
### 为增强现有wsth服务，尝试通过javaagent+javassist实现零侵入增强服务能力
### 比如增加函数耗时日志、增加函数入口出口参数日志
## 2 增强器链
### 2-1 入口：MethodTimePreMain.myTransformer()
### 2-2 增强器顺序：MyClassTransformer(ClassEnhancer...enhancer)，按构造器传入的顺序执行

## 3 增强器javassist开关
### 3-1 一级开关：boolean TransformerSwitch.transform(String className)
### 3-2 二级开关：boolean JavassistClassEnhancer.transformSwitch(String className)

## TODO：
### 1.LogEnhancer不够通用
### 2.开关配置和premain函数配置化能力缺失
### 3.增强器类中代码抽象度低，待优化

