package io.github.amzexin.commons.pathplan.astar;

import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * Description: A*寻路算法
 * https://www.cnblogs.com/iwiniwin/p/10793654.html
 *
 * @author Vincent
 * @date 2020-05-26 21:49
 */
@Slf4j
public class AStarSearch {

    private AStarSearch() {
    }

    public static List<Point> searchPath(int[][] mat, Point startPosition, Point endPosition) {
        // 未访问的可行区域
        PriorityQueue<AStarNode> openList = new PriorityQueue<>(mat.length * mat[0].length);
        // 已经访问、障碍物
        Set<Point> closeList = new HashSet<>();
        // 1、将初始节点放入到open列表中。
        openList.add(new AStarNode(startPosition));
        long start = System.currentTimeMillis();
        for (; ; ) {
            // 2、判断open列表。如果为空，则搜索失败。如果open列表中存在目标节点，则搜索成功。
            if (openList.isEmpty()) {
                throw new RuntimeException("搜索失败");
            }
            // 3、从open列表中取出F值最小的节点作为当前节点，并将其加入到close列表中。
            AStarNode currentNode = openList.poll();
            Point currentPosition = currentNode.getPosition();
            closeList.add(currentPosition);
            // 4、计算当前节点的相邻的所有可到达节点，生成一组子节点。对于每一个子节点：
            List<Point> nearPositionList = nearPointList(mat, currentPosition);
            for (Point nearPosition : nearPositionList) {
                // 4.1、如果该节点在close列表中，则丢弃它
                if (closeList.contains(nearPosition)) {
                    continue;
                }

                // 寻到结束点了
                if (nearPosition.equals(endPosition)) {
                    // 整理结果
                    LinkedList<Point> path = new LinkedList<>();
                    path.addFirst(endPosition);
                    for (AStarNode aStarNode = currentNode; aStarNode.getParent() != null; aStarNode = aStarNode.getParent()) {
                        path.addFirst(aStarNode.getPosition());
                    }
                    log.info("A* 寻路结束。 耗时:{}ms", System.currentTimeMillis() - start);
                    return path;
                }

                // 4.2、如果该节点在open列表中，则检查其通过当前节点计算得到的F值是否更小，如果更小则更新其F值，并将其父节点设置为当前节点。
                AStarNode nearNode = getAStarNode(openList, nearPosition);
                if (nearNode != null) {
                    double newG = reCalculateG(nearNode, currentNode);
                    if (newG < nearNode.getGCost()) {
                        nearNode.setGCost(newG);
                        nearNode.setParent(currentNode);
                    }
                }
                // 4.3、如果该节点不在open列表中，则将其加入到open列表，并计算F值，设置其父节点为当前节点。
                else {
                    nearNode = new AStarNode(nearPosition);
                    nearNode.calculateF(currentNode, endPosition);
                    nearNode.setParent(currentNode);
                    openList.add(nearNode);
                }
            }
            // 5、转到2步骤
        }
    }

    private static AStarNode getAStarNode(PriorityQueue<AStarNode> aStarNodes, Point Point) {
        for (AStarNode aStarNode : aStarNodes) {
            if (aStarNode.getPosition().equals(Point)) {
                return aStarNode;
            }
        }
        return null;
    }

    private static double reCalculateG(AStarNode currentNode, AStarNode parentNode) {
        return parentNode.getGCost() + AStarCostUtil.cost(parentNode.getPosition(), currentNode.getPosition());
    }

    private static List<Point> nearPointList(int[][] map, Point currentPosition) {
        List<Point> nearPointList = new ArrayList<>();
        int height = map.length;
        int weight = map[0].length;
        for (int[] point : nearPosition) {
            int x = (int) (currentPosition.getX() + point[0]);
            int y = (int) (currentPosition.getY() + point[1]);

            if (x < 0 || y < 0 || x >= weight || y >= height) {
                continue;
            }

            if (map[y][x] == 0) {
                continue;
            }

            nearPointList.add(new Point(x, y));
        }
        return nearPointList;
    }

    // 左上, 上, 右上, 右, 右下, 下, 左下, 左
    private static int[][] nearPosition = {{-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}};
}
