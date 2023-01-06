#include <stdio.h>
#include <stdlib.h>

#include "ArrayList.h"
#include "CommonUtil.h"

void SeqListTest() {
    ArrayList *seqList = arrayListInitiate(2, equalsStudent, toStringStudent);
    arrayListPrintf(seqList);

    Student *student1 = malloc(sizeof(Student));
    student1->id = "1001";
    student1->name = "王涛";
    arrayListInsertElement(seqList, student1, 0);

    Student *student2 = malloc(sizeof(Student));
    student2->id = "1002";
    student2->name = "潘小欣";
    arrayListInsertElement(seqList, student2, 1);

    Student *student3 = malloc(sizeof(Student));
    student3->id = "1003";
    student3->name = "张艳";
    arrayListInsertElement(seqList, student3, 2);

    Student *student4 = malloc(sizeof(Student));
    student4->id = "1004";
    student4->name = "赵李军";
    arrayListInsertElement(seqList, student4, 3);

    Student *student5 = malloc(sizeof(Student));
    student5->id = "1005";
    student5->name = "刘勇";
    arrayListInsertElement(seqList, student5, 4);
    arrayListPrintf(seqList);

    int index = 3;
    Student *student = arrayListGetElement(seqList, index);
    printf("第%d个元素是:%s\n", index + 1, toStringStudent(student));

    index = arrayListLocateElement(seqList, student3);
    printf("[%s]所在的位置是[%d]\n", student3->name, index + 1);

    arrayListDeleteElement(seqList, 3);
    arrayListPrintf(seqList);
}

void charPointerTest() {
    ArrayList *seqList = arrayListInitiate(10, equalsString, toStringString);
    arrayListAddElement(seqList, "英勇无比没有腹肌的Vincent");
    arrayListAddElement(seqList, "特想打球约不到人的Vincent");
    arrayListAddElement(seqList, "疑问很多无人交流的Vincent");
    arrayListAddElement(seqList, "Vincent");

    arrayListPrintf(seqList);
}

void intPointerTest() {
    ArrayList *seqList = arrayListInitiate(2, equalsInt, toStringInt);

    int *a;

    for (int i = 0; i < 10; ++i) {
        a = malloc(sizeof(int));
        *a = i;
        arrayListAddElement(seqList, a);
    }

    arrayListPrintf(seqList);
}

int main() {
    SeqListTest();
    //    charPointerTest();
    //    intPointerTest();
}