# 向工程添加多个特定的头文件搜索路径
include_directories(${PROJECT_SOURCE_DIR}/LinearList/ArrayList)
include_directories(${PROJECT_SOURCE_DIR}/LinearList/SinglyLinkedList)
include_directories(${PROJECT_SOURCE_DIR}/Queue/SinglyLinkedQueue)

# 把一个目录下所有的源代码文件名，赋值给变量SOURCE_NAMES
aux_source_directory(. SOURCE_NAMES)

# 生成静态库AdjacencyMatrixGraph
add_library(AdjacencyMatrixGraphLib STATIC ${SOURCE_NAMES})
