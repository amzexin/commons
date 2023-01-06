# 向工程添加多个特定的头文件搜索路径
include_directories(${PROJECT_SOURCE_DIR}/LinearList/ArrayList)
include_directories(${PROJECT_SOURCE_DIR}/LinearList/SinglyLinkedList)
include_directories(${PROJECT_SOURCE_DIR}/Stack/ArrayStack)
include_directories(${PROJECT_SOURCE_DIR}/Stack/SinglyLinkedStack)
include_directories(${PROJECT_SOURCE_DIR}/Queue/ArrayQueue)
include_directories(${PROJECT_SOURCE_DIR}/Queue/SinglyLinkedQueue)
include_directories(${PROJECT_SOURCE_DIR}/BinaryTree/LinkBinaryTree)
include_directories(${PROJECT_SOURCE_DIR}/Graph/AdjacencyMatrixGraph)
include_directories(${PROJECT_SOURCE_DIR}/BinarySearch)
include_directories(${PROJECT_SOURCE_DIR}/Sort/StraightInsertionSort)
include_directories(${PROJECT_SOURCE_DIR}/Sort/BubbleSort)
include_directories(${PROJECT_SOURCE_DIR}/Sort/QuickSort)
include_directories(${PROJECT_SOURCE_DIR}/Sort/StraightSelectSort)
include_directories(${PROJECT_SOURCE_DIR}/Sort/MergeSort)

# CommonUtil 测试程序
add_executable(CommonUtilTest CommonUtilTest.c)

# ArrayList 测试程序
add_executable(ArrayListTest ArrayListTest.c)
target_link_libraries(ArrayListTest ArrayListLib)

# SinglyLinkedList 测试程序
add_executable(SinglyLinkedListTest SinglyLinkedListTest.c)
target_link_libraries(SinglyLinkedListTest SinglyLinkedListLib)

# ArrayStack 测试程序
add_executable(ArrayStackTest ArrayStackTest.c)
target_link_libraries(ArrayStackTest ArrayStackLib)

# SinglyLinkedStack 测试程序
add_executable(SinglyLinkedStackTest SinglyLinkedStackTest.c)
target_link_libraries(SinglyLinkedStackTest SinglyLinkedStackLib)

# ArrayQueue 测试程序
add_executable(ArrayQueueTest ArrayQueueTest.c)
target_link_libraries(ArrayQueueTest ArrayQueueLib)

# SinglyLinkedQueue 测试程序
add_executable(SinglyLinkedQueueTest SinglyLinkedQueueTest.c)
target_link_libraries(SinglyLinkedQueueTest SinglyLinkedQueueLib)

# LinkBinaryTree 测试程序
add_executable(LinkBinaryTreeTest LinkBinaryTreeTest.c)
target_link_libraries(LinkBinaryTreeTest LinkBinaryTreeLib SinglyLinkedListLib)

# AdjacencyMatrixGraph 测试程序
add_executable(AdjacencyMatrixGraphTest AdjacencyMatrixGraphTest.c)
target_link_libraries(AdjacencyMatrixGraphTest AdjacencyMatrixGraphLib ArrayListLib SinglyLinkedListLib SinglyLinkedQueueLib)

# BinarySearch 测试程序
add_executable(BinarySearchTest BinarySearchTest.c)
target_link_libraries(BinarySearchTest BinarySearchLib)

# StraightInsertionSort 测试程序
add_executable(StraightInsertionSortTest StraightInsertionSortTest.c)
target_link_libraries(StraightInsertionSortTest StraightInsertionSortLib)

# BubbleSort 测试程序
add_executable(BubbleSortTest BubbleSortTest.c)
target_link_libraries(BubbleSortTest BubbleSortLib)

# QuickSort 测试程序
add_executable(QuickSortTest QuickSortTest.c)
target_link_libraries(QuickSortTest QuickSortLib)

# StraightSelectSort 测试程序
add_executable(StraightSelectSortTest StraightSelectSortTest.c)
target_link_libraries(StraightSelectSortTest StraightSelectSortLib)

# MergeSort 测试程序
add_executable(MergeSortTest MergeSortTest.c)
target_link_libraries(MergeSortTest MergeSortLib)

