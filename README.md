  dt(a javafx dictionary)
========

纯个人编写、经历多次重构、目前代码流程已经比较完善。有需要可以更改源码  
顺便跪求一个 javafx 大神帮忙设计界面（还是h5界面好写QAQ）

启动时会在jar包目录下创建 dt-config.properties 配置文件，对其进行的更改在下次启动会生效

支持使用剪切板、鼠标选择（划词）作为输入源、支持自定义翻译快捷键、自定义展示位置(固定位置、相对鼠标位置)。
进行翻译后显示爱词霸和百度翻译的翻译结果，在爱词霸翻译界面可以查看音标和读音

## 发布版 0.2.1
点击[这里下载](https://github.com/FlowBreeze/dt/releases)  
需要在 java 1.8 以上环境运行  
> java -jar dt-0.2.1.jar
如果出现 java.lang.NoClassDefFoundError: javafx/application/Application  
请尝试安装 openjfx  
>sudo apt install openjfx

## 使用库
>使用 JNativeHook 库监听鼠标点击事件  
>使用 java.awt SystemSelection 获取选择文本  
>使用 retrofit2 进行 http 通讯  
>使用 javafx 进行查词结果显示  
>使用 owner 读取配置信息

## TODO（下一步可能会做）
>>添加对windows操作系统支持  
>>系统托盘图标  
>>添加可视化配置界面  
>>添加界面样式,更换主题

## 已知问题
>> 启动后有几率无法使用，需要重新启动  
>> 在 ubuntu 下所有功能正常，windows 下未测试
