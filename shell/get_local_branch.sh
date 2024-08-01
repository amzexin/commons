#!/bin/bash

# 检查是否至少提供了一个文件夹路径
if [ "$#" -lt 1 ]; then
    echo "使用方法: $0 /path/to/first/project [/path/to/second/project ...]"
    exit 1
fi

# 函数：处理Git仓库
process_git_repo() {
    local repo_path="$1"
    
    # 检查是否是Git仓库
    if git -C "$repo_path" rev-parse --is-inside-work-tree > /dev/null 2>&1; then
        echo "=========>>>找到Git仓库在: $repo_path"
        
        # 获取远程仓库地址
        REMOTE_URL=$(git -C "$repo_path" config --get remote.origin.url 2>/dev/null)
        if [ -n "$REMOTE_URL" ]; then
            echo "远程仓库地址: $REMOTE_URL"
        else
            echo "未找到远程仓库地址"
        fi
        
        # 获取所有本地分支
        LOCAL_BRANCHES=$(git -C "$repo_path" branch)
        echo "本地分支列表:"
        echo "$LOCAL_BRANCHES"
    fi
}

# 遍历所有提供的文件夹路径
for FOLDER_PATH in "$@"; do
    echo "检查路径: $FOLDER_PATH"
    
    # 检查当前路径是否是Git仓库，并处理它
    if [ -d "$FOLDER_PATH/.git" ]; then
        process_git_repo "$FOLDER_PATH"
        # 如果当前目录是Git仓库，跳过其子目录的检查
        continue
    fi
    
    # 如果当前目录不是Git仓库，递归查找Git仓库
    find "$FOLDER_PATH" -type d -name '.git' | while read -r git_dir; do
        # 从.git目录路径中提取实际的仓库路径
        repo_path="${git_dir%/*}"
        process_git_repo "$repo_path"
    done
done


# ./get_local_branch.sh ~/projects
