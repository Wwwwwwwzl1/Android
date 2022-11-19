# 	Exp_Mid Android NotePad笔记本应用

## ①基础功能：

### 1. NoteList中显示条目增加时间戳显示

- #### 思路

  ①note_item中设置TextView以显示时间

  ②在EditActivity类中新增将时间转化为特定格式的方法dateEdit()，当判断编辑与旧数据有变动时进行数据更新

- #### 部分实验代码：

  note_item.xml

  ```xml
    <TextView
          android:id="@+id/time"
          android:layout_width="wrap_content"
          android:layout_height="wrap_content"
          android:text="Time"
          android:textSize="16dp"
          android:textColor="@color/near_black"/>
  ```

  EditActivity.java

  ```java
      public String dateEdit () {
              Date date = new Date();
              SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
              return simpleDateFormat.format(date);
          }
  ```

  #### 实验截图：  
  
  图01： 原始数据加载
  
  图02：新增笔记中显示当前时间戳
  
  <img src="https://user-images.githubusercontent.com/106793045/202859958-550af9f8-10ce-4bce-91b7-40385ee84037.png" width='250px' alt='01'><img src="https://user-images.githubusercontent.com/106793045/202859960-f586b3fb-cb01-4d1d-bde9-4d80cab9ef01.png" width="250px">  
  
   

### 2.添加笔记查询功能（根据内容查询）

- #### 思路：

  ①利用SearchView实现

  ②界面上增加搜索的item和点击后触发输入关键词并进行搜索

  ③定义关键词过滤规则存储过滤后的笔记信息并刷新界面

- #### 部分实验代码：

  main_menu.xml

  ```xml
     <item
          android:id="@+id/conten_search"
          android:icon="@drawable/ic_search_black_24dp"
          app:showAsAction="always"
          app:actionViewClass="androidx.appcompat.widget.SearchView"
          android:title="Search"
          />
  ```

  NoteAdapter.java

  ```java
  class FilterSearch extends Filter  {
       //定义过滤规则  
          @Override     
         @Override
          protected FilterResults performFiltering(CharSequence charSequence) {
              FilterResults result = new FilterResults();
              List<Note> resultlist;
   
              if (TextUtils.isEmpty(charSequence)) {  //当关键字为空的时候，显示所有的数据
                  resultlist = backupList;
              } else {
                  //把符合条件的数据对象添加到集合中
                  resultlist = new ArrayList<>();
                  for (Note note : backupList) {
                      if (note.getContent().contains(charSequence)) {
                          resultlist.add(note);
                      }
  
                  }
              }
              result.values = resultlist; //将得到的集合保存到FilterResults的value变量中
              result.count = resultlist.size();//将集合的大小保存到FilterResults的count变量中
              return result;
          }
          //在publishResults方法中告知适配器要更新界面
          @Override
          protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
              noteList = (List<Note>)filterResults.values;
              if (filterResults.count>0){
                  notifyDataSetChanged();//告知数据发生了改变
              }else {
                  notifyDataSetInvalidated();//告知数据失效了
              }
          }
    }
  ```
  
  #### 实验截图：
  
  图03： 点击右上角搜索icon显示输入框  
  
  图04： 实时根据搜索词进行筛选显示
  
  <img src="https://user-images.githubusercontent.com/106793045/202859962-ceb762bc-8682-4b98-938c-a40efb72cb48.png" width='250px' alt='03'/><img src="https://user-images.githubusercontent.com/106793045/202859964-01f1e9cd-0701-44b1-8a4d-784d4f91490a.png" width='250px' alt='04' />

## ②附加功能：

### 1. 笔记标签分类，可自定义新标签，按标签分类统计/查询，标签颜色对应编辑器的颜色及该笔记在主页显示出来的颜色

* #### 思路

  ①新增数据项属性标签，可自定义标签名称，选择颜色进行新增【提供默认标签和无标签状态且总标签数设置上限为9】

  ②若监听到触发根据标签分类查询的方法按该标签对象对已有数据项进行筛选刷新展示

  ③不同类的标签有不同的颜色，标签颜色对应编辑器的颜色及该笔记在主页显示出来的颜色

