#include <stdio.h>

#include "CommonUtil.h"
#include "SinglyLinkedQueue.h"

void SinglyLinkedQueueTest() {
    SinglyLinkedQueue *queue = singlyLinkedQueueInitiate(equalsString, toStringString);
    int empty = singlyLinkedQueueIsEmpty(queue);
    printf("emptyQueue = %d \n", empty);

    singlyLinkedQueueEnElement(queue, "王涛");

    singlyLinkedQueueEnElement(queue, "潘小欣");

    singlyLinkedQueueEnElement(queue, "张艳");

    singlyLinkedQueueEnElement(queue, "赵李军");

    singlyLinkedQueueEnElement(queue, "刘勇");

    singlyLinkedQueuePrintf(queue);

    char *getTopElement = singlyLinkedQueueGetHead(queue);

    printf("getTop元素是:%s\n", toStringString(getTopElement));

    for (int i = 0; i < 5; i++) {
        char *outQueueElement = singlyLinkedQueueOutElement(queue);
        printf("\n第%d次outQueue元素是:%s\n", i + 1, toStringString(outQueueElement));
        singlyLinkedQueuePrintf(queue);
    }
}

int main() { SinglyLinkedQueueTest(); }