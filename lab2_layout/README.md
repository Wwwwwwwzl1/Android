# Exp_02 Android 界面布局

### 1.项目的基本结构

![1](https://user-images.githubusercontent.com/106793045/194738296-25f21b4e-6f80-4630-9c26-d1298f8840bf.jpg)



- linearLayoutActivity类是线性布局实验的启动类

  activity_linearlayout.xml是该实验的布局

  

- TableLayoutActivity类是表格布局实验的启动类

  activity_tablelayout.xml是该实验的布局

  

- ConStraintLayout_1Activity类是约束布局1实验的启动类

  activity_constraintlayout_1.xml是该实验的布局

  

- ConStraintLayout_2Activity类是约束布局2实验的启动类

  activity_constraintlayout_2.xml是该实验的布局

  

### 2. 线性布局实验

- #### 思路

  利用LinearLayout嵌套，首先放一个垂直的LinearLayout布局，内层再嵌套四个水平的LinearLayout，除第三行设置1:1:1:1权重外，第一和第二行设置1:1.3:1:1,第四行设置1:1.2:1:1。

- #### 部分实验代码：

  ```xml
  <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      android:background="@color/black"
      android:orientation="vertical"
      android:paddingLeft="4dp"
      android:paddingTop="5dp"
      android:paddingRight="4dp">
      <LinearLayout
          android:layout_width="match_parent"
          android:layout_height="50dp"
          android:orientation="horizontal">
          <TextView
              android:layout_width="0dp"
              android:layout_height="50dp"
              android:layout_weight="1"
              android:background="@drawable/border"
              android:gravity="center"
              android:text="One,One"
              android:textColor="#7E7E7E"
              android:textSize="12dp" />
                      ……………………………………
  ```

- #### 实验结果如下图：

- ![2](https://user-images.githubusercontent.com/106793045/194738298-dc4b1728-8bdb-426d-90d0-24c472d2f6d2.jpg)



### 3.表格布局实验

- #### 思路：

  ①利用了Android Studio布局编辑器实现

  ②左右对齐在两个TextView在子元素中进行gravity设置即可完成

- #### 部分实验代码：

  ```xml
  <TableLayout xmlns:android="http://schemas.android.com/apk/res/android"
      android:layout_width="match_parent"
      android:layout_height="match_parent">
      <TableRow
          android:layout_width="match_parent"
          android:layout_height="match_parent">
          <TextView
              android:id="@+id/textView1"
              android:layout_width="246dp"
              android:layout_height="wrap_content"
              android:paddingLeft="25dp"
              android:text="open..."
              android:textSize="20sp" />
  
          <TextView
              android:id="@+id/textView2"
              android:layout_width="83dp"
              android:layout_height="match_parent"
              android:gravity="right"
              android:text="Ctrl-O"
              android:textSize="20sp" />
      </TableRow>
                       ……………………………………
  ```
  
- #### 实验结果如下图：

- ![3](https://user-images.githubusercontent.com/106793045/194738301-0f4d0466-ab08-4b4f-8a20-1502872a94ec.jpg)



### 4.约束布局1实验

- #### 思路：

  利用了Android Studio布局编辑器实现

- #### 部分实验代码：

```xml
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#FFFFFF">
    
    <Button
        android:id="@+id/button19"
        android:layout_width="80dp"
        android:layout_height="48dp"
        android:layout_marginStart="8dp"
        android:backgroundTint="#E8E3E3"
        android:text="="
        android:textColor="#000000"
        app:layout_constraintStart_toStartOf="@+id/guideline11"
        app:layout_constraintTop_toTopOf="@+id/button18" />
    <Button
        android:id="@+id/button17"
        android:layout_width="80dp"
        android:layout_height="48dp"
        android:layout_marginEnd="16dp"
        android:backgroundTint="#E8E3E3"
        android:text="."
        android:textColor="#000000"
        app:layout_constraintEnd_toStartOf="@+id/button18"
        app:layout_constraintTop_toTopOf="@+id/button18" />
    <Button
        android:id="@+id/button18"
        android:layout_width="85dp"
        android:layout_height="48dp"
        android:layout_marginEnd="8dp"
        android:backgroundTint="#E8E3E3"
        android:text="0"
        android:textColor="#000000"
        app:layout_constraintEnd_toStartOf="@+id/guideline11"
        app:layout_constraintTop_toTopOf="@+id/guideline15" />

    <Button
        android:id="@+id/button20"
        android:layout_width="80dp"
        android:layout_height="48dp"
        android:layout_marginStart="16dp"
        android:backgroundTint="#E8E3E3"
        android:text="-"
        android:textColor="#000000"
        app:layout_constraintStart_toEndOf="@+id/button19"
        app:layout_constraintTop_toTopOf="@+id/button18" />
    
        							 ……………………………………
```

- #### 实验结果如下图:

- ![4](https://user-images.githubusercontent.com/106793045/194738300-f039fc99-d42d-4a41-867c-fd2eaeb77e07.jpg)



### 4.约束布局2实验

- #### 思路：

  ①利用了Android Studio布局编辑器实现

  ②对于菜单栏可以设置水平链

- #### 部分实验代码：

  ```xml
  <androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:app="http://schemas.android.com/apk/res-auto"
      xmlns:tools="http://schemas.android.com/tools"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      android:background="#FFFFFF"
      android:backgroundTint="#FFFFFF"
      tools:layout_editor_absoluteX="9dp"
      tools:layout_editor_absoluteY="14dp">
      <TextView
          android:id="@+id/textView12"
          android:layout_width="113dp"
          android:layout_height="77dp"
          android:layout_marginStart="12dp"
          android:background="#009688"
          android:gravity="center"
          android:text="MARS"
          android:textColor="#FFFFFF"
          app:layout_constraintBottom_toTopOf="@+id/guideline2"
          app:layout_constraintStart_toStartOf="@+id/guideline7"
          app:layout_constraintTop_toTopOf="@+id/guideline16"
          app:layout_constraintVertical_bias="0.0" />
      <TextView
          android:id="@+id/textView7"
          android:layout_width="119dp"
          android:layout_height="77dp"
          android:layout_marginEnd="12dp"
          android:background="#009688"
          android:gravity="center"
          android:text="DCA"
          android:textColor="#FFFFFF"
          app:layout_constraintBottom_toTopOf="@+id/guideline2"
          app:layout_constraintEnd_toStartOf="@+id/guideline7"
          app:layout_constraintTop_toTopOf="@+id/guideline16"
          app:layout_constraintVertical_bias="0.0" />
  
      <ImageView
          android:id="@+id/imageView2"
          android:layout_width="60dp"
          android:layout_height="52dp"
          android:layout_marginTop="13dp"
          android:src="@drawable/double_arrows"
          app:layout_constraintEnd_toEndOf="parent"
          app:layout_constraintHorizontal_bias="0.498"
          app:layout_constraintStart_toStartOf="parent"
          app:layout_constraintTop_toTopOf="@+id/guideline16" />
  
      <ImageView
          android:id="@+id/imageView3"
          android:layout_width="93dp"
          android:layout_height="81dp"
          android:layout_marginEnd="156dp"
          android:src="@drawable/galaxy"
          app:layout_constraintBottom_toTopOf="@+id/button2"
          app:layout_constraintEnd_toEndOf="parent"
          app:layout_constraintTop_toTopOf="@+id/guideline2"
          app:layout_constraintVertical_bias="0.557" />
  
      <ImageView
          android:id="@+id/imageView4"
          android:layout_width="39dp"
          android:layout_height="29dp"
          android:layout_marginEnd="24dp"
          android:src="@drawable/rocket_icon"
          app:layout_constraintBottom_toTopOf="@+id/button2"
          app:layout_constraintEnd_toStartOf="@+id/imageView3"
          app:layout_constraintHorizontal_bias="1.0"
          app:layout_constraintStart_toStartOf="parent"
          app:layout_constraintTop_toTopOf="@+id/guideline2"
          app:layout_constraintVertical_bias="0.551" />
  								 ……………………………………
  ```

- #### 实验结果如下图：

- ![5](https://user-images.githubusercontent.com/106793045/194738303-bfe77432-22e7-4051-9055-d808a4f60d34.jpg)