* #### 部分实验代码：

  MainAcitivty.java

  ```java
  //新增自定义标签
     add_tag.setOnClickListener(new OnClickListener() {
                      @Override
                      public void onClick(View v) {
         if(sharedPreferences.getString("tagList","").split("_").length<9) {//设置标签只能有9个
         AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
   View inflate = View.inflate(MainActivity.this, R.layout.add_tag, null);
   
             //获取新增标签界面，9个颜色方格的Textview，用于进行设置颜色
           colorlist.add((TextView) inflate.findViewById(R.id.tag_1));
  		 colorlist.add((TextView) inflate.findViewById(R.id.tag_2));
  		 colorlist.add((TextView) inflate.findViewById(R.id.tag_3));
             ............
  		 colorlist.add((TextView) inflate.findViewById(R.id.tag_8));
           colorlist.add((TextView) inflate.findViewById(R.id.tag_9));
             
              builder.setView(inflate).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                  
      		@Override
         public void onClick(DialogInterface dialog, int which) {
             
       List<String> tagList = Arrays.asList(sharedPreferences.getString("tagList", null).split("_")); //获取tags
             
     EditText tag_name=(EditText)inflate.findViewById(R.id.tag_name);
  
  List<String> tagListColor = Arrays.asList(sharedPreferences.getString("tagListColor", null).split("_")); //获取tagColor
             
  String name = tag_name.getText().toString();
            
   		if (!tagList.contains(name)) {//标签名字不重复
                          
  SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences
      (context);
                          
   	 String oldtagList = sharedPreferences.getString("tagList", null);
              
  	String newtagList = oldtagList + "_" + name;
              
      String oldtagListColor=sharedPreferences.getString("tagListColor", null);
              
       String newtagListColor = oldtagListColor + "_" + choose_Color;
  
     SharedPreferences.Editor editor = sharedPreferences.edit();
     
              editor.putString("tagList", newtagList);
                                                 	 			 		             editor.putString("tagListColor",newtagListColor);
              
                 editor.commit();
                 refreshTagList();
  			...........
  
    //统计不同标签的笔记数
      public List<Integer> CountTagNum(List<String> StringList){
          //初始化numbers数组
          Integer[] numbers = new Integer[StringList.size()];
          //初始化所有类别的数量为0
          for(int i = 0; i < numbers.length; i++) numbers[i] = 0;
          for(int i = 0; i < noteList.size(); i++){
              numbers[noteList.get(i).getTag() - 1] ++;
          }
          numbers[0]=noteList.size();
          return Arrays.asList(numbers);
      }
              
     //生成标签列
      private void refreshTagList() {
  SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences
      (context);
          
          List<String> tagList = Arrays.asList(sharedPreferences.getString("tagList", null).split("_")); //获取tags
          List<String> tagListColor = Arrays.asList(sharedPreferences.getString("tagListColor", null).split("_")); //获取tagColors
          tagAdapter = new TagAdapter(context, tagList, 			                     CountTagNum(tagList),tagListColor);
          lv_tag.setAdapter(tagAdapter);
          tagAdapter.notifyDataSetChanged();
      }         
   
     //新增标签页面选择颜色，选中标签的为红框
   public void chooseColor(View viewed){
          for (TextView view:
                colorlist) {
              switch (view.getId()){
  
                  case R.id.tag_1:
                      view.setBackgroundResource(R.color.tag_1);break;
                  case R.id.tag_2:
                      view.setBackgroundResource(R.color.tag_2);break;
           				 ........
                  case R.id.tag_8:
                      view.setBackgroundResource(R.color.tag_8);break;
                  case R.id.tag_9:
                      view.setBackgroundResource(R.color.tag_9);break;
              }
          }
  
          switch (viewed.getId()){
  
              case R.id.tag_1: choose_Color="tag-01-color";
                  viewed.setBackgroundResource(R.drawable.textview_border2);break;
              case R.id.tag_2:choose_Color="tag-02-color";
                  viewed.setBackgroundResource(R.drawable.textview_border3);break;
             		 ........
              case R.id.tag_8:choose_Color="tag-08-color";
                  viewed.setBackgroundResource(R.drawable.textview_border8);break;
              case R.id.tag_9:choose_Color="tag-09-color";
                  viewed.setBackgroundResource(R.drawable.textview_border9);break;
          }
  
      }
  ```

  

  TagAdapter.java

  ```java
  //不同类别标签进行相应颜色的渲染
  LinearLayout  list=(LinearLayout)v.findViewById(R.id.tag_item);
  switch (tagListColor.get(position)){
  
          case "tag-01-color":list.setBackgroundResource(R.color.tag_1_color);break;
          case "tag-02-color":list.setBackgroundResource(R.color.tag_2_color);break;
          		 ........
          case "tag-08-color":list.setBackgroundResource(R.color.tag_8_color);break;
          case "tag-09-color":list.setBackgroundResource(R.color.tag_9_color);break;
  
  
  }
  ```

  

  

  #### 实验截图：

  图05：左菜单显示统计对应tag的笔记数 

  图06：点击add new tag 输入自定义tag名称，选择颜色

  <img src="https://user-images.githubusercontent.com/106793045/202859965-d87ae3b9-33d7-412d-ac1a-edea120809e5.png" width='250px' alt='13'/><img src="https://user-images.githubusercontent.com/106793045/202859966-6de1df6b-cfb5-4545-93a5-cf329b7095fe.png" width='250px' alt='14'/> 

  

  

  图07：新增’new tag已显示                     

  图08：点击life标签项只显示该标签下的笔记

  <img src="https://user-images.githubusercontent.com/106793045/202859968-8f05f064-9f9c-4273-a44e-3a6ee9299924.png" width='250px' alt='15'/><img src="https://user-images.githubusercontent.com/106793045/202859971-0bb69236-10d6-4744-a9ef-aede2558295b.png" width='250px' alt='16'/>



