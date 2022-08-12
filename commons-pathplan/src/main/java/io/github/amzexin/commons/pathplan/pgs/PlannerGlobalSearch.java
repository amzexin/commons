package io.github.amzexin.commons.pathplan.pgs;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @author Lizexin
 * @date 2020-08-17 17:41
 */
public class PlannerGlobalSearch {

    private GlobalSearch globalSearch;

    /**
     * 算法源坐标
     */
    private AlgorithmPosition algorithmSourcePosition;

    /**
     * 地图分辨率
     */
    private double mapResolution;

    /**
     * 初始化
     *
     * @param imagePath
     * @param sourcePosition
     * @param resolution
     * @throws IOException
     */
    public PlannerGlobalSearch(String imagePath, AlgorithmPosition sourcePosition, double resolution) throws IOException {
        this.globalSearch = new GlobalSearch(imagePath);
        this.algorithmSourcePosition = sourcePosition;
        this.mapResolution = resolution;
    }

    /**
     * 寻路
     *
     * @param start
     * @param end
     * @return¬
     * @throws IOException
     */
    public List<AlgorithmPosition> search(AlgorithmPosition start, AlgorithmPosition end) throws IOException {
        List<Point> path = globalSearch.search(algorithmPosition2pixelPosition(start), algorithmPosition2pixelPosition(end));
        List<AlgorithmPosition> result = new ArrayList<>(path.size() * 2);
        for (Point position : path) {
            result.add(pixelPosition2algorithmPosition(position));
        }
        return result;
    }

    /**
     * 绘制不可通行区域
     *
     * @param area
     */
    public void drawRect(List<AlgorithmPosition> area) throws IOException {
        List<Point> areaForPixel = area.stream().map(this::algorithmPosition2pixelPosition).collect(Collectors.toList());
        globalSearch.drawRect(areaForPixel);
    }

    private Point algorithmPosition2pixelPosition(AlgorithmPosition position) {
        int algorithmX = (int) (position.getX() / mapResolution - (algorithmSourcePosition.getX() / mapResolution));
        int algorithmY = (int) (position.getY() / mapResolution - (algorithmSourcePosition.getY() / mapResolution));
        algorithmY = globalSearch.height() - algorithmY;
        return new Point(algorithmX, algorithmY);
    }

    private AlgorithmPosition pixelPosition2algorithmPosition(Point position) {
        double pixelX = (position.getX() + (algorithmSourcePosition.getX() / mapResolution)) * mapResolution;
        double pixelY = (position.getY() + (algorithmSourcePosition.getY() / mapResolution)) * mapResolution;
        return new AlgorithmPosition(pixelX, pixelY);
    }

}
