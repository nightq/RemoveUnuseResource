# RemoveUnuseResource
==
在标准的android studio 工程下面，可以删除通过 android studio 的 Analyse/Inspect Code 工具监测出来的 AndroidLintUnusedResources.xml里面的没有使用的 string dimen array drawable 等资源.

＃准备工作
== 
1. 打开 android studio 的 Analyse/Inspect Code
2. 完成之后从 Inspection 面板中导出为 xml 文件
3. 从导出的xml 文件中找到 AndroidLintUnusedResources.xml 待用

＃使用流程
==
1. fork库下来，然后库名字RemoveUnuseResource保存到本地。因为java代码用了 RemoveUnuseResource 作为包名
2. 在 RemoveUnuseResource 目录上编译RemoveUnuseResource.java：javac RemoveUnuseResource.java 
3. 在 RemoveUnuseResource 目录上运行 ：java RemoveUnuseResource.RemoveUnuseResource <参数一：工程目录> <参数二：AndroidLintUnusedResources 路径>  例如 java RemoveUnuseResource.RemoveUnuseResource ./AndroidProject ./RemoveUnuseResource/AndroidLintUnusedResources.xml .
4. 检查是否误删
5. 删完之后xml文件中可能会出现很多 空行，可以使用 command+R 正则替换\n 全部替换后，使用option＋command＋L 格式化即可
