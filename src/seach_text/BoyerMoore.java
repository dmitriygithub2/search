/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seach_text;


/**
 * Author: Alexandre Koubenski
 *
 * Быстрый алгоритм поиска подстроки в строке.
 * Преимущества: хорошая скорость работы на длинных строках, содержащих много
 *               различных символов.
 * Недостатки:   замедление скорости работы при наличии большого числа повторяющихся
 *               символов и фрагментов строк в образце.
 *
 * Скорость работы:
 *    - в лучшем случае О(n/m)
 *    - в среднем       О(n)
 *    - в худшем случае О(n*m)
 */
public class BoyerMoore {
  private static final int shLen = 256;
  private static int hash(char c) { return c & 0xFF; }
  
  // После каждого вызова функции поиска содержит общее число сравнений.
  private static int compares = 0;

   /**
   * Алгоритм Бойена - Мура реализован в виде статического метода класса,
   * получающего исходные строки в качестве аргументов и возвращающего
   * индекс найденной подстроки в качестве результата.
   *
   * Если подстрока не найдена - результат работы равен -1.
   *
   * @param where - исходная строка, в которой ищут.
   * @param what  - подстрока, которую ищут.
   * @return      - найденный индекс или -1
   */
  public static int boyerMoore(String where, String what) {
    compares = 0;
    int n = where.length();    // Длина исходной строки
    int m = what.length();    // Длина образца
    // Массив сдвигов формируется в предположении, что коды символов
    // находятся в диапазоне от 0 до 255, однако можно хешировать символы,
    // выбирая меньшую длину массива
    int[] shifts = new int[shLen];
    // Для символов, отсутствующих в образце, сдвиг равен длине образца
    for (int i = 0; i < shLen; i++) {
      shifts[i] = m;
    }
    // Для символов из образца сдвиг равен расстоянию от
    // последнего вхождения символа в образец до конца образца
    for (int i = 0; i < m-1; i++) {
      shifts[hash(what.charAt(i))] = m-i-1;
    }

    for (int i = 0; i <= n-m; ) {
       // Сравнение начинается с конца образца
      for (int j = m-1; j>=0; j--) {
        compares++;
        if (where.charAt(i+j) == what.charAt(j)) {
          if (j == 0) return i;
        } else {
          break;
        }
      }
      // Сдвиг производится в соответствии с кодом последнего из сравниваемых символов
      i += shifts[hash(where.charAt(i+m-1))];
    }
    return -1;
  }

  
}
