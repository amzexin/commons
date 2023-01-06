#include <stdlib.h>

#include "CommonUtil.h"
#include "SinglyLinkedStack.h"

void SinglyLinkedStackTest() {
    SinglyLinkedStack *singlyLinkedStack =
            singlyLinkedStackInitiate(equalsStudent, toStringStudent);
    bool isEmpty = singlyLinkedStackIsEmpty(singlyLinkedStack);
    printf("emptySinglyLinkedStack = %s \n", isEmpty ? "true" : "false");

    Student *student1 = malloc(sizeof(Student));
    student1->id = "student1";
    student1->name = "王涛";
    singlyLinkedStackPush(singlyLinkedStack, student1);

    Student *student2 = malloc(sizeof(Student));
    student2->id = "student2";
    student2->name = "潘小欣";
    singlyLinkedStackPush(singlyLinkedStack, student2);

    Student *student3 = malloc(sizeof(Student));
    student3->id = "student3";
    student3->name = "张艳";
    singlyLinkedStackPush(singlyLinkedStack, student3);

    Student *student4 = malloc(sizeof(Student));
    student4->id = "student4";
    student4->name = "赵李军";
    singlyLinkedStackPush(singlyLinkedStack, student4);

    Student *student5 = malloc(sizeof(Student));
    student5->id = "student5";
    student5->name = "刘勇";
    singlyLinkedStackPush(singlyLinkedStack, student5);
    singlyLinkedStackPrintf(singlyLinkedStack);

    Student *getTopStudent = singlyLinkedStackGetTop(singlyLinkedStack);
    printf("getTop元素是: %s\n", singlyLinkedStack->toStringElement(getTopStudent));

    Student *popStudent = singlyLinkedStackPop(singlyLinkedStack);
    printf("pop元素是: %s\n", singlyLinkedStack->toStringElement(popStudent));

    singlyLinkedStackPrintf(singlyLinkedStack);
}

int main() { SinglyLinkedStackTest(); }