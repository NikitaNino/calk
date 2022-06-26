import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        // Читаем строку ввода
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        // Парсим операнды и бинарный оператор
        String[] expressionArray = parse(input);
        
        // Считаем выражение
        System.out.println(calc(expressionArray));
    }

    private static String[] parse(String expression) {
        // Разбиваем выражение на массив, используя в качестве разделителя пробел
        String[] inputStrings = expression.split(" ");
        try {
            // Выбрасываем исключение если это не выражение с двумя операндами и бинарным оператором
            if (inputStrings.length != 3) {
                throw new IOException();
            }

            return inputStrings;
        } catch (IOException e) {
            throw new RuntimeException("Неверный формат ввода");
        }
    }


    public static String calc(String[] expressionTokens) throws Exception {
        // Проверяем систему счисления и корректность введенных данных
        boolean isRomanOperands = false;

        // Оба числа римсие - ставим флаг, что мы считаем в римской системе счисления
        if(isRoman(expressionTokens[0]) && isRoman(expressionTokens[2])){
            isRomanOperands = true;
        // Если оба числа не римские, но хотя бы одно - римское, значит у нас смесь систем - кидаем ошибку
        } else if(isRoman(expressionTokens[0]) || isRoman(expressionTokens[2])){
            throw new RuntimeException("Оба числа должны быть в одной система счисления");
        }

        // Разобрались с системой счисления - парсим сами числа и оператор, кладем в отдельные переменные
        int operandA = parseOperand(expressionTokens[0]);
        int operandB = parseOperand(expressionTokens[2]);
        String operator = expressionTokens[1];

        // Разрешаем только числа от 0 до 10
        if (((operandA < 0) || (operandB < 0)) || ((operandA > 10) || (operandB > 10))) {
            throw new Exception("Неверный формат ввода");
        }
        
        // Переменная с возвращаемым результатом
        Integer result = null;

        // Применяем оператор к операндам
        switch (operator) {
            case "+" : result = (operandA + operandB); break;
            case "-" : result = (operandA - operandB); break; 
            case "*" : result = (operandA * operandB); break;
            case "/" : result = (operandA / operandB); break;
            default : System.out.println("Допустимые знаки операции (+,-,*,/)");
        }

        // Выкидываем исключение в случае римской системы и отрицательного резултата
        if (isRomanOperands && result < 1) {
            throw new Exception("в римской системе нет отрицательных чисел");
        }

        // Если мы в римкой системе счисления - возвращаем результат так же в римской системе, иначе - просто число
        if (isRomanOperands) {
            return arabToRoman(result);
        } else {
            return Integer.toString(result);
        }

    }

    // Функция для парсинга числа в римской или арабской системах счисления
    private static int parseOperand(String operand) {
        // Пытаемся запарсить целое число в арабской системе
        try {
            return Integer.parseInt(operand);
        } catch (NumberFormatException e) {
            // Возникла ошибка - но что если это не совсем ошибка, а пользователь просто ввел римскую цифру, проверяем
            if(isRoman(operand)){
                return romanToArab(operand);
            } else {
                throw new RuntimeException("Операнд не является целым числом");
            }
        }
    }

    private static boolean isRoman(String operand) {
        // Регулярное выражения для прверки что данная строка - валидное число в римской системе счисления
        // https://stackoverflow.com/questions/38214744/java-roman-numeral-validity
        return operand.matches("^M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$");
    }

    static final String[] Roman1before10 = new String[]{"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
    static final String[] RomanFull = new String[]{"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC", "C"};

    private static int romanToArab(String sense) {
        for (int i = 0; i < Roman1before10.length; i++) {
            if (Roman1before10[i].equals(sense)) {
                return i;
            }
        }
        return 0;
    }

    static String arabToRoman(int sense) {
        return RomanFull[sense / 10] + Roman1before10[sense % 10];
    }
}