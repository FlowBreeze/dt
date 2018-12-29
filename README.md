  dt(a javafx dictionary)
========
最近在 ubuntu 上读英语文档很不方便，每次碰到不认识的单词都要复制粘贴,  
在 git 上找了一圈，发现没有 java 语言的划词应用，于是自己实现了一个  

代码不多，简单易懂，有需要可以自己改改，目前很多功能不是很完善

## 目前版本 0.1.0
>使用 JNativeHook 库监听鼠标点击事件  
>使用 java.awt SystemSelection 获取选择文本  
>使用 retrofit2 进行 http 通讯  
>使用 javafx 进行查词结果显示  
>使用 owner 读取配置信息

## 不足
>只支持英译汉  
>界面样式不完善  
>无法播放语音  

## TODO
>添加界面样式，并支持自定义（使用 fxml）、更换主题  
>添加语音支持 

>### 可能会做
>>添加对windows操作系统支持  
>>系统托盘图标

## 已知问题
>> 在 SystemSelection 响应事件的环境下控制台会报错，但不影响使用 如：Intellij IDEA
>> 启动后有几率无法使用，需要重新启动
