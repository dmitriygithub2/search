
package seach_text;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import static seach_text.BoyerMoore.boyerMoore;



public class SeachText {
    
   /***********************Входные параметры**********************/
    private static Character start_symbole; // открывающий символ блока
    private static Character end_symbole; // закрывающий символ блока
    private static String text; // текст для поиска
    
   /***********************Выходные параметры**********************/
    private static ArrayList <Integer> inx_start = new <Integer> ArrayList();
    private static ArrayList <Integer> inx_stop = new <Integer> ArrayList();
    private static ArrayList <String> results = new <String> ArrayList();
    
    
    private static void make_report(){
        ArrayList <JSONObject> report = new <JSONObject>ArrayList(); //список json объктов для отчета
        ArrayList <Integer> res = new <Integer> ArrayList(); // список длин найденных строк 
        int num = results.size(); //общее количество полученных строк
        for(int i = 0;i<results.size();i++){
           res.add(results.get(i).length()); 
        }
        int min = Collections.min(res); //минимальная длина
        int max = Collections.max(res); //максимальная длина
        double average = 0;
        if (res.size() > 0) //находим среднюю длину строки
        {
            double sum = 0;
            for (int j = 0; j < res.size(); j++) {
                sum += res.get(j);
            }
            average = sum / res.size();
        }
        
        
        JSONObject overall = new JSONObject(); //сохраняем общие данные в JSON-объект
        overall.put("Общее количество найденных блоков текста", num);
        overall.put("Cредняя длина блока текста в символах", (int)average);
        overall.put("Максимальная длина блока текста в символах", max);
        overall.put("Минимальная длина блока текста в символах", min);
        report.add(overall);
        
        for(int i = 0;i<results.size();i++) // собираем данные об каждой строке в json объекты
        {
           JSONObject find_text = new JSONObject(); 
           find_text.put("Обнаруженный текст",results.get(i));
           find_text.put("Длина текста в символах",results.get(i).length());
           Integer c = 0;  // счетчик цифр
           Integer lat = 0; // счетчик латинских смиволов
           Integer kir = 0; // счетчик кириллицы
           Integer an = 0; // счетчик других символов
           String str = results.get(i);
           for(char ch:str.toCharArray())//проверяем из чего состоит строка
           {
            if( (int)ch >47 && (int)ch<58 ){ 
                c++;
            }
            else {
                if( ((int)ch >64 && (int)ch<91)||((int)ch >96 && (int)ch<123) ){
                    lat++;
                }else
                    if(((int)ch >1039 && (int)ch<1104)) kir++;
                    else an++;
                }
            }
           
           find_text.put("Количество символов латиницы (aA-zZ)",lat);
           find_text.put("Количество символов кириллицы (аА-яЯ)",kir);
           find_text.put("Количество символов арабских цифр (0-9)",c);
           find_text.put("Количество других символов",an);
           report.add(find_text);
           
            
        }
        
        
        try (FileWriter f = new FileWriter("report.json", false)) {
            for(int i = 0;i<report.size();i++){
                f.write(report.get(i).toJSONString()); //записываем полученные json объекты в файл
                f.write("\n\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(SeachText.class.getName()).log(Level.SEVERE, null, ex);
        }
        

    }
    
    
    private static  ArrayList <String> seach_text(String t,Character start,Character stop) //поиск блоков текста
    {
      ArrayList<String> res = new <String>ArrayList();
      int index_start,index_stop;
      text = t;
      start_symbole = start;
      end_symbole = stop;

     index_start = boyerMoore(text, start_symbole.toString())+1; //поиск 1ого стартового-символа
     String txet = new StringBuffer(text).reverse().toString();
     index_stop = text.length() - (1+boyerMoore(txet, end_symbole.toString())); //поиск последнего стоп символа
     if(index_stop<index_start||(index_start==0||index_stop==0))return res = null; 
     text = text.substring(index_start-1, index_stop+1);
     String text1 = text;
     String text2 = text;
     int length = text.length();
     while(true)//поиск стартовых символов
     {
          int ind = boyerMoore(text1, start_symbole.toString());
          if(ind == -1)break;
          inx_start.add((length-text1.length())+ind);
          text1 = text1.substring(ind+1);
     }
     while(true)//поиск конечных символов
     {
          int ind = boyerMoore(text2, end_symbole.toString());
          if(ind == -1)break;
          inx_stop.add((length-text2.length())+ind);
          text2 = text2.substring(ind+1);
     }
     
    
     
    for(int i=0;i<inx_start.size();i++){
        for(int j=0;j<inx_stop.size();j++){
          
            if(inx_start.get(i) < inx_stop.get(j)){
               res.add(text.substring(inx_start.get(i)+1,inx_stop.get(j))); //сохраняем все найденные строки 
            }
        
        }
    }
      
     return res;  
        
    }
    
    
    public static void main(String[] args) {
        
        Scanner in = new Scanner(System.in);
        boolean flag_1 = true,flag_2 = true,flag_3 = true;
        String buf;
        System.out.println("Вас приветствует программа для поиска по тексту.");
        System.out.println("Введите строку для поиска, открывающий и закрывающий символы.");
        System.out.println("Доброго дня и удачного поиска!");
        
        while(true){
        if(flag_1 == true){    
            System.out.println("Введите текст для поиска:");
            text = in.nextLine();
            
            if(text.length() == 0){
                System.out.println("Пожалуйста введите текст, иначе просто негде искать!");
                continue;
            }
            flag_1 = false;
        }
        if(flag_2 == true){
            System.out.println("Введите cтартовый-символ:");
            buf = in.nextLine();
             if(buf.length() == 0){
                System.out.println("Вы не ввели символ!");
                continue;
            }
            if(buf.length()>1){
                System.out.println("Введите не больше одного символа!");
                continue;
            }
        flag_2 = false;    
        start_symbole = buf.charAt(0);
        buf = null;
        }
        if(flag_3 == true){
            System.out.println("Введите cтоп-символ:");
            buf = in.nextLine();
             if(buf.length() == 0){
                System.out.println("Вы не ввели символ!");
                continue;
            }
            if(buf.length()>1){
                System.out.println("Введите не больше одного символа!");
                continue;
            }
        flag_3 = false;    
        end_symbole = buf.charAt(0);
        break;
        }
        
        }
        System.out.println("Входные данные успешно получены, начинаю поиск.");
      
        
        
        results = seach_text(text,start_symbole,end_symbole);
        if(results == null)System.out.println("Увы!Ничего не найдено.");
        else make_report();
    

    }
    
  
  
}
