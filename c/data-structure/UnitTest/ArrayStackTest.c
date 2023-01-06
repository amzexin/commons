#include <stdlib.h>

#include "ArrayStack.h"
#include "CommonUtil.h"

void ArrayStackTest() {
    ArrayStack *arrayStack =
            arrayStackInitiate(5, equalsStudent, toStringStudent);
    bool isEmpty = arrayStackIsEmpty(arrayStack);
    printf("emptyArrayStack = %s \n", isEmpty ? "true" : "false");

    Student *student1 = malloc(sizeof(Student));
    student1->id = "student1";
    student1->name = "王涛";
    arrayStackPush(arrayStack, student1);

    Student *student2 = malloc(sizeof(Student));
    student2->id = "student2";
    student2->name = "潘小欣";
    arrayStackPush(arrayStack, student2);

    Student *student3 = malloc(sizeof(Student));
    student3->id = "student3";
    student3->name = "张艳";
    arrayStackPush(arrayStack, student3);

    Student *student4 = malloc(sizeof(Student));
    student4->id = "student4";
    student4->name = "赵李军";
    arrayStackPush(arrayStack, student4);

    Student *student5 = malloc(sizeof(Student));
    student5->id = "student5";
    student5->name = "刘勇";
    arrayStackPush(arrayStack, student5);
    arrayStackPrintf(arrayStack);

    Student *getTopElement = arrayStackGetTop(arrayStack);
    printf("getTop元素是:%s\n", arrayStack->toStringElement(getTopElement));

    Student *popElement = arrayStackPop(arrayStack);
    printf("pop元素是:%s\n", arrayStack->toStringElement(getTopElement));

    arrayStackPrintf(arrayStack);
}

int main() { ArrayStackTest(); }