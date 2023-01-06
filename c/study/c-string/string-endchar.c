/*
 * string-endchar-test.c
 *
 *  Created on: 2022年10月7日
 *      Author: zexin.li
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/**
 * 获取到一个数字的位数
 */
int numbe_digits(int num) {
    if (num < 0) {
        num = -num;
    }
    int i = 0;
    do {
        num = num / 10;
        i++;
    } while (num != 0);
    return i;
}

/**
 * 打印字符数组
 */
void printf_char_array(const char* function_name, char char_array[], int array_length) {
    printf("%s", function_name);
    for (int i = 0; i < array_length; i++) {
        if (i == 0) {
            printf("\t\t(%d)[%c", array_length, char_array[i]);
        } else {
            printf(", %c", char_array[i]);
        }
        if (i == array_length - 1) {
            printf("]");
        }
    }
    printf("\t\t\t\"%s\"\n", char_array);
}

/**
 * 结束符: '\0'
 */
void string_endchar_0(){
	char str[4] = {'a', '0', '\0', 'b'};
    int length = sizeof(str) / sizeof(str[0]);
    printf_char_array(__FUNCTION__, str, length);
}

/**
 * 结束符: 0
 */
void string_endchar_1(){
	char str[4] = {'a', '0', 0, 'b'};
    int length = sizeof(str) / sizeof(str[0]);
    printf_char_array(__FUNCTION__, str, length);
}

/**
 * 结束符: NULL
 */
void string_endchar_2(){
	char str[4] = {'a', '0', NULL, 'b'};
    int length = sizeof(str) / sizeof(str[0]);
    printf_char_array(__FUNCTION__, str, length);
}

/**
 * 没有结束符的字符数组，将会打印乱码
 */
void string_endchar_3() {
    char str[] = {'a', '0', 'b'};
    int length = sizeof(str) / sizeof(str[0]);
    printf_char_array(__FUNCTION__, str, length);
}

/*
 * 双引号引用的字符串，不指定字符数组初始长度
 * 猜测：底层在把“a”赋值给str数组时，判断已经是一个数组就没有将“b”赋值给str数组了
 * 所以：字符一定要用单引号
 */
void string_endchar_4() {
    char str[] = {"a", "b"};
    int length = sizeof(str) / sizeof(str[0]);
    printf_char_array(__FUNCTION__, str, length);
}

/**
 * 双引号引用的字符串，指定字符数组初始长度
 */
void string_endchar_5() {
    char str[4] = {"a", "b"};
    int length = sizeof(str) / sizeof(str[0]);
    printf_char_array(__FUNCTION__, str, length);
}

/**
 * 数组中间含有结束符，但是故意打印后续字符是仍旧可以继续打印的
 */
void string_endchar_6() {
    char str[4] = {'a', '\0', 'b', 0};
    int length = sizeof(str) / sizeof(str[0]);
    printf_char_array(__FUNCTION__, str, length);
}

int main() {
    printf("=======>>>%s<<<========\n", __FILE__);
    printf("方法名称\t\t\t(数组长度)数组\t\t\t字符串\n");
    string_endchar_0();
	string_endchar_1();
	string_endchar_2();
    string_endchar_3();
    string_endchar_4();
    string_endchar_5();
    string_endchar_6();
	return EXIT_SUCCESS;
}
