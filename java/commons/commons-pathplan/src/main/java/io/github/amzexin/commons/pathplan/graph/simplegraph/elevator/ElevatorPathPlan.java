package io.github.amzexin.commons.pathplan.graph.simplegraph.elevator;

import io.github.amzexin.commons.pathplan.graph.Graph;
import io.github.amzexin.commons.pathplan.graph.simplegraph.ShortestPath;
import io.github.amzexin.commons.pathplan.graph.simplegraph.SimpleGraph;

import java.util.*;
import java.util.function.Function;

/**
 * Description:
 *
 * @author Lizexin
 * @date 2020-01-19 13:29
 */
public class ElevatorPathPlan {

    private Graph<WaitElevatorPoint> graph;

    private List<WaitElevatorPoint> allCallLiftPoint;

    private Map<String, Set<WaitElevatorPoint>> theSameLiftIdCallLiftPoint;

    private Map<String, Set<WaitElevatorPoint>> theSameMapIdCallLiftPoint;

    public ElevatorPathPlan(List<WaitElevatorPoint> allCallLiftPoint) {
        this.allCallLiftPoint = allCallLiftPoint;
        buildGraph();
    }

    private void buildGraph() {
        this.graph = new SimpleGraph<>(allCallLiftPoint.size(), false);
        this.theSameLiftIdCallLiftPoint = new HashMap<>(allCallLiftPoint.size());
        this.theSameMapIdCallLiftPoint = new HashMap<>(allCallLiftPoint.size());

        for (WaitElevatorPoint callingLiftPoint : allCallLiftPoint) {
            // 将相同电梯编号的召梯点放在一起
            theSameLiftIdCallLiftPoint.computeIfAbsent(callingLiftPoint.getLiftId(), new Function<String, Set<WaitElevatorPoint>>() {
                @Override
                public Set<WaitElevatorPoint> apply(String s) {
                    return new HashSet<>();
                }
            }).add(callingLiftPoint);

            // 将相同楼层的召梯点放在一起
            theSameMapIdCallLiftPoint.computeIfAbsent(callingLiftPoint.getMapId(), new Function<String, Set<WaitElevatorPoint>>() {
                @Override
                public Set<WaitElevatorPoint> apply(String s) {
                    return new HashSet<>();
                }
            }).add(callingLiftPoint);
        }

        // 将相同电梯编号的召梯点连接起来
        Set<Map.Entry<String, Set<WaitElevatorPoint>>> theSameLiftIdCallLiftPointEntries = theSameLiftIdCallLiftPoint.entrySet();
        for (Map.Entry<String, Set<WaitElevatorPoint>> theSameLiftIdCallLiftPointEntry : theSameLiftIdCallLiftPointEntries) {
            ArrayList<WaitElevatorPoint> callingLiftPoints = new ArrayList<>(theSameLiftIdCallLiftPointEntry.getValue());
            for (int i = 0; i < callingLiftPoints.size(); i++) {
                for (int j = i + 1; j < callingLiftPoints.size(); j++) {
                    graph.addEdge(callingLiftPoints.get(i), callingLiftPoints.get(j));
                }
            }
        }

        // 将相同楼层的召梯点连接起来
        Set<Map.Entry<String, Set<WaitElevatorPoint>>> theSameFloorCallLiftPointEntries = theSameMapIdCallLiftPoint.entrySet();
        for (Map.Entry<String, Set<WaitElevatorPoint>> theSameFloorCallLiftPointEntry : theSameFloorCallLiftPointEntries) {
            ArrayList<WaitElevatorPoint> callingLiftPoints = new ArrayList<>(theSameFloorCallLiftPointEntry.getValue());
            for (int i = 0; i < callingLiftPoints.size(); i++) {
                for (int j = i + 1; j < callingLiftPoints.size(); j++) {
                    graph.addEdge(callingLiftPoints.get(i), callingLiftPoints.get(j));
                }
            }
        }

    }

    public List<WaitElevatorPoint> path(String fromMapId, String toMapId) {
        ArrayList<WaitElevatorPoint> fromFloorCallLiftPoints = new ArrayList<>(theSameMapIdCallLiftPoint.get(fromMapId));
        ArrayList<WaitElevatorPoint> toFloorCallLiftPoints = new ArrayList<>(theSameMapIdCallLiftPoint.get(toMapId));

        // 从起始楼层中随便选一个作为source节点，并进行最短路径计算
        ShortestPath<WaitElevatorPoint> shortestPath = new ShortestPath<>(graph, fromFloorCallLiftPoints.get(0));

        // 从目标楼层中随便选一个作为target节点，并获取与source节点之间的路线
        List<WaitElevatorPoint> path = shortestPath.path(toFloorCallLiftPoints.get(0));

        // 计算起始楼层和目标楼层规划路径中的召梯点数量
        int fromPointSize = 0;
        int toPointSize = 0;
        for (WaitElevatorPoint callLiftPoint : path) {
            if (callLiftPoint.getMapId().equalsIgnoreCase(fromMapId)) {
                fromPointSize++;
            }
            if (callLiftPoint.getMapId().equalsIgnoreCase(toMapId)) {
                toPointSize++;
            }
        }

        /**
         * 《截去多余的召梯点》
         * 如果source节点正好选中 All_1-1, target节点正好选中 B_6-2
         * 最终会规划出 All_1-1 -> All_1-2 -> B_6-1 -> B_6-2
         * 此时我们应该截去多余的节点最终变为: All_1-2 -> B_6-1
         */
        int fromIndex = fromPointSize == 1 ? 0 : fromPointSize - 1;
        int toIndex = toPointSize == 1 ? path.size() : path.size() - toPointSize + 1;
        path = path.subList(fromIndex, toIndex);

        return path;
    }
}