图09：not tag 标签的颜色            

图10：所处not tag标签笔记条目的颜色

<img src="https://user-images.githubusercontent.com/106793045/202859973-8ee5db0d-e3df-4bd5-8bac-9fb084ff9e3e.png" width='250px' alt='15'/><img src="https://user-images.githubusercontent.com/106793045/202859975-b8c84a04-d1af-4e5e-aa1d-f99d514b4a0e.png" width='250px' alt='15'/>



图11：属于not tag标签类别的笔记，点进去编辑器的颜色

图12：属于study标签类别的笔记，点进去编辑器的颜色

<img src="https://user-images.githubusercontent.com/106793045/202859976-c15c19fc-5a7c-4296-9cf0-3a854fe7164a.png" width='250px' alt='15'/><img src="https://user-images.githubusercontent.com/106793045/202859980-01032783-a115-4e09-8f2a-c4959483375b.png" width='250px' alt='15'/>

### 2.  按时间正序或倒序、按标签的顺序排列笔记，长按单个删除及全部删除

* #### 思路

  ①编写sortNotes及sortNotesByTag相关方法对数据库中已有的笔记项进行排序

  ②利用onItemLongClick方法进行删除操作，操作完成后结果通知界面刷新展示

* #### 部分实验代码：

  MainActivity.java

  ```java
  //按模式时间排序笔记
      public void sortNotes(List<Note> noteList, final int mode) {
          Collections.sort(noteList, new Comparator<Note>() {
              @Override
              public int compare(Note o1, Note o2) {
                  try {
                      if (mode == 1) {
                          Log.d(TAG, "sortnotes 1");
                          return npLong(dateStrToSec(o2.getTime()) - dateStrToSec(o1.getTime()));
                      }
                      else if (mode == 2) {
                          Log.d(TAG, "sortnotes 2");
                          return npLong(dateStrToSec(o1.getTime()) - dateStrToSec(o2.getTime()));
                      }
  //按标签颜色排序
  public void sortNotesByTag(List<Note> noteList, final int mode) {
  
          if(mode!=3) return;
          Collections.sort(noteList, new Comparator<Note>() {
              @Override
              public int compare(Note o1, Note o2) {
  
                          return npLong((long) (o1.getTag()-o2.getTag()));
  
              }
          });
      }
                      
                      
  ```

  

  ```java
  //刷新Listview
  public void refreshListView() {
      SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
      //initialize CRUD
      NoteOP op = new NoteOP(context);
      op.open();
  
     noteList.clear();
      noteList.addAll(op.getAllNotes());
      adapter.notifyDataSetChanged();
      if (sharedPreferences.getBoolean("reverseSort", false)) sortNotes(noteList, 2);
      else sortNotes(noteList, 1);
  
      if (sharedPreferences.getBoolean("SortByTag", false)) sortNotesByTag(noteList, 3);
      else sortNotesByTag(noteList, 1);
      op.close();
  
  
  }
  ```

  

  

  ```java
  //长按删除
  public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
          switch (parent.getId()){
              case R.id.lv:
                  final Note note = noteList.get(position);
                  new AlertDialog.Builder(MainActivity.this)
                          .setMessage("Do you want to delete this note ?")
                          .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                   NoteOP op = new NoteOP(context);
                                  op.open();
                                  op.removeNote(note);
                                  op.close();
                                  refreshListView();
                              }
  ```

  

  ```java
  //删除全部笔记
  
     new AlertDialog.Builder(MainActivity.this)
                      .setMessage("Delete All Notes ?")
                      .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          dbHelper = new NoteDatabase(context);
                          SQLiteDatabase db = dbHelper.getWritableDatabase();
                          db.delete("notes", null, null)
                          db.execSQL("update sqlite_sequence set seq=0 where name='notes'"); 
                          refreshListView();
                      }
                  }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          dialog.dismiss();
                      }
                  }).create().show();break;
  
  
  ```

  

  

  

  #### 实验截图：

  图13： 开启按时间倒序排列                     

  图14： 倒序排列后的效果

  <img src="https://user-images.githubusercontent.com/106793045/202859982-c6ed3baa-3ed7-409c-82da-0f3f6ae8c2bd.png" width='250px' alt='09'/><img src="https://user-images.githubusercontent.com/106793045/202859985-d50b55f2-89e4-4574-8505-4b19008613e5.png" width='250px' alt='10'/>

  图15： 开启按标签顺序排列               

  图16： 标签的顺序

  <img src="https://user-images.githubusercontent.com/106793045/202859987-25fd444f-2c19-4fec-ba6e-553cb09a867c.png" width='250px' alt='11'/><img src="https://user-images.githubusercontent.com/106793045/202859991-95b18839-6d60-4244-ab43-8f35ff7bad1e.png" width='250px' alt='11'/>

  

  

  图17： 标签排序后的结果             

  <img src="https://user-images.githubusercontent.com/106793045/202859988-3778a82f-7a58-4c60-bec9-3c28244baad0.png" width='250px' alt='11'/>

  

  

  图18： 删除单个笔记              

  图19： 删除所有笔记

  <img src="https://user-images.githubusercontent.com/106793045/202859997-cf238d27-420c-4c6b-8eaf-57e4b493f08c.png" width='250px' alt='11'/><img src="https://user-images.githubusercontent.com/106793045/202859994-29105bf4-31f0-482a-b3a5-30d35656556e.png" width='250px' alt='12'/>


