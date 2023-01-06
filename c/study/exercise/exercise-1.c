#include <stdio.h>
#include <stdlib.h>

struct Str {
	int length;
	char arr[];
};

/**
 * 使用键盘输入手机号, 将该号码分成两两一组的整数, 输出该整数对应的ASCII字符
 */
int main() {
	printf("请输入手机号:");
	fflush(stdout);

	// 读取字符并打印. 如果是换行, 说明后续没有字符了, 直接退出; 否则继续执行
	struct Str str;
	str.length = 0;
	char c;
	while ((c = getchar()) != '\n') {
		str.arr[str.length++] = c;
	}
	str.arr[str.length] = '\0';
	printf("您的手机号是:%s\n", str.arr);

	// 两两组成一组并打印对应的ASCII字符
	char s[3] = { '\0' };
	for (int i = 0; i < str.length; i += 2) {
		s[0] = str.arr[i];
		if (i + 1 < str.length) {
			s[1] = str.arr[i + 1];
		} else {
			s[1] = '\0';
		}
		int num = atoi(s);
		printf("整数%d对应的ASCII字符是:[%c]\n", num, num);
	}

	return EXIT_SUCCESS;
}
