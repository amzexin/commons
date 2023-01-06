#include <stdlib.h>

#include "CommonUtil.h"
#include "SinglyLinkedList.h"

void SinglyLinkedListTest() {
    SinglyLinkedList *linkList =
            singlyLinkedListInitiate(equalsStudent, toStringStudent);
    singlyLinkedListPrintf(linkList);

    Student *element1 = malloc(sizeof(Student));
    element1->id = "1001";
    element1->name = "王涛";
    singlyLinkedListInsertElement(linkList, element1, 0);

    Student *element2 = malloc(sizeof(Student));
    element2->id = "1002";
    element2->name = "潘小欣";
    singlyLinkedListAddElement(linkList, element2);

    Student *element3 = malloc(sizeof(Student));
    element3->id = "1003";
    element3->name = "张艳";
    singlyLinkedListInsertElement(linkList, element3, 2);

    Student *element4 = malloc(sizeof(Student));
    element4->id = "1004";
    element4->name = "赵李军";
    singlyLinkedListInsertElement(linkList, element4, 3);

    Student *element5 = malloc(sizeof(Student));
    element5->id = "1005";
    element5->name = "刘勇";
    singlyLinkedListInsertElement(linkList, element5, 4);
    singlyLinkedListPrintf(linkList);

    int index = 3;
    Student *element = singlyLinkedListGetElement(linkList, index);
    printf("第%d个元素是:%s\n", index + 1, toStringStudent(element));

    index = singlyLinkedListLocateElement(linkList, element3);
    printf("[%s]所在的位置是[%d]\n", element3->name, index + 1);

    singlyLinkedListDeleteElement(linkList, 3);
    singlyLinkedListPrintf(linkList);
}

int main() { SinglyLinkedListTest(); }