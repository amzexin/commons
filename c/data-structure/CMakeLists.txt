cmake_minimum_required(VERSION 3.17)

project(data_structure C)

set(CMAKE_C_STANDARD 11)

# 向当前工程添加存放源文件的子目录
add_subdirectory(LinearList/ArrayList)
add_subdirectory(LinearList/SinglyLinkedList)
add_subdirectory(Stack/ArrayStack)
add_subdirectory(Stack/SinglyLinkedStack)
add_subdirectory(Queue/ArrayQueue)
add_subdirectory(Queue/SinglyLinkedQueue)
add_subdirectory(BinaryTree/LinkBinaryTree)
add_subdirectory(Graph/AdjacencyMatrixGraph)
add_subdirectory(BinarySearch)
add_subdirectory(Sort/StraightInsertionSort)
add_subdirectory(Sort/BubbleSort)
add_subdirectory(Sort/QuickSort)
add_subdirectory(Sort/StraightSelectSort)
add_subdirectory(Sort/MergeSort)
add_subdirectory(UnitTest)