package com.uhf.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
* 用法：
* DBHelper dbHelper = new DBHelper(this);
* dbHelper.createDataBase();
* SQLiteDatabase db = dbHelper.getWritableDatabase();
* Cursor cursor = db.query()
* db.execSQL(sqlString);
* 注意：execSQL不支持带;的多条SQL语句
* 见execSQL的源码注释 (Multiple statements separated by ;s are not supported.)
* 将把assets下的数据库文件直接复制到DB_PATH，但数据库文件大小限制在1M以下
* 如果有超过1M的大文件，则需要先分割为N个小文件，然后使用copyBigDatabase()替换copyDatabase()
*/
public class DBHelper extends SQLiteOpenHelper {
   // 用户数据库文件的版本
   private static final int DB_VERSION = 1;
   // 数据库文件目标存放路径为系统默认位置，cn.arthur.examples 是你的包名
   private static String DB_PATH = "/data/data/com.uhf.rfid/databases/";
   /*
   //如果你想把数据库文件存放在SD卡的话
   private static String DB_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "目标路径";
   */
   //下面两个静态变量分别是目标文件的名称和在assets文件夹下的文件名
   private static String DB_NAME = "asset.db";
   private static String ASSETS_NAME = "asset.db";

   private SQLiteDatabase myDataBase = null;
   private final Context myContext;

   /** 
    * 如果数据库文件较大，使用FileSplit分割为小于1M的小文件
    * 此例中分割为 hello.db.101    hello.db.102    hello.db.103
    */
   //第一个文件名后缀
   private static final int ASSETS_SUFFIX_BEGIN = 101;
   //最后一个文件名后缀
   private static final int ASSETS_SUFFIX_END = 103;
   
   /**
    * 在SQLiteOpenHelper的子类当中，必须有该构造函数
    * @param context    上下文对象
    * @param name        数据库名称
    * @param factory    一般都是null
    * @param version    当前数据库的版本，值必须是整数并且是递增的状态
    */
   public DBHelper(Context context, String name, CursorFactory factory, int version) {
       //必须通过super调用父类当中的构造函数
       super(context, name, null, version);
       this.myContext = context;
   }
   
   public DBHelper(Context context, String name, int version){
       this(context,name,null,version);
   }

   public DBHelper(Context context, String name){
       this(context,name,DB_VERSION);
   }
   
   public DBHelper (Context context) {
       this(context, DB_PATH + DB_NAME);
   }
   
   public void createDataBase() throws IOException{
       boolean dbExist = checkDataBase();
       if(dbExist)
       {
           //数据库已存在，不做任何操作
       }
       else
       {
           //创建数据库
           try {
               File dir = new File(DB_PATH);
               if(!dir.exists()){
                   dir.mkdirs();
               }
               File dbf = new File(DB_PATH + DB_NAME);
               if(dbf.exists()){
                   dbf.delete();
               }
               SQLiteDatabase.openOrCreateDatabase(dbf, null);
               // 复制asseets中的数据库文件到DB_PATH下
               copyDataBase();
           } catch (IOException e) {
               throw new Error("数据库创建失败");
           }
       }
   }
   
   //检查数据库是否有效
   private boolean checkDataBase(){
       SQLiteDatabase checkDB = null;
       String myPath = DB_PATH + DB_NAME;
       try{            
           checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
       }catch(SQLiteException e){
           //database does't exist yet.
       }
       if(checkDB != null){
           checkDB.close();
       }
       return checkDB != null ? true : false;
   }

   /**
    * 复制assets文件中的数据库到指定路径
    * 使用输入输出流进行复制
   **/
   private void copyDataBase() throws IOException{
       
       InputStream myInput = myContext.getAssets().open(ASSETS_NAME);
       String outFileName = DB_PATH + DB_NAME;
       OutputStream myOutput = new FileOutputStream(outFileName);
       byte[] buffer = new byte[1024];
       int length;
       while ((length = myInput.read(buffer))>0){
           myOutput.write(buffer, 0, length);
       }
       myOutput.flush();
       myOutput.close();
       myInput.close();
   }
   
   //复制assets下的大数据库文件时用这个
   private void copyBigDataBase() throws IOException{
       InputStream myInput;
       String outFileName = DB_PATH + DB_NAME;
       OutputStream myOutput = new FileOutputStream(outFileName);
       for (int i = ASSETS_SUFFIX_BEGIN; i < ASSETS_SUFFIX_END+1; i++) {
           myInput = myContext.getAssets().open(ASSETS_NAME + "." + i);
           byte[] buffer = new byte[1024];
           int length;
           while ((length = myInput.read(buffer))>0){
               myOutput.write(buffer, 0, length);
           }
           myOutput.flush();
           myInput.close();
       }
       myOutput.close();
   }
   
   @Override
   public synchronized void close() {
       if(myDataBase != null){
           myDataBase.close();
       }
       super.close();
   }
   
   
   @Override
   public void onCreate(SQLiteDatabase db) 
   {
   }
   
   /**
    * 数据库创建时执行，如果不是预制的数据库，可以在这些写一些创建表和添加初始化数据的操作
    * 如：db.execSQL("create table cookdata (_id integer primary key,cook_name 
    * varchar(20),cook_sort varchar(20))");
    */
   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
   {
       /**
        * 数据库升级时执行，前面我们定义的DB_VERSION就是数据库版本，在版本升高时执行
        * 一般做一些数据备份和恢复到新数据库的操作。
        */
   }
}

