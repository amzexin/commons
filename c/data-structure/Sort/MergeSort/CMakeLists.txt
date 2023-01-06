# 把一个目录下所有的源代码文件名，赋值给变量SOURCE_NAMES
aux_source_directory(. SOURCE_NAMES)

# 生成静态库MergeSort
add_library(MergeSortLib STATIC ${SOURCE_NAMES})