# Exp_03   Android界面组件实验

### 1.项目的基本结构

![1](https://user-images.githubusercontent.com/106793045/197791597-34965342-b5d3-4c14-9496-67da7092e73d.jpg)

- MainActivity类是 “Android ListView的用法”实验的启动类

  activity_main.xml，simple_item.xml是该实验的布局文件

- AlertDialogActivity类是”创建自定义布局的AlertDialog“实验的启动类

  activity_alertdialog.xml，alert.xml是该实验的布局

- MenuActivity类是”使用XML定义菜单“实验的启动类

  activity_menu.xml,menu_demo.xml是该实验的布局

- ActionModeActivity类是"创建上下文操作模式(ActionMode)的上下文菜单"的启动类

  activity_actionmode.xml，menu_ blank.xml，simple_item_1.xml，是该实验的布局



### 2. Android ListView的用法

- #### 思路

  ①layout文件中编写单元布局文件，在MainActivity中利用SimpleAdapter绑定组件

  ②Toast显示，在onItemClick用Toast.makeText创建Toast，设置基本属性并显示

- #### ①部分实验代码：

  ```java
  
  
          //loading name list
       List<Map<String, Object>> listItems =
                  new ArrayList<Map<String, Object>>();
          for (int i = 0; i < names.length; i++)
          {
              Map<String, Object> listItem = new HashMap<String, Object>();
              listItem.put("header", imageIds[i]);
              listItem.put("personName", names[i]);
              listItems.add(listItem);
          }
  
  
          //create a SimpleAdapter
             SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
                  R.layout.simple_item,
                  new String[] { "header" , "personName"},
                  new int[] { R.id.header , R.id.name});
          ListView list = (ListView) findViewById(R.id.mylist);
    	
  
          //set Adapter for ListView
          list.setAdapter(simpleAdapter);
  
  		……………………………………
  ```

  ```xml
  <! -- SIMPLE_ITEM.XML-->
      <LinearLayout
          android:layout_width="match_parent"
          android:layout_height="60dp"
          android:gravity="center_vertical"
          android:orientation="horizontal"
          >
  
          <TextView
              android:id="@+id/name"
              android:layout_width="0dp"
              android:layout_height="wrap_content"
              android:layout_weight="1"
              android:paddingLeft="10dp"
              android:textColor="@color/black"
              android:textSize="20dp" />
          <ImageView
              android:id="@+id/header"
              android:layout_width="50dp"
              android:layout_height="50dp"
              android:gravity="right"
              android:layout_marginRight="10dp"/>
  ```

- #### ②部分实验代码：

  ```java
      //Toast and change background
              list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
  
                  for(int i=0;i<parent.getCount();i++){
  
                      View item=parent.getChildAt(i);
  
                      if (position == i) {
                          item.setBackgroundResource(R.color.red);
                      } else {
                          item.setBackgroundResource(R.color.white);
                      }
                  }
                  Toast toast = Toast.makeText(MainActivity.this, names[position],Toast.LENGTH_SHORT);
                  toast.setGravity(Gravity.TOP, 0, 1150);
                  toast.show();
              }
          });
  ```

- #### 实验结果如下图：

- ![1](https://user-images.githubusercontent.com/106793045/197790461-233b2deb-3943-43ff-bdaa-cbfa2ddc9014.jpg)

### 3.创建自定义布局的AlertDialog

- #### 思路：

  ①layout文件中编写alert.xml作为AlertDialog的布局文件

  ②通过AlertDialog.builder设置builder.setView，通过builder.show()进行显示AlertDialog

- #### ①部分实验代码：

  ```xml
  <androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:app="http://schemas.android.com/apk/res-auto"
      xmlns:tools="http://schemas.android.com/tools"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      android:backgroundTint="#222121">
  
      <TextView
          android:id="@+id/textView"
          android:layout_width="match_parent"
          android:layout_height="60dp"
          android:background="#FFC107"
          android:gravity="center"
          android:text="RNOROID APP"
          android:textColor="@color/white"
          android:textSize="24sp"
          app:layout_constraintEnd_toEndOf="parent"
          app:layout_constraintHorizontal_bias="0.0"
          app:layout_constraintStart_toStartOf="parent"
          app:layout_constraintTop_toTopOf="parent" />
  
      <TableLayout
          android:layout_width="match_parent"
          android:layout_height="144dp"
          android:background="#F4F4F4"
          app:layout_constraintEnd_toEndOf="@+id/textView"
          app:layout_constraintStart_toStartOf="@+id/textView"
          app:layout_constraintTop_toBottomOf="@+id/textView">
  
          <TableRow
              android:layout_width="match_parent"
              android:layout_height="45dp"
              android:paddingLeft="3dp"
              android:paddingRight="3dp">
  
              <EditText
                  android:id="@+id/editTextTextPersonName"
                  android:layout_width="411dp"
                  android:layout_height="match_parent"
                  android:ems="10"
  
                  android:hint="Username"
                  android:inputType="textPersonName" />
          </TableRow>
  					…………………………
  ```
  
- #### ②部分实验代码：

  ```java
     AlertDialog.Builder builder = new AlertDialog.Builder(this);
     builder.setView(View.inflate(this, R.layout.alert, null));
     builder.show();
  ```

- #### 实验结果如下图：

- ![2](https://user-images.githubusercontent.com/106793045/197790422-5a413fc2-b08a-4db2-80ac-8289cda9974c.jpg)

### 4.使用XML定义菜单

- #### 思路：

  ①创建menu_demo.xml并编写菜单的布局

  ②在MenuActivity中用onCreateOptionsMenu设置菜单

- #### ①部分实验代码：

  ```xml
  <menu>
      <item android:title="字体大小">
          <menu >
              <item
                  android:id="@+id/font_small"
                  android:title="小" />
              <item
                  android:id="@+id/font_mid"
                  android:title="中" />
              <item
                  android:id="@+id/mi_big"
                  android:title="大" />
          </menu>
      </item>
  </menu>
  ```
  
- #### ②部分实验代码：

  ```java
    public boolean onCreateOptionsMenu(Menu m)
      {
          this.getMenuInflater().inflate(R.menu.menu_demo, m);
          return true;
      }
  ```

- #### 实验结果如下图：

- ![2](https://user-images.githubusercontent.com/106793045/197790471-bbfddd29-e5a2-4540-bbc5-fd88a8c255dc.jpg)

### 5.创建上下文操作模式(ActionMode)的上下文菜单

- #### 思路：

  ①创建菜单样式menu_blank，使用simpleAdapter创建List

  ②调用 startActionMode()启用ActionMode，实现ActionMode.callback回调

  ③ 定义了selected以及List<Boolean> selected_posistion，selected用于记录被选中了几个，且利用了selected_posistion集合记录了List中每个view是否被点中，以便改变颜色，进行selected的动态变化。

  

- #### 部分代码：

- #### ①部分实验代码：

  ```java
   //create SimpleAdapter
  
  private String[] names = new String[]
              { "One", "Two", "Three", "Four","Five"};
  ……………………
  
  SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
                  R.layout.simple_item_1,
                  new String[] { "Name"},
                  new int[] { R.id.name});
    ListView list = (ListView) findViewById(R.id.actionModeList);
  
   list.setAdapter(simpleAdapter);
  
       //ActionMode.Callback
   private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
  
  
          @Override
          public boolean onCreateActionMode(ActionMode mode, Menu menu) {
  
              MenuInflater inflater = mode.getMenuInflater();
              inflater.inflate(R.menu.menu_blank, menu);
              return true;
          }
          @Override
          public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
  
              actionMode.setTitle(selected + " selected");
              return true;
  
          }
      	
      	…………
    };
      
  ```

  

- #### ②部分实验代码：



```java
    ActionMode actionMode;
    int selected = 0;


    private  List<Boolean> selected_posistion=new ArrayList<Boolean>();
    
    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

      if(actionMode==null) actionMode=startActionMode(actionModeCallback);

      selected_posistion.set(position,!selected_posistion.get(position));

            if(selected_posistion.get(position)){
                
                    view.setBackgroundResource(R.color.purple_200);
                
                         selected++;
                
                } else {
                
         			 view.setBackgroundResource(R.color.white);
                
         				 selected--;
                }

            actionModeCallback.onActionItemClicked(actionMode,null);
        }
    });

}
```

- #### 实验结果如下图：

- ![2](https://user-images.githubusercontent.com/106793045/197790403-76256dce-a39a-486e-8d4b-790bc2544add.jpg)