### 3. 上传本地照片到记事本中功能(图文混合)

* #### 思路

  ①将图片插入文本。首先接收到从图库中选择到的图片，然后获取到它在手机中的储存地址，将地址保存到`path`中，然后利用SpannableString和ImageSpan，将图片插入到`EditText`中

  ②再次打开文本显示图片。首先将代表图片的字符串全部提取出来，即`<img src="xxx" />` ，然后将里面的图片地址提取出来，但是要保存`<img ....>` 这一段的起始地址和结束地址，用于将这一段替换成图片，而不要再显示出来了。

* #### 部分实验代码：

  EditAcitivty.java

  ```java
  //调用图库   
  Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                      intent1.addCategory(Intent.CATEGORY_OPENABLE);
                      intent1.setType("image/*");
                      startActivityForResult(intent1, 1);
  
  
  ```

  ```java
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  
      EditText content=findViewById(R.id.edit_content);
      super.onActivityResult(requestCode, resultCode, data);
      Bitmap bm = null;
      // 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
      ContentResolver resolver = getContentResolver();
      if (requestCode == 1) {
          try {
              // 获得图片的uri
              Uri originalUri = data.getData();
    
              //获取地址
              File file = uriToFileApiQ(originalUri,this);
              file.getAbsoluteFile();
              insertImg(file.getAbsoluteFile().toString(),content);
     
          } catch (Exception e) {
              e.printStackTrace();
              Toast.makeText(EditActivity.this, "图片插入失败", Toast.LENGTH_SHORT).show();
          }
      }
      
  }
  ```

  

  ```
  //插入图片
  private void insertImg(String path,EditText content){
      String tagPath = "<img src=\""+path+"\"/>";//为图片路径加上<img>标签
      Bitmap bitmap = BitmapFactory.decodeFile(path);
      if(bitmap != null){
          SpannableString ss = getBitmapMime(path,tagPath);
          Editable et = content.getText();
          int start = content.getSelectionStart();
          et.insert(start,ss);
          content.setText(et);
          content.setSelection(start+ss.length());
          content.setFocusableInTouchMode(true);
          content.setFocusable(true);
          content.append("\n");
          Log.d("YYPT", content.getText().toString());
      }
  }
  ```

  

  ```java
  //android10以上将uri转换成地址方法
  @RequiresApi(api = Build.VERSION_CODES.Q)
      public File uriToFileApiQ(Uri uri, Context context) {
          File file = null;
          if(uri == null) return file;
          //android10以上转换
          if (uri.getScheme().equals(ContentResolver.SCHEME_FILE)) {
              file = new File(uri.getPath());
          } else if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
              //把文件复制到沙盒目录
              ContentResolver contentResolver = context.getContentResolver();
              String displayName = System.currentTimeMillis()+ Math.round((Math.random() + 1) * 1000)
                      +"."+ MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri));
  
              try {
                  InputStream is = contentResolver.openInputStream(uri);
                  File cache = new File(context.getCacheDir().getAbsolutePath(), displayName);
                  FileOutputStream fos = new FileOutputStream(cache);
                  FileUtils.copy(is, fos);
                  file = cache;
                  fos.close();
                  is.close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
          return file;
      }
  ```

  

  ```java
  //点开笔记图片回显
  String input = old_content;
  //将图片那一串字符串解析出来,即<img src=="xxx" />
  Pattern p = Pattern.compile("<img.*?>");
  Matcher m = p.matcher(input);
  
  //使用SpannableString
  SpannableString spannable = new SpannableString(input);
  while(m.find()){
      String s = m.group();
      int start = m.start();
      int end = m.end();
      //path是去掉<img src=""/>的中间的图片路径
      String path = s.replaceAll("<img src=\"","").trim().replaceAll("\"/>","").trim();
  
      //利用spannableString和ImageSpan来替换掉这些图片
      int width = ScreenUtils.getScreenWidth(EditActivity.this);
      int height = ScreenUtils.getScreenHeight(EditActivity.this);
  
      Bitmap bitmap = ImageUtils.getSmallBitmap(path,width/2,120);
      ImageSpan imageSpan = new ImageSpan(this, bitmap);
      spannable.setSpan(imageSpan,start,end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
  
  }
  edit_content.setText(spannable);
  edit_content.append("\n");
  ```

  #### 实验截图：

  图20：图文混合

  <img src="https://user-images.githubusercontent.com/106793045/202859999-6d5907bc-fa89-4a74-b414-a388c1f64212.png" width='250px' alt='13'/> 

  



