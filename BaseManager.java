import java.sql.*;
import java.util.*;


public class BaseManager {
 private String url; //Путь к серверу базы 
 private String sql; //Строка запроса
 private Connection conn; //Объект соединения
 private Statement stmt;//Оператор-объект для посылки SQL операторов в драйвер
 private ResultSet rs; //ResultSet объект 
 private boolean eLoadDriver; //Ошибка при загрузке драйвера
 private boolean eConBase; //Ошибка при соединении
 private int kolVer; //Количество проверенных идентификаторов наплавки
 
  
 //Конструктор класса 
 public BaseManager(){
   eLoadDriver=false;
   eConBase=false;
   stmt=null;
   rs=null;
   kolVer=0;
  // url="jdbc:mysql:///mysql?user=root";
  //Грузим дрова
 // try {
   //Class.forName("org.gjt.mm.mysql.Driver").newInstance();
  // Class.forName("com.mysql.jdbc.Driver");//.newInstance();
 // }
 // catch (Exception ex){
 //  eLoadDriver=true;	  
  // System.out.print("Метод BaseManager.BaseManager():"+ex.getMessage());	  
 // }
  
}
 
 //Метод для соединения с базой данных
 public boolean connect(){
  if (!eLoadDriver){ //Если дрова загружены 
   try{
	 conn=DriverManager.getConnection(url); //Соединение
	 //Выдаём информацию по соединению
	 DatabaseMetaData dma = conn.getMetaData ();
	 System.out.println("\nConnected to " + dma.getURL());
	 System.out.println("Driver " +dma.getDriverName());
     System.out.println("Version " +dma.getDriverVersion());
	 System.out.println("\n");
   }catch (SQLException ex){
//	eConBase=true;	  
    System.out.print("Метод BaseManager.connect():"+ex.getMessage()+"\n");
    System.out.print("SQLState:"+ex.getSQLState()+"\n");
    System.out.print("Cause:"+ex.getCause() +"\n");
    System.out.print("LocalizedMessage:"+ex.getLocalizedMessage()+"\n");
    System.out.print("VendorError:"+ex.getErrorCode()+"\n");
   }
   return !eConBase;
  }
  else
   return !eLoadDriver;	  
 }
 
 //Метод для разъединения с базой данных 
 public void disconnect(){
  try {
   if (rs!=null) rs.close();	  
   if (stmt!=null) stmt.close();
   if (conn!=null) conn.close();
  }catch (SQLException ex){
   System.out.print("Класс BaseManager:"+ex.getMessage());	   
  }
 }
 
 //Метод записывает панель ПИД - регулятор в базу
 public void pidPanelSave(Double vars[]){
  if (!eConBase){ //Если есть соединение с базой
   System.out.print("lawCode:"+vars[0].toString()+"\n"); //Закон
   System.out.print("idNap:"+vars[1].toString()+"\n"); //Идентификатор наплавки
   System.out.print("kpField:"+vars[2].toString()+"\n"); //Kp
   System.out.print("TiField:"+vars[3].toString()+"\n"); //Ti
   System.out.print("TdField:"+vars[4].toString()+"\n"); //Td
   System.out.print("ToField:"+vars[5].toString()+"\n"); //To
   System.out.print("lspField:"+vars[6].toString()+"\n"); //lsp   
  }
 }
 
 //Метод записывает панель Процесс
 public void procPanelSave(Double vars[]){
  if (!eConBase){ //Если есть соединение с базой
   System.out.print("eCode:"+vars[0].toString()+"\n"); //Электрод
   System.out.print("idNap:"+vars[1].toString()+"\n"); //Идентификатор наплавки
   System.out.print("UaField:"+vars[2].toString()+"\n"); //Ua
   System.out.print("UkField:"+vars[3].toString()+"\n"); //Uk
   System.out.print("idField:"+vars[4].toString()+"\n"); //id
   System.out.print("UdField:"+vars[5].toString()+"\n"); //Ud
   System.out.print("TdugField:"+vars[6].toString()+"\n"); //Tdug   
  }
	 
 }
 
 //Метод запсиывает данные Процесса в базу
 public void procDataSave(Vector procData){
  int i,j;
  String resRow[]=new String[6];
  String temp;
  if (!eConBase){ //Если есть соединение с базой
   Iterator itr=procData.iterator();
   while(itr.hasNext()) {
 	temp=itr.next().toString(); 
	 //Разбор строки
	 i=0; j=0;resRow[0]="";
	 while(temp.length()!=i){
	  temp.charAt(i);
	  if (temp.charAt(i)==',') {j++;resRow[j]="";i++;}
	  if (temp.charAt(i)!='[' && temp.charAt(i)!=']') 
 	   resRow[j]+=temp.charAt(i);
	  i++;	
	 }
	 System.out.print(Double.parseDouble(resRow[0])+" "+Double.parseDouble(resRow[1])+" "+Double.parseDouble(resRow[2])+" "+Double.parseDouble(resRow[3])+" "+Double.parseDouble(resRow[4])+" "+Double.parseDouble(resRow[5])+"\n");
   }
  }
 }
 
//Метод сохраняет данные о наплавке(Идентификатор, Дата наплавки, Пользователь
public void naplavData(long idNap,int UserNap){
 String tmpStr;	
 if (!eConBase){ //Если есть соединение с базой
  Calendar calendar=Calendar.getInstance();
  tmpStr="idNap:"+Long.toString(idNap)+" DateNap:"+calendar.get(Calendar.DAY_OF_MONTH)+"."+calendar.get(Calendar.MONTH)+"."+calendar.get(Calendar.YEAR)+"UserNap:Шкурников Е.В.";
  System.out.print(tmpStr+"\n");
 }
}

 
//Метод проверяет сгенерированный идентификатор наплавки
 public boolean verifyIdNap(long idNap){
  if (kolVer<100){
   kolVer++;
   return false;
  }
  else
  {
   kolVer=0;	  
   return true;
  }
 }
 
}
