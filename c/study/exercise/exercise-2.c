#include <stdio.h>
#include <stdlib.h>
#include <math.h>

/**
 * 两数相除.
 * TODO 优化1: 计算的结果要优化
 * 1.1 能除尽的直接显示
 * 1.2 不能除尽显示分数
 * 思路: 如何知道一个数能除尽
 * TODO 优化2: 入参类型需要从整数类型切换为字符串类型
 */
//char* chu(int a, int b) {
//	if (a % b == 0) {
//		char result[] = itoa(a / b);
//		return result;
//	}
//	int yue = 2;
//	while (yue <= a || yue <= b) {
//		if (a % yue == 0 && b % yue == 0) {
//			a /= yue;
//			b /= yue;
//			yue = 2;
//		}
//		yue++;
//	}
//	itoa(a);
//	return 0;
//}

/**
 * 求解一元二次方程ax²+bx+c=0的解。其中a、b、c在键盘输入。根据根的情况，分别显示有“两个根分别是”、“一个根是”或者“无解”。
 */
int main() {
	printf("求解一元二次方程ax²+bx+c=0的解\n");
	printf("请输入a,b,c的值(逗号分割):");
	fflush(stdout);

	float a, b, c;
	scanf("%f,%f,%f", &a, &b, &c);

	float derta = b * b - 4 * a * c;
	if (derta > 0) {
		int dertasqrt = sqrt(derta);
		float x1 = (-b + dertasqrt) / (2 * a);
		float x2 = (-b - dertasqrt) / (2 * a);
		printf("两个根分别是: x1 = %f, x2 = %f\n", x1, x2);
	} else if (derta == 0) {
		float x1 = -b / (2 * a);
		printf("一个根是: x1 = x2 = %f\n", x1);
	} else {
		printf("无解\n");
	}

	return EXIT_SUCCESS;
}