### 4. UI美化 

* #### 思路

  ①对note_item及edit_laylout的布局进行美化

* #### 部分实验代码：

  note_item.xml

  ```xml
  
  <TextView
      android:id="@+id/tv_title"
      android:layout_width="match_parent"
      android:layout_height="wrap_content"
      android:ellipsize="end"
      android:singleLine="true"
      android:text="Content"
      android:textColor="@color/white"
      android:textSize="20dp" />
  
  <TextView
    android:id="@+id/tv_content"
      android:layout_width="match_parent"
      android:layout_height="wrap_content"
      android:layout_marginTop="8dp"
      android:ellipsize="end"
      android:singleLine="true"
      android:text="Content"
      android:textColor="@color/black"
      android:textSize="16dp" />
  
  <TextView
      android:id="@+id/time"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      android:text="Time"
      android:textSize="16dp"
      android:textColor="@color/near_black"/>
  ```
  
  edit_layout.xml
  
  ```xml
  <LinearLayout
    android:id="@+id/edit_list"
      android:layout_width="match_parent"
      android:layout_height="wrap_content"
      android:orientation="vertical">
  
      <EditText
          android:id="@+id/edit_title"
          android:layout_width="match_parent"
          android:layout_height="wrap_content"
          android:layout_margin="8dp"
          android:ellipsize="none"
          android:gravity="top"
          android:hint="请输入标题..."
          android:maxLength="20"
          android:textColor="?attr/tvMainColor"
  
          android:textSize="26dp"
          android:textStyle="bold" />
  
      <EditText
          android:id="@+id/edit_content"
          android:layout_width="match_parent"
          android:layout_height="wrap_content"
          android:layout_margin="8dp"
          android:background="@null"
          android:gravity="start"
          android:hint="请输入正文...."
          android:letterSpacing="0.03"
          android:lineSpacingMultiplier="1.5"
          android:maxLines="30"
          android:minLines="20"
          android:textSize="22dp" />
  ```
  
  
  
  
  
  #### 实验截图：
  
  图21： 主页                                       
  
  图22： 编译器
  
  <img src="https://user-images.githubusercontent.com/106793045/202861261-f0a3c29b-1ffe-4829-93e5-b0e4d38ed42a.png" width='250px' alt='05'/><img src="https://user-images.githubusercontent.com/106793045/202860761-d6b8a715-8725-4113-b76a-ca92f73996c2.png" width='250px' alt='06'/>
  
  

