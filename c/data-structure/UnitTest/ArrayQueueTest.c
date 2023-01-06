#include "ArrayQueue.h"
#include "CommonUtil.h"
#include <stdlib.h>

void ArrayQueueTest() {
    ArrayQueue *arrayQueue = arrayQueueInitiate(6, equalsStudent, toStringStudent);
    bool isEmpty = arrayQueueIsEmpty(arrayQueue);
    printf("emptyQueue = %s \n", isEmpty ? "true" : "false");

    Student *student1 = malloc(sizeof(Student));
    student1->id = "student1";
    student1->name = "王涛";
    arrayQueueEnElement(arrayQueue, student1);

    Student *student2 = malloc(sizeof(Student));
    student2->id = "student2";
    student2->name = "潘小欣";
    arrayQueueEnElement(arrayQueue, student2);

    Student *student3 = malloc(sizeof(Student));
    student3->id = "student3";
    student3->name = "张艳";
    arrayQueueEnElement(arrayQueue, student3);

    Student *student4 = malloc(sizeof(Student));
    student4->id = "student4";
    student4->name = "赵李军";
    arrayQueueEnElement(arrayQueue, student4);

    Student *student5 = malloc(sizeof(Student));
    student5->id = "student5";
    student5->name = "刘勇";
    arrayQueueEnElement(arrayQueue, student5);
    arrayQueuePrintf(arrayQueue);

    Student *getHeadStudent = arrayQueueGetHeadElement(arrayQueue);
    printf("getTop元素是:%s\n", arrayQueue->toStringElement(getHeadStudent));

    Student *outQueueStudent = arrayQueueOutElement(arrayQueue);
    printf("outQueue元素是:%s\n", arrayQueue->toStringElement(outQueueStudent));
    arrayQueuePrintf(arrayQueue);

    outQueueStudent = arrayQueueOutElement(arrayQueue);
    printf("outQueue元素是:%s\n", arrayQueue->toStringElement(outQueueStudent));
    arrayQueuePrintf(arrayQueue);
}

int main() { ArrayQueueTest(); }