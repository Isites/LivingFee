### 日志

> 日志主要通过覆写EditText实现了一个富文本编辑功能主要实现的功能如下

* 加粗选中的字体

* 引用选中的段落

* 段落列表

* 添加图片的功能

  ```java
  editor.setStyle(TextStyle.LI);
  editor.render();
  /**-----------------------------*/
  UriImageGetter getter = new UriImageGetter(editor.getWidth(), this);
  ImageStyle imageStyle = new ImageStyle(editor,getter.getDrawable(uri.toString()), uri.toString());
  editor.setStyleInstance(imageStyle);
  editor.render();
  ```

  ​

