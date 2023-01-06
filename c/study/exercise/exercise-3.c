/*
 * 编写函数，计算字符串中各字符ASCII码值的和。如果包含非数字，则返回-1
 * 使用键盘输入本人手机号码，测试函数的正确性
 *
 *  Created on: 2022年10月12日
 *      Author: zexin.li
 */
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

/**
 * 定义变长数组
 */
struct Str {
	int len;
	char arr[];
};

/**
 * 计算字符ASCII
 */
int compute(char *str) {
	int result = 0;
	int len = strlen(str);
	for (int i = 0; i < len; i++) {
		if (str[i] < '0' || str[i] > '9') {
			return -1;
		}
		result += str[i];
	}
	return result;
}

/**
 * 读取键盘输入的字符
 */
struct Str getstr() {
	struct Str str;
	str.len = 0;

	char c;
	while ((c = getchar()) != '\n') {
		str.arr[str.len++] = c;
	}
	str.arr[str.len] = '\0';

	return str;
}

int main() {
	printf("请输入手机号:");
	fflush(stdout);

	struct Str str = getstr();
	printf("您的手机号是:%s\n", str.arr);

	int result = compute(str.arr);
	printf("各字符ASCII码值的和为:%d\n", result);
	return EXIT_SUCCESS;
}
