//
// Created by Vincent on 2021/5/15.
//

#ifndef INC_COMMONUTIL_H
#define INC_COMMONUTIL_H

#include <stdbool.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

typedef struct {
    /**
     * 编号
     */
    char *id;
    /**
     * 名称
     */
    char *name;
} Student;

bool equalsStudent(void *a, void *b) {
    Student *aa = (Student *) a;
    Student *bb = (Student *) b;
    return strcmp(aa->id, bb->id) == 0 && strcmp(aa->name, bb->name) == 0;
}

char *toStringStudent(void *data) {
    Student *aa = (Student *) data;
    char *result = malloc(sizeof(char *) * 2 + sizeof("{ id = ") + sizeof(", name = }"));
    strcat(result, "{ id = ");
    strcat(result, aa->id);
    strcat(result, ", name = ");
    strcat(result, aa->name);
    strcat(result, "}");
    return result;
}

bool equalsString(void *a, void *b) {
    char *aa = (char *) a;
    char *bb = (char *) b;
    return strcmp(aa, bb) == 0;
}

char *toStringString(void *a) {
    return (char *) a;
}

bool equalsInt(void *a, void *b) {
    int *aa = (int *) a;
    int *bb = (int *) b;
    return *aa == *bb;
}

int compareInt(void *a, void *b) {
    int *aa = (int *) a;
    int *bb = (int *) b;
    return *aa - *bb;
}

char *toStringInt(void *a) {
    int *aa = (int *) a;
    char *result = malloc(sizeof(char *));
    sprintf(result, "%d", *aa);
    return result;
}

bool ascSorted(int arrLength, int arr[arrLength]) {
    for (int i = 0; i < arrLength - 1; ++i) {
        if (arr[i] > arr[i + 1]) {
            printf("数组并未从小到大排序，第%d位(%d) > 第%d位(%d)\n", i, arr[i], i + 1, arr[i + 1]);
            return false;
        }
    }
    printf("数组从小到大有序\n");
    return true;
}

void arrPrintf(int arrLength, int arr[]) {
    if (arrLength == 0) {
        printf("[]\n");
        return;
    }
    printf("[%d", arr[0]);
    for (int i = 1; i < arrLength - 1; ++i) {
        printf(", %d", arr[i]);
    }
    printf(", %d]\n", arr[arrLength - 1]);
}

#endif  // INC_COMMONUTIL_H
