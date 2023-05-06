#include <stdio.h>
#include <stdlib.h>

// 计算一个数的阶乘
int factorial(int n) {
    if (n == 1) {
        return 1;
    }
    return n * factorial(n - 1);
}

// 将 n! 分解成素数因子
void f(int n) {
    // 计算n的阶乘
    int factorialResult = factorial(n);
    printf("%d! = %d = ", n, factorialResult);

    // 当前素数，初始素数为2
    int currentPrimeNumber = 2;
    // 当前素数的幂，初始幂为0
    int currentPrimeNumberPower = 0;
    // 阶乘结果没有变成1，就不断地计算
    while (factorialResult != 1) {
        // 通过取模的方式，计算是否能把素数整除
        int modResult = factorialResult % currentPrimeNumber;
        while (modResult == 0) {
            // 当前素数次幂+1
            currentPrimeNumberPower++;
            // 除以当前素数
            factorialResult /= currentPrimeNumber;
            // 继续取模
            modResult = factorialResult % currentPrimeNumber;
        }

        // 只有当前素数的幂不为0，才能打印
        if (currentPrimeNumberPower != 0) {
            printf(currentPrimeNumber == 2 ? "%d" : " * %d", currentPrimeNumber);
            if (currentPrimeNumberPower > 1) {
                printf("^%d", currentPrimeNumberPower);
            }
        }

        // 素数+1，并重置幂；开始计算下一个素数的结果
        currentPrimeNumber++;
        currentPrimeNumberPower = 0;
    }
}

int main() {
    f(501);
    return EXIT_SUCCESS;
}